import express, { Request, Response } from 'express';
import dotenv from 'dotenv';
import swaggerUi from 'swagger-ui-express';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// OpenAPI spec (minimal)
const openApiSpec = {
  openapi: '3.0.0',
  info: {
    title: 'Solar System Monitoring API',
    version: '1.0.0',
    description: 'Minimal API surface for the Solar Monitoring backend'
  },
  servers: [
    {
      url: 'http://localhost:' + PORT,
      description: 'Local dev'
    }
  ],
  paths: {
    '/api/health': {
      get: {
        summary: 'Server health probe',
        responses: {
          '200': {
            description: 'OK',
            content: {
              'application/json': {
                schema: {
                  type: 'object',
                  properties: {
                    status: { type: 'string' },
                    timestamp: { type: 'string', format: 'date-time' },
                    service: { type: 'string' }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
};

// Swagger UI
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openApiSpec));

// Free tier/low cost strategy: Simple HTTP triggers for future agent invocations
app.get('/api/health', (req: Request, res: Response) => {
  res.status(200).json({
    status: 'UP',
    timestamp: new Date(),
    service: 'Solar Monitoring Backend'
  });
});

app.listen(PORT, () => {
  console.log(`[Server]: Running at http://localhost:${PORT}`);
  console.log(`[Swagger]: http://localhost:${PORT}/api-docs`);
});