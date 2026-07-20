---
name: pr-finalizer
description: Finalize a PR for review — commits, pushes, creates or updates the PR from this repo's template, and links the issue.
tools: ['github-personal/search_pull_requests', 'github-personal/pull_request_read', 'github-personal/create_pull_request', 'github-personal/update_pull_request', 'run_in_terminal', 'read_file']
---
# PR Finalizer

Execute every step for real. If anything fails, STOP and tell the user.
Never construct URLs — only use URLs from tool responses.
GitHub: `github.com`, owner `abaiao-r`, repo `nothing-ultra-battery-mode`.
NEVER set reviewers, request reviews, or merge the PR — that stays a manual step for a solo project.

## How It Works

Parse what the user wants. Default is the full flow (commit, push, create/update PR). The user may ask for specific actions:

| User says | What to do |
|---|---|
| `@pr-finalizer` | Full flow: commit -> push -> create/update PR |
| `@pr-finalizer push` | Just commit + push (amend existing commit). Update PR description if PR exists. |
| `@pr-finalizer squash` | Squash all commits into 1, force push |
| `@pr-finalizer update` | Just refresh the PR description from the current diff |
| `@pr-finalizer help` | Show this command table |

## Steps (Full Flow)

### 1. Get issue info and stage
- `git branch --show-current` -> extract issue number (e.g. `feature/12-ultra-mode-toggle` -> `12`)
- Call `github-personal/issue_read` for that number to get the title, if available. If it fails, ask the user.
- `git add .`

### 2. Commit and push (single commit rule — this repo requires exactly one commit per branch)
Run `git log --oneline main..HEAD` to count commits:
- **0 commits:** `git commit -m "Short description"` then `git push -u origin HEAD`
- **1 commit:** `git commit --amend --no-edit` then `git push --force-with-lease`
- **2+ commits:** squash with `git reset --soft $(git merge-base HEAD main) && git commit -m "Short description"` then `git push --force-with-lease`. Confirm with the user before squashing if they didn't ask for it explicitly.

Commit message: short, imperative, no issue-number prefix (the PR title/body carries the link).

### 3. Create or update PR
- Find an existing PR for this branch via `github-personal/search_pull_requests` or `github-personal/pull_request_read`.
- If none exists, create one with base `main` using `github-personal/create_pull_request`.
- PR title: short description of the change.
- Fill the description using this repo's `.github/PULL_REQUEST_TEMPLATE.md` sections:

```
## Issue
Closes: #{issue-number}

## Summary
{2-3 sentences describing the change, from the issue + diff}

## ATP Scenarios
{List ATP scenario IDs added/changed (ATP-<area>-<number>), or "not impacted" with a one-line reason}

## How to Test
{Steps for the reviewer to verify the change}

## Checklist
- [x] Linked issue
- [x] One commit per branch
- [x] Unit tests updated (if applicable)
- [x] ATP scenarios updated or marked not impacted
```

When updating an existing PR, refresh all sections from the current diff without discarding content the user manually added.

### 4. Report
Tell the user: PR URL, whether it was created or updated, and remind them CI (governance, build, unit, ATP checks) must pass before they merge (squash merge only, per branch protection).
