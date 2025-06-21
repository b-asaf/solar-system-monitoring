@echo off
REM Run the application in dev profile (Windows)
cd ..
REM Run the application with the dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev