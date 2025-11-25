<<<<<<< HEAD
=======
Sernova Technical Test — Spring Boot + JPA

Purpose
This repository is a hands-on technical assessment. Your job is to get a small Spring Boot app to build, start, and serve REST endpoints backed by JPA.

What you are expected to do
- Review the codebase and build configuration.
- Identify the reasons the application does not build/start.
- Make minimal, sensible changes to get it running.
- Keep your fixes clean and conventional (Spring Boot + Maven best practices).
- Briefly document what you changed and why (commit messages or a short NOTES.md).

What is being assessed
- Ability to diagnose startup/build issues in a Spring Boot app.
- Understanding of JPA basics and typical configuration.
- Ability to reason about one-to-many data access and performance.
- Code quality: small, focused commits and clear explanations.

What’s in the project
- Spring Boot 3 application with Web and Data JPA
- Simple REST controller
- JPA entities and repositories
- Data model: Person with multiple Address records (one-to-many)

Suggested approach
1) Try to build and run the app.
2) Inspect errors and logs; locate misconfigurations or missing pieces.
3) Apply minimal fixes to make the app start.
4) Verify the REST API works.
5) Run the performance exercise and propose at least one improvement.

Run instructions (after you fix the project)
- Build: mvn clean package
- Run: mvn spring-boot:run (or run the SernovaApplication main method)
- Test endpoints:
  - GET  http://localhost:8080/api/hello
  - GET  http://localhost:8080/api/persons
  - GET  http://localhost:8080/api/persons-with-addresses
  - POST http://localhost:8080/api/seed/people   (inserts 10,000 people with 2 addresses each)

Performance exercise: assess and improve one-to-many access performance
This app is set up so a one-to-many load can generate excessive queries.

Steps
1) Prepare data (once the app runs):
   - POST http://localhost:8080/api/seed/people (creates 10,000 persons with 2 addresses each)
2) Probe the endpoint that returns persons with addresses:
   - GET http://localhost:8080/api/persons-with-addresses
3) Measure and observe:
   - Example timing (Windows): curl -s -o NUL -w "time_total=%{time_total}\n" http://localhost:8080/api/persons-with-addresses
   - Enable/inspect SQL logs to see the number of SELECTs.
4) Improve performance using a technique of your choice (e.g., fetch strategies, entity graphs, pagination, or DTO projections).
5) Acceptance: repeat step 3 and demonstrate reduced queries and improved response time on the same dataset and environment.

Rules and constraints
- You may use an embedded or local database during development.
- Do not commit secrets or credentials.
- Prefer fixing and configuring existing code over removing features.
- Keep the test self-contained and easy to review.

What to submit
- Submit to your own repository
- A running project (after your fixes). 
- A brief note explaining:
  - The issues you identified.
  - The changes you made (and why).
  - Your performance improvement approach and its impact (before/after in short).

Files of interest
- pom.xml — build and dependency management
- src/main/java/com/sernova/SernovaApplication.java — entry point
- src/main/java/com/sernova/web/ApiController.java — REST endpoints
- src/main/java/com/sernova/domain/Person.java — JPA entity
- src/main/java/com/sernova/domain/Address.java — JPA entity
- src/main/java/com/sernova/domain/PersonRepository.java — JPA repository
- src/main/java/com/sernova/domain/AddressRepository.java — JPA repository
- src/main/resources/application.properties — application configuration

Notes
- This project is for assessment. Do not commit secrets.
>>>>>>> 2c82d13 (Update read me)
