import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, waitFor } from '@testing-library/react';
import { useServices } from '../hooks/useServices';
import { ApiService } from '../services/api';

// Mock the API service
vi.mock('../services/api');

describe('useServices Hook', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should fetch services on mount', async () => {
    const mockData = [
      {
        title: 'All Services',
        checks: [
          {
            id: 1,
            name: 'GitHub',
            url: 'https://github.com',
            checkType: 'HTTP',
            currentStatus: 'Operational' as const,
            lastCheckedAt: '2025-10-27T22:00:00Z',
          },
        ],
        status: 'Operational' as const,
      },
    ];

    vi.spyOn(ApiService, 'fetchServices').mockResolvedValue(mockData);

    const { result } = renderHook(() => useServices());

    expect(result.current.loading).toBe(true);

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.services).toEqual(mockData);
    expect(result.current.error).toBeNull();
  });

  it('should handle fetch errors', async () => {
    const errorMessage = 'Failed to connect';
    vi.spyOn(ApiService, 'fetchServices').mockRejectedValue(new Error(errorMessage));

    const { result } = renderHook(() => useServices());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.error).toBe(errorMessage);
    expect(result.current.services).toEqual([]);
  });

  it('should provide refetch function', async () => {
    const mockData = [
      {
        title: 'All Services',
        checks: [],
        status: 'Operational' as const,
      },
    ];

    const fetchSpy = vi.spyOn(ApiService, 'fetchServices').mockResolvedValue(mockData);

    const { result } = renderHook(() => useServices());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    // Initial fetch
    expect(fetchSpy).toHaveBeenCalledTimes(1);

    // Manual refetch
    result.current.refetch();

    await waitFor(() => {
      expect(fetchSpy).toHaveBeenCalledTimes(2);
    });
  });
});
