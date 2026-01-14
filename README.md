**Project Review and Fixes Summary**

1. **Build & Startup Issues:**
   While reviewing the codebase and attempting to start the application using: mvn spring-boot:run -X , the build failed with the following error:
   
   "[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.3.5:run (default-cli) on project sernova-testing: Process terminated with exit code: 1
org.apache.maven.lifecycle.LifecycleExecutionException".

**Root causes that I identified are:**

i) The project included spring-boot-starter-data-jpa, which implicitly requires a valid database connection at application startup.

ii) No database driver or configuration was provided.

iii) JPA entity classes were missing mandatory identifiers.

iv) Java and Maven were using different Java versions, leading to build/runtime inconsistencies.


**Fixes I applied:**

i) Added MYSQL dependency in pom.xml

ii) Added MYSQL database configuration in application.properties file.

iii) Added missing @Id annotation in entities inorder to make them valid entities.

iv) Aligned maven version to 21 to match the Java version as maven uses JVM to compile and run the application. A mismatch between these version will cause build/run time issues.

After these changes, the application built and started successfully.

2. **N+1 Query Problem Analysis:**
   The /persons-with-addresses endpoint was tested against a dataset of 10,000 persons.Hibernate executed the following pattern
   -- Load all persons
  SELECT * FROM person;

  -- Then, for each person, load addresses
  SELECT * FROM address WHERE person_id = ?;
  SELECT * FROM address WHERE person_id = ?;
  SELECT * FROM address WHERE person_id = ?;
  ...

  This resulted in: 1 query to fetch all persons + 1 additional query per person to fetch addresses. With 10,000 persons we would get 10,000 queries in total. (1+10,000).
  I also measured the response time using curl command and result was 'time_total = 3.082790 seconds'


  According to my understanding, 
  "The N+1 problem occurred because persons and their addresses were loaded separately by Hibernate, even though the API returned them together in the response. In this setup, the relationship between Person and Address is defined using @OneToMany, which is lazy by default. As a result, when personRepository.findAll() is called, Hibernate executes a single query to fetch all Person records using a select * from person query, without loading their associated addresses.The issue appears later in the controller layer. Since the controller returns Person entities directly, Spring uses Jackson to convert these Java objects into JSON for the   HTTP response. During this serialization process, Jackson calls the getter methods on the Person entity, including getAddresses(). Because the addresses collection was not initialized earlier, Hibernate detects that a lazy-loaded collection is being accessed and triggers an additional database query to fetch the addresses for that specific person. This process repeats for every person in the result set, resulting in one initial query to load all persons and then one additional query per person to load their addresses—leading to the classic N+1 query problem".

  **Performance Improvements Implemented**

  To address the performance issue, I evaluated multiple techniques where I have noted down in table below, the response times for each technique.
  I choose **DTO projection + pagination** as my solution to improve the performance where I observed that this approach not only avoids returning the entities directly but also fetches the required fields (if we want) , applies pagination at DB level and prevents high memory usage and query explosion, better response time.
  Lastly, "DTO projection + pagination loads person and their address data in one SQL query and avoids the N+1 problem."
  

** Response Time Comparison (in seconds)**

| Technique                        | 1st Run  | 2nd Run  | 3rd Run  | Average      |
|----------------------------------|----------|----------|----------|------------- |
| JOIN FETCH                       | 0.683194 | 0.731773 | 0.716022 | 0.710329666  |
| DTO Projections                  | 0.342144 | 0.327969 | 0.370617 | 0.34691      |
| DTO Projections + Pagination     | 0.054362 | 0.071624 | 0.072443 | 0.066143     |
| Entity Graphs                    | 0.659531 | 0.655873 | 0.647552 | 0.6543186667 |

