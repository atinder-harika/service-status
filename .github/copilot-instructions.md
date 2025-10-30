# Copilot Instructions for Service Status Monitor

This document defines the project's architecture, code quality standards, and conventions. All contributions MUST follow these guidelines.

---

## 📁 Project Overview

**Full-stack service health monitoring platform**
- **Frontend:** React 18 + TypeScript + Vite + Tailwind CSS
- **Backend:** Spring Boot 3.5.7 + Java 21 + PostgreSQL
- **Deployment:** GitHub Pages (frontend), Railway/Render (backend), Supabase (database)
- **CI/CD:** GitHub Actions (tests + build + deploy)

---

## 🌳 Branching & Workflow

**Branch Strategy:**
- `main` - Production (auto-deploys to GitHub Pages) - **Protected**
- `development` - Integration branch - **PR target for all contributors**
- Feature branches: `feat/`, `fix/`, `docs/`, `chore/`, `test/`, `refactor/`

**Workflow:**
1. Fork repository
2. Create branch from `development`
3. Make changes + tests
4. Push to fork
5. Open PR to `development` (NOT `main`)
6. CI tests run automatically
7. Maintainer reviews and merges
8. Maintainer periodically merges `development` → `main` for deployment

---

## 🏗️ Architecture Rules (Non-Negotiable)

### Backend: Strict Layered Architecture
- **Pattern:** Controller → Service → Repository (MUST follow)
- Controllers: Thin, handle HTTP only
- Services: Business logic, scheduling, WebSocket dispatch
- Repositories: Data access only
- All `@Scheduled` tasks: Use `fixedDelay` (not `fixedRate`), wrap in `try/catch`

### Frontend: TypeScript Strict Mode
- **NO `any` type** (strictly prohibited)
- All props, state, models MUST have explicit `interface` or `type`
- Functional components ONLY (no class components)
- React Hooks for state management
- Tailwind utility classes (no custom CSS)

### Testing Requirements
- **Coverage:** 70%+ lines minimum
- Test happy path + ≥3 edge/error cases
- Frontend: Vitest + React Testing Library
- Backend: JUnit 5 + Mockito (all mocked, no DB dependency)

---

## 📝 Code Style

### Frontend (React/TypeScript)
- PascalCase: Components (`ServiceCard.tsx`)
- camelCase: Variables, functions, hooks
- File naming: `ComponentName.tsx`, `fileName.ts`
- Export types from `src/types/`

### Backend (Java/Spring Boot)
- Java 21+ features (records, pattern matching)
- Java Streams over loops (where clear)
- Naming: `*Controller`, `*Service`, `*Repository`
- Lombok annotations for boilerplate reduction

---

## 🗂️ Project Structure

```
service-status/
├── frontend/          # React + TypeScript + Vite
│   ├── src/
│   │   ├── pages/     # Page controllers
│   │   ├── components/ # Reusable UI
│   │   ├── services/  # API layer
│   │   ├── hooks/     # Custom hooks
│   │   ├── utils/     # Pure functions
│   │   ├── config/    # Constants
│   │   └── types/     # TypeScript interfaces
│
├── backend/           # Spring Boot + Java 21
│   ├── src/main/java/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   └── dto/
│   └── src/main/resources/
│       └── db/migration/  # Flyway SQL
│
└── .github/workflows/ # CI/CD
```

---

## 🎨 Conventions

### Status Colors (Tailwind)
- Operational: `text-status-operational` (green)
- Degraded: `text-status-degraded` (yellow)
- Maintenance: `text-status-maintenance` (blue)
- Down: `text-status-down` (red)

### Commit Messages
Follow [Conventional Commits](https://www.conventionalcommits.org/):
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `test:` Tests
- `chore:` Maintenance

---

## ⚠️ Critical Cautions

1. **Never commit credentials** (API keys, passwords, tokens)
2. **Use `.env.example`** templates, not actual `.env` files
3. **Backend tests** must NEVER require database (mock everything)
4. **Supabase:** Always use **Session Pooling** JDBC URL (not Direct Connection)
5. **PR target:** `development` branch (not `main`)

---

## 📚 Quick Reference

- **Dev setup:** See [SETUP.md](../SETUP.md)
- **Contributing:** See [CONTRIBUTING.md](../CONTRIBUTING.md)
- **Issues:** Check [GitHub Issues](https://github.com/atinder-harika/service-status/issues)

---

**This file is the source of truth for coding standards and architecture decisions.**
