#!/bin/bash

# Define an array of microservices
services=("eureka" "user" "restaurant" "order" "gateway" "webapp" "adminapp" "restaurantadmin" "login")

# Iterate through each service and run it
for service in "${services[@]}"; do
  echo "Starting $service..."
  (cd $service && mvn spring-boot:run &) # Replace `mvn` with `gradle` if using Gradle
done

# Wait for all processes to complete (optional)
wait

echo "All microservices are running!"

