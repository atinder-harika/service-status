import type { ServiceGroup } from '../types';
import { API_BASE_URL } from '../config/constants';

/**
 * API Service Layer - handles all backend communication
 * Similar to Repository layer in Spring Boot backend
 */

export class ApiService {
  /**
   * Fetch all service groups with their current status
   */
  static async fetchServices(): Promise<ServiceGroup[]> {
    const response = await fetch(`${API_BASE_URL}/api/services`);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    return response.json();
  }

  /**
   * Fetch single service by ID
   */
  static async fetchServiceById(id: number): Promise<ServiceGroup> {
    const response = await fetch(`${API_BASE_URL}/api/services/${id}`);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    return response.json();
  }

  /**
   * Health check endpoint (for keepalive)
   */
  static async healthCheck(): Promise<{ status: string }> {
    const response = await fetch(`${API_BASE_URL}/actuator/health`);
    return response.json();
  }
}
