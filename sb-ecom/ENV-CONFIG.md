# Environment Configuration Guide

This application has been configured to use environment variables for sensitive and configurable values, following the [12-Factor App](https://12factor.net/config) methodology.

## 📁 Files Overview

- **`.env`** - Your actual environment variables (DO NOT commit to Git!)
- **`.env.example`** - Template file showing all available variables
- **`application.properties`** - Spring Boot configuration using environment variables

## 🚀 Quick Setup

1. **Copy the example file:**
   ```bash
   cp .env.example .env
   ```

2. **Edit `.env` with your actual values:**
   ```bash
   nano .env  # or your preferred editor
   ```

3. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

## 🔧 Configuration Categories

### Database Configuration
```properties
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

### JWT Security
```properties
JWT_SECRET=your-super-secret-jwt-key-minimum-256-bits-long
JWT_EXPIRATION_MS=86400000
JWT_COOKIE_NAME=springBootEcom
```

### Application Settings
```properties
PROJECT_IMAGE_DIR=images/
FRONTEND_ALLOWED_ORIGINS=http://localhost:3000/,http://localhost:5173/
IMAGE_BASE_URL=http://localhost:8080/images
```

### External APIs
```properties
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key_here
```

### Debug/Development
```properties
DEBUG_ENABLED=false
SQL_DEBUG_ENABLED=false
SECURITY_DEBUG_ENABLED=false
```

## 🌍 Environment-Specific Configurations

### Development
```properties
DB_URL=jdbc:postgresql://localhost:5432/ecommerce_dev
FRONTEND_ALLOWED_ORIGINS=http://localhost:3000/,http://localhost:5173/
DEBUG_ENABLED=true
SQL_DEBUG_ENABLED=true
```

### Production
```properties
DB_URL=jdbc:postgresql://prod-host:5432/ecommerce_prod
FRONTEND_ALLOWED_ORIGINS=https://yourdomain.com/
DEBUG_ENABLED=false
COOKIE_DOMAIN=.yourdomain.com
STRIPE_SECRET_KEY=sk_live_your_live_key
```

### Testing
```properties
DB_URL=jdbc:h2:mem:test
DEBUG_ENABLED=false
SQL_DEBUG_ENABLED=false
```

## 🔒 Security Best Practices

### DO NOT commit sensitive values:
- Database passwords
- JWT secrets
- API keys
- Production URLs

### Use strong values:
- **JWT_SECRET**: Minimum 256 bits (32+ characters)
- **DB_PASSWORD**: Use complex passwords
- **STRIPE_SECRET_KEY**: Never use test keys in production

### Environment separation:
- Different `.env` files for dev/staging/prod
- Use CI/CD tools to inject variables
- Consider using secret management services

## 📦 Deployment Options

### Docker
```dockerfile
# In Dockerfile
ENV DB_URL=jdbc:postgresql://db:5432/ecommerce
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
```

### Docker Compose
```yaml
# docker-compose.yml
environment:
  - DB_URL=jdbc:postgresql://db:5432/ecommerce
  - DB_USERNAME=${DB_USERNAME}
  - DB_PASSWORD=${DB_PASSWORD}
env_file:
  - .env
```

### Kubernetes
```yaml
# Use ConfigMaps and Secrets
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
type: Opaque
data:
  db-password: <base64-encoded-password>
  jwt-secret: <base64-encoded-jwt-secret>
```

### AWS Elastic Beanstalk
Set environment variables in the EB console or use `.ebextensions`:

```yaml
# .ebextensions/environment.config
option_settings:
  aws:elasticbeanstalk:application:environment:
    DB_URL: "jdbc:postgresql://your-rds-endpoint:5432/ecommerce"
    SERVER_PORT: "5000"
```

### Heroku
```bash
heroku config:set DB_URL=jdbc:postgresql://...
heroku config:set JWT_SECRET=your-secret
```

## 🧪 Testing Configuration

### Unit Tests
Environment variables with defaults will work automatically:
```bash
mvn test
```

### Integration Tests
Use test-specific values:
```bash
# Create .env.test
DB_URL=jdbc:h2:mem:test
DEBUG_ENABLED=true

# Run with test profile
mvn test -Dspring.profiles.active=test
```

## 🔍 Troubleshooting

### Application won't start
1. Check if `.env` file exists
2. Verify all required variables are set
3. Check for typos in variable names

### Database connection issues
1. Verify `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
2. Check if database server is running
3. Test connection manually

### JWT authentication issues
1. Ensure `JWT_SECRET` is set and long enough
2. Check `JWT_EXPIRATION_MS` value
3. Verify `JWT_COOKIE_NAME` matches frontend

### Environment variables not loading
1. Ensure `.env` is in the root directory
2. Check `spring.config.import` in `application.properties`
3. Restart the application

## 📝 Variable Reference

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `DB_URL` | Yes | - | Database connection URL |
| `DB_USERNAME` | Yes | - | Database username |
| `DB_PASSWORD` | Yes | - | Database password |
| `JWT_SECRET` | Yes | - | JWT signing secret |
| `JWT_EXPIRATION_MS` | No | `86400000` | JWT expiration time |
| `JWT_COOKIE_NAME` | No | `springBootEcom` | JWT cookie name |
| `STRIPE_SECRET_KEY` | Yes | - | Stripe API secret key |
| `PROJECT_IMAGE_DIR` | No | `images/` | Image directory |
| `FRONTEND_ALLOWED_ORIGINS` | No | `http://localhost:5173/` | CORS origins |
| `IMAGE_BASE_URL` | No | `http://localhost:8080/images` | Image base URL |
| `SERVER_PORT` | No | `8080` | Server port |
| `DEBUG_ENABLED` | No | `false` | Enable debug logging |
| `SQL_DEBUG_ENABLED` | No | `false` | Enable SQL debug |
| `SECURITY_DEBUG_ENABLED` | No | `false` | Enable security debug |

## 🔄 Migration from application.properties

If you're migrating from hardcoded values in `application.properties`:

1. **Identify sensitive values** (passwords, secrets, API keys)
2. **Move to `.env`** with appropriate variable names
3. **Update `application.properties`** to use `${VARIABLE_NAME:default}`
4. **Test thoroughly** in development
5. **Deploy with proper environment variables**

Remember: Environment variables provide flexibility, security, and follow cloud-native best practices!