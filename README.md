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

### Quick Start (No Database Setup Required)

**For contributors:** Use H2 in-memory database for instant local development!

#### Frontend
```bash
cd frontend
npm install
cp .env.example .env.local  # Already configured for localhost:8080
npm run dev                 # http://localhost:5173/service-status/
npm test                    # Vitest tests (no backend needed)
```

#### Backend
```bash
cd backend
./mvnw test                                      # Uses H2 (no Supabase needed)
./mvnw spring-boot:run -Dspring-boot.run.profiles=test  # Run with H2
# Backend runs on http://localhost:8080
```

### Production-Like Setup (Optional)

Only needed if you want to test with Supabase PostgreSQL:

1. **Configure Supabase**
   ```bash
   cd backend/src/main/resources
   cp application.properties.example application.properties
   # Edit application.properties with your Supabase credentials
   ```

2. **Run Migrations**
   ```bash
   ./mvnw flyway:migrate
   ```

3. **Start Backend**
   ```bash
   ./mvnw spring-boot:run
   ```

**Prerequisites:** 
- Node.js 20+ (frontend)
- Java 21+ (backend)
- Maven (bundled as `mvnw`)
- PostgreSQL (optional - H2 used by default)

## Releases
- **v0.0.1** - Phase 0: docs, LICENSE, CI setup
- **v0.1.0** - Phase I-A: Vite + TypeScript + Tailwind migration
- **v0.2.0** - Phase I-B: Spring Boot backend with PostgreSQL integration

## Deployment

### Architecture
- **Frontend:** GitHub Pages (auto-deployed via CI/CD)
- **Backend:** Render free tier (manual deployment)
- **Database:** Supabase PostgreSQL free tier

### Deployment Workflow

#### 1. First-Time Setup

**A. Supabase Database**
1. Create account at [supabase.com](https://supabase.com) (free tier)
2. Create new project, note connection details
3. Configure backend locally:
   ```bash
   cd backend/src/main/resources
   cp application.properties.example application.properties
   # Add your Supabase credentials
   ```
4. Run Flyway migrations:
   ```bash
   ./mvnw flyway:migrate
   ```

**B. Render Backend**
1. Create account at [render.com](https://render.com) (free tier)
2. Create new Web Service:
   - **Name:** `service-status-backend`
   - **Repository:** Connect your GitHub repo
   - **Branch:** `main`
   - **Root Directory:** `backend`
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/*.jar`
   - **Environment:** Add these environment variables:
     ```
     SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_HOST:5432/YOUR_DB
     SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD
     SPRING_PROFILES_ACTIVE=production
     ```
3. Note your backend URL (e.g., `https://service-status-backend.onrender.com`)

**C. GitHub Secrets**
1. Go to your repo: **Settings > Secrets and variables > Actions**
2. Add new repository secret:
   - **Name:** `BACKEND_URL`
   - **Value:** `https://service-status-backend.onrender.com` (your Render URL)

#### 2. Continuous Deployment

**Automated (Frontend):**
- Push to `main` branch triggers GitHub Actions
- Workflow builds frontend with `BACKEND_URL` injected
- Auto-deploys to GitHub Pages
- Access at: `https://YOUR_USERNAME.github.io/service-status/`

**Manual (Backend):**
- Render free tier doesn't support webhooks (cost $)
- After pushing to `main`:
  1. Go to [Render Dashboard](https://dashboard.render.com/)
  2. Select `service-status-backend`
  3. Click **"Manual Deploy" > "Deploy latest commit"**
  4. Wait ~2-3 minutes for build

#### 3. Verification

After deployment:
1. **Backend Health:** Visit `https://YOUR_BACKEND_URL/actuator/health`
   - Should return: `{"status":"UP"}`
2. **Backend API:** Visit `https://YOUR_BACKEND_URL/api/services`
   - Should return JSON array of services
3. **Frontend:** Visit `https://YOUR_USERNAME.github.io/service-status/`
   - Should display service cards with statuses
   - Check browser console for errors

### Troubleshooting Deployment

**Frontend shows "Failed to fetch services":**
- Verify `BACKEND_URL` secret is set correctly in GitHub
- Check CORS configuration in backend `application.properties`
- Verify backend is running on Render (check logs)

**Backend build fails on Render:**
- Check Render logs for Java/Maven errors
- Verify environment variables are set
- Ensure `pom.xml` dependencies are correct

**Database connection fails:**
- Verify Supabase connection string in Render environment variables
- Check Supabase project is active (free tier pauses after inactivity)
- Test connection locally with same credentials

**GitHub Pages shows 404:**
- Ensure repo Settings > Pages > Source is set to "gh-pages branch"
- Workflow may need 2-3 minutes after first push
- Check Actions tab for deployment errors

---

## 🚀 Future Enhancements (Resume-Worthy Features)

> These enhancements are documented as GitHub Issues for incremental development and to showcase learning progression.

### Priority 1: Real-Time & Performance
- [ ] **WebSocket/STOMP Integration** (#1)
  - Replace 30s polling with instant server→client push updates
  - Technologies: Spring WebSocket, STOMP.js, SockJS
  - Benefit: Demonstrates real-time architecture (Slack/Discord-style)

- [ ] **Metrics Dashboard** (#2)
  - Display avg latency, uptime %, last 24h trends
  - Add line charts with Chart.js or Recharts
  - Benefit: Data visualization skills + business intelligence

- [ ] **Circuit Breaker Pattern** (#3)
  - Implement Resilience4j for health checks
  - Prevent cascading failures with fallback logic
  - Benefit: Shows understanding of fault tolerance (enterprise-grade)

### Priority 2: DevOps & Automation
- [ ] **Automated Keepalive System** (#4)
  - GitHub Actions cron workflow to ping `/actuator/health` every 10-14 mins
  - Keeps Render free tier awake + monitors all tracked services
  - Benefit: Full-stack automation + demonstrates cron job understanding

- [ ] **Docker Compose Setup** (#5)
  - Local dev environment with PostgreSQL + backend + frontend in containers
  - One-command startup: `docker-compose up`
  - Benefit: Shows modern DevOps practices

- [ ] **CI/CD Deployment Pipeline** (#6)
  - Automated deployment to Render (backend) + GitHub Pages (frontend)
  - Triggered on merge to `main` branch
  - Benefit: Complete CI/CD workflow implementation

### Priority 3: Features & UX
- [ ] **Alerting/Notifications** (#7)
  - Slack/Discord webhook when service goes DOWN
  - Email alerts via SendGrid (free tier)
  - Benefit: Integration with third-party APIs

- [ ] **Status Page Embed Widget** (#8)
  - Create `<iframe>`-able widget for embedding in other sites
  - URL: `/embed?service=X`
  - Benefit: Shows reusability and API design skills

- [ ] **Dark Mode Toggle** (#9)
  - Tailwind CSS dark mode implementation
  - Persistent user preference (localStorage)
  - Benefit: Modern UI/UX feature

- [ ] **Historical Data Visualization** (#10)
  - Track service uptime over time (7 days, 30 days, 90 days)
  - Display uptime percentage badges
  - Benefit: Time-series data handling

### Priority 4: Security & Scalability
- [ ] **Rate Limiting** (#11)
  - Spring Boot rate limiter on API endpoints
  - Prevent abuse with bucket4j library
  - Benefit: Security awareness + scalability

- [ ] **API Authentication (Optional)** (#12)
  - JWT-based auth for admin endpoints (e.g., add/remove services)
  - Benefit: Security + authentication patterns

---

## 📊 Why These Enhancements Matter

| Feature | Resume Keywords | Impact |
|---------|----------------|--------|
| WebSocket/STOMP | Real-time, bidirectional communication, event-driven | High - Industry standard for live apps |
| Circuit Breaker | Fault tolerance, resilience, distributed systems | High - Shows enterprise architecture knowledge |
| Docker Compose | Containerization, DevOps, microservices | High - Modern deployment skills |
| Metrics Dashboard | Data visualization, analytics, business intelligence | Medium - Product-focused thinking |
| Automated Keepalive | Automation, cron jobs, system reliability | Medium - Problem-solving creativity |
| Alerting | Third-party integrations, webhooks, notifications | Medium - API integration skills |

---

## 🎯 Suggested Implementation Order

1. **WebSocket** (1-2 hours) - Highest impact for demonstrations
2. **Docker Compose** (1 hour) - Makes local dev easy for collaborators
3. **Metrics Dashboard** (2-3 hours) - Visual appeal for portfolio
4. **Circuit Breaker** (1 hour) - Resume keyword booster
5. **Automated Keepalive** (30 mins) - Practical utility
6. **Remaining features** - Based on time/interest

---

## License
MIT © 2025 Atinder Singh Hari
