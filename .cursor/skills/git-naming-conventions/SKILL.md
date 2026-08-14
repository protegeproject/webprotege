---
name: git-naming-conventions
description: >-
  Enforces GitHub branch naming, Conventional Commits / PR titles, and
  Conventional Comments for Issue/PR/Review. Use when creating branches,
  writing commit messages, opening PRs, drafting or posting GitHub comments
  or reviews, or when the user asks about 分支命名, 提交备注, commit message,
  PR title, GitHub comment, review comment, or 评论规范.
---

# Git Naming & Comment Conventions

Standardize branch names, commit messages, PR titles, and GitHub comments.
Always-on summary: `.cursor/rules/git-branch-commit.mdc`.
Examples: [reference.md](reference.md).

## When to use

- Create / rename a branch
- Draft or finalize a commit message
- Create a PR (title + body)
- Draft or post Issue / PR / Review comments (`gh issue comment`, `gh pr comment`, `gh pr review`)
- Audit whether current branch / commits / comment drafts comply

## Workflow checklist

```
Progress:
- [ ] 1. Resolve base branch
- [ ] 2. Choose branch type + name
- [ ] 3. Create branch from updated base
- [ ] 4. Draft Conventional Commit message
- [ ] 5. Validate before commit / push / PR
- [ ] 6. PR title & body
- [ ] 7. GitHub comments / reviews (when posting)
```

### 1. Resolve base branch

```bash
git symbolic-ref refs/remotes/origin/HEAD 2>/dev/null || true
git remote show origin | sed -n '/HEAD branch/s/.*: //p'
```

Use that default (`master` or `main`). Never push directly to it.

### 2. Choose branch name

Pattern: `<type>/<optional-ticket>-<short-desc>`

Allowed `type`: `feature` | `fix` | `hotfix` | `test` | `refactor` | `docs` | `chore` | `release`

Map intent → type:

| User intent | Branch type |
|-------------|-------------|
| New capability | `feature` |
| Bug (non-prod-urgent) | `fix` |
| Production emergency | `hotfix` |
| Deploy/CI verification | `test` |
| Docs only | `docs` |
| Refactor, same behavior | `refactor` |
| Tooling/deps | `chore` |
| Cut a release | `release` |

Sanitize `short-desc`: lowercase, kebab-case, no spaces/`_`/camelCase.

### 3. Create branch

```bash
git fetch origin
git checkout <base>
git pull --ff-only origin <base>
git checkout -b <type>/<optional-ticket>-<short-desc>
```

If current branch name is non-compliant and unpushed / safe to rename:

```bash
git branch -m <compliant-name>
```

If already pushed under a bad name, rename locally then `git push -u origin HEAD` and delete the old remote branch only when the user explicitly asks.

### 4. Draft commit message

```
<type>(<optional-scope>): <subject>

[optional body — why, not file dump]

[optional footer — Closes #N / BREAKING CHANGE:]
```

Commit `type` ↔ branch `type` (usual):

| Branch | Commit type |
|--------|-------------|
| `feature/` | `feat` |
| `fix/`, `hotfix/` | `fix` |
| `docs/` | `docs` |
| `refactor/` | `refactor` |
| `test/` | `test` or `chore` |
| `chore/` | `chore` / `ci` / `build` |
| `release/` | `chore` |

Subject rules: imperative, present tense, no trailing period, ≤ 72 chars, focus on **why**.

When the user asks to commit, follow the repo commit user-rule (status/diff/log → HEREDOC message → status). Message **must** match this format.

### 5. Validate

Before `git commit` / `git push` / `gh pr create`, run:

```bash
python3 .cursor/skills/git-naming-conventions/scripts/validate_names.py --branch "$(git branch --show-current)" --message "$(git log -1 --pretty=%B 2>/dev/null || true)"
```

Or validate a draft only:

```bash
python3 .cursor/skills/git-naming-conventions/scripts/validate_names.py --branch 'feature/123-add-search' --message 'feat(search): add entity short-form lookup'
```

Validate a comment draft:

```bash
python3 .cursor/skills/git-naming-conventions/scripts/validate_names.py --comment 'suggestion: extract null-check into Optional'
```

Fix any `FAIL` before proceeding.

### 6. PR title & body

- **Title**: same Conventional Commit line as the primary change (or a summary of the branch theme).
- **Body** (via HEREDOC for `gh pr create`):

```markdown
## Summary
- <1-3 bullets: what/why>

## Test plan
- [ ] <verification steps>

Closes #<issue>   # if applicable
```

### 7. GitHub comments & reviews

Use **Conventional Comments** for Issue comments, PR conversation comments, and review bodies/line comments:

```
<label> [optional decorations]: <subject>

[optional body]
```

| Intent | label | Default |
|--------|-------|---------|
| Must fix before merge | `issue` or `todo` | blocking |
| Concrete improvement | `suggestion` | non-blocking unless `(blocking)` |
| Style nits | `nitpick` | non-blocking |
| Need clarification | `question` | wait for answer |
| Idea only | `thought` | non-blocking |
| Thanks / good pattern | `praise` | non-blocking |
| Process / CI note | `chore` / `note` | context-dependent |

**Posting with `gh` (always HEREDOC):**

```bash
# Issue comment
gh issue comment <n> --body "$(cat <<'EOF'
question: can you share the browser version where this reproduces?

Without that we cannot match server logs to the failure window.
EOF
)"

# PR conversation comment
gh pr comment <n> --body "$(cat <<'EOF'
suggestion: move the Optional unwrap next to the Lucene hit mapping

Keeps null handling consistent with DeprecatedEntitiesIndexLuceneImpl.
EOF
)"

# PR review (comment | approve | request-changes)
gh pr review <n> --request-changes --body "$(cat <<'EOF'
issue (blocking): logout does not invalidate the server session before clearing the cookie

Concurrent requests can reuse a half-closed session. Please invalidate first, then clear the client cookie.
EOF
)"
```

Review event mapping:
- any `issue` / `todo` unresolved → prefer `--request-changes`
- only `suggestion` / `nitpick` / `thought` / `praise` → `--comment` or `--approve`
- never approve with unresolved blocking comments

## Hard stops

- Do not commit with subject `update`, `fix`, `WIP`, or empty body-only dumps of filenames.
- Do not create branches like `ArchitectureEnhancement`, `Feature/Foo`, `my_branch`.
- Do not push to default branch; open a PR.
- Do not invent ticket IDs; omit the ticket segment if none exists.
- Do not post GitHub comments that are only `LGTM`, `+1`, `fix this`, or vague criticism without label + subject + evidence.

## Additional resources

- Examples and anti-patterns: [reference.md](reference.md)
- Always-on rule: `.cursor/rules/git-branch-commit.mdc`
