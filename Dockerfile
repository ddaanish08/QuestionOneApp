FROM openjdk:8-alpine
ENV HOME /root
COPY ./build/libs/employeeInventoryApp-*.jar employeeInventoryApp.jar
WORKDIR /root/
CMD ["java","-jar","/employeeInventoryApp.jar"]
