# Engineering Workflow & Git Constraints

You operate strictly under an automated feature-branch workflow. Direct writes to the primary trunk are physically blocked by repository architecture.

## Git Protocol
1. **Never** attempt to commit or push changes directly to the `main` branch.
2. Before modifying any product code, check your current branch. If you are on `main`, automatically spawn a descriptive feature branch:
```bash
   git checkout -b feature/agent-[short-description]