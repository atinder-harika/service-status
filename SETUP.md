# Local Development Setup

Complete guide to setting up and running the service-status project on your local machine.

---

## 📋 Prerequisites

Install these first:

| Tool | Version | Download |
|------|---------|----------|
| **Node.js** | 20+ | [nodejs.org](https://nodejs.org/) |
| **Java** | 21+ | [adoptium.net](https://adoptium.net/) |
| **Git** | Latest | [git-scm.com](https://git-scm.com/) |

**Verify installation:**
```bash
node --version  # Should show v20.x or higher
java -version   # Should show 21.x or higher
git --version   # Any recent version
```

---

## 🚀 Quick Start (5 Minutes)

### 1. Clone the Repository

```bash
git clone https://github.com/atinder-harika/service-status.git
cd service-status
```

---

## 🎨 Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Copy environment template
cp .env.example .env.local

# Edit .env.local (optional - defaults work for local backend)
# VITE_API_URL=http://localhost:8080

# Start development server
npm run dev
```

**Frontend runs at:** http://localhost:5173/service-status/

**Available scripts:**
- `npm run dev` - Start dev server
- `npm test` - Run tests in watch mode
- `npm run build` - Build for production

---

## ☕ Backend Setup

### Step 1: Start a local PostgreSQL

```bash
docker run -d --name ss-pg -p 5432:5432 -e POSTGRES_PASSWORD=pg postgres:16-alpine
```

### Step 2: Configure Backend

```bash
cd backend
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

The example already points at that container. In production (Railway) the `prod` profile reads `DATABASE_URL`, `DB_USER` and `DB_PASSWORD`, which reference the Railway Postgres service, plus `JWT_SECRET` and `ADMIN_PASSWORD`.

Or run the whole stack with metrics: `docker compose -f ops/docker-compose.yml up --build` (Grafana on http://localhost:3001).

### Step 3: Run Backend

```bash
# Run with Maven wrapper (local profile is default)
./mvnw spring-boot:run

# Or run tests only (no database needed)
./mvnw test
```

**Backend runs at:** http://localhost:8080

**Verify it's working:**
- Open http://localhost:8080/actuator/health in your browser
- Should return: `{"status":"UP"}`

---

## 🧪 Testing

```bash
# Frontend (Vitest + React Testing Library)
cd frontend
npm test              # Watch mode
npm test -- --run     # Run once
npm test -- --coverage

# Backend (JUnit 5 + Mockito - no database needed!)
cd backend
./mvnw test
```

**Coverage Target:** 70%+ lines

---

## 🔧 Common Tasks

### Add a Service to Monitor

Connect to your database and run:
```sql
INSERT INTO dev.services (name, url, check_type) 
VALUES ('My API', 'https://api.example.com/health', 'HTTP');
```

The frontend will automatically fetch and display the new service on next refresh.

### Change Health Check Interval

**Backend:** `backend/src/main/java/.../service/HealthCheckService.java`
```java
@Scheduled(fixedDelay = 30000)  // Change to desired milliseconds
```

**Frontend:** `frontend/src/config/constants.ts`
```typescript
export const POLLING_INTERVAL = 30000; // Match backend interval
```

---

## 🚨 Troubleshooting

### Backend won't start
- Verify Java 21: `java -version`
- Check the database credentials in `application-local.properties`
- Use **Session Pooling** JDBC URL (not Direct Connection!)
- Ensure the Postgres container is running

### Frontend can't connect
- Verify backend is running: Open http://localhost:8080/actuator/health
- Check `VITE_API_URL` in `.env.local`
- Restart dev server after changing `.env.local`

### Tests failing
- Frontend: Clear cache `rm -rf node_modules && npm install`
- Backend: Clean build `./mvnw clean test`
- Backend tests should NEVER need database (all mocked)

### Port already in use
```bash
# Windows PowerShell
netstat -ano | findstr :5173  # Find PID
taskkill /PID <PID> /F        # Kill process
```

---

## 📦 Build for Production

```bash
# Frontend
cd frontend && npm run build  # Output: dist/

# Backend
cd backend && ./mvnw clean package -DskipTests  # Output: target/*.jar
```

---

## ❓ Getting Help

- **Bug reports:** [Open an issue](https://github.com/atinder-harika/service-status/issues)
- **Questions:** Check existing issues
- **Pull requests:** See [CONTRIBUTING.md](CONTRIBUTING.md)
