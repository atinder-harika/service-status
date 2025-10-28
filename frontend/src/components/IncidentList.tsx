import type { Incident } from '../types';

interface IncidentListProps {
  incidents: Incident[];
}

/**
 * IncidentList Component - displays recent incidents/warnings
 */
export default function IncidentList({ incidents }: IncidentListProps) {
  if (incidents.length === 0) {
    return null;
  }

  return (
    <section className="mb-12">
      <h2 className="text-2xl font-bold mb-6 text-neutral-200">Incidents</h2>
      <div className="flex flex-col gap-4">
        {incidents.map((incident, index) => (
          <div key={index} className="bg-zinc-800 rounded-xl p-6 shadow-lg">
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
  );
}
