# Task 002: CI Pipeline Setup & Agent Registry Definition

## Status: DONE
## Assignee: Architect & DevOps Agent (Bootstrap Mode)
## Branch: `feat/ci-pipeline-and-agents-registry`

## Description
1. Create a GitHub Actions workflow to run `npm test` on every Pull Request to `main`.
2. Define the structural roles for the core agents: Product, Architect, BE, FE, QA, and Code Reviewer.

## Steps
1. [x] Create a GitHub Actions workflow at `.github/workflows/ci.yml` that triggers on PRs.
2. [x] Add a dummy test script in `backend/package.json` to ensure the CI passes initially.
3. [x] Create the full profile files for all requested agents under `agents/roles/`.

## Definition of Done (DOD)
- GitHub Actions automatically triggers when a PR is created.
- The command `npm test` runs successfully in the pipeline environment.
- All 6 agent role files exist in the codebase in English.