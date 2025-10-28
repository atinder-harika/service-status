import { useState, useEffect } from 'react';
import { INCIDENTS_DATA } from './data';
import type { ServiceGroup, ServiceStatus } from './types';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const StatusPage = (): JSX.Element => {
  const [services, setServices] = useState<ServiceGroup[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchServices = async (): Promise<void> => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/services`);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const data: ServiceGroup[] = await response.json();
      setServices(data);
      setError(null);
      setLoading(false);
    } catch (err) {
      console.error('Failed to fetch services:', err);
      setError(err instanceof Error ? err.message : 'Failed to connect to backend');
      setLoading(false);
    }
  };

  useEffect(() => {
    // Initial fetch
    fetchServices();
    
    // Poll every 30 seconds (matches backend scheduler)
    const interval = setInterval(fetchServices, 30000);
    
    return () => clearInterval(interval);
  }, []);

  const getStatusColorClass = (status: ServiceStatus): string => {
    switch (status) {
      case 'Operational':
        return 'text-status-operational';
      case 'Degraded':
        return 'text-status-degraded';
      case 'Maintenance':
        return 'text-status-maintenance';
      case 'Down':
        return 'text-status-down';
      default:
        return 'text-neutral-400';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl text-neutral-300">Loading services...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="text-xl text-red-400 mb-4">Failed to load services</div>
          <div className="text-sm text-neutral-400">{error}</div>
          <button 
            onClick={fetchServices}
            className="mt-4 px-4 py-2 bg-zinc-700 hover:bg-zinc-600 text-white rounded-lg transition-colors"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  return (
    <>
      <header className="text-center mb-12">
        <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-zinc-100 mb-2">
          Service Status
        </h1>
        <p className="text-neutral-400 text-lg">
          Real-time status updates for all our web services.
        </p>
      </header>

      <section className="mb-12">
        {services.map((group) => (
          <div key={group.title} className="mb-8">
            <h2 className="text-2xl font-bold mb-6 text-neutral-200">
              {group.title}
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {group.checks.map((service) => (
                <a
                  key={service.name}
                  href={service.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="bg-zinc-800 rounded-xl p-6 flex flex-col items-start shadow-lg hover:bg-neutral-700 transition-colors cursor-pointer no-underline"
                >
                  <div className="flex items-center gap-3 mb-2">
                    <span
                      className={`h-4 w-4 rounded-full ${getStatusColorClass(service.currentStatus)}`}
                    >
                      <svg
                        className="animate-pulse fill-current h-4 w-4"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          fillRule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                          clipRule="evenodd"
                        />
                      </svg>
                    </span>
                    <p className="text-lg font-medium text-zinc-100">
                      {service.name}
                    </p>
                  </div>
                  <p className={`text-sm font-semibold ${getStatusColorClass(service.currentStatus)}`}>
                    {service.currentStatus}
                  </p>
                  {service.lastCheckedAt && (
                    <p className="text-xs text-neutral-500 mt-1">
                      Last checked: {new Date(service.lastCheckedAt).toLocaleTimeString()}
                    </p>
                  )}
                </a>
              ))}
            </div>
          </div>
        ))}
      </section>

      <div className="text-center text-sm text-neutral-500 mb-8">
        Auto-refreshing every 30 seconds
      </div>

      <section className="mb-12">
        <h2 className="text-2xl font-bold mb-6 text-neutral-200">
          Incidents
        </h2>
        <div className="flex flex-col gap-4">
          {INCIDENTS_DATA.map((incident, index) => (
            <div
              key={index}
              className="bg-zinc-800 rounded-xl p-6 shadow-lg"
            >
              <p className="text-lg font-medium text-zinc-100">
                <span className="font-semibold text-status-degraded">
                  [{incident.serviceName}]
                </span>{' '}
                <span className="text-neutral-200">{incident.message}</span>
              </p>
            </div>
          ))}
        </div>
      </section>
    </>
  );
};

export default StatusPage;
