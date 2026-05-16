# Task 003: Atomic PR & Continuous Integration Protocol

## Status: IN_PROGRESS
## Assignee: Architect & Code Reviewer Agent
## Branch: `feat/atomic-pr-protocol`

## Description
Establish architectural rules that force Developer Agents to break large features into small, isolated, and runnable Pull Requests (max 150 lines of code change per PR) without breaking the `main` branch.

## Steps
1. [ ] Create `agents/roles/code_reviewer.md` constraints for PR size.
2. [ ] Establish the Feature Branching & Feature Flag strategy documentation.
3. [ ] Create a core architecture rule: Every PR must compile and pass tests independently.

## Definition of Done (DOD)
- Protocol rules are safely committed to `agents/roles/code_reviewer_agent.md`.
- No code layout is altered, only governance files are added.
- CI pipeline from Task 002 remains green.