import { useState, useEffect, useCallback } from 'react';
import type { ServiceGroup } from '../types';
import { ApiService } from '../services/api';
import { POLLING_INTERVAL } from '../config/constants';

/**
 * Custom hook for fetching and managing service data
 * Similar to Service layer in Spring Boot - contains business logic
 */
export function useServices() {
  const [services, setServices] = useState<ServiceGroup[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchServices = useCallback(async () => {
    try {
      const data = await ApiService.fetchServices();
      setServices(data);
      setError(null);
      setLoading(false);
    } catch (err) {
      console.error('Failed to fetch services:', err);
      setError(err instanceof Error ? err.message : 'Failed to connect to backend');
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // Initial fetch
    fetchServices();
    
    // Poll every 30 seconds (matches backend scheduler)
    const interval = setInterval(fetchServices, POLLING_INTERVAL);
    
    return () => clearInterval(interval);
  }, [fetchServices]);

  return { services, loading, error, refetch: fetchServices };
}
