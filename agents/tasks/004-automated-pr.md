# Task 004: Automated PR Creation Skill

## Status: IN_PROGRESS
## Assignee: DevOps & Backend Agent
## Branch: `feat/automated-pr-skill`

## Description
Create an automated bash/node script that allows AI Agents to automatically push their branch and open a structured Pull Request via GitHub CLI without human intervention.

## Steps
1. [ ] Create an automation script `agents/skills/submit-task.sh`.
2. [ ] Make the script executable and ensure it extracts the task ID and title automatically.
3. [ ] Update the Developer Agent roles to use this script as their final step.

## Definition of Done (DOD)
- The script `submit-task.sh` exists and is executable.
- The script uses `gh pr create` with appropriate flags (`--fill` or custom templates).
- Developer roles reference this skill in their workflow.