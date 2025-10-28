import { useServices } from '../hooks/useServices';
import { APP_CONFIG } from '../config/constants';
import type { Incident } from '../types';
import ServiceGroup from '../components/ServiceGroup';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import IncidentList from '../components/IncidentList';
import AutoRefreshIndicator from '../components/AutoRefreshIndicator';

// Temporary mock incidents (TODO: fetch from backend API)
const INCIDENTS_DATA: Incident[] = [
  { 
    serviceName: 'System', 
    type: 'info', 
    message: 'All services operational.' 
  },
];

/**
 * StatusPage - Main page component (like Controller in backend)
 * Orchestrates data fetching and component rendering
 */
export default function StatusPage() {
  const { services, loading, error, refetch } = useServices();

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <ErrorMessage error={error} onRetry={refetch} />;
  }

  return (
    <>
      <header className="text-center mb-12">
        <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-zinc-100 mb-2">
          {APP_CONFIG.name}
        </h1>
        <p className="text-neutral-400 text-lg">{APP_CONFIG.description}</p>
      </header>

      <section className="mb-12">
        {services.map((group) => (
          <ServiceGroup key={group.title} group={group} />
        ))}
      </section>

      {APP_CONFIG.autoRefresh && <AutoRefreshIndicator />}

      <IncidentList incidents={INCIDENTS_DATA} />
    </>
  );
}
