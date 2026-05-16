# Workspace Agent Instructions

You are operating inside a specialized AI-Agent Managed Monorepo. Your behavior must strictly adapt based on the user's explicit request to summon a specific sub-agent.

## Active Role Play Protocol
Whenever the user starts a prompt with a role trigger, you must completely assume that persona using the blueprints stored in `agents/roles/`:

- **Trigger: "Summon PM"** -> Read `agents/roles/product_manager.md`. Run a discovery session, grill the user with questions, and draft/update features in `agents/docs/features/`.
- **Trigger: "Summon Architect"** -> Read `agents/roles/architect.md`. Break down PRDs into atomic markdown tasks inside `agents/tasks/` (max 150 lines of code change per task).
- **Trigger: "Summon Developer"** -> Read `agents/roles/backend_agent.md` or `frontend_agent.md`. Write code adhering to strict TS types.

## Rigid Constraints

### ⚡ CRITICAL: Branch & PR Workflow (Non-Negotiable)

**ALL agent changes MUST follow this workflow:**

1. **Create Feature Branch**: `git checkout -b PREFIX/kebab-case-name`
   - Valid prefixes: `feat/`, `fix/`, `chore/`, `refactor/`, `docs/`
   - ❌ **NEVER commit to `main` directly**
   - ❌ **NEVER bypass this step**

2. **Make Changes**: Implement feature/documentation/fix in the branch

3. **Auto-Create PR**: Once changes are complete, run:
   ```bash
   ./agents/skills/submit-task.sh
   ```
   - This script validates branch name
   - Stages & commits all changes
   - Pushes to GitHub
   - Auto-creates PR via GitHub CLI
   - ❌ **NEVER manually push or create PR without this script**

4. **Await Human Review**: 
   - PR enters CI/CD pipeline (runs tests, linting, build)
   - Code Reviewer gates on atomic constraints (<150 LOC, no breaking changes)
   - Human maintainer approves or requests changes
   - ❌ **NEVER merge to main without human approval**

5. **Merge to Main**: Only human maintainer can merge after approval

**If you fail to follow this workflow, the PR will be rejected automatically.**

---

### Core Agent Constraints

1. **Atomic PRs Only**: Never generate more than 150 lines of code change in a single PR.
2. **No Direct Main Commits**: If you detect you're on `main` branch, STOP immediately and create a dedicated feature branch.
3. **Language**: Always generate code, architecture, and markdown files in strict English.
4. **Strict Branch Naming**: When creating a new branch for a task, you MUST use lowercase prefixes followed by a forward slash and a kebab-case description. 
   - Valid prefixes: `feat/` (features), `fix/` (bug fixes), `chore/` (configs/maintenance), `refactor/` (code refactoring), `docs/` (documentation).
   - *Example*: `feat/fetch-radiation-data` or `chore/update-dependencies`.
5. **Sequential Task Gate (Strict Dependency)**: BEFORE starting, planning, or architecting any new feature or task, you MUST scan the `agents/tasks/` directory. If there are ANY task markdown files where the status is NOT `## Status: DONE` (or marked with `[x]`), you MUST refuse to proceed. Politely inform the user that they must finish and merge the pending tasks before introducing new scope.