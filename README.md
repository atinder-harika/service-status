# Service Status Monitor

> **Real-time service health monitoring platform** built with Spring Boot and React, featuring automated health checks, live status updates, and a modern responsive dashboard.

[![CI/CD](https://github.com/atinder-harika/service-status/actions/workflows/full-stack-ci.yml/badge.svg)](https://github.com/atinder-harika/service-status/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## Status

The backend is back on Railway (API: https://service-status-backend-production.up.railway.app/api/services), now on Railway's own PostgreSQL after the free Supabase database was shut down. The old deployment crashed after a few hours; the root cause is still being confirmed, so this version ships with the evidence it needs: a heap dump on out-of-memory, rolling GC logs, and Prometheus metrics for heap, threads and check timings.

---

## 📖 What is Service Status Monitor?

A full-stack web application that monitors the health and availability of multiple services in real-time. Perfect for tracking uptime of APIs, websites, and other HTTP/HTTPS endpoints with automated checks every 30 seconds.

**Key Features:**
- 🟢 Automated health checks for HTTP/HTTPS endpoints
- 📊 Real-time status dashboard with auto-refresh
- 💾 Persistent storage with PostgreSQL
- 🎨 Modern, responsive UI built with React + TypeScript + Tailwind
- 🧪 Comprehensive test coverage (JUnit, Mockito, Vitest)
- 🚀 CI/CD pipeline with GitHub Actions

---

## Design notes (why, how, trade-off)

- **Checks on virtual threads.** Each check runs on its own virtual thread, so a cycle takes as long as the slowest service instead of the sum of all of them. Blocking `WebClient.block()` is fine on a virtual thread. Trade-off: a burst of checks can now hit the DB pool at once, so the pool size (10) caps real concurrency on saves.
- **Timeouts at two levels.** A 5 s socket connect and response timeout, plus a 5 s reactive timeout, so a host that never answers can't hold a connection open.
- **JWT for writes.** Reads are public (it's a status page). `POST /api/auth/token` swaps the admin login for a short-lived HS256 JWT; adding or removing services needs the `admin` scope. Stateless, so no sessions or CSRF tokens. Trade-off: one shared admin, no user accounts.
- **Metrics.** `/actuator/prometheus` exposes `service_status_check` (per service, p95/p99), `service_status_check_cycle`, and up/down gauges, plus JVM heap, GC and thread metrics. `ops/docker-compose.yml` runs Postgres + backend + Prometheus + Grafana with a ready dashboard.
- **Crash evidence.** The container sizes the heap from its memory limit (`MaxRAMPercentage=75`), writes a heap dump and exits on OOM so the platform restarts it, and keeps rolling GC logs in `/diag`.

---

## 🛠️ Tech Stack

**Frontend:** React 18 • TypeScript • Vite • Tailwind CSS • Vitest  
**Backend:** Spring Boot 3.5.7 • Java 21 • PostgreSQL • Flyway  
**Infrastructure:** Railway (backend + PostgreSQL) • GitHub Pages • GitHub Actions  
**Ops:** Spring Boot Actuator • Micrometer • Prometheus • Grafana • Spring Security (JWT)

---

## 📁 Project Structure

```
service-status/
├── frontend/          # React + TypeScript + Vite
├── backend/           # Spring Boot + Java 21
├── .github/           # CI/CD workflows
└── docs/              # Documentation
```

---

## 🚀 Quick Start

```bash
git clone https://github.com/atinder-harika/service-status.git
cd service-status

# Frontend
cd frontend && npm install && npm run dev

# Backend (separate terminal)
cd backend && ./mvnw spring-boot:run
```

**Need detailed setup instructions?** → See **[SETUP.md](SETUP.md)**

---

## � Contributing

We welcome contributions! Whether you're fixing bugs, adding features, or improving documentation.

**Get Started:**
1. Read **[CONTRIBUTING.md](CONTRIBUTING.md)** for workflow and guidelines
2. Check open [Issues](https://github.com/atinder-harika/service-status/issues) for tasks
3. Fork, create a branch, and submit a PR to `development`

**Branch Naming:** `feat/`, `fix/`, `docs/`, `chore/`  
**PR Target:** `development` branch (not `main`)

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| **[SETUP.md](SETUP.md)** | Local development setup guide |
| **[CONTRIBUTING.md](CONTRIBUTING.md)** | Contribution workflow and guidelines |
| **[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)** | Community guidelines |
| **[.github/copilot-instructions.md](.github/copilot-instructions.md)** | AI coding assistant guidelines (for Copilot/agents) |
| **[LICENSE](LICENSE)** | MIT License |

> **💡 Using GitHub Copilot or AI agents?** This project includes a [copilot-instructions.md](.github/copilot-instructions.md) file with architecture rules, code standards, and project conventions to help AI assistants contribute effectively.

---

## 🧪 Testing

```bash
# Frontend tests
cd frontend && npm test

# Backend tests  
cd backend && ./mvnw test
```

**Coverage Target:** 70%+ lines

---

## 🎯 Why This Project?

Built to demonstrate:
- ✅ Full-stack development (React + Spring Boot)
- ✅ RESTful API design with clean architecture
- ✅ Database migrations with Flyway
- ✅ Automated CI/CD pipeline
- ✅ TypeScript strict mode (zero `any` types)
- ✅ Comprehensive testing practices

---

## 📄 License

MIT License - see [LICENSE](LICENSE) for details.

---

## 👤 Author

**Atinder Singh Hari**  
GitHub: [@atinder-harika](https://github.com/atinder-harika)

---

**⭐ If you find this project useful, please star it!**
