# Invoice Management System

A comprehensive, multi-tenant Spring Boot application for managing invoices, customers, payments, and business operations. Built with modern Java technologies and designed for scalability and security.

## 🚀 Features

### Core Functionality
- **Invoice Management**: Create, update, delete, and track invoices with automatic numbering
- **Customer Management**: Complete customer lifecycle management
- **Payment Processing**: Handle payments with vouchers and receipts
- **Ledger System**: Automatic ledger entries for all financial transactions
- **Dashboard Analytics**: Real-time statistics and reporting
- **Multi-tenancy**: Support for multiple businesses with isolated data

### Business Features
- **Business Registration**: Self-service business onboarding
- **Role-based Access Control**: Multiple user roles (Owner, Admin, Manager, Pharmacist, etc.)
- **Customizable Settings**: Business-specific configurations (invoice prefixes, currency, etc.)
- **Financial Reporting**: Monthly statistics, ledger reports, and transaction history
- **Payment Tracking**: Monitor invoice status (Paid, Partially Paid, Unpaid)

### Technical Features
- **JWT Authentication**: Secure token-based authentication
- **Database Isolation**: Separate databases per tenant
- **RESTful APIs**: Comprehensive API endpoints
- **Exception Handling**: Global exception management
- **Validation**: Input validation and business rule enforcement
- **Async Processing**: Background tasks for database operations

## 🏗️ Architecture

### Multi-Tenant Architecture
The application uses a **database-per-tenant** approach:
- **Central Database**: Stores business registration and master user data
- **Tenant Databases**: Isolated databases for each business
- **Dynamic DataSource Routing**: Automatic tenant context switching
- **Tenant Context**: Thread-local storage for current tenant identification

### Technology Stack
- **Java 17**: Latest LTS version
- **Spring Boot 3.5.0**: Modern Spring framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations
- **MySQL**: Primary database
- **JWT**: Token-based authentication
- **Lombok**: Code generation
- **Maven**: Build tool

### Project Structure
```
src/main/java/com/app/invoice/
├── configs/           # Configuration classes
│   ├── tenants/      # Multi-tenancy configuration
│   └── WebSecurityConfig.java
├── exception/         # Custom exceptions
├── master/           # Central/master functionality
│   ├── controllers/  # Business registration APIs
│   ├── entity/       # Master entities
│   ├── repos/        # Master repositories
│   └── services/     # Master services
├── security/         # Security configuration
│   ├── jwt/          # JWT utilities
│   └── services/     # User details service
├── tenant/           # Tenant-specific functionality
│   ├── controllers/  # Business logic APIs
│   ├── dto/          # Data transfer objects
│   ├── entity/       # Tenant entities
│   ├── mapper/       # Entity mappers
│   ├── repos/        # Tenant repositories
│   ├── services/     # Business services
│   └── serviceImpl/  # Service implementations
└── utils/            # Utility classes
```

## 🛠️ Setup & Installation

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup
1. **Create MySQL databases**:
```sql
CREATE DATABASE pharmacy_db;
CREATE DATABASE pharmacy_central_db;
CREATE DATABASE pharmacy_tenant_db;
```

2. **Configure database connection** in `application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.central.username=your_username
spring.datasource.central.password=your_password
spring.datasource.tenant.username=your_username
spring.datasource.tenant.password=your_password
```

### Application Setup
1. **Clone the repository**:
```bash
git clone <repository-url>
cd invoice-application
```

2. **Build the project**:
```bash
mvn clean install
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Superuser Account
- **Username**: `superadmin`
- **Password**: `SuperAdmin123!`
- **Email**: `superadmin@pharmacy.com`

## 🔐 Authentication & Authorization

### JWT Configuration
- **Secret Key**: Configured in `application.properties`
- **Access Token Expiry**: 24 hours
- **Refresh Token Expiry**: 7 days

### User Roles
- **OWNER**: Full system access, creates the business
- **PHARMACY_ADMIN**: Administrative access
- **MANAGER**: Staff and inventory management
- **PHARMACIST**: Licensed medication dispensing
- **PHARMACY_ASSISTANT**: Non-dispensing tasks
- **CASHIER**: Payment handling
- **INVENTORY_CLERK**: Inventory management

### API Authentication
Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
X-TenantID: <tenant-id>
```

## 📡 API Endpoints

### Master APIs (Business Registration)
- `POST /api/v1/master/business/register` - Register new business
- `GET /api/v1/master/business/activated` - Get activated businesses
- `PUT /api/v1/master/business/{id}/activate` - Activate business
- `PUT /api/v1/master/business/{id}/deactivate` - Deactivate business

### Authentication APIs
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh JWT token
- `POST /api/v1/auth/reset-password` - Password reset

### Invoice Management
- `POST /api/v1/invoices` - Create invoice
- `POST /api/v1/invoices/all` - Get all invoices
- `POST /api/v1/invoices/{id}/update` - Update invoice
- `POST /api/v1/invoices/{id}/delete` - Delete invoice

### Customer Management
- `POST /api/v1/customers` - Create customer
- `POST /api/v1/customers/all` - Get all customers
- `POST /api/v1/customers/{id}/update` - Update customer
- `POST /api/v1/customers/{id}/delete` - Delete customer

### Payment Management
- `POST /api/v1/payments/{invoiceId}/pay` - Process payment
- `POST /api/v1/payments/all` - Get all payments
- `POST /api/v1/receipts/all` - Get all receipts

### Dashboard & Analytics
- `POST /api/v1/stats` - Get dashboard statistics
- `POST /api/v1/ledger` - Get ledger summary
- `POST /api/v1/ledger/transactions` - Get ledger transactions
- `POST /api/v1/ledger/summary` - Get detailed ledger report

### Business Settings
- `POST /api/v1/business/settings` - Update business settings
- `POST /api/v1/business/settings/current` - Get current settings

## 📊 Dashboard Features

### Key Metrics
- Total customers, invoices, receipts, and vouchers
- Total sales and payments
- Overdue and upcoming invoices
- Monthly revenue statistics

### Recent Activity
- Latest 5 invoices, receipts, and payments
- Recent ledger transactions
- Real-time financial updates

### Financial Reports
- **Ledger Summary**: Total credits, debits, and balance
- **Transaction History**: Chronological ledger entries
- **Monthly Statistics**: Revenue trends by month

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Database Configuration
spring.datasource.jdbc-url=jdbc:mysql://localhost/pharmacy_db
spring.datasource.central.jdbc-url=jdbc:mysql://localhost/pharmacy_central_db
spring.datasource.tenant.jdbc-url=jdbc:mysql://localhost/pharmacy_tenant_db

# JWT Configuration
pharmacy.app.jwtSecret=your-secret-key
pharmacy.app.jwtExpirationMs=86400000
pharmacy.app.jwtRefreshExpirationMs=604800000

# Superuser Configuration
pharmacy.superuser.username=superadmin
pharmacy.superuser.password=SuperAdmin123!
pharmacy.superuser.email=superadmin@pharmacy.com

# Multitenancy Configuration
multitenancy.datasource.base-url=jdbc:mysql://localhost:3306/
```

### Business Settings
Each business can customize:
- Invoice/receipt/voucher prefixes
- Currency and currency symbol
- Invoice terms and notes
- Business logo and branding

## 🚀 Deployment

### Production Deployment
1. **Build JAR file**:
```bash
mvn clean package -DskipTests
```

2. **Run with production profile**:
```bash
java -jar target/pharmacy-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/pharmacy-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### API Testing
Use tools like Postman or curl to test the APIs:

```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phoneNo":"superadmin","password":"SuperAdmin123!"}'

# Create Invoice (with JWT token)
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Authorization: Bearer <jwt-token>" \
  -H "X-TenantID: <tenant-id>" \
  -H "Content-Type: application/json" \
  -d '{"customer":{"id":1},"invoiceItems":[{"description":"Item 1","quantity":2,"unitPrice":100.00}]}'
```

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: Fine-grained access control
- **Input Validation**: Comprehensive validation for all inputs
- **SQL Injection Prevention**: Parameterized queries
- **XSS Protection**: Input sanitization
- **CORS Configuration**: Cross-origin resource sharing setup

## 📝 Error Handling

The application includes comprehensive error handling:
- **Custom Exceptions**: Business-specific exception types
- **Global Exception Handler**: Centralized error processing
- **Validation Errors**: Input validation feedback
- **HTTP Status Codes**: Appropriate status codes for different scenarios

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔄 Version History

- **v0.0.1-SNAPSHOT**: Initial release with core invoice management features
- Multi-tenant architecture implementation
- JWT authentication system
- Dashboard and reporting features
- Payment processing system

---

**Built with ❤️ using Spring Boot and modern Java technologies** 
