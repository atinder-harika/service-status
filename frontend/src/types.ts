export type ServiceStatus = 'Operational' | 'Degraded' | 'Maintenance' | 'Down';

export interface ServiceCheck {
  name: string;
  url: string;
  status?: ServiceStatus;
}

export interface ServiceGroup {
  title: string;
  checks: ServiceCheck[];
}

export type IncidentType = 'warning' | 'info';

export interface Incident {
  serviceName: string;
  type: IncidentType;
  message: string;
}
