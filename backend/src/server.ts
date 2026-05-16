import express, { Request, Response } from 'express';
import dotenv from 'dotenv';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

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
});