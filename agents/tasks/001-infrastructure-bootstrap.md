# Task 001: Infrastructure Bootstrap

## Status: IN_PROGRESS
## Assignee: Backend Agent
## Branch: `feat/bootstrap-agent-infrastructure`

## Description
Remove legacy Java, Cursor, and pipeline configurations. Initialize a clean Node.js/TypeScript environment for the Backend and set up the foundation for Agent roles.

## Steps
1. [x] Create branch `feat/bootstrap-agent-infrastructure`.
2. [x] Remove legacy Java files, pipeline workflows, and `.cursor` directory.
3. [ ] Initialize Node.js `package.json` in `/backend`.
4. [ ] Create a basic Express server in TypeScript (`backend/src/server.ts`).
5. [ ] Define initial shared domain models in `shared/types.ts`.

## Definition of Done (DOD)
- Repository is clear of Java/Cursor artifacts.
- Backend project compiles with TypeScript and runs an Express health-check endpoint.
- All code, tasks, and roles are written in strict English.
- Pull Request is opened for human review.