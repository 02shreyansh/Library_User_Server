# 📚 Library User Service

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

## 📌 Overview
Library User Service is a subset of the Library Server responsible for managing user-related functionalities. This service handles user authentication, role management, and user status tracking within the library system.

## 🛠️ Technologies Used
- **Spring Boot** - Backend framework
- **Spring JPA (Hibernate)** - ORM for database interactions
- **PostgreSQL** - Database
- **HikariCP** - Connection pooling

## 🔑 Features
- User registration and authentication
- Role-based access control (Admin/User)
- User status tracking (Pending, Approved, Rejected)
- Integration with PostgreSQL
- Secure environment variable management

## 📂 Database Schema
The service uses the following tables:

### **Users Table**
| Column            | Type          | Constraints         |
|------------------|--------------|---------------------|
| `id`            | UUID          | Primary Key, Unique |
| `full_name`     | VARCHAR(255)  | Not Null           |
| `email`         | TEXT          | Not Null, Unique   |
| `university_id` | BIGINT        | Not Null, Unique   |
| `password`      | TEXT          | Not Null           |
| `university_card` | TEXT        | Not Null           |
| `status`        | ENUM         | Default: PENDING   |
| `role`          | ENUM         | Default: USER      |
| `last_activity_date` | DATE     | Default: NOW()     |
| `created_at`    | TIMESTAMP    | Default: NOW()     |

## 🔧 Setup Instructions

### **1️⃣ Clone the Repository**
```bash
 git clone https://github.com/yourusername/library-user-service.git
 cd library-user-service
```

### **2️⃣ Configure Environment Variables**
Create a `.env` file in the root directory and add the following:
```properties
SPRING_DATASOURCE_URL=jdbc:your_database_url
SPRING_DATASOURCE_USERNAME=your_user_name
SPRING_DATASOURCE_PASSWORD=your_secure_password
SPRING_DATASOURCE_DRIVER-CLASS-NAME=org.postgresql.Driver
```

### **3️⃣ Run the Application**
```bash
./mvnw spring-boot:run
```

## 📖 API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/users/register` | Register a new user |
| `POST` | `/api/users/login` | User authentication |
| `GET` | `/api/users/{id}` | Get user details by ID |
| `PATCH` | `/api/users/{id}/status` | Update user status |

## 🔒 Security & Best Practices
- 🔹 Use **environment variables** instead of hardcoding credentials.
- 🔹 Ensure **password hashing** before storing passwords.
- 🔹 Implement **role-based access control** to protect sensitive actions.

## ✨ Contributing
Pull requests are welcome! Please open an issue first to discuss any changes.

## 📜 License
This project is licensed under the MIT License .

---
🚀 Developed by **Shreyansh** using **Spring Boot & PostgreSQL**

