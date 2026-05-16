# Task 010: Implement Alert & Threshold Service

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-alert-service`

## Description
Create the `AlertService` that manages power thresholds (min/max Watts) and evaluates them against incoming power data. Generate alert records when thresholds are breached. Persist alerts for retrieval via API.

## Steps
1. [ ] Create `backend/src/services/AlertService.ts`
2. [ ] Implement `setThresholds(min, max)` (store threshold config)
3. [ ] Implement `getThresholds()` (retrieve current config)
4. [ ] Implement `evaluateThreshold(power, timestamp)` (check breach)
5. [ ] Implement `getAlerts(date?)` (retrieve alert history)

## Definition of Done (DOD)
- Thresholds stored and retrievable
- Alert generated when power < min OR power > max
- Alerts include: timestamp, breach type, actual value, threshold
- Alert history persisted to JSON file
- No duplicate alerts for same breach within cooldown period
- npm test passes; code compiles

## Expected Output
- `backend/src/services/AlertService.ts` (~100 lines)
  - Constructor initializes threshold config and storage
  - `setThresholds(min, max)` validates and stores
  - `getThresholds()` returns current config
  - `evaluateThreshold(power, timestamp)` → Alert | null
  - `getAlerts(date?)` returns today's alerts by default
  - `persistAlerts()` saves to JSON

## Notes for Developer
- Persist thresholds to `data/thresholds.json`
- Persist alerts to `data/alerts.json` (append per day)
- Cooldown: Skip duplicate alerts within 5 minutes
- Alert structure: `{ id, timestamp, type, power, threshold, message }`
- Integrate with SolarEdgeService polling in next task
