# Freelancer Management System

This project is a **Freelancer Management System** that allows freelancers to register, add projects to their portfolio, and search for projects based on title or technologies used. The system is built using **Spring Boot** for the backend and **React Native** for the frontend.

---

## **Table of Contents**
1. [Technologies Used](#technologies-used)
2. [Setup Instructions](#setup-instructions)
    - [Backend (Spring Boot)](#backend-spring-boot)
3. [API Endpoints](#api-endpoints)
4. [Assumptions](#assumptions)
5. [Testing](#testing)
6. [Deliverables](#deliverables)

---

## **Technologies Used**
- **Backend**: Spring Boot, PostgreSQL, Hibernate, Lombok
- **Database**: PostgreSQL
- **Tools**: Postman (for API testing), Git (for version control)

---

## **Setup Instructions**

### **Backend (Spring Boot)**

1. **Prerequisites**:
    - Java 17 or higher
    - Maven (for dependency management)
    - PostgreSQL (for the database)

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/Abdelrahman-Magdii/freelancer-management-system-backend.git
   cd freelancer-management-system/backend
   ```

3. **Database Setup**:
    - Create a PostgreSQL database named `freelancer_db`.
    - Update the `application.properties` file with your database credentials:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/freelancer
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      ```

4. **Run the Application**:
    - Build and run the Spring Boot application:
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    - The backend will start at `http://localhost:8080`.

---

## **API Endpoints**

### **Backend API**
- **Register a Freelancer**:
    - `POST /api/register`
    - Request Body:
      ```json
      {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "password": "password123",
        "specialization": "Web Development"
      }
      ```

- **Add a Project to Portfolio**:
    - `POST /api/portfolio/add?freelancerId=1`
    - Request Body:
      ```json
      {
        "title": "E-commerce Website",
        "description": "Developed a fully functional e-commerce website.",
        "technologiesUsed": ["React", "Node.js", "PostgreSQL"]
      }
      ```

- **Search Projects**:
    - `GET /api/portfolio/search?query=e-commerce`
    - Response:
      ```json
      [
        {
          "id": 1,
          "title": "E-commerce Website",
          "description": "Developed a fully functional e-commerce website.",
          "technologiesUsed": ["React", "Node.js", "PostgreSQL"],
          "freelancer": {
            "id": 1,
            "name": "John Doe",
            "email": "john.doe@example.com",
            "specialization": "Web Development"
          }
        }
      ]
      ```

---

## **Assumptions**
1. **Freelancer Registration**:
    - A freelancer must provide a unique email address during registration.
    - The password is stored in plain text for simplicity (in a real-world scenario, use password hashing).

2. **Project Management**:
    - Each project must be associated with a freelancer.
    - The `technologiesUsed` field is stored as a list of strings in a separate table (`project_technologies`).

3. **Search Functionality**:
    - The search functionality uses PostgreSQL's full-text search to query projects by title or technologies used.

4. **Database Schema**:
    - The database schema is automatically updated using Hibernate's `spring.jpa.hibernate.ddl-auto=update`.

---

## **Testing**
- Use **Postman** to test the backend API endpoints.
- A Postman collection is provided in the `postman` folder for easy testing.
- **Postman Doc**: [Link](https://documenter.getpostman.com/view/36966051/2sAYk8wPh9)
---

## **Deliverables**
- **GitHub Repository**: Contains both frontend and backend source code.
- **README.md**: This file, explaining setup instructions and assumptions.
- **Postman Collection**: For testing the backend API endpoints.

---

## **Contact**
If you have any questions or need further assistance, feel free to reach out:
- **Email**: abdulrahmanmagdi527@gmail.com
- **GitHub**: [Abdelrahman-Magdii](https://github.com/Abdelrahman-Magdii)
- **LinkedIn**: [Abdelrahman Magdi](www.linkedin.com/in/abdelrahman-magdii)

---