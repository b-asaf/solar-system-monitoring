#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# 1. Get the current branch name
BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)

if [ "$BRANCH_NAME" == "main" ]; then
    echo " [ERROR] You are on the main branch. Agents cannot submit from main."
    exit 1
fi

echo " [Agent Skill] Preparing to submit work from branch: $BRANCH_NAME"

# 2. Automatically format a clean commit message if there are unstaged changes
if [ -n "$(git status --porcelain)" ]; then
    echo " [Agent Skill] Staging and committing remaining changes..."
    git add .
    git commit -m "feat: automated agent update for $BRANCH_NAME"
fi

# 3. Push to origin
echo " [Agent Skill] Pushing branch to GitHub..."
git push origin "$BRANCH_NAME"

# 4. Create Pull Request using GitHub CLI
echo " [Agent Skill] Creating Pull Request automatically..."
gh pr create \
    --title "Automated PR: $BRANCH_NAME" \
    --body "This Pull Request was automatically generated and submitted by the Agent Framework for branch \`$BRANCH_NAME\`. Please review the corresponding Task file in \`agents/tasks/\` to verify the Definition of Done (DOD)." \
    --reviewer "@me"

echo " [SUCCESS] PR created successfully! Over to you, Manager."