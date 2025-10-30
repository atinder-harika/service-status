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

### Step 1: Create Supabase Account (Free)

1. Go to [supabase.com](https://supabase.com) and sign up
2. Create a new project (choose free tier)
3. Wait for project to initialize (~2 minutes)

### Step 2: Get Database Connection String

**Important:** Use **Session Pooling** JDBC URL, not Direct Connection!

1. In Supabase dashboard: **Settings → Database**
2. Find **Connection String** section
3. Select **JDBC** tab
4. Copy the **Session Pooling** URL (format: `jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres?user=postgres.xxxxx&password=[YOUR-PASSWORD]`)
5. You'll need to split this into 3 parts:
   - **URL:** `jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres` (everything before `?`)
   - **Username:** `postgres.xxxxx` (the `user=` part)
   - **Password:** Your actual database password (replace `[YOUR-PASSWORD]`)

### Step 3: Configure Backend

```bash
cd backend

# Copy configuration template
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties

# Edit application-local.properties with your Supabase details
```

**Example `application-local.properties`:**
```properties
# Supabase Session Pooling JDBC URL (IMPORTANT: Use session pooling!)
spring.datasource.url=jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres
spring.datasource.username=postgres.xxxxx
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

### Step 4: Run Backend

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

Connect to your Supabase database and run:
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
- Check Supabase credentials in `application-local.properties`
- Use **Session Pooling** JDBC URL (not Direct Connection!)
- Ensure Supabase project is active

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
