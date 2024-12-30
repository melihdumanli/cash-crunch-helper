# Cash Crunch Helper

**Cash Crunch Helper** is a backend application designed for managing customer loans, including loan creation, installment tracking, and payments. It provides RESTful APIs for these operations, secured with authentication.

---

## **Requirements**

- **Java Version**: 17
- **Build Tool**: Gradle

---

## **Features**

1. **Loan Management**: Create and manage loans for customers.
2. **Installment Tracking**: View loan installments with due dates.
3. **Payment Processing**: Pay off loan installments with rules for early/late payments.
4. **H2 In-Memory Database**: Simplified database access for development/testing.
5. **API Documentation**: Interactive Swagger UI for testing and understanding APIs.

---

## **How to Run the Project**

### **1. Clone the Repository**
```bash
https://github.com/your-repository-url.git
```

### **2. Navigate to the Project Directory**
```bash
cd cash-crunch-helper
```

### **3. Build the Project**
```bash
./gradlew build
```

### **4. Run the Application**
```bash
./gradlew bootRun
```

---

## **Accessing the Application**

### **1. Swagger UI**
- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Use this interface to explore and test API endpoints.

### **2. H2 Database Console**
- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- Database Connection Details:
  - **JDBC URL**: `jdbc:h2:mem:testdb`
  - **Username**: `sa`
  - **Password**: *password*

---

## **API Endpoints**

### **1. Loan Management**
- **POST** `/loans`: Create a new loan for a customer.
- **GET** `/loans`: Retrieve all loans for a customer.

### **2. Installment Management**
- **GET** `/loans/{loanId}/installments`: Retrieve installments for a specific loan.

### **3. Payment Processing**
- **POST** `/loans/{loanId}/pay`: Make a payment for one or more installments.

---

## **Default Configuration**

- **Port**: `8080`
- **Database**: H2 (In-Memory)
- **Swagger**: Enabled at `/swagger-ui.html`

---

## **Developer Notes**

### **Customization**
- Update application configurations in `src/main/resources/application.properties`.
- Example:
  ```properties
  server.port=8080
  spring.datasource.url=jdbc:h2:mem:testdb
  ```


