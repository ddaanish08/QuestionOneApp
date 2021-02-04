# Employee Inventory App
Please follow the below steps to execute the application

Run as JAR
1. Run the command ./gradlew build to build the jar
2. Go to build/libs and run the Jar using java -jar <Jar_Name>

Run as Container:
1. Run the command ./gradlew build to build the jar
2. Build the image with command  docker build -t <IMAGE_NAME>:<VERSION> .
3. docker run -p 1010:1010  <IMAGE_NAME>:<VERSION> 

It Expose below API 
1. POST API -> /api/v1/employee/upload 
Query Param: action -> Its supports upload as action
Body as formData key: files and values as txt file
2. GET API ->  /api/v1/employee/status
Query Param: taskUID : This can be fetched as response from API 1 

3. GET API ->  /api/v1/employee/getAllEmployees
It will return the list of employess

4.  Delete API ->  /api/v1/employee/deleteEmployee
Query Param: employeeId

5. GET API ->  /api/v1/employee/findEmployee
Query Param: employeeId
