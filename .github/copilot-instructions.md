# Copilot instructions for this repo

This document consolidates the project architecture, code quality contracts, and conventions. All future work MUST strictly adhere to these instructions.

---

## PROJECT STATUS: ✅ PHASE I COMPLETE

**Current Version:** v1.0.0  
**Status:** Full-stack application with CI/CD deployed and operational

### Completed Phases:
- ✅ **Phase 0:** Repository setup, docs, LICENSE, basic CI
- ✅ **Phase I-A:** Frontend migration (Vite + TypeScript + Tailwind)
- ✅ **Phase I-B:** Backend development (Spring Boot + PostgreSQL + REST API)
- ✅ **Phase I-C:** CI/CD pipeline (GitHub Actions, automated testing, frontend deployment)

### Current Deployment:
- **Frontend:** GitHub Pages (auto-deployed via CI/CD)
- **Backend:** Manual deployment (Render/Railway/Heroku)
- **Database:** Supabase PostgreSQL
- **CI/CD:** GitHub Actions (tests + build + deploy on every push)

### Next Steps:
See [FUTURE_PLANS.md](../FUTURE_PLANS.md) for planned enhancements (WebSocket, metrics, circuit breaker, etc.)

---

## PHASE 0: Repository Setup & Professionalization ✅ COMPLETED

**Status:** Phase 0 complete, tagged as v0.0.1

### A. GitHub Configuration (Completed)
- Repository name: `service-status`
- Description: "Real-time service health monitoring platform with Spring Boot and React"
- Topics/Tags: full-stack, react, springboot, health-check, monitoring, observability, java, typescript, postgresql, ci-cd

### B. Foundational Documents (Completed)
- ✅ [`LICENSE`](../LICENSE) - MIT License, Copyright (c) 2025 Atinder Singh Hari
- ✅ [`README.md`](../README.md) - Project overview, features, tech stack, quick start
- ✅ [`SETUP.md`](../SETUP.md) - Detailed local development setup guide
- ✅ [`CONTRIBUTING.md`](../CONTRIBUTING.md) - PR workflow, code style, testing requirements
- ✅ [`FUTURE_PLANS.md`](../FUTURE_PLANS.md) - Planned enhancements with priorities
- ✅ [`.github/workflows/full-stack-ci.yml`](workflows/full-stack-ci.yml) - CI/CD pipeline

### C. Versioning & Releases
- **Semantic Versioning:** MAJOR.MINOR.PATCH
- **Tagging strategy:**
  - v0.0.1 = Phase 0 (docs, CI)
  - v0.1.0 = Phase I-A (frontend migration)
  - v0.2.0 = Phase I-B (backend REST API)
  - v1.0.0 = Phase I-C (CI/CD complete, deployed)
- **Branching:**
  - `main` is protected (no direct pushes)
  - Feature branches: `feat/...`, `fix/...`, `chore/...`, `docs/...`
  - PR required, CI must pass, squash-merge into `main`

---

## PHASE I-A: Frontend Migration to Vite + TypeScript + Tailwind ✅ COMPLETED

**Status:** ✅ Complete, deployed to GitHub Pages

### Migration Summary
Migrated from Create React App (CRA) to Vite + TypeScript + Tailwind while preserving exact UI behavior and styling.

### Key Changes
1. **Build tool:** CRA → Vite 6.x
   - Faster dev server with HMR
   - Production builds to `dist/` instead of `build/`
   - Deploy script updated: `gh-pages -d dist`

2. **Language:** JavaScript → TypeScript (strict mode, no `any`)
   - `tsconfig.json` with strict compiler options
   - All files migrated: `.js` → `.tsx`, `.ts`
   - Type definitions in `src/types.ts`

3. **Styling:** Custom CSS → Tailwind CSS 3.x
   - `tailwind.config.js`, `postcss.config.js` configured
   - `src/index.css` now uses `@tailwind` directives
   - All components use Tailwind utility classes
   - Exact visual parity with original design

4. **Testing:** Jest (via react-scripts) → Vitest 2.x
   - `vitest.config.ts` with jsdom environment
   - Tests pass: 2/2 in `src/App.test.tsx`
   - CI updated to use `npx vitest run --coverage`

### Current File Structure (Frontend)
```
/
├── index.html (Vite entry point)
├── vite.config.ts (Vite config)
├── vitest.config.ts (Vitest config with test setup)
├── tsconfig.json (TypeScript strict config)
├── tailwind.config.js (Tailwind config with custom status colors)
├── postcss.config.js (PostCSS for Tailwind)
├── package.json (Vite scripts: dev, build, test, deploy)
├── .vscode/settings.json (suppress CSS @tailwind warnings)
└── src/
    ├── main.tsx (entry point, mounts React app)
    ├── index.css (Tailwind directives + base styles)
    ├── App.tsx (wrapper component)
    ├── StatusPage.tsx (main UI with service status logic)
    ├── types.ts (TypeScript interfaces: ServiceStatus, ServiceCheck, ServiceGroup, Incident)
    ├── data.ts (SERVICE_CONFIG, INCIDENTS_DATA)
    ├── setupTests.ts (imports @testing-library/jest-dom)
    └── App.test.tsx (Vitest tests, 2 passing)
```

### Developer Workflows (Updated)
- **Dev server:** `npm run dev` → http://localhost:5173/service-status/
- **Build:** `npm run build` (TypeScript check + Vite build)
- **Test:** `npm test` (Vitest watch mode) or `npx vitest run` (CI mode)
- **Deploy:** `npm run deploy` (builds to `dist/`, deploys to GitHub Pages)

### Architecture and Data Flow (Current)
- **Entry:** `src/main.tsx` mounts `<App />` in StrictMode
- **Composition:** `src/App.tsx` renders `<StatusPage />` inside Tailwind container
- **Core UI:** `src/StatusPage.tsx`
  - Sources `SERVICE_CONFIG` and `INCIDENTS_DATA` from `src/data.ts`
  - Local state `services` (type `ServiceGroup[]`) holds groups with computed `status`
  - `updateAllServices()` randomizes status: 80% 'Operational', 20% 'Down' (will be replaced by real API calls in Phase I-B)
  - Runs on mount and every 60s via `setInterval`
  - Renders service groups as clickable status cards + static incidents list
- **Styling:** Tailwind utilities with custom colors in `tailwind.config.js`:
  - `status-operational`: #16a34a (green)
  - `status-degraded`: #fbbf24 (yellow)
  - `status-maintenance`: #3b82f6 (blue)
  - `status-down`: #e74c3c (red)

### Data Contracts (TypeScript)
See `src/types.ts`:
```typescript
type ServiceStatus = 'Operational' | 'Degraded' | 'Maintenance' | 'Down';
interface ServiceCheck { name: string; url: string; status?: ServiceStatus; }
interface ServiceGroup { title: string; checks: ServiceCheck[]; }
interface Incident { serviceName: string; type: 'warning' | 'info'; message: string; }
```

`SERVICE_CONFIG` (see `src/data.ts`):
- Array of `ServiceGroup[]`
- Example groups: "My Services", "External Services"
- Each check has `name`, `url`; `status` is augmented at runtime

`INCIDENTS_DATA`:
- Array of `Incident[]`
- Static data for now (Phase I-B will fetch from backend)

### Known Issues (Non-blocking)
- `vitest.config.ts` has a TypeScript error due to Vitest bundling its own Vite version (type mismatch with `@vitejs/plugin-react`). **Suppressed with `@ts-expect-error` comment.** Tests run perfectly.
- CSS warnings about `@tailwind` directives are suppressed in `.vscode/settings.json` (processed by PostCSS at build time, not actual errors).

---

## PHASE I: Project Architecture & Stack (The Blueprint)

### A. Core Technologies & Languages

| Component | Technology | Rationale | Status |
|-----------|-----------|-----------|--------|
| Frontend UI | React 18 + TypeScript | Static analysis, maintainability | ✅ Migrated |
| Build Tool | Vite 6.x | Fast dev server, optimized builds | ✅ Configured |
| Styling | Tailwind CSS 3.x | Utility-first, consistent design | ✅ Integrated |
| Frontend Tests | Vitest 2.x + React Testing Library | Fast, Vite-native testing | ✅ Working |
| Backend API | Spring Boot (Java 17+) | Enterprise-grade, async, scheduling | 🔜 Phase I-B |
| Data Persistence | PostgreSQL (Supabase) | Robust, relational, free tier | 🔜 Phase I-B |
| Real-Time Layer | Spring WebSocket (STOMP) | Instant server-to-client updates | 🔜 Phase I-B |
| Health Check Engine | Spring @Scheduled Tasks | Background cron-like checks | 🔜 Phase I-B |
| Build Tool (Backend) | Maven | Simpler onboarding, extensive docs | 🔜 Phase I-B |

### B. Deployment & Hosting (Free Tier Strategy)
- **Frontend:** GitHub Pages (from `dist/` build output)
- **Backend:** Render free tier (Spring Boot app)
- **Database:** Supabase free tier (PostgreSQL)
  - Single Supabase project with two schemas: `dev`, `prod`
  - Separate DB users per schema
  - Flyway migrations target schema via env var
- **Keepalive strategy:** External cron job pings backend health endpoint to prevent Render free tier sleep (keeps all other free servers alive as a side effect)

### C. Quality & Performance Rules (Non-Negotiable)

**R1: Strict Layered Architecture (SoC) - Backend:**
- Controller → Service → Repository pattern (MUST)
- Service layer encapsulates all business logic (scheduling, health-check invocation, WebSocket dispatch)
- Controllers and Repositories MUST remain thin

**R2: Explicit React/TypeScript Typing:**
- NO `any` type usage (strictly prohibited)
- All component props, state, and data models MUST have explicit `interface` or `type` definitions
- Example: `src/types.ts` defines all domain types

**R3: Efficient Scheduling & Resilience - Backend:**
- All Spring `@Scheduled` tasks MUST use `fixedDelay` (not `fixedRate`) for back-pressure
  - Example: `@Scheduled(fixedDelay = 30000)` (30s default)
- All scheduled task logic MUST be wrapped in `try/catch` to prevent thread pool halts

**R4: Health Check Defaults (Backend - Phase I-B):**
- HTTP checks: connect timeout 2s, read timeout 3s (max 5s per check), 1 retry with exponential backoff (1s + jitter)
- DB checks: 2s query timeout, 1 retry
- Circuit breaker: open after 5 consecutive failures, cool-off 60s
- Scheduling: `fixedDelay = 30000` (30s)

### D. Code Style & Syntax Conventions

**Frontend (React/TS):**
- Functional Components ONLY (no class components)
- React Hooks for state management
- PascalCase for components, camelCase for variables/hooks
- Tailwind utility classes (no custom CSS files)
- File naming: `ComponentName.tsx` for components, `fileName.ts` for utils/types

**Backend (Java/Spring - Phase I-B):**
- Java 17+ features (e.g., `record` for DTOs)
- Java Streams over imperative loops (where clarity is maintained)
- Naming: Controllers end in `Controller`, Services in `Service`, Repositories in `Repository`

### E. Testing Strategy

**Frontend:**
- Vitest + React Testing Library
- Test files: `*.test.tsx` next to source files
- Coverage goal: 70%+ lines (initial), raise over time
- MUST cover: happy path + ≥3 failure/edge cases

**Backend (Phase I-B):**
- JUnit 5 + Mockito for Service/Repository unit tests
- Testcontainers for integration tests (or H2 for simplicity)
- Coverage goal: 70%+ lines
- MUST cover: happy path + ≥3 failure/edge cases (e.g., network timeout, service DOWN, invalid config)

---

## PHASE I-B: Backend Development (Next Steps - NOT STARTED)

### Monorepo Structure (Planned)
```
/
├── frontend/ (migrate current src/ into this folder)
├── backend/ (Spring Boot Maven project)
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── src/test/java/
│   └── pom.xml
├── .github/workflows/ci.yml (extend for backend tests)
└── README.md (update with backend setup)
```

### Backend Implementation Checklist
- [ ] Initialize Spring Boot project (Maven, Java 17+)
- [ ] Add dependencies: Spring Web, WebSocket, STOMP, JDBC, PostgreSQL driver, Flyway, Actuator
- [ ] Configure WebSocket/STOMP endpoints (e.g., `/ws`, topic `/topic/status`)
- [ ] Implement health check scheduler (HTTP, ping, DB checks)
- [ ] Create Controller → Service → Repository layers
- [ ] Add Flyway migrations for database schema (services, checks, history)
- [ ] Configure Supabase connection (environment variables)
- [ ] Add Spring Actuator for health/metrics endpoints
- [ ] Write JUnit 5 + Mockito unit tests
- [ ] Update CI workflow to run backend tests
- [ ] Deploy to Render free tier
- [ ] Wire frontend to backend WebSocket

### Database Schema (Planned - Phase I-B)
Fields: service/check id, timestamp, status, latency, httpCode, error message (if any)
Retention policy: TBD (e.g., 30 days rolling window)

---

## Conventions and Examples

### Adding Services
Edit `src/data.ts`:
```typescript
export const SERVICE_CONFIG: ServiceGroup[] = [
  {
    title: 'My Services',
    checks: [
      { name: 'GitHub Portfolio', url: 'https://example.com' },
      // Add more here
    ],
  },
];
```

### Status Color Mapping (Tailwind)
- `Operational` → `text-status-operational` (green #16a34a)
- `Degraded` → `text-status-degraded` (yellow #fbbf24)
- `Maintenance` → `text-status-maintenance` (blue #3b82f6)
- `Down` → `text-status-down` (red #e74c3c)

### Testing Examples
```typescript
// src/App.test.tsx
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from './App';

describe('App', () => {
  it('renders Service Status heading', () => {
    render(<App />);
    const heading = screen.getByRole('heading', { name: /service status/i });
    expect(heading).toBeInTheDocument();
  });
});
```

---

## User Preferences & Decisions

- **Author:** Atinder Singh Hari (GitHub: atinder-harika)
- **Repo name:** `service-status` (kept as-is, no rename needed)
- **Monorepo:** Single repo with frontend/ and backend/ folders
- **Vite vs CRA:** Vite chosen (faster, modern)
- **Maven vs Gradle:** Maven (simpler, better docs)
- **License:** MIT (free/open)
- **Hosting:** GitHub Pages (frontend), Render (backend), Supabase (DB) - all free tiers
- **Ports:** No preference (default: frontend 5173, backend 8080 suggested)
- **Spring Actuator:** Include (useful for health/metrics)
- **Naming conventions:** Use professional standards (consistent within project)
- **Ultimate goal:** Monitor multiple free services' uptime, use as keepalive cron job target to prevent Render/other free tier services from sleeping

---

## Notes for Future Copilot Sessions

If context resets, tell Copilot to:
1. Read this file (`.github/copilot-instructions.md`)
2. Check recent commits (`git log --oneline -10`)
3. Review current file structure
4. Check TODO list in chat history (if available)

This file is the **source of truth** for the project's architecture and decisions.
