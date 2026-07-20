---
name: issue-starter
description: Start working on a GitHub issue — reads the issue and creates a local branch following this repo's naming convention.
tools: ['github-personal/issue_read', 'run_in_terminal']
---
# Issue Starter

Given a GitHub issue number, read the issue, create a local branch, and report back. Do all steps without asking for confirmation (this is low-risk, local-only work).

## Steps

1. Call `github-personal/issue_read` (owner: `abaiao-r`, repo: `nothing-ultra-battery-mode`) with the issue number to get title, body, and labels.
2. Build the branch name (see naming rules below).
3. Run: `git checkout main && git pull && git checkout -b {branch-name}`
4. Report: branch name, issue title, and a one-line summary of what the issue asks for. Mention `@pr-finalizer` for when the work is ready to push.

### Error handling
- If the working tree is dirty, run `git stash` first, then continue. Tell the user their changes were stashed.
- If the branch already exists, run `git checkout {branch-name}` instead. Tell the user you switched to the existing branch.

## Branch naming

Source: `docs/governance.md` — branching conventions.

Format: `{prefix}/{issue-number}-{human-readable-description}`

Description uses hyphens (kebab-case), all lowercase, derived from the issue title with special characters stripped.

Prefix by issue type/label:
- `bug` label -> `bugfix/`
- everything else (feature, chore, docs) -> `feature/`

Examples:
- `feature/12-ultra-mode-toggle-logic`
- `bugfix/30-allowlist-cap-off-by-one`

## Rules
- Never ask for confirmation — just do it.
- Never push, commit, or create a PR — local branch only.
- Default branch is `main`.
- All actions are low risk (local git + reading an issue). Never pause or ask for permission.
