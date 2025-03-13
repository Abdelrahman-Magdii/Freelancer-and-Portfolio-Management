# Freelancer Management System

This project is a **Freelancer Management System** that allows freelancers to register, add projects to their portfolio, and search for projects based on title or technologies used. The system is built using **Spring Boot** for the backend and **React Native** for the frontend.

---

## **Table of Contents**
1. [Technologies Used](#technologies-used)
2. [Setup Instructions](#setup-instructions)
    - [Backend (Spring Boot)](#backend-spring-boot)
3. [API Endpoints](#api-endpoints)
4. [Full Text Search With Indexing](#full-text-search-with-indexing)
5. [Assumptions](#assumptions)
6. [Testing](#testing)
7. [Deliverables](#deliverables)

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

### **Full-text search with indexing**

To implement **full-text search with indexing** in PostgreSQL for the `Project` entity, we need to follow these steps:

1. **Add a `search_vector` column** to the `Project` table to store the full-text search index.
2. **Create a GIN index** on the `search_vector` column for faster search queries.
3. **Create a trigger** to automatically update the `search_vector` column whenever a `Project` is inserted or updated.
4. **Write a function** to generate the `search_vector` by combining the `title` and `technologiesUsed` fields.

Below is the complete implementation:

---

### **Step 1: Add `search_vector` Column**
Add a `search_vector` column to the `Project` table to store the full-text search index.

```sql
ALTER TABLE Project ADD COLUMN search_vector tsvector;
```

---

### **Step 2: Create a GIN Index**
Create a GIN index on the `search_vector` column to optimize full-text search queries.

```sql
CREATE INDEX idx_project_search ON Project USING GIN (search_vector);
```

---

### **Step 3: Create a Function to Update `search_vector`**
Create a PostgreSQL function to generate the `search_vector` by combining the `title` (with weight `A`) and `technologiesUsed` (with weight `B`).

```sql
CREATE OR REPLACE FUNCTION update_search_vector() RETURNS TRIGGER AS $$
DECLARE
    tech_text TEXT;
BEGIN
    -- Convert the array of technologies into a space-separated string
    SELECT array_to_string(NEW.technologiesUsed, ' ') INTO tech_text;

    -- Ensure technologies are not null
    tech_text := COALESCE(tech_text, '');

    -- Update search_vector with title (Weight 'A') and technologies (Weight 'B')
    NEW.search_vector =
            setweight(to_tsvector('english', COALESCE(NEW.title, '')), 'A') ||
            CASE
                WHEN tech_text <> '' THEN setweight(to_tsvector('english', tech_text), 'B')
                ELSE ''::tsvector
                END;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

---

### **Step 4: Create a Trigger**
Create a trigger to automatically update the `search_vector` column whenever a `Project` is inserted or updated.

```sql
CREATE TRIGGER trg_update_search_vector
    BEFORE INSERT OR UPDATE ON Project
    FOR EACH ROW EXECUTE FUNCTION update_search_vector();
```

---

### **Step 5: Perform Full-Text Search**
To search for projects based on `title` or `technologiesUsed`, use the `tsquery` syntax with the `search_vector` column.

#### Example Query:
```sql
SELECT *
FROM Project
WHERE search_vector @@ to_tsquery('english', 'e-commerce & React');
```

This query will return all projects where the `title` or `technologiesUsed` contains the terms `e-commerce` and `React`.

---

### **Step 6: Update Existing Data**
If you already have data in the `Project` table, you need to update the `search_vector` column for all existing rows.

```sql
UPDATE Project
SET search_vector = setweight(to_tsvector('english', COALESCE(title, '')), 'A') ||
                    setweight(to_tsvector('english', COALESCE(array_to_string(technologiesUsed, ' '), '')), 'B');
```

---

### **Step 7: Integration with Spring Boot**
In your Spring Boot application, you can use the `@Query` annotation to perform full-text search queries.

#### Example Repository Method:
```java
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(value = "SELECT * FROM Project WHERE search_vector @@ to_tsquery('english', :query)", nativeQuery = true)
    List<Project> searchProjects(@Param("query") String query);
}
```

#### Example Service Method:
```java
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> searchProjects(String query) {
        return projectRepository.searchProjects(query);
    }
}
```

#### Example Controller Endpoint:
```java
@RestController
@RequestMapping("/api/portfolio")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/search")
    public List<Project> searchProjects(@RequestParam String query) {
        return projectService.searchProjects(query);
    }
}
```

---

### **Example API Call**
- **Endpoint**: `GET /api/portfolio/search?query=e-commerce`
- **Response**:
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

### **Summary**
- The `search_vector` column is used to store the full-text search index.
- A GIN index is created on the `search_vector` column for faster search queries.
- A trigger automatically updates the `search_vector` whenever a `Project` is inserted or updated.
- Full-text search queries can be performed using the `tsquery` syntax.

This implementation ensures efficient and accurate search functionality for projects based on `title` and `technologiesUsed`. Let me know if you need further assistance!

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

----
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