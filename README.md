# Service Status Monitor

> **Real-time service health monitoring platform** built with Spring Boot and React, featuring automated health checks, live status updates, and a modern responsive dashboard.

[![CI/CD](https://github.com/atinder-harika/service-status/actions/workflows/full-stack-ci.yml/badge.svg)](https://github.com/atinder-harika/service-status/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🚀 Live Demo

**Frontend:** [https://atinder-harika.github.io/service-status/](https://atinder-harika.github.io/service-status/)

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

## 🛠️ Tech Stack

**Frontend:** React 18 • TypeScript • Vite • Tailwind CSS • Vitest  
**Backend:** Spring Boot 3.5.7 • Java 21 • PostgreSQL • Flyway  
**Infrastructure:** GitHub Pages • Supabase • GitHub Actions

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
