import type { ServiceStatus } from '../types';

/**
 * Utility functions for status styling and formatting
 */

/**
 * Get Tailwind CSS class for service status color
 */
export function getStatusColorClass(status: ServiceStatus): string {
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
}

/**
 * Get background color class for status badges
 */
export function getStatusBgClass(status: ServiceStatus): string {
  switch (status) {
    case 'Operational':
      return 'bg-green-100 text-green-800';
    case 'Degraded':
      return 'bg-yellow-100 text-yellow-800';
    case 'Maintenance':
      return 'bg-blue-100 text-blue-800';
    case 'Down':
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
}

/**
 * Format timestamp to localized time string
 */
export function formatTimestamp(timestamp: string | null): string {
  if (!timestamp) return 'Never';
  return new Date(timestamp).toLocaleTimeString();
}
