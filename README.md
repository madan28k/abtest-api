# A/B Test Results API

A REST API built with Java Spring Boot that ingests experiment results and performs statistical analysis to determine if a treatment is statistically significant over a control group.

## Features
- Create and manage A/B experiments
- Submit control and treatment variant results
- Compute statistical metrics: mean, standard deviation, z-score, p-value
- Determine significance at 95% confidence level (two-tailed z-test)

## Tech Stack
Java 25 · Spring Boot 3.2 · Maven · REST API

## Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/experiments` | Create a new experiment |
| GET | `/experiments` | List all experiments |
| POST | `/experiments/{id}/results` | Submit a variant result |
| GET | `/experiments/{id}/analysis` | Get statistical analysis |

## Sample Analysis Output
```json
{
  "control_count": 3,
  "treatment_count": 3,
  "control_mean": 0.3067,
  "treatment_mean": 0.4533,
  "z_score": 10.5685,
  "p_value": 0.0,
  "significant": true,
  "verdict": "Treatment is statistically significant at 95% confidence"
}
```

## Running Locally
```bash
mvn spring-boot:run
```
API runs on `http://localhost:8080`