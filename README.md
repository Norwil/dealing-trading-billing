# Dealing-Trading-Billing

Dealing-Trading-Billing is a back-end application built with Java and Spring Boot for handling financial processes such as wire fees, trade fees, and custody fee calculations (including prorated amounts). This project is designed to streamline billing operations and ensure accuracy in fee computations.

## Features
- Wire Fee Calculation: Computes fees associated with wire transfers based on predefined rules.
- Trade Fee Calculation: Handles fees for financial trades, ensuring compliance with billing schedules.
- Custody Fee Calculation: Calculates custody fees, including prorated amounts, based on account activities and schedules.

## Tech Stack
- Java: Core programming language.
- Spring Boot: Framework for building the application.
- Maven: Dependency management.
- PostgreSQL: Database for storing and managing data.

## Project Structure
- Controller Layer: Manages incoming API requests and handles routing. Examples include AccountController and BillingController.
- Service Layer: Business logic for calculations and processing, such as BillingService.
- Repository Layer: Interfaces with the database using JPA, e.g., AccountRepository and ActivityRepository.
- DTO Layer: Defines data transfer objects for response formatting, such as FeeCalculationResponseDTO.
- Model Layer: Contains core entities like Account, Activity, and FeeSchedule.

## Installation & Setup
1. Clone the repository:
   git clone https://github.com/Norwil/dealing-trading-billing.git

2. Navigate to the project directory:
   cd dealing-trading-billing

3. Configure the database:
   Update the `application.properties` file with your PostgreSQL credentials.

4. Build the project using Maven:
   mvn clean install

5. Run the application:
   mvn spring-boot:run

6. Access the API via:
   http://localhost:8080

## How It Works
1. Wire Fee Calculation:
   Computes fees based on transaction amount and predefined fee schedules.
2. Trade Fee Calculation:
   Processes trade activities and calculates applicable charges.
3. Custody Fee Calculation:
   Determines custody fees, including prorated amounts based on activity durations and schedules.

## Future Enhancements
- Add front-end integration for user interaction.
- Expand fee calculation logic to support additional transaction types.
- Implement advanced error handling and logging mechanisms.
