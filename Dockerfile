FROM openjdk:8-alpine
ENV HOME /root
#ENV BRANCH_NAME $branch_name
#ARG build_id
#ENV BUILD_ID $build_id
COPY ./build/libs/employeeInventoryApp-*.jar employeeInventoryApp.jar
WORKDIR /root/
CMD ["java","-jar","/employeeInventoryApp.jar"]