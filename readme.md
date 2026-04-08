## How to Run the Project

### Prerequisites
- Java 21 or higher installed
- Maven installed
- SQLite (included via Maven dependency)

### Building the Project
Run the following command in the project root directory:
```
mvn clean compile
```

### Running the Project
Execute the main application:
```
mvn exec:java -Dexec.mainClass="com.example.simulearn.Menu"
```

This will start the SimuLearn GUI application and the chat server in the background.
