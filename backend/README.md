# CourseFlow AI Quiz - Spring Boot Backend

## Prerequisites

1. **Java 17 or higher** - Already installed (Java 22 detected)
2. **Maven** - For building the project
3. **MySQL** - Database server (MySQL 8.0 detected and running)
4. **OpenAI API Key** - For quiz generation

## Setup Instructions

### 1. Configure MySQL Database

First, create the database and user. Open MySQL command line or MySQL Workbench and run:

```sql
CREATE DATABASE IF NOT EXISTS courseflow_aiquiz;
CREATE USER IF NOT EXISTS 'courseflow'@'localhost' IDENTIFIED BY 'courseflow123';
GRANT ALL PRIVILEGES ON courseflow_aiquiz.* TO 'courseflow'@'localhost';
FLUSH PRIVILEGES;
```

**OR** if you want to use the root user (not recommended for production):

Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_ROOT_PASSWORD
```

### 2. Set OpenAI API Key

Set the environment variable:

**PowerShell:**
```powershell
$env:OPENAI_API_KEY="your-api-key-here"
```

**OR** edit `src/main/resources/application.properties` and replace:
```properties
openai.api.key=your-actual-api-key-here
```

### 3. Build and Run

```powershell
# Set JAVA_HOME
$env:JAVA_HOME="C:\Program Files\Java\jdk-22"

# Build the project
mvn clean install

# Run the application
java -jar target/aiquiz-0.0.1-SNAPSHOT.jar
```

The server will start on **http://localhost:5000**

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login

### Courses
- `GET /courses` - Get all courses
- `GET /courses/{id}` - Get single course

### Quiz
- `POST /api/quiz` - Generate quiz from PDF (multipart/form-data with `file` and optional `level`)
- `POST /quiz/evaluate` - Evaluate quiz answers

## Frontend Integration

The frontend is configured to connect to `http://localhost:5000`. Make sure both servers are running:

1. **Backend (Spring Boot)**: `http://localhost:5000`
2. **Frontend (Vite)**: `http://localhost:5173`

## Troubleshooting

### Database Connection Error

If you see "HikariPool" or database connection errors:
1. Ensure MySQL is running: `Get-Service -Name "*mysql*"`
2. Verify database credentials in `application.properties`
3. Check if the database exists: `SHOW DATABASES;` in MySQL

### Port Already in Use

If port 5000 is in use, change in `application.properties`:
```properties
server.port=8080
```

Then update frontend `.env` file to match.
