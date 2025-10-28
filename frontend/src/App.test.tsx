import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import App from './App';
import { ApiService } from './services/api';

// Mock the API service
vi.mock('./services/api');

describe('App', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    
    // Mock successful API response
    vi.spyOn(ApiService, 'fetchServices').mockResolvedValue([
      {
        title: 'All Services',
        checks: [],
        status: 'Operational',
      },
    ]);
  });

  it('renders loading state initially', () => {
    render(<App />);
    expect(screen.getByText(/Loading services.../i)).toBeInTheDocument();
  });

  it('renders Service Status heading after loading', async () => {
    render(<App />);
    
    await waitFor(() => {
      const heading = screen.getByRole('heading', { name: /service status monitor/i });
      expect(heading).toBeInTheDocument();
    });
  });
});
