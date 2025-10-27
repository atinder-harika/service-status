import type { ServiceGroup, Incident } from './types';

export const SERVICE_CONFIG: ServiceGroup[] = [
  {
    title: 'My Services',
    checks: [
      { name: 'GitHub Portfolio', url: 'https://example.com' },
      { name: 'Render Server', url: 'https://example.com' },
      { name: 'MongoDB Atlas', url: 'https://example.com' },
    ],
  },
  {
    title: 'External Services',
    checks: [
      { name: 'Google AI Studio', url: 'https://example.com' },
      { name: 'Hacker News', url: 'https://example.com' },
    ],
  },
];

export const INCIDENTS_DATA: Incident[] = [
  { 
    serviceName: 'MongoDB Atlas', 
    type: 'warning', 
    message: 'Scheduled maintenance from 02:00 AM to 04:00 AM UTC.' 
  },
  { 
    serviceName: 'Render Server', 
    type: 'info', 
    message: 'Experienced an API outage from 15:30 to 16:45 UTC. The issue has been resolved.' 
  },
];
