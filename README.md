# Solar System Monitoring

A TypeScript/Node.js backend service for monitoring residential solar PV systems via SolarEdge integration. Features real-time power tracking, daily analytics, threshold-based alerts, and intelligent caching for low-cost operation.

---

## Table of Contents

- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Deployment](#deployment)
- [Development](#development)
- [Contributing](#contributing)

---

## Quick Start

### Prerequisites

- **Node.js** 18+ (or latest LTS)
- **npm** 9+
- **Git**
- **SolarEdge API Key** (obtain from [SolarEdge Monitoring Portal](https://www.solaredge.com))

### Installation & Run

```bash
# Clone the repository
git clone https://github.com/b-asaf/solar-system-monitoring.git
cd solar-system-monitoring

# Install dependencies
npm install --workspace=backend

# Create environment file
cp backend/.env.example backend/.env
# Edit backend/.env with your SolarEdge API key

# Start the development server
npm run dev --workspace=backend

# Server runs at http://localhost:3000
```

---

## Architecture

### System Overview

```
┌─────────────────────────────────────────────────────────┐
│  Client Applications (Web, Mobile, Dashboard)           │
└────────────────┬────────────────────────────────────────┘
                 │ HTTP/JSON
                 ▼
┌─────────────────────────────────────────────────────────┐
│  Express API Layer                                       │
│  ├─ GET  /api/health              (Server status)       │
│  ├─ GET  /api/solar/status        (Current power)       │
│  ├─ POST /api/solar/refresh       (Manual fetch)        │
│  ├─ GET  /api/solar/daily-analytics (Daily stats)      │
│  ├─ GET  /api/solar/alerts        (Alert history)       │
│  ├─ GET  /api/solar/thresholds    (Current config)      │
│  └─ POST /api/solar/thresholds    (Update config)       │
└────────────────┬────────────────────────────────────────┘
                 │
         ┌───────┴────────┬──────────────┐
         ▼                ▼              ▼
    ┌────────────┐  ┌──────────────┐  ┌────────────┐
    │ SolarEdge  │  │ Analytics    │  │ Alert      │
    │ Service    │  │ Service      │  │ Service    │
    │ (Fetch)    │  │ (Daily Agg)  │  │ (Thresholds)
    └────────────┘  └──────────────┘  └────────────┘
         │                │              │
         ▼                ▼              ▼
    ┌────────────────────────────────────────────┐
    │  Data Layer                                 │
    │  ├─ In-Memory Cache (10-min TTL)           │
    │  ├─ Persistent Store (data/*.json)         │
    │  └─ SolarEdge API Client                   │
    └────────┬────────────────────────────────────┘
             │
             ▼
    ┌────────────────────────────────────────────┐
    │  SolarEdge Cloud API                       │
    │  (External, Rate-Limited: 1 call/min)     │
    └────────────────────────────────────────────┘
```

### Data Flow

#### Polling & Alert Evaluation (10-minute intervals)

```
1. Server starts
   ▼
2. Initialize services (SolarEdge, Analytics, Alert)
   ▼
3. Start 10-minute polling loop
   ▼
4. Poll Loop:
   a. SolarEdgeService.getLatestPower()
      ├─ Check cache TTL
      ├─ If fresh: return cached value
      └─ If stale: call SolarEdgeClient.fetchCurrentPower()
   ▼
   b. AlertService.evaluateThreshold(power)
      ├─ Check if power < min_threshold
      ├─ Check if power > max_threshold
      └─ Create alert if breached
   ▼
   c. AnalyticsService.recordSnapshot(power)
      ├─ Store 10-min sample
      └─ Compute daily stats if complete
   ▼
5. Repeat step 4 every 10 minutes
```

#### Manual Refresh (User Triggered)

```
User calls: POST /api/solar/refresh
   │
   ▼
Express Handler
   │
   ├─ Check rate-limit quota
   │
   ▼ (if allowed)
SolarEdgeService.refreshPowerData()
   │
   ├─ Bypass cache TTL
   ├─ Call SolarEdgeClient.fetchCurrentPower() directly
   ├─ Update in-memory cache
   │
   ▼
Return fresh power data to client
   │
Note: Also triggers alert evaluation & analytics recording
```

#### Daily Analytics Computation

```
Every 10-minute snapshot:
   │
   ├─ Record power value + timestamp
   ├─ Check if day boundary crossed
   │
   ▼ (if new day)
   Compute previous day stats:
   ├─ Daily Yield = Σ(power * 10min / 3600) in kWh
   ├─ Peak Power = max(power) in Watts
   ├─ Peak Time = timestamp of max power
   ├─ Efficiency % = (daily_yield / system_capacity) * 100
   │
   ▼
   Persist to data/daily-stats.json
   │
   ▼
   Reset snapshots for new day
```

#### Threshold Alert System

```
Power sample received → Evaluate thresholds
   │
   ├─ Check: power < minWatts?
   │         YES → Alert type: "UNDER_MIN"
   │         NO  → Continue
   │
   ├─ Check: power > maxWatts?
   │         YES → Alert type: "OVER_MAX"
   │         NO  → Continue
   │
   ├─ Check: Is this a duplicate alert (within 5-min cooldown)?
   │         YES → Skip (avoid spam)
   │         NO  → Continue
   │
   ▼
   Create Alert Record:
   {
     id: UUID,
     timestamp: ISO-8601,
     type: "UNDER_MIN" | "OVER_MAX",
     power: number,
     threshold: number,
     message: "System power fell below minimum..."
   }
   │
   ▼
   Persist to data/alerts.json
   │
   ▼
   Return in GET /api/solar/alerts response
```

---

## API Endpoints

### 1. Server Health Check

```http
GET /api/health
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "timestamp": "2026-05-16T10:30:00Z",
  "service": "Solar Monitoring Backend"
}
```

---

### 2. Current Power Status

```http
GET /api/solar/status
```

**Response (200 OK):**
```json
{
  "timestamp": "2026-05-16T10:30:00Z",
  "currentPowerWatts": 3450,
  "systemStatus": "OK",
  "lastFetchTime": "2026-05-16T10:30:00Z",
  "cacheAge": "PT2M30S",
  "stale": false
}
```

**Response (503 if data unavailable):**
```json
{
  "error": "Solar data unavailable",
  "message": "SolarEdge API unreachable; cache is empty",
  "retry_after": 60
}
```

---

### 3. Manual Data Refresh

```http
POST /api/solar/refresh
```

**Response (200 OK):**
```json
{
  "timestamp": "2026-05-16T10:31:15Z",
  "currentPowerWatts": 3455,
  "systemStatus": "OK",
  "refreshedAt": "2026-05-16T10:31:15Z"
}
```

**Response (429 if rate-limited):**
```json
{
  "error": "Rate limit exceeded",
  "message": "Maximum 1 refresh per minute allowed",
  "retry_after": 45
}
```

---

### 4. Daily Analytics

```http
GET /api/solar/daily-analytics
```

**Response (200 OK):**
```json
{
  "date": "2026-05-16",
  "totalYieldKwh": 18.4,
  "peakPowerWatts": 5200,
  "peakTimeUtc": "2026-05-16T12:45:00Z",
  "efficiencyPercent": 87.3,
  "snapshotCount": 144,
  "systemCapacityWatts": 6000
}
```

---

### 5. Alert History

```http
GET /api/solar/alerts?date=2026-05-16
```

**Response (200 OK):**
```json
{
  "date": "2026-05-16",
  "alerts": [
    {
      "id": "alert-001",
      "timestamp": "2026-05-16T06:15:00Z",
      "type": "UNDER_MIN",
      "power": 85,
      "threshold": 100,
      "message": "System power fell below minimum threshold of 100W"
    },
    {
      "id": "alert-002",
      "timestamp": "2026-05-16T17:30:00Z",
      "type": "OVER_MAX",
      "power": 5850,
      "threshold": 5800,
      "message": "System power exceeded maximum threshold of 5800W"
    }
  ]
}
```

---

### 6. Get Thresholds

```http
GET /api/solar/thresholds
```

**Response (200 OK):**
```json
{
  "minWatts": 100,
  "maxWatts": 5800,
  "lastUpdated": "2026-05-15T14:20:00Z"
}
```

---

### 7. Update Thresholds

```http
POST /api/solar/thresholds
Content-Type: application/json

{
  "minWatts": 150,
  "maxWatts": 6000
}
```

**Response (200 OK):**
```json
{
  "minWatts": 150,
  "maxWatts": 6000,
  "lastUpdated": "2026-05-16T10:40:00Z",
  "message": "Thresholds updated successfully"
}
```

---

## Configuration

### Environment Variables

Create `backend/.env` file:

```bash
# SolarEdge Integration
SOLAR_EDGE_API_KEY=your_api_key_here
SOLAR_EDGE_SITE_ID=your_site_id_here
SOLAR_EDGE_MOCK_MODE=false          # Set to 'true' for testing without real API

# Server
PORT=3000
NODE_ENV=development               # development | production

# Polling
POLL_INTERVAL_MINUTES=10            # How often to fetch from SolarEdge
RATE_LIMIT_PER_MINUTE=1             # Max SolarEdge API calls per minute

# System Configuration
SYSTEM_CAPACITY_WATTS=6000          # Your inverter capacity
ALERT_COOLDOWN_MINUTES=5            # Prevent duplicate alerts

# Storage
DATA_DIR=./data                     # Directory for cache/alerts/stats
```

### Example `.env.example`

```bash
SOLAR_EDGE_API_KEY=TBD
SOLAR_EDGE_SITE_ID=TBD
SOLAR_EDGE_MOCK_MODE=true
PORT=3000
NODE_ENV=development
POLL_INTERVAL_MINUTES=10
RATE_LIMIT_PER_MINUTE=1
SYSTEM_CAPACITY_WATTS=6000
ALERT_COOLDOWN_MINUTES=5
DATA_DIR=./data
```

---

## Running Locally

### Setup

```bash
# Navigate to backend directory
cd backend

# Install dependencies
npm install

# Copy environment template
cp .env.example .env

# Edit .env with your configuration
nano .env
```

### Development Mode (with auto-reload)

```bash
npm run dev
```

Watch mode enabled. Changes to files auto-restart the server.

### Production Mode

```bash
# Build TypeScript
npm run build

# Run compiled JavaScript
npm start
```

### Running Tests

```bash
# Run all tests
npm test

# Run tests in watch mode
npm test -- --watch

# Generate coverage report
npm test -- --coverage
```

---

## Deployment

### Prerequisites for Deployment

- [ ] SolarEdge API key obtained and verified
- [ ] SolarEdge Site ID configured
- [ ] Environment variables set securely
- [ ] Backup/restore strategy for `data/` directory defined
- [ ] Monitoring & alerting setup (TBD)

### Docker Deployment (TBD)

```bash
# Build Docker image
docker build -t solar-monitoring-backend .

# Run container
docker run -e SOLAR_EDGE_API_KEY=xxx -p 3000:3000 solar-monitoring-backend
```

**Dockerfile location:** TBD  
**Docker registry:** TBD  
**Kubernetes deployment:** TBD

### Cloud Deployment Options

#### Option A: AWS (TBD)
- **Service:** EC2, ECS, Lambda
- **Database:** DynamoDB for persistent storage
- **Configuration:** TBD

#### Option B: Google Cloud (TBD)
- **Service:** Cloud Run, App Engine, Compute Engine
- **Database:** Firestore for alerts/stats
- **Configuration:** TBD

#### Option C: Heroku (TBD)
- **Buildpack:** Node.js
- **Add-ons:** PostgreSQL (for future persistence)
- **Configuration:** TBD

#### Option D: Self-Hosted (TBD)
- **Provider:** DigitalOcean, Linode, custom VPS
- **Deployment tool:** Docker Compose, systemd
- **Configuration:** TBD

### CI/CD Pipeline

- **GitHub Actions workflow:** `.github/workflows/ci.yml`
- **Triggers:** Push to `main`, Pull Requests
- **Steps:**
  1. Install dependencies
  2. Run linter (eslint) – TBD
  3. Run tests (`npm test`)
  4. Build (`npm run build`)
  5. Deploy (if on `main`) – TBD

---

## Development

### Project Structure

```
solar-system-monitoring/
├── README.md (this file)
├── IMPLEMENTATION.md (detailed flows & diagrams)
├── backend/
│   ├── src/
│   │   ├── server.ts (Express entry point)
│   │   ├── services/
│   │   │   ├── SolarEdgeService.ts
│   │   │   ├── AnalyticsService.ts
│   │   │   └── AlertService.ts
│   │   ├── clients/
│   │   │   └── SolarEdgeClient.ts
│   │   ├── types/
│   │   │   ├── solar.ts
│   │   │   ├── alerts.ts
│   │   │   └── cache.ts
│   │   ├── utils/
│   │   │   └── cache.ts
│   │   └── __tests__/ (test files)
│   ├── package.json
│   ├── tsconfig.json
│   └── .env.example
├── shared/
│   └── types.ts (shared domain types)
└── agents/
    ├── roles/ (AI agent blueprints)
    ├── tasks/ (implementation tasks)
    ├── docs/features/ (PRDs)
    └── skills/ (automation scripts)
```

### Adding a New Feature

1. **PM Phase:** Summon PM to grill requirements → Draft PRD
2. **Architect Phase:** Summon Architect to break into atomic tasks
3. **Dev Phase:** Implement tasks sequentially; each <150 LOC per PR
4. **QA Phase:** Write tests; verify coverage >80%
5. **Review Phase:** Code Reviewer gates on atomic PR constraints
6. **Merge:** Manual human review + merge to `main`

### Code Style

- **Language:** TypeScript (strict mode)
- **Format:** Prettier (auto-format on save)
- **Linter:** ESLint (config TBD)
- **Tests:** Jest with >80% coverage target

### Git Workflow

1. Create feature branch: `git checkout -b feat/your-feature-name`
2. Implement & commit: `git add . && git commit -m "feat: description"`
3. Push & open PR: `./agents/skills/submit-task.sh`
4. Wait for review & merge

---

## Contributing

### Prerequisites

- Node.js 18+
- Familiarity with TypeScript, Express, and async/await
- SolarEdge API documentation

### Workflow

1. Fork the repository (or create a branch if maintainer)
2. Create feature branch from `main`
3. Implement feature (see Development section)
4. Submit PR with detailed description
5. Address review comments
6. Maintainer merges once approved

### Code Review Checklist

- [ ] Code compiles: `npm run build`
- [ ] Tests pass: `npm test`
- [ ] No breaking changes to existing APIs
- [ ] All types are strict (no `any`)
- [ ] Changes <150 LOC (atomic PR principle)
- [ ] Error handling included
- [ ] Documentation updated

---

## Monitoring & Observability

### Logs

- **Location:** `logs/` directory (TBD)
- **Format:** JSON structured logs (TBD)
- **Levels:** ERROR, WARN, INFO, DEBUG

### Metrics (TBD)

- API request latency
- Cache hit rate
- SolarEdge API call frequency
- Alert generation rate
- System uptime

### Alerting (TBD)

- SolarEdge API unavailable for >30 mins
- Power generation anomalies
- Alert cooldown threshold breached
- Server crashes

---

## FAQ

**Q: Can I use this with other solar providers (Fronius, Enphase)?**  
A: Currently SolarEdge only (MVP scope). Future tasks will abstract the provider interface.

**Q: How accurate is the yield calculation?**  
A: As accurate as the 10-minute power samples. Assumes linear power generation between samples.

**Q: Can multiple users use the same system?**  
A: No, MVP is single-site, single-homeowner. Future: multi-tenant architecture (TBD).

**Q: What happens if SolarEdge API goes down?**  
A: System returns cached data (with `stale: true` flag) for up to 10 minutes. After that, HTTP 503.

**Q: How do I deploy this to production?**  
A: See [Deployment](#deployment) section. Cloud provider setup TBD.

---

## Support & Issues

- **Bug reports:** [GitHub Issues](https://github.com/b-asaf/solar-system-monitoring/issues)
- **Feature requests:** [GitHub Discussions](https://github.com/b-asaf/solar-system-monitoring/discussions)
- **Documentation:** See IMPLEMENTATION.md for detailed flows

---

## License

TBD

---

## Changelog

- **v1.0.0 (May 16, 2026):** Initial architecture & README. Implementation in progress.