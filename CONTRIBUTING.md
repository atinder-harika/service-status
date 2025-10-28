# Contributing to Service Status Monitor

Thank you for your interest in contributing! This guide will help you get started with contributing code, documentation, or bug reports.

---

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Branching Strategy](#branching-strategy)
- [Code Style Guidelines](#code-style-guidelines)
- [Testing Requirements](#testing-requirements)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)

---

## Code of Conduct

- Be respectful and constructive in discussions
- Focus on the code, not the person
- Welcome newcomers and help them learn
- Follow the project's coding standards

---

## How to Contribute

### Reporting Bugs

1. Check if the bug is already reported in [Issues](https://github.com/atinder-harika/service-status/issues)
2. If not, create a new issue with:
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots (if applicable)
   - Environment details (OS, browser, versions)

### Suggesting Features

1. Check [FUTURE_PLANS.md](FUTURE_PLANS.md) for planned enhancements
2. Open a new issue with tag `enhancement`:
   - Describe the feature and use case
   - Explain why it would be valuable
   - Suggest implementation approach (optional)

### Contributing Code

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write/update tests
5. Ensure all tests pass
6. Submit a pull request

---

## Development Workflow

### 1. Fork and Clone

```bash
# Fork on GitHub, then clone your fork
git clone https://github.com/YOUR_USERNAME/service-status.git
cd service-status

# Add upstream remote
git remote add upstream https://github.com/atinder-harika/service-status.git
```

### 2. Set Up Development Environment

See [SETUP.md](SETUP.md) for detailed setup instructions.

**Quick setup:**
```bash
# Frontend
cd frontend
npm install

# Backend
cd backend
./mvnw install
```

### 3. Create a Branch

```bash
# Update main
git checkout main
git pull upstream main

# Create feature branch
git checkout -b feat/your-feature-name
```

### 4. Make Changes

- Follow [code style guidelines](#code-style-guidelines)
- Write tests for new features
- Update documentation as needed
- Keep commits focused and atomic

### 5. Run Tests

```bash
# Frontend tests
cd frontend
npm test

# Backend tests
cd backend
./mvnw test
```

### 6. Commit and Push

```bash
git add .
git commit -m "feat: add your feature description"
git push origin feat/your-feature-name
```

### 7. Open Pull Request

- Go to your fork on GitHub
- Click "New Pull Request"
- Fill out the PR template
- Wait for review

---

## Branching Strategy

### Branch Naming Convention

Use prefixes to indicate the type of work:

- `feat/` - New features
  - Example: `feat/websocket-integration`
- `fix/` - Bug fixes
  - Example: `fix/health-check-timeout`
- `docs/` - Documentation changes
  - Example: `docs/update-setup-guide`
- `chore/` - Maintenance tasks
  - Example: `chore/update-dependencies`
- `test/` - Test-only changes
  - Example: `test/add-service-controller-tests`
- `refactor/` - Code refactoring
  - Example: `refactor/extract-api-service`

### Branch Protection

- `main` is protected (no direct pushes)
- All changes via pull requests
- CI must pass before merging
- Squash-merge into `main`

---

## Code Style Guidelines

### Frontend (React/TypeScript)

**✅ DO:**
- Use **functional components** only (no class components)
- Use **React Hooks** for state management
- Use **PascalCase** for components, **camelCase** for variables/functions
- Use **TypeScript strict mode** (no `any` types)
- Use **Tailwind utility classes** (no custom CSS files)
- Export types from `src/types/index.ts`

**❌ DON'T:**
- Use `any` type (strictly prohibited)
- Create inline styles
- Use class components
- Ignore TypeScript errors

**Example:**
```typescript
// ✅ Good
interface ServiceCardProps {
  name: string;
  status: ServiceStatus;
}

export const ServiceCard = ({ name, status }: ServiceCardProps): JSX.Element => {
  return <div className="rounded-lg bg-white p-4">{name}</div>;
};

// ❌ Bad
export const ServiceCard = (props: any) => {
  return <div style={{ padding: '16px' }}>{props.name}</div>;
};
```

### Backend (Java/Spring Boot)

**✅ DO:**
- Follow **Controller → Service → Repository** layering
- Use **Java 21+** features (records, pattern matching, etc.)
- Use **Lombok** annotations to reduce boilerplate
- Use **Java Streams** over imperative loops (where clarity is maintained)
- Keep controllers thin (delegate to services)
- Use `@Scheduled(fixedDelay=...)` with `try/catch` for scheduled tasks

**❌ DON'T:**
- Put business logic in controllers
- Ignore exception handling
- Use imperative loops when streams are clearer

**Naming Conventions:**
- Controllers: `*Controller.java`
- Services: `*Service.java`
- Repositories: `*Repository.java`
- DTOs: `*DTO.java`
- Entities: No suffix (e.g., `MonitoredService.java`)

**Example:**
```java
// ✅ Good
@RestController
@RequestMapping("/api/services")
@Slf4j
public class ServiceController {
    
    private final ServiceRepository repository;
    
    @GetMapping
    public List<ServiceGroupDTO> getAllServices() {
        return repository.findAll().stream()
            .map(this::toDTO)
            .toList();
    }
}

// ❌ Bad - business logic in controller
@GetMapping
public List<ServiceGroupDTO> getAllServices() {
    List<Service> services = repository.findAll();
    // 50 lines of transformation logic...
    return result;
}
```

---

## Testing Requirements

### Minimum Coverage

- **Target:** 70%+ line coverage
- **Mandatory:** All new features must include tests

### Frontend Testing (Vitest + React Testing Library)

**Test at least:**
- Happy path (feature works as expected)
- 3+ edge cases/error scenarios
- User interactions (clicks, form submissions)
- Conditional rendering

**Example:**
```typescript
describe('ServiceCard', () => {
  it('renders service name', () => {
    render(<ServiceCard name="Test" status="Operational" />);
    expect(screen.getByText('Test')).toBeInTheDocument();
  });

  it('applies correct status color for Operational', () => {
    // ...
  });

  it('handles missing lastCheckedAt gracefully', () => {
    // ...
  });
});
```

### Backend Testing (JUnit 5 + Mockito)

**Test at least:**
- Happy path (method works as expected)
- 3+ edge cases (null inputs, empty collections, exceptions)
- Service layer logic with mocked repositories
- Controller endpoints with MockMvc

**Example:**
```java
@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {
    
    @Mock
    private ServiceRepository repository;
    
    @InjectMocks
    private HealthCheckService service;
    
    @Test
    void checkService_operational_whenHttpReturns200() {
        // Given: Mock service
        MonitoredService service = new MonitoredService(/*...*/);
        when(repository.findById(1L)).thenReturn(Optional.of(service));
        
        // When: Check runs
        service.checkService(1L);
        
        // Then: Status updated
        verify(repository).save(argThat(s -> 
            s.getCurrentStatus().equals("Operational")
        ));
    }
}
```

### Running Tests Before PR

```bash
# Frontend
cd frontend
npm test -- --run
npm run build  # Ensure TypeScript compiles

# Backend
cd backend
./mvnw test
./mvnw clean package  # Ensure build succeeds
```

---

## Commit Messages

### Format

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation only
- `style:` - Code style (formatting, no logic change)
- `refactor:` - Code refactoring
- `test:` - Add/update tests
- `chore:` - Maintenance (deps, config, etc.)
- `ci:` - CI/CD changes

### Examples

```bash
# Good commits
feat(frontend): add dark mode toggle with localStorage persistence
fix(backend): handle null lastCheckedAt in health check service
docs: update SETUP.md with Supabase configuration steps
test(backend): add edge case tests for HealthCheckService
chore: update Spring Boot to 3.5.7

# Bad commits
fixed bug
updated code
changes
```

---

## Pull Request Process

### Before Opening PR

1. ✅ All tests pass locally
2. ✅ Code follows style guidelines
3. ✅ New features have tests (70%+ coverage)
4. ✅ Documentation updated (if needed)
5. ✅ Commit messages follow convention
6. ✅ Branch is up-to-date with `main`

### PR Checklist

**Title:** Use conventional commit format  
**Example:** `feat: add WebSocket real-time updates`

**Description should include:**
- What does this PR do?
- Why is this change needed?
- How was it tested?
- Screenshots (for UI changes)
- Related issues (e.g., `Closes #42`)

**Template:**
```markdown
## Description
Brief summary of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Frontend tests pass (`npm test`)
- [ ] Backend tests pass (`./mvnw test`)
- [ ] Manual testing completed

## Screenshots (if applicable)

## Checklist
- [ ] Code follows project style guidelines
- [ ] Tests added/updated (70%+ coverage)
- [ ] Documentation updated
- [ ] No TypeScript `any` types
- [ ] Commit messages follow convention
```

### Review Process

1. **CI Check:** GitHub Actions must pass (tests, build)
2. **Code Review:** Maintainer reviews code for quality
3. **Feedback:** Address review comments
4. **Approval:** Maintainer approves PR
5. **Merge:** Squash-merge into `main`

### After Merge

- Delete your feature branch
- Pull latest `main` for next contribution
- Celebrate! 🎉

---

## Questions or Need Help?

- **Setup issues:** See [SETUP.md](SETUP.md)
- **Bug reports:** [Open an issue](https://github.com/atinder-harika/service-status/issues)
- **Feature ideas:** Check [FUTURE_PLANS.md](FUTURE_PLANS.md) first
- **General questions:** Start a [discussion](https://github.com/atinder-harika/service-status/discussions)

---

**Thank you for contributing! Every contribution makes this project better.** 🚀
