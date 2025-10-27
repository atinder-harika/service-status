import { useState, useEffect } from 'react';
import { SERVICE_CONFIG, INCIDENTS_DATA } from './data';
import type { ServiceGroup, ServiceStatus } from './types';

const StatusPage = (): JSX.Element => {
  const [services, setServices] = useState<ServiceGroup[]>([]);

  const updateAllServices = (): void => {
    const updatedServices: ServiceGroup[] = [];
    
    for (const group of SERVICE_CONFIG) {
      const updatedGroupChecks = [];
      for (const service of group.checks) {
        const isOperational = Math.random() > 0.2;
        updatedGroupChecks.push({
          ...service,
          status: (isOperational ? 'Operational' : 'Down') as ServiceStatus,
        });
      }
      updatedServices.push({
        ...group,
        checks: updatedGroupChecks
      });
    }
    
    setServices(updatedServices);
  };

  useEffect(() => {
    updateAllServices();
    const interval = setInterval(updateAllServices, 60000); 
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
                      className={`h-4 w-4 rounded-full ${getStatusColorClass(service.status ?? 'Down')}`}
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
                  <p className={`text-sm font-semibold ${getStatusColorClass(service.status ?? 'Down')}`}>
                    {service.status ?? 'Down'}
                  </p>
                </a>
              ))}
            </div>
          </div>
        ))}
      </section>

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
