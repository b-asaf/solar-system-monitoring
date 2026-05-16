# Task 011: Wire SolarEdge Endpoints to Express Server

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-api-endpoints`

## Description
Integrate all SolarEdge services into the Express server and expose 6 MVP endpoints. Connect polling to alert evaluation, analytics recording, and status responses.

## Steps
1. [ ] Create route handlers in `backend/src/server.ts`
2. [ ] Implement `GET /api/solar/status` endpoint
3. [ ] Implement `POST /api/solar/refresh` endpoint
4. [ ] Implement `GET /api/solar/daily-analytics` endpoint
5. [ ] Implement `GET /api/solar/alerts` endpoint
6. [ ] Implement `POST /api/solar/thresholds` endpoint
7. [ ] Implement `GET /api/solar/thresholds` endpoint
8. [ ] Integrate alert evaluation into polling loop

## Definition of Done (DOD)
- All 6 endpoints respond with correct status codes
- Request/response payloads match PRD specification
- Alert evaluation runs on every polling cycle
- Analytics updated on every snapshot
- Error responses include meaningful messages
- npm test passes; npm run build succeeds
- No breaking changes to `/api/health` endpoint

## Expected Output
- Updates to `backend/src/server.ts` (~140 lines of new endpoint code)
  - Services initialized at server startup
  - Polling starts on server init
  - Polling loop: fetch → evaluate alerts → record snapshot → compute stats
  - All endpoints return typed JSON responses
  - Error handling with 4xx/5xx status codes

## Notes for Developer
- Instantiate services once at server startup
- Call `evaluateThreshold()` during each polling cycle
- Call `recordSnapshot()` after successful fetch
- Return stale data with `stale: true` flag if cache fallback
- All responses should include `timestamp` field
