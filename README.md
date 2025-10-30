# Service Status Monitor

> **Real-time service health monitoring platform** built with Spring Boot and React, featuring automated health checks, live status updates, and a modern responsive dashboard.

[![CI/CD](https://github.com/atinder-harika/service-status/actions/workflows/full-stack-ci.yml/badge.svg)](https://github.com/atinder-harika/service-status/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🚀 Live Demo

- **Frontend:** [https://atinder-harika.github.io/service-status/](https://atinder-harika.github.io/service-status/)

---

## ✨ Features

- ✅ **Automated Health Checks** - HTTP/HTTPS endpoint monitoring every 30 seconds
- ✅ **Real-Time Status** - Live dashboard with auto-refresh polling
- ✅ **Status Levels** - Operational (🟢), Degraded (🟡), Maintenance (🔵), Down (🔴)
- ✅ **Persistent Storage** - PostgreSQL via Supabase with Flyway migrations
- ✅ **Clean Architecture** - Strict layering (Controller → Service → Repository)
- ✅ **Type-Safe Frontend** - TypeScript with strict mode, zero `any` types
- ✅ **Comprehensive Tests** - 15 backend + 20 frontend tests (mock-based, no DB required)
- ✅ **CI/CD Pipeline** - Automated testing and deployment via GitHub Actions

---

## 🛠️ Tech Stack

### Frontend
- **Framework:** React 18 with TypeScript
- **Build Tool:** Vite 7.x (fast dev server, optimized builds)
- **Styling:** Tailwind CSS 3.x (utility-first, responsive)
- **Testing:** Vitest + React Testing Library
- **Architecture:** MVC-like (pages, components, services, hooks, utils)

### Backend
- **Framework:** Spring Boot 3.5.7 (Java 21)
- **Database:** PostgreSQL (Supabase cloud)
- **ORM:** Spring Data JDBC with Flyway migrations
- **Health Checks:** Scheduled tasks with WebClient
- **Testing:** JUnit 5 + Mockito (100% mocked, no DB dependency)
- **API:** RESTful endpoints with JSON responses

### Infrastructure
- **Frontend Hosting:** GitHub Pages (auto-deployed via CI/CD)
- **Backend Hosting:** Manual deployment (Render/Railway/Heroku)
- **Database:** Supabase PostgreSQL free tier
- **CI/CD:** GitHub Actions (tests on every push/PR)

---

## 📁 Project Structure

```
service-status/
├── frontend/              # React + TypeScript + Vite
│   ├── src/
│   │   ├── pages/         # Page controllers
│   │   ├── components/    # Reusable UI components
│   │   ├── services/      # API layer (fetch)
│   │   ├── hooks/         # Custom React hooks
│   │   ├── utils/         # Pure utility functions
│   │   ├── config/        # Constants and configuration
│   │   └── types/         # TypeScript interfaces
│   └── package.json
│
├── backend/               # Spring Boot + Java 21
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      # Controllers, Services, Repositories
│   │   │   └── resources/
│   │   │       └── db/migration/  # Flyway SQL migrations
│   │   └── test/          # JUnit 5 + Mockito tests
│   └── pom.xml
│
├── .github/workflows/     # CI/CD automation
├── SETUP.md               # Detailed setup guide
├── CONTRIBUTING.md        # Contribution guidelines
├── FUTURE_PLANS.md        # Planned enhancements
└── README.md              # You are here
```

---

## 🏃 Quick Start

### Prerequisites
- Node.js 20+
- Java 21+
- Git

### Run Locally (5 Minutes)

```bash
# Clone repository
git clone https://github.com/atinder-harika/service-status.git
cd service-status

# Frontend
cd frontend
npm install
npm run dev  # Runs at http://localhost:5173

# Backend (separate terminal)
cd backend
./mvnw spring-boot:run  # Runs at http://localhost:8080
```

**For detailed setup instructions and configuration, see [SETUP.md](SETUP.md)**

---

## 🧪 Testing

### Run Tests

```bash
# Frontend (Vitest + React Testing Library)
cd frontend
npm test              # Watch mode
npm test -- --run     # Run once
npm test -- --coverage

# Backend (JUnit 5 + Mockito)
cd backend
./mvnw test           # All mocked, no database needed
```

**Test Coverage:** 70%+ lines (goal)  
**Test Count:** 15 backend + 20 frontend = 35 total tests

---

## 📚 Documentation

- **[SETUP.md](SETUP.md)** - Detailed local development setup guide
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - How to contribute (PR workflow, code style)
- **[FUTURE_PLANS.md](FUTURE_PLANS.md)** - Planned enhancements and roadmap
- **[frontend/ARCHITECTURE.md](frontend/ARCHITECTURE.md)** - Frontend architecture details
- **[.github/copilot-instructions.md](.github/copilot-instructions.md)** - Project conventions

---

## 🎯 Key Highlights

**For Resume/Portfolio:**
- ✅ Full-stack application (React + Spring Boot)
- ✅ RESTful API design with clean architecture
- ✅ Automated CI/CD pipeline with GitHub Actions
- ✅ Database migrations with Flyway
- ✅ Comprehensive testing (JUnit 5, Mockito, Vitest)
- ✅ TypeScript strict mode (type-safe frontend)
- ✅ Cloud deployment (GitHub Pages, Render, Supabase)
- ✅ Monorepo structure with clear separation of concerns

**Technical Skills Demonstrated:**
- Backend: Spring Boot, Spring Data JDBC, Scheduled Tasks, WebClient
- Frontend: React 18, TypeScript, Vite, Tailwind CSS, Custom Hooks
- Testing: JUnit 5, Mockito, Vitest, React Testing Library
- DevOps: GitHub Actions, Flyway, Maven, npm
- Database: PostgreSQL, SQL migrations, schema management
- Architecture: MVC pattern, layered architecture, separation of concerns

---

## 🔮 Future Enhancements

See [FUTURE_PLANS.md](FUTURE_PLANS.md) for detailed roadmap including:

- 🔴 **High Priority:** WebSocket/STOMP real-time updates, Circuit Breaker pattern, Metrics dashboard
- 🟡 **Medium Priority:** Alerting system (Slack/Discord), Historical data visualization, Rate limiting
- 🟢 **Nice to Have:** Docker Compose, Dark mode, Embed widget, API authentication

**Estimated Total:** 12 enhancements, ~15-20 hours of development

---

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Branching strategy (`feat/`, `fix/`, `docs/`, etc.)
- Code style requirements (TypeScript strict, no `any`, etc.)
- Testing requirements (70% coverage minimum)
- PR workflow and review process

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Atinder Singh Hari**  
GitHub: [@atinder-harika](https://github.com/atinder-harika)

---

## 🙏 Acknowledgments

- Built as a portfolio project to demonstrate full-stack development skills
- Uses free tiers: GitHub Pages, Render, Supabase (no credit card required)
- Inspired by modern observability platforms (Datadog, New Relic, StatusPage.io)

---

**⭐ If you find this project useful, please consider giving it a star!**
