# Task 008: Implement Cache & Fetch Service

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-cache-fetch-service`

## Description
Create the `SolarEdgeService` that wraps the client and implements intelligent caching with 10-minute TTL. This service handles cache hits, cache misses, and manual refresh bypasses.

## Steps
1. [ ] Create `backend/src/utils/cache.ts` (generic TTL cache utility)
2. [ ] Create `backend/src/services/SolarEdgeService.ts`
3. [ ] Implement `getLatestPower()` (returns cached or fetches fresh)
4. [ ] Implement `refreshPowerData()` (manual bypass)
5. [ ] Implement `startPolling()` (10-min interval poller)

## Definition of Done (DOD)
- Cache TTL respected (10 minutes)
- Manual refresh bypasses cache
- Polling starts on server init
- Falls back to stale cache if SolarEdge unreachable
- All methods typed; npm test passes
- No code exceeds 150 lines total

## Expected Output
- `backend/src/utils/cache.ts` (~40 lines) - Generic TTL cache
- `backend/src/services/SolarEdgeService.ts` (~90 lines)
  - Constructor initializes cache and client
  - `getLatestPower()` with TTL logic
  - `refreshPowerData()` for manual triggers
  - `startPolling()` sets up 10-min interval

## Notes for Developer
- Use `Map` for in-memory cache
- Track cache metadata: `{ value, timestamp, ttl }`
- On cache miss, fetch from client and update cache
- Handle rate-limit errors gracefully
- Do NOT implement alerts here; that's Task 010
