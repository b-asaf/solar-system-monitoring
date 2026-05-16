# Product Requirement Document (PRD): SolarEdge Solar Monitoring API

**Feature ID:** 001-solaredge-solar-api  
**Version:** 1.0  
**Date:** May 16, 2026  
**Owner:** Product Manager  
**Status:** APPROVED FOR ARCHITECTURE

---

## Executive Summary

The **SolarEdge Solar Monitoring API** enables homeowners to efficiently monitor and manage their residential solar photovoltaic (PV) systems. The system fetches real-time power generation data from SolarEdge, caches it intelligently, and provides analytics and alert capabilities—all while maintaining a low-cost, rate-limited architecture.

---

## User Stories

### US-001: Homeowner Views Current Power Output
**As a** homeowner with a SolarEdge solar system  
**I want to** check my current real-time power generation with a single API call  
**So that** I can understand if my system is producing as expected right now

**Acceptance Criteria:**
- API returns current AC power (Watts) instantly
- Data is cached for efficiency; manual refresh triggers a new SolarEdge API fetch
- Response includes timestamp and system status (OK, WARNING, ERROR)

---

### US-002: Homeowner Monitors Daily Analytics
**As a** homeowner  
**I want to** see my daily energy yield (kWh), efficiency metrics, and peak production time  
**So that** I can track performance trends and detect anomalies

**Acceptance Criteria:**
- API returns aggregated daily stats: total yield (kWh), efficiency %, peak power, peak time
- Data is computed from cached 10-minute snapshots
- Includes comparison to historical averages (if available)

---

### US-003: Homeowner Sets Production Thresholds & Alerts
**As a** homeowner  
**I want to** define minimum/maximum power thresholds and receive alerts when breached  
**So that** I can detect system failures, inverter issues, or shading problems early

**Acceptance Criteria:**
- API allows configurable min/max power thresholds (Watts)
- System evaluates thresholds on every 10-minute data refresh
- Alerts stored in a notifications queue (email/push can be added later)
- Alert includes: trigger time, threshold value, actual power, suggested action

---

### US-004: Homeowner Manually Triggers Data Refresh
**As a** homeowner  
**I want to** manually request a fresh data fetch from SolarEdge on demand  
**So that** I don't have to wait for the next 10-minute interval if I suspect an issue

**Acceptance Criteria:**
- API exposes a `/refresh` endpoint for manual triggers
- Manual refresh bypasses rate-limiting (within quota)
- Returns latest data from SolarEdge immediately
- Respects SolarEdge API rate limits to avoid service interruption

---

## Functional Requirements

### FR-1: Real-Time Power Fetch (SolarEdge Integration)
- Integrate with SolarEdge API (requires API key in environment)
- Fetch current AC power and system status every 10 minutes
- Store fetched data in an in-memory or lightweight cache (e.g., Node.js Map)
- Return HTTP 200 with power data; HTTP 5xx if SolarEdge is unreachable

### FR-2: Data Caching Strategy
- **Default Cache TTL:** 10 minutes between automated fetches
- **Manual Refresh:** Bypasses cache, hits SolarEdge directly (within rate limits)
- **Rate Limiting:** Max 1 API call to SolarEdge per minute per site (configurable)
- **Cache Warmup:** On server startup, fetch initial data from SolarEdge

### FR-3: Analytics & Aggregation
- Compute daily yield (sum of 10-min power samples * interval duration)
- Track peak power and time-of-peak each day
- Calculate efficiency % based on historical system capacity
- Store aggregates per day in persistent storage (SQLite or JSON files initially)

### FR-4: Threshold & Alert Management
- Allow homeowners to configure min/max power thresholds
- Evaluate thresholds on every 10-minute data update
- Create alert records when thresholds are breached
- Store alerts with timestamp, threshold value, actual value, and remediation hints

### FR-5: API Endpoints (MVP)
```
GET    /api/solar/status
       → Returns current power, status, last fetch time
       
GET    /api/solar/daily-analytics
       → Returns daily yield, efficiency, peak power, peak time
       
POST   /api/solar/refresh
       → Manual data fetch from SolarEdge (bypasses cache)
       
GET    /api/solar/alerts
       → Lists all threshold breach alerts for the current day
       
POST   /api/solar/thresholds
       BODY: { minWatts: 100, maxWatts: 5000 }
       → Set or update power thresholds
       
GET    /api/solar/thresholds
       → Returns current threshold configuration
```

### FR-6: Error Handling & Graceful Degradation
- If SolarEdge API is down, return cached data with a `stale_data: true` flag
- If cache is empty, return HTTP 503 with retry-after header
- Log all external API failures for debugging
- Never break the main server health-check endpoint

### FR-7: Security & Environment
- SolarEdge API key stored in `.env` (never committed)
- Validate all query parameters and request bodies
- Rate-limit per-IP for unauthenticated endpoints (if exposed publicly)
- Log all API access for audit trails

---

## Scope

### In Scope (MVP)
- ✅ Real-time power fetch from SolarEdge API
- ✅ 10-minute interval polling with caching
- ✅ Manual refresh capability
- ✅ Daily analytics aggregation (yield, efficiency, peak)
- ✅ Min/max power thresholds with alerts
- ✅ In-memory caching with persistent daily stats
- ✅ HTTP error handling and graceful degradation
- ✅ Environment-based configuration

### Out of Scope (Future)
- ❌ Multi-site/multi-homeowner support (single site MVP)
- ❌ Email/SMS/push notifications (alerts stored only)
- ❌ User authentication & authorization (assumed API is internal/trusted)
- ❌ Historical trend analysis (only daily stats retained)
- ❌ Advanced ML-based anomaly detection
- ❌ Mobile app (API only; UI can consume later)
- ❌ Backward integration with other solar providers (SolarEdge only)

---

## Technical Constraints

1. **Free Tier APIs Only:** SolarEdge free tier allows ~1-2 API calls/minute
2. **Low-Cost Execution:** Use in-memory caching to minimize external calls
3. **Max 150 LOC per PR:** All implementation must respect atomic PR limits
4. **TypeScript Strict:** All code must compile with strict type checking
5. **No Breaking Changes:** Every PR must leave the system in a runnable state
6. **CI/CD Gate:** All PRs must pass `npm test` before merge

---

## Success Metrics

- **API Availability:** 99% uptime (excluding external SolarEdge outages)
- **Response Latency:** Cache hits <50ms; SolarEdge calls <2s
- **Rate Limit Compliance:** Zero SolarEdge API overage charges
- **Alert Accuracy:** 100% detection of threshold breaches
- **Code Quality:** All endpoints tested; >80% coverage

---

## Acceptance Criteria (Definition of Done)

- ✅ All endpoints implemented and tested
- ✅ SolarEdge integration functional with mocked/real API key
- ✅ Caching strategy reduces API calls to <60/day per site
- ✅ Daily analytics computed and stored correctly
- ✅ Threshold logic triggers alerts on breach
- ✅ All code passes TypeScript compilation and CI/CD
- ✅ Code review approved; PR merged to `main`

---

## Notes for Architect

- Break this PRD into **atomic, sequential tasks** (max 150 LOC each)
- Suggested task sequence:
  1. Create domain models & SolarEdge API client interface
  2. Implement real-time power fetch + caching layer
  3. Implement daily analytics aggregation
  4. Implement threshold & alert management
  5. Wire all endpoints into Express
  6. Integration tests + CI validation
- Ensure each task compiles and passes tests independently
- Use feature flags if any step is incomplete (no "dark code")
