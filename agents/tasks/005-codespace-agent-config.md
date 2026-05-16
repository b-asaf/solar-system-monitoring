# Task 005: Configure Codespace Native Agent Rules

## Status: DONE
## Assignee: Architect & DevOps Agent
## Branch: `feat/configure-codespace-agent`

## Description
Configure GitHub Copilot / VS Code Agent in the Codespace to recognize and embody the custom roles (PM, Architect, etc.) defined in `agents/roles/` using native workspace instructions.

## Steps
1. [x] Create a `.github/copilot-instructions.md` or `.vscode/settings.json` rule set.
2. [x] Instruct the native agent to always look at `agents/roles/` before execution.
3. [x] Run the first product discovery session using the native chat window.

## Definition of Done (DOD)
- Configuration file is committed.
- Native Codespace Agent successfully reads our roles and acts as the PM when requested.