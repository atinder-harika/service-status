import type { ServiceGroup as ServiceGroupType } from '../types';
import ServiceCard from './ServiceCard';

interface ServiceGroupProps {
  group: ServiceGroupType;
}

/**
 * ServiceGroup Component - displays a group of related services
 */
export default function ServiceGroup({ group }: ServiceGroupProps) {
  return (
    <div className="mb-8">
      <h2 className="text-2xl font-bold mb-6 text-neutral-200">{group.title}</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {group.checks.map((service) => (
          <ServiceCard key={service.id} service={service} />
        ))}
      </div>
    </div>
  );
}
