/**
 * TypeScript Type Definitions
 * Central location for all application types
 */

export type ServiceStatus = 'Operational' | 'Degraded' | 'Maintenance' | 'Down' | 'Unknown';

export interface ServiceCheck {
  id: number;
  name: string;
  url: string;
  checkType: string;
  currentStatus: ServiceStatus;
  lastCheckedAt: string | null;
}

export interface ServiceGroup {
  title: string;
  checks: ServiceCheck[];
  status: ServiceStatus;
}

export type IncidentType = 'warning' | 'info';

export interface Incident {
  serviceName: string;
  type: IncidentType;
  message: string;
}
