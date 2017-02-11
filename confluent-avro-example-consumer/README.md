# Maven Java Building

Basic building structure with **Apache Maven** for **Java** development.

## Requirements

- JDK 1.8
- Apache Maven 3.x

## Building

The first time just use:

`$ mvn -N io.takari:maven:wrapper`

For develop cycle use:

`$ ./mvnw clean package exec:exec`

For execute in standalone use:

`$ java -jar target/mvn-java-building.jar`