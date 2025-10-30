/**
 * Application Configuration Constants
 */

export const API_BASE_URL = import.meta.env.VITE_API_URL;

export const POLLING_INTERVAL = 30000; // 30 seconds (matches backend scheduler)

export const APP_CONFIG = {
  name: 'Service Status Monitor',
  description: 'Real-time status updates for all our web services',
  autoRefresh: true,
} as const;
