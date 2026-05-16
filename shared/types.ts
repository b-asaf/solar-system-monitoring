/**
 * Shared types for the Solar Monitoring System.
 * Used by both Backend and AI Agents for analysis.
 */

export interface SolarTelemetry {
  timestamp: string;
  currentPowerWatts: number;
  totalYieldWh: number;
}

export interface SystemHealth {
  status: 'OK' | 'WARNING' | 'ERROR';
  message: string;
}