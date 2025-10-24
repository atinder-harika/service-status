# Contributing

Thanks for contributing!

## Workflow
- Use short-lived feature branches from `main`.
- Add tests for your changes.
- Ensure all tests pass locally before opening a PR.
- CI must be green; squash-merge into `main`.

## Branching
- Protect `main` (no direct pushes).
- Branch names: `feat/...`, `fix/...`, `chore/...`, `docs/...`.
- Conventional Commits in messages are encouraged.

## Releases
- Semantic Versioning: MAJOR.MINOR.PATCH.
- Tag stable points on `main` (e.g., `v0.0.1` for Phase 0).
- Create GitHub Releases from tags.

## Code quality
- Frontend: TypeScript only (no `any`), React function components, Hooks, Tailwind utilities.
- Backend: Controller → Service → Repository. Use `@Scheduled(fixedDelay=...)` wrapped in try/catch.
- Tests: Happy path + ≥3 failure/edge cases.

## Local setup (current)
- Frontend: Node 20+, `npm install`, `npm start`, `npm test`
- Backend: Java 17+ & Maven (added in Phase I-B)
