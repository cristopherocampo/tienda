
#Etapa 1: compilacion 
FROM maven:3.8.5-openjdk-17 as build
workdir /app
COPY . .
RUN mvn -f pom.xml clean package -DskipTests
 
#ETAPA 2: CREACION DE LA IMAGEN FINAL
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
expose 80
ENTRYPOINT ["java","-jar","app.jar"] 