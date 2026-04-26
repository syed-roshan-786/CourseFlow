# Quick Start Guide for Spring Boot Backend

## ⚠️ IMPORTANT: MySQL Setup Required

Before running the application, you need to configure your MySQL credentials.

### Option 1: Set Your MySQL Password (Recommended)

1. Open `src/main/resources/application.properties`
2. Update line 7 with your MySQL root password:
   ```properties
   spring.datasource.password=YOUR_PASSWORD_HERE
   ```

### Option 2: Create a Local Configuration File

1. Copy `application-local.properties.template` to `application-local.properties`
2. Update it with your MySQL credentials
3. Run with: `java -jar target/aiquiz-0.0.1-SNAPSHOT.jar --spring.profiles.active=local`

## Running the Application

Once MySQL is configured:

```powershell
# Navigate to backend folder
cd backend

# Run the application
java -jar target/aiquiz-0.0.1-SNAPSHOT.jar
```

The server will start on **http://localhost:5000**

## What Password to Use?

If you don't know your MySQL root password:
- Try common defaults: empty string, "root", "password", "admin"
- Or reset it following MySQL documentation
- Or create a new MySQL user with known credentials

## Testing the Connection

You can test your MySQL credentials with:
```powershell
mysql -u root -p
# Enter your password when prompted
```

If this works, use the same password in `application.properties`.

## Need Help?

Check the full [README.md](./README.md) for more detailed setup instructions.
