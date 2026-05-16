# Task 007: Implement SolarEdge API Client

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-edge-client`

## Description
Create a SolarEdge API wrapper client that handles authentication, HTTP calls, error handling, and response parsing. This is the single point of contact with the external SolarEdge service.

## Steps
1. [ ] Create `backend/src/clients/SolarEdgeClient.ts`
2. [ ] Implement `fetchCurrentPower()` method (hits SolarEdge API)
3. [ ] Implement rate-limit tracking (1 call/min per site)
4. [ ] Add error handling with typed exceptions
5. [ ] Add JSDoc documentation for all methods

## Definition of Done (DOD)
- Client can successfully fetch data from SolarEdge API (or mock)
- Rate-limiting enforced: throws error if quota exceeded
- All errors logged and typed
- Code compiles; npm test passes
- No hardcoded secrets (uses environment variables)

## Expected Output
- `backend/src/clients/SolarEdgeClient.ts` (~80 lines)
  - Constructor takes API key from env
  - `fetchCurrentPower()` returns `Promise<SolarEdgeResponse>`
  - `isRateLimited()` checks quota
  - Error handling with typed exceptions

## Notes for Developer
- Use `node-fetch` or native `fetch` (Node 18+)
- Store last-call timestamp to enforce 1 call/min limit
- Do NOT cache here; that's the next task
- Mock response on env var `SOLAR_EDGE_MOCK_MODE=true`
