# TournaSys
Sports Tournament Management System for CENG106 Object-Oriented Programming.

## Project Summary
TournaSys is a GUI-based Java desktop application for managing sports tournaments. The system includes user registration and login, tournament creation, team management, match scheduling, score tracking, and standings display.

## Technologies
- Java 21
- JavaFX
- SQLite
- JDBC
- Maven
- Git / GitHub

## Main Features
- User registration and login with manager/player roles
- Tournament creation and deletion
- Team creation and duplicate team validation
- League fixture generation where every team plays every other team once
- Knockout fixture generation with automatic next-round creation
- Match score entry with validation
- Standings table updated from completed match results
- SQLite persistence for users, tournaments, teams, matches, and standings

## Package Structure
- `app`         -> Application bootstrap
- `controller`  -> JavaFX controllers
- `service`     -> Business logic
- `model`       -> Domain entities
- `repository`  -> Data access layer
- `exception`   -> Custom exceptions
- `interfaces`  -> Interfaces and contracts
- `util`        -> Helper utilities
- `config`      -> Database and app configuration
- `resources`   -> FXML views and CSS styles

## Initial Team Task Mapping
- **Berke Koyuncu**: GUI Design, Screen Implementation, UI Integration with Backend
- **Muhammed Taha Eren**: Match Scheduling, Score Tracking, Standings
- **Yago Rodriguez de Pauli**: User Authentication and Role Management
- **Yunus Gedik**: Tournament and Team Management

## Local Setup
1. Install JDK 21 or a compatible JDK.
2. Install Maven and make sure `mvn` is available in PATH.
3. Open the project in IntelliJ IDEA or Eclipse.
4. Run `mvn clean test`.
5. Run `mvn clean javafx:run`.

Maven tests use `target/test-tournasys.db`, while the desktop application uses `tournasys.db` in the project root.

## Demo Account
- Username: `berke`
- Password: `1234`

## Notes
- Keep OOP principles visible in code design.
- Use at least one interface and proper exception handling.
- Keep GUI synchronized with the database.
- Knockout tournaments require an even number of teams.
- Knockout match scores cannot end in a draw.
