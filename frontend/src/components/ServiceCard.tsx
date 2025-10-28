import type { ServiceCheck } from '../types';
import { getStatusColorClass, formatTimestamp } from '../utils/status';

interface ServiceCardProps {
  service: ServiceCheck;
}

/**
 * ServiceCard Component - displays individual service status
 * Reusable UI component
 */
export default function ServiceCard({ service }: ServiceCardProps) {
  return (
    <a
      href={service.url}
      target="_blank"
      rel="noopener noreferrer"
      className="bg-zinc-800 rounded-xl p-6 flex flex-col items-start shadow-lg hover:bg-neutral-700 transition-colors cursor-pointer no-underline"
    >
      <div className="flex items-center gap-3 mb-2">
        <span className={`h-4 w-4 rounded-full ${getStatusColorClass(service.currentStatus)}`}>
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
        <p className="text-lg font-medium text-zinc-100">{service.name}</p>
      </div>
      <p className={`text-sm font-semibold ${getStatusColorClass(service.currentStatus)}`}>
        {service.currentStatus}
      </p>
      {service.lastCheckedAt && (
        <p className="text-xs text-neutral-500 mt-1">
          Last checked: {formatTimestamp(service.lastCheckedAt)}
        </p>
      )}
    </a>
  );
}
