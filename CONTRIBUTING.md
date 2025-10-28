# Contributing

Thanks for contributing!

## Workflow
- Use short-lived feature branches from `main`.
- Add tests for your changes.
- Ensure all tests pass locally before opening a PR.
- CI must be green; squash-merge into `main`.

## Branching
- Protect `main` (no direct pushes).
- Branch names: `feat/...`, `fix/...`, `chore/...`, `docs/...`.
- Conventional Commits in messages are encouraged.

## Releases
- Semantic Versioning: MAJOR.MINOR.PATCH.
- Tag stable points on `main` (e.g., `v0.0.1` for Phase 0).
- Create GitHub Releases from tags.

## Code quality
- Frontend: TypeScript only (no `any`), React function components, Hooks, Tailwind utilities.
- Backend: Controller → Service → Repository. Use `@Scheduled(fixedDelay=...)` wrapped in try/catch.
- Tests: Happy path + ≥3 failure/edge cases.

## Local setup (current)

### Prerequisites
- **Node.js 20+** (for frontend)
- **Java 21+** (for backend)
- **Maven** (bundled with project via `mvnw`)
- **PostgreSQL** (optional - H2 used by default for local dev)

### Quick Start (No Database Setup Required)

#### Option 1: Local Development with H2 (Recommended for Contributors)
This option uses an in-memory H2 database - no PostgreSQL setup needed!

1. **Clone the repository**
   ```bash
   git clone https://github.com/atinder-harika/service-status.git
   cd service-status
   ```

2. **Backend Setup**
   ```bash
   cd backend
   # Tests automatically use H2 (see application-test.properties)
   ./mvnw test
   
   # Run with H2 for local development
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=test
   # Backend runs on http://localhost:8080
   ```

3. **Frontend Setup** (in a new terminal)
   ```bash
   cd frontend
   npm install
   
   # Create local environment file
   cp .env.example .env.local
   # .env.local already has VITE_API_URL=http://localhost:8080
   
   # Run tests
   npm test
   
   # Start dev server
   npm run dev
   # Frontend runs on http://localhost:5173
   ```

4. **Access the Application**
   - Frontend: http://localhost:5173/service-status/
   - Backend API: http://localhost:8080/api/services
   - Actuator Health: http://localhost:8080/actuator/health

#### Option 2: Production-Like Setup with Supabase (Optional)
Only needed if you want to test with a real PostgreSQL database.

1. **Create Supabase Account** (free tier)
   - Sign up at https://supabase.com
   - Create new project, note down connection details

2. **Configure Backend**
   ```bash
   cd backend/src/main/resources
   cp application.properties.example application.properties
   # Edit application.properties with your Supabase credentials:
   # - spring.datasource.url (replace YOUR_SUPABASE_HOST, YOUR_DB_NAME)
   # - spring.datasource.password (your actual password)
   ```

3. **Run Flyway Migration**
   ```bash
   ./mvnw flyway:migrate
   # Creates dev.services table in your Supabase database
   ```

4. **Start Backend**
   ```bash
   ./mvnw spring-boot:run
   # Runs with Supabase connection
   ```

### Making Changes

1. **Create Feature Branch**
   ```bash
   git checkout -b feat/your-feature-name
   ```

2. **Run Tests Before Committing**
   ```bash
   # Frontend tests
   cd frontend && npm test
   
   # Backend tests (uses H2, no Supabase needed)
   cd backend && ./mvnw test
   ```

3. **Ensure Code Quality**
   - Frontend: No TypeScript `any` types, use Tailwind utilities
   - Backend: Follow Controller → Service → Repository pattern
   - Add tests for new features (happy path + 3 edge cases minimum)

4. **Commit and Push**
   ```bash
   git add .
   git commit -m "feat: your descriptive message"
   git push origin feat/your-feature-name
   ```

5. **Open Pull Request**
   - CI will run tests automatically (uses H2 for backend)
   - Wait for green checkmark before requesting review
   - Squash-merge into `main` after approval

### Troubleshooting

**Backend won't start:**
- Make sure Java 21+ is installed: `java -version`
- If using Supabase, verify credentials in `application.properties`
- For local H2 dev, use: `./mvnw spring-boot:run -Dspring-boot.run.profiles=test`

**Frontend can't connect to backend:**
- Check backend is running on port 8080
- Verify `.env.local` has `VITE_API_URL=http://localhost:8080`
- Check browser console for CORS errors (shouldn't happen with local setup)

**Tests failing:**
- Backend tests should pass with H2 (no configuration needed)
- Frontend tests use mocked API (no backend connection needed)
- Clear caches: `npm clean-install` (frontend), `./mvnw clean` (backend)

### Project Structure
```
service-status/
├── frontend/          # React + TypeScript + Vite
│   ├── src/
│   │   ├── pages/     # Page controllers
│   │   ├── components/# Reusable UI components
│   │   ├── services/  # API layer
│   │   ├── hooks/     # Custom React hooks
│   │   ├── utils/     # Pure utility functions
│   │   ├── config/    # Constants and configuration
│   │   └── types/     # TypeScript interfaces
│   ├── .env.example   # Template for local dev
│   └── package.json
│
└── backend/           # Spring Boot + Java 21
    ├── src/
    │   ├── main/
    │   │   ├── java/  # Controllers, Services, Repositories, Entities
    │   │   └── resources/
    │   │       ├── application.properties.example  # Template for Supabase
    │   │       └── db/migration/  # Flyway SQL migrations
    │   └── test/
    │       ├── java/  # JUnit 5 + Mockito tests
    │       └── resources/
    │           └── application-test.properties  # H2 config (auto-used by tests)
    └── pom.xml
```

### Common Tasks

**Add a new service to monitor:**
1. Insert into database (H2/Supabase):
   ```sql
   INSERT INTO dev.services (name, url, check_type) 
   VALUES ('My API', 'https://api.example.com/health', 'HTTP');
   ```
2. Frontend will automatically fetch and display it

**Change health check interval:**
- Edit `backend/.../HealthCheckService.java`
- Update `@Scheduled(fixedDelay = 30000)` (value in milliseconds)
- Update `frontend/src/config/constants.ts` → `POLLING_INTERVAL` to match

**Add new API endpoint:**
1. Add method to `ServiceController.java`
2. Add corresponding function to `frontend/src/services/api.ts`
3. Update TypeScript types in `frontend/src/types/index.ts`
4. Write tests for both layers
