FROM amazoncorretto:17.0.11
LABEL authors="exkernel"

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/app.jar
COPY ./ca.crt /app/ca.crt

EXPOSE 8000

CMD ["java", "-jar", "/app/app.jar"]
