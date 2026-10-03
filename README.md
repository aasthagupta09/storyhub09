# StoryHub
Spring Boot 3 + Thymeleaf + JPA. Requires JDK 17+ and Maven.

    mvn spring-boot:run        # then open http://localhost:8080
    Demo login: demo / demo123
    H2 console: http://localhost:8080/h2-console  (JDBC URL jdbc:h2:file:./data/storyhub, user sa)

For MySQL: run database/schema.sql, then switch the datasource lines in src/main/resources/application.properties.
