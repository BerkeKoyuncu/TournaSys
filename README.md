# TournaSys
Sports Tournament Management System for CENG106 Object Oriented Programming.

## Project Summary
TournaSys is a GUI-based Java desktop application for managing sports tournaments. The system includes user registration and login, tournament creation, team management, match scheduling, score tracking, and standings display.

## Technologies
- Java 21
- JavaFX
- SQLite
- JDBC
- Maven
- Git / GitHub

## Package Structure
- `app`         -> Application bootstrap
- `ui`          -> JavaFX controllers and views
- `service`     -> Business logic
- `model`       -> Domain entities
- `dao`         -> Data access layer
- `exception`   -> Custom exceptions
- `interfaces`  -> Interfaces and contracts
- `util`        -> Helper utilities
- `config`      -> Database and app configuration

## Initial Team Task Mapping
- **Berke Koyuncu**: GUI Design, Screen Implementation, UI Integration with Backend
- **Muhammed Taha Eren**: Match Scheduling, Score Tracking, Standings
- **Yago Rodríguez de Pauli**: User Authentication and Role Management
- **Yunus Gedik**: Tournament and Team Management

## Local Setup
1. Install JDK 21 or a compatible JDK.
2. Open the project in IntelliJ IDEA or Eclipse.
3. Run `mvn clean javafx:run`.

## Notes
- Keep OOP principles visible in code design.
- Use at least one interface and proper exception handling.
- Keep GUI synchronized with the database.
- Document how to run the project before final submission.