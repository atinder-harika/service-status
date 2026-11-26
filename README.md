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
- **Soak test** (`ops/soak/soak.sh`, Nov 26 2026). The old and current code ran side by side for 44 minutes on a 128 MB heap, each checking 60 services: 30 that answer, 15 that hang for 30 s and 15 at an IP that never answers. Results:

  | | Old (sequential) | Current (virtual threads) |
  |---|---|---|
  | Median time between checks of a service (30 s interval) | 180 s | 35 s |
  | Checks completed | 899 | 4,620 |
  | Heap used, start → end | 42 → 30 MB | 41 → 36 MB |
  | Live threads, start → end | 62 → 95 | 55 → 109 |
  | Out of memory | no | no |

  Neither version leaked memory or threads in that window, so the old crash is **not** explained by this code alone. The likely suspect is the Supabase connection pooler, which this version no longer uses. The soak also caught a real log flood: a 5 s reactive timeout raced the 5 s socket timeout, and the losing error was logged as `onErrorDropped` (754 ERROR lines in 44 min). The reactive timeout is now a 6 s backstop, and a re-run logged 0.
