import { describe, it, expect } from 'vitest';
import { getStatusColorClass, getStatusBgClass, formatTimestamp } from '../utils/status';

describe('Status Utilities', () => {
  describe('getStatusColorClass', () => {
    it('should return correct class for Operational', () => {
      expect(getStatusColorClass('Operational')).toBe('text-status-operational');
    });

    it('should return correct class for Degraded', () => {
      expect(getStatusColorClass('Degraded')).toBe('text-status-degraded');
    });

    it('should return correct class for Maintenance', () => {
      expect(getStatusColorClass('Maintenance')).toBe('text-status-maintenance');
    });

    it('should return correct class for Down', () => {
      expect(getStatusColorClass('Down')).toBe('text-status-down');
    });

    it('should return default class for Unknown', () => {
      expect(getStatusColorClass('Unknown')).toBe('text-neutral-400');
    });
  });

  describe('getStatusBgClass', () => {
    it('should return correct background class for Operational', () => {
      expect(getStatusBgClass('Operational')).toBe('bg-green-100 text-green-800');
    });

    it('should return correct background class for Down', () => {
      expect(getStatusBgClass('Down')).toBe('bg-red-100 text-red-800');
    });
  });

  describe('formatTimestamp', () => {
    it('should format valid timestamp', () => {
      const timestamp = '2025-10-27T22:00:00Z';
      const formatted = formatTimestamp(timestamp);
      expect(formatted).toMatch(/\d{1,2}:\d{2}:\d{2}/); // Matches time format
    });

    it('should return "Never" for null timestamp', () => {
      expect(formatTimestamp(null)).toBe('Never');
    });
  });
});
