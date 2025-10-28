# Frontend Architecture

## 📁 Project Structure

```
frontend/src/
├── components/          # Reusable UI Components
│   ├── ServiceCard.tsx         # Individual service status card
│   ├── ServiceGroup.tsx        # Group of related services
│   ├── LoadingSpinner.tsx      # Loading state component
│   ├── ErrorMessage.tsx        # Error state component
│   ├── IncidentList.tsx        # Incidents display
│   ├── AutoRefreshIndicator.tsx # Polling status indicator
│   └── index.ts                # Barrel exports
│
├── pages/               # Page-level Components (Controllers)
│   └── StatusPage.tsx          # Main status page (orchestrates components)
│
├── services/            # API Layer (Repository Pattern)
│   └── api.ts                  # Backend API calls
│
├── hooks/               # Custom React Hooks (Business Logic)
│   └── useServices.ts          # Service data fetching & polling logic
│
├── types/               # TypeScript Definitions
│   └── index.ts                # All application types
│
├── config/              # Configuration & Constants
│   └── constants.ts            # API URLs, polling intervals, app config
│
├── utils/               # Helper Functions
│   └── status.ts               # Status color mapping, formatting
│
├── data.ts              # Mock/Static Data (incidents)
├── App.tsx              # Root component
├── main.tsx             # Application entry point
└── index.css            # Global styles (Tailwind)
```

## 🏗️ Architecture Pattern (Similar to Spring Boot MVC)

### Layer Responsibilities:

| Layer | Frontend | Backend Equivalent | Purpose |
|-------|----------|-------------------|---------|
| **Pages** | `StatusPage.tsx` | `@Controller` | Orchestrates data flow, renders components |
| **Services** | `api.ts` | `@Repository` | Handles API calls, data fetching |
| **Hooks** | `useServices.ts` | `@Service` | Business logic, state management |
| **Components** | `ServiceCard.tsx`, etc. | View Templates | Reusable UI elements |
| **Utils** | `status.ts` | Utility classes | Helper functions |
| **Config** | `constants.ts` | `application.properties` | Configuration constants |
| **Types** | `index.ts` | DTOs/Models | Type definitions |

## 🔄 Data Flow

```
User Opens Page
      ↓
main.tsx → App.tsx → StatusPage.tsx (Page/Controller)
      ↓
useServices() hook (Business Logic)
      ↓
ApiService.fetchServices() (Repository)
      ↓
Backend API (http://localhost:8080/api/services)
      ↓
State Update (React useState)
      ↓
Components Re-render (ServiceGroup → ServiceCard)
```

## 📝 Naming Conventions

- **Components**: PascalCase (e.g., `ServiceCard.tsx`)
- **Hooks**: camelCase with `use` prefix (e.g., `useServices.ts`)
- **Utilities**: camelCase (e.g., `getStatusColorClass`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `API_BASE_URL`)
- **Types**: PascalCase (e.g., `ServiceGroup`)

## 🧪 Testing Strategy (Phase 2)

```
tests/
├── components/
│   ├── ServiceCard.test.tsx
│   └── ServiceGroup.test.tsx
├── hooks/
│   └── useServices.test.ts
├── services/
│   └── api.test.ts
└── pages/
    └── StatusPage.test.tsx
```

## 🚀 Key Benefits of This Structure

1. **Separation of Concerns**: Each file has a single responsibility
2. **Testability**: Components, hooks, and services can be tested independently
3. **Reusability**: Components can be used across different pages
4. **Maintainability**: Easy to locate and modify specific functionality
5. **Scalability**: Simple to add new features without affecting existing code
6. **Type Safety**: Centralized type definitions prevent inconsistencies
