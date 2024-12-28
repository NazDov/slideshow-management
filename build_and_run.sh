#!/bin/bash

# Define environment variables (you can also export them earlier in the terminal if desired)
export DB_URL="${DB_URL:-localhost}"
export DB_NAME="${DB_NAME:-sm-db}"
export DB_USER="${DB_USER:-sm-db-user}"
export DB_PASSWORD="${DB_PASSWORD:-test}"
export DB_SHOW_SQL="${DB_SHOW_SQL:-false}"
export KAFKA_CONNECT="${KAFKA_CONNECT:-localhost:9092}"
export SLIDESHOW_KAFKA_TOPIC="${SLIDESHOW_KAFKA_TOPIC:-slideshow_event_logs}"

# Clean, package the project with Maven
echo "Cleaning and packaging the project..."
mvn clean package

# Check if the build was successful
if [ $? -ne 0 ]; then
  echo "Maven build failed. Exiting."
  exit 1
fi

# Run the application with the necessary environment variables and java command
echo "Running the application..."
java -Dspring.r2dbc.url="r2dbc:pool:mysql://${DB_URL}:3306/${DB_NAME}?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC" \
-Dspring.r2dbc.username="${DB_USER}" \
-Dspring.r2dbc.password="${DB_PASSWORD}" \
-Dspring.r2dbc.show-sql="${DB_SHOW_SQL}" \
-Dspring.flyway.enabled=true \
-Dspring.flyway.user="${DB_USER}" \
-Dspring.flyway.password="${DB_PASSWORD}" \
-Dspring.flyway.locations="classpath:migrations" \
-Dspring.flyway.url="jdbc:mysql://${DB_URL}:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true" \
-Dspring.main.allow-bean-definition-overriding=true \
-Dspring.kafka.bootstrap-servers="${KAFKA_CONNECT}" \
-Dspring.kafka.producer.key-serializer="org.apache.kafka.common.serialization.StringSerializer" \
-Dspring.kafka.producer.value-serializer="org.apache.kafka.common.serialization.StringSerializer" \
-Dslideshow.kafka.topic.name="${SLIDESHOW_KAFKA_TOPIC}" \
-jar app/target/slideshow-management.jar
