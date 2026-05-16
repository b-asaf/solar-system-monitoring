# Task 009: Implement Daily Analytics Service

## Status: NOT_STARTED
## Assignee: Backend Agent
## Branch: `feat/solar-analytics-service`

## Description
Create the `AnalyticsService` that aggregates 10-minute power snapshots into daily statistics: total yield (kWh), efficiency %, peak power, and peak time. Store aggregates persistently for historical tracking.

## Steps
1. [ ] Create `backend/src/services/AnalyticsService.ts`
2. [ ] Implement `recordSnapshot()` (store 10-min samples)
3. [ ] Implement `computeDailyStats()` (aggregate current day)
4. [ ] Implement `getDailyAnalytics()` (return cached daily stats)
5. [ ] Persist daily stats to JSON file or lightweight store

## Definition of Done (DOD)
- Daily yield calculated from power samples
- Peak power and time tracked
- Efficiency % calculated (vs. system capacity)
- Stats persisted across server restarts
- All calculations verified with unit tests
- npm test passes; code compiles

## Expected Output
- `backend/src/services/AnalyticsService.ts` (~110 lines)
  - Constructor accepts storage path
  - `recordSnapshot(power, timestamp)` stores sample
  - `computeDailyStats()` aggregates current day
  - `getDailyAnalytics()` returns DailyAnalytics object
  - `persistToDisk()` saves to JSON file

## Notes for Developer
- Store snapshots in memory per day
- At day boundary, flush to disk
- Daily yield = sum(power * interval / 60) in kWh
- Peak = max(power) for the day
- Efficiency % = (total yield / theoretical max) * 100
- Use `fs.promises` for async file I/O
