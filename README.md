# TaskFlow — Task Management Application

TaskFlow is a task management web application built with **Java 21**, **Spring Boot 4.1.1**, **Thymeleaf**, and **Spring Data JPA**.

The application provides a clean, responsive interface for creating, organizing, updating, and deleting tasks.

## Features

- **Task management:** Create, view, edit, and delete tasks.
- **Task status:** Track progress using `TO_DO`, `IN_PROGRESS`, and `DONE`.
- **Due dates:** Assign optional deadlines to tasks.
- **Form validation:** Validate required fields and display helpful error messages.
- **Internationalization:** Switch between French and English.
- **Responsive interface:** Use the application on desktop, tablet, and mobile.
- **User feedback:** Display confirmation messages and deletion warnings.
- **Error handling:** Custom 404 and 500 error pages.
- **Automated testing:** Unit tests and controller tests with JUnit 5, Mockito, and MockMvc.

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot 4.1.1 |
| Web | Spring MVC |
| Frontend | Thymeleaf, HTML, CSS, JavaScript |
| Persistence | Spring Data JPA, Hibernate |
| Database | H2 (in-memory) |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5, Mockito, MockMvc |
| Build Tool | Maven |
| Version Control | Git & GitHub |

## Screenshots

Screenshots of the application will be added here.

<!-- Example:
![TaskFlow task list](docs/screenshots/task-list.png)
![TaskFlow task form](docs/screenshots/task-form.png)
-->

## Getting Started

### Prerequisites

- Java 21
- Git

The Maven Wrapper is included in the project, so a separate Maven installation is not required.

### Clone the Repository

```bash
git clone https://github.com/kardiguemagassa/task-management.git
cd task-management
```

### Run the Application

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Once the application has started, open:

**http://localhost:8080/tasks**

### H2 Database Console

The application uses an in-memory H2 database for development.

H2 console:

**http://localhost:8080/h2-console**

JDBC URL:

```text
jdbc:h2:mem:taskdb
```

Username:

```text
sa
```

Password: leave blank, unless you have changed the database configuration.

**Note:** Because H2 runs in memory, task data is not preserved when the application stops.

## Running Tests

Execute the complete test suite:

```bash
./mvnw clean test
```

The current test suite includes:

| Test Class | Tests |
|---|---:|
| TaskManagementApplicationTests | 1 |
| TaskServiceTest | 7 |
| TaskControllerTest | 11 |
| **Total** | **19** |

Tests cover application context loading, task service operations, HTTP routes, form validation, and error handling.

## Project Structure

```text
src/
├── main/
│   ├── java/com/kmgtechnologies/taskmanagement/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── i18n/
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       ├── templates/
│       │   ├── error/
│       │   ├── fragments/
│       │   └── tasks/
│       └── application.yml
└── test/
    └── java/com/kmgtechnologies/taskmanagement/
        ├── controller/
        └── service/
```

## Application Routes

| Method | Route | Description |
|---|---|---|
| GET | `/tasks` | Display all tasks |
| GET | `/tasks/new` | Display the creation form |
| POST | `/tasks` | Create a task |
| GET | `/tasks/{id}/edit` | Display the editing form |
| POST | `/tasks/{id}` | Update a task |
| POST | `/tasks/{id}/delete` | Delete a task |

## Internationalization

TaskFlow supports two languages:

- English
- French

Translations are stored in:

```text
src/main/resources/i18n/messages.properties
src/main/resources/i18n/messages_fr.properties
```

The selected language is managed through Spring MVC locale configuration.

## Future Improvements

- Persistent database support with PostgreSQL.
- Task search and filtering.
- User authentication and authorization.
- Pagination and sorting.
- Continuous integration with GitHub Actions.
- Deployment to a cloud hosting platform.

## Author

**Kardigue Magassa**

GitHub: [@kardiguemagassa](https://github.com/kardiguemagassa)

## License

No license has been specified for this project yet.