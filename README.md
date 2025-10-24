# service-status

Full-stack, real-time observability platform that monitors service health (HTTP/Ping/DB) and pushes instant status updates to a modern React/TypeScript dashboard via Spring Boot WebSockets.

## Features
- Real-time status updates via WebSocket/STOMP
- Scheduled health checks (HTTP, ping, PostgreSQL)
- Status levels: Operational (green), Degraded (yellow), Maintenance (blue), Down (red)
- Historical records in PostgreSQL (Supabase)
- Strict layering: Controller → Service → Repository
- Strong typing on the frontend (no `any`)
- Unit tests across frontend and backend

## Stack
- Frontend: React 18 + TypeScript + Tailwind CSS 3.x, Vite 6.x ✅
- Backend: Spring Boot 3.2 (Java 17+), WebSocket/STOMP, Scheduling ✅
- Database: PostgreSQL (Supabase)
- Testing: Vitest (frontend), JUnit 5 + Mockito (backend)
- CI: GitHub Actions (tests/build)
- Hosting: GitHub Pages (frontend), Render (backend), Supabase (DB)

## Monorepo Structure
```
/
├── frontend/ (root) — React + TypeScript + Vite + Tailwind
│   ├── src/
│   ├── index.html
│   ├── vite.config.ts
│   └── package.json
├── backend/ — Spring Boot (Maven), Flyway migrations, unit tests
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── src/test/java/
│   └── pom.xml
└── .github/workflows/ — CI for frontend and backend
```

## Development

### Frontend
```bash
npm install
npm run dev    # http://localhost:5173/service-status/
npm test       # Vitest
npm run build
```

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run  # http://localhost:8080
mvn test
```

**Prerequisites:** 
- Node.js 20+
- Java 17+
- Maven 3.8+
- PostgreSQL (or Supabase connection)

## Releases
- **v0.0.1** - Phase 0: docs, LICENSE, CI setup
- **v0.1.0** - Phase I-A: Vite + TypeScript + Tailwind migration
- **v0.2.0** - Phase I-B: Spring Boot backend (in progress)

## License
MIT © 2025 Atinder Singh Hari
