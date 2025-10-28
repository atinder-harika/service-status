import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import ServiceCard from '../components/ServiceCard';
import type { ServiceCheck } from '../types';

describe('ServiceCard Component', () => {
  const mockService: ServiceCheck = {
    id: 1,
    name: 'GitHub',
    url: 'https://github.com',
    checkType: 'HTTP',
    currentStatus: 'Operational',
    lastCheckedAt: '2025-10-27T22:00:00Z',
  };

  it('should render service name and status', () => {
    render(<ServiceCard service={mockService} />);
    
    expect(screen.getByText('GitHub')).toBeInTheDocument();
    expect(screen.getByText('Operational')).toBeInTheDocument();
  });

  it('should render last checked timestamp when available', () => {
    render(<ServiceCard service={mockService} />);
    
    expect(screen.getByText(/Last checked:/)).toBeInTheDocument();
  });

  it('should not render timestamp when lastCheckedAt is null', () => {
    const serviceWithoutTimestamp = { ...mockService, lastCheckedAt: null };
    render(<ServiceCard service={serviceWithoutTimestamp} />);
    
    expect(screen.queryByText(/Last checked:/)).not.toBeInTheDocument();
  });

  it('should render as a link to service URL', () => {
    render(<ServiceCard service={mockService} />);
    
    const link = screen.getByRole('link');
    expect(link).toHaveAttribute('href', 'https://github.com');
    expect(link).toHaveAttribute('target', '_blank');
    expect(link).toHaveAttribute('rel', 'noopener noreferrer');
  });

  it('should apply correct status color class for Operational', () => {
    render(<ServiceCard service={mockService} />);
    
    const statusText = screen.getByText('Operational');
    expect(statusText).toHaveClass('text-status-operational');
  });

  it('should apply correct status color class for Down', () => {
    const downService = { ...mockService, currentStatus: 'Down' as const };
    render(<ServiceCard service={downService} />);
    
    const statusText = screen.getByText('Down');
    expect(statusText).toHaveClass('text-status-down');
  });
});
