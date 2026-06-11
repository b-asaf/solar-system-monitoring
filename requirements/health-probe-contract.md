# Health Probe Contract

Purpose: define the minimal health probe contract for the Solar System Monitoring backend so operations, load balancers, and CI can verify service liveness.

Endpoint
 - Method: GET
 - Path: /api/health

Success Response (200 OK)
 - Content-Type: application/json
 - Body:
```json
{
  "status": "UP",
  "timestamp": "2026-01-01T00:00:00.000Z",
  "service": "Solar Monitoring Backend"
}
```

Failure Modes
 - 503 Service Unavailable: returned when critical subsystems (e.g., SolarEdge client init) are not ready.
 - 500 Internal Server Error: unexpected failures.

Operational Notes
 - The probe must return within 500ms under normal load.
 - The `/api/health` endpoint should not require authentication.
 - Kubernetes readinessProbe can use `GET /api/health` and expect `200`.

Example
```bash
curl -fsS http://localhost:3000/api/health
```
