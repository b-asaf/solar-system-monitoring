# Task 006: Define SolarEdge Domain Models & Types

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-domain-models`

## Description
Create TypeScript interfaces and domain models for the SolarEdge Solar Monitoring system. Define the contract between the backend services, API layer, and external SolarEdge API.

## Steps
1. [ ] Extend `shared/types.ts` with new SolarEdge domain interfaces
2. [ ] Create `backend/src/types/solar.ts` for SolarEdge API response contracts
3. [ ] Create `backend/src/types/alerts.ts` for alert domain models
4. [ ] Create `backend/src/types/cache.ts` for cache structures
5. [ ] Ensure all types use strict TypeScript (no `any`)

## Definition of Done (DOD)
- All new type files created and exported from appropriate index files
- Code compiles with `npm run build`
- No breaking changes to existing `shared/types.ts` interfaces
- All types are documented with JSDoc comments

## Expected Output
- `shared/types.ts` updated with: `SolarTelemetry`, `SystemHealth` (already exist), plus new `DailyAnalytics`, `AlertThreshold`
- `backend/src/types/solar.ts`: SolarEdge API response types
- `backend/src/types/alerts.ts`: Alert, AlertThreshold, AlertEvaluation
- `backend/src/types/cache.ts`: CacheEntry, CacheConfig

## Notes for Developer
- Reference SolarEdge API docs for real response structure
- Keep types focused on MVP requirements only
- Use `readonly` for immutable structures
- Export all types for reuse across services
