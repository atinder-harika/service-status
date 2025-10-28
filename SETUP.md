# Local Development Setup

Complete guide to setting up and running the service-status project on your local machine.

---

## Prerequisites

Ensure you have the following installed:

- **Node.js 20+** ([Download](https://nodejs.org/))
- **Java 21+** ([Download](https://adoptium.net/))
- **Git** ([Download](https://git-scm.com/))
- **PostgreSQL** (optional - for production-like testing with Supabase)

---

## Quick Start (5 Minutes)

### 1. Clone the Repository

```bash
git clone https://github.com/atinder-harika/service-status.git
cd service-status
```

### 2. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Create local environment file
cp .env.example .env.local

# Start development server
npm run dev
```

Frontend runs at: **http://localhost:5173/service-status/**

### 3. Backend Setup

**Option A: Without Database (Frontend Only)**

The frontend can run standalone and will show connection errors (useful for UI development).

**Option B: With Backend API**

```bash
cd backend

# Run tests (uses mocked data, no database needed)
./mvnw test

# Run backend (connects to your Supabase or configure H2)
./mvnw spring-boot:run
```

Backend runs at: **http://localhost:8080**

---

## Configuration

### Frontend Environment Variables

Create `frontend/.env.local`:

```env
# Backend API URL (default: local Spring Boot server)
VITE_API_URL=http://localhost:8080
```

### Backend Configuration

#### Option 1: Use Your Own Supabase (Recommended)

1. Create a free account at [supabase.com](https://supabase.com)
2. Create a new project
3. Copy connection details from: **Settings → Database**

Create `backend/src/main/resources/application.properties`:

```properties
spring.application.name=service-status-backend
server.port=8080

# Your Supabase PostgreSQL Connection
spring.datasource.url=jdbc:postgresql://YOUR_SUPABASE_HOST:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# Flyway Configuration (creates 'dev' schema and tables)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.schemas=dev
spring.flyway.default-schema=dev

# Spring Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always

# Logging
logging.level.com.atinder.service_status_backend=DEBUG
logging.level.org.springframework.jdbc.core=DEBUG
```

4. Run Flyway migration to create tables:

```bash
./mvnw flyway:migrate
```

5. Insert test services:

```sql
INSERT INTO dev.services (name, url, check_type) 
VALUES 
  ('Google', 'https://www.google.com', 'HTTP'),
  ('GitHub', 'https://github.com', 'HTTP');
```

#### Option 2: Run Tests Only (No Database)

Tests use Mockito mocks and don't require any database connection:

```bash
./mvnw test
```

All 15 tests should pass instantly with no configuration needed.

---

## Running the Full Stack Locally

### Terminal 1: Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Terminal 2: Frontend
```bash
cd frontend
npm run dev
```

### Terminal 3: Watch Tests (Optional)
```bash
# Frontend tests
cd frontend
npm test

# Backend tests
cd backend
./mvnw test
```

---

## Project Structure

```
service-status/
├── frontend/              # React + TypeScript + Vite + Tailwind
│   ├── src/
│   │   ├── pages/         # Page controllers (StatusPage)
│   │   ├── components/    # Reusable UI components
│   │   ├── services/      # API layer (fetch calls)
│   │   ├── hooks/         # Custom React hooks (useServices)
│   │   ├── utils/         # Pure utility functions (status helpers)
│   │   ├── config/        # Constants (API_BASE_URL, POLLING_INTERVAL)
│   │   └── types/         # TypeScript interfaces
│   ├── .env.example       # Template for local development
│   └── package.json
│
├── backend/               # Spring Boot + Java 21 + PostgreSQL
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── controller/    # REST API endpoints
│   │   │   │   ├── service/       # Business logic (health checks)
│   │   │   │   ├── repository/    # Database access (Spring Data JDBC)
│   │   │   │   ├── model/         # Entity classes (MonitoredService)
│   │   │   │   └── dto/           # Data transfer objects
│   │   │   └── resources/
│   │   │       ├── application.properties.example
│   │   │       └── db/migration/  # Flyway SQL migrations
│   │   └── test/
│   │       └── java/              # JUnit 5 + Mockito tests
│   └── pom.xml
│
└── .github/
    └── workflows/
        └── full-stack-ci.yml      # CI/CD pipeline
```

---

## Testing

### Frontend Tests (Vitest + React Testing Library)

```bash
cd frontend

# Run tests once
npm test

# Run tests in watch mode
npm test -- --watch

# Run with coverage
npm test -- --coverage
```

**Coverage goal:** 70%+ lines

### Backend Tests (JUnit 5 + Mockito)

```bash
cd backend

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=HealthCheckServiceTest

# Run with coverage
./mvnw test jacoco:report
```

**Coverage goal:** 70%+ lines

---

## Common Tasks

### Add a New Service to Monitor

**Option 1: Via Database**
```sql
INSERT INTO dev.services (name, url, check_type) 
VALUES ('My API', 'https://api.example.com/health', 'HTTP');
```

**Option 2: Via API (when implemented)**
```bash
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{"name":"My API","url":"https://api.example.com/health","checkType":"HTTP"}'
```

Frontend will automatically fetch and display the new service.

### Change Health Check Interval

**Backend:** Edit [`backend/src/main/java/.../HealthCheckService.java`](backend/src/main/java/.../HealthCheckService.java ):
```java
@Scheduled(fixedDelay = 30000)  // 30 seconds (30000ms)
```

**Frontend:** Edit [`frontend/src/config/constants.ts`](frontend/src/config/constants.ts ):
```typescript
export const POLLING_INTERVAL = 30000; // Match backend interval
```

### View Backend Logs

```bash
cd backend
./mvnw spring-boot:run

# Check health endpoint
curl http://localhost:8080/actuator/health

# Check API endpoint
curl http://localhost:8080/api/services
```

---

## Troubleshooting

### Frontend can't connect to backend

**Symptom:** "Failed to fetch services" error

**Solutions:**
1. Verify backend is running: `curl http://localhost:8080/actuator/health`
2. Check `frontend/.env.local` has correct `VITE_API_URL`
3. Restart dev server after changing `.env.local`
4. Check browser console for CORS errors

### Backend won't start

**Symptom:** "Failed to load ApplicationContext" or database errors

**Solutions:**
1. Verify Java 21 installed: `java -version`
2. Check `application.properties` has valid Supabase credentials
3. Ensure Flyway migrations ran: `./mvnw flyway:info`
4. Check Supabase project is active (free tier pauses after inactivity)

### Tests failing

**Frontend:**
- Clear node_modules: `rm -rf node_modules && npm install`
- Check `setupTests.ts` imports `@testing-library/jest-dom`

**Backend:**
- Clean build: `./mvnw clean test`
- Tests should NOT require database (all mocked)
- If seeing database errors, ensure tests use `@Mock` not `@SpringBootTest`

### Port already in use

**Frontend (5173):**
```bash
# Kill process on port 5173
lsof -ti:5173 | xargs kill -9  # Mac/Linux
netstat -ano | findstr :5173   # Windows (find PID, then taskkill)
```

**Backend (8080):**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9  # Mac/Linux
netstat -ano | findstr :8080   # Windows
```

---

## Build for Production

### Frontend

```bash
cd frontend
npm run build
```

Output: `frontend/dist/` (static files ready for deployment)

### Backend

```bash
cd backend
./mvnw clean package -DskipTests
```

Output: `backend/target/service-status-backend-0.0.1-SNAPSHOT.jar`

Run JAR:
```bash
java -jar backend/target/*.jar
```

---

## Deployment

See [README.md](README.md) deployment section for:
- GitHub Pages (frontend)
- Render/Railway (backend)
- Supabase (database)

---

## Additional Resources

- [Contributing Guide](CONTRIBUTING.md) - How to contribute code
- [Future Plans](FUTURE_PLANS.md) - Planned enhancements
- [Architecture Docs](frontend/ARCHITECTURE.md) - Frontend architecture details
- [Copilot Instructions](.github/copilot-instructions.md) - Project conventions

---

## Getting Help

- **Bug reports:** [Open an issue](https://github.com/atinder-harika/service-status/issues)
- **Questions:** Check existing issues or start a discussion
- **Pull requests:** See [CONTRIBUTING.md](CONTRIBUTING.md)
