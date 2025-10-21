# Cash Desk Application

## Overview

This Java Spring Boot application simulates a cash desk system, handling operations such as deposits, withdrawals, and returns to vault. It supports multiple currencies, manages denominations, and validates transaction requests rigorously.

## Features

- Support for various currencies and denominations configurable from external files.
- Robust transaction processing with validation and error handling.
- Dynamic generation of currency denominations based on cashier and currency.
- Logging of transactions and errors for audit and debugging purposes.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven or Gradle build tool
- An IDE such as IntelliJ IDEA, Eclipse, or Visual Studio Code

### Installation

1. Clone the repository:

git clone <repository_url>
cd cash-desk-application

text

2. Build the project with Maven:

mvn clean install

text

3. Run the application:

mvn spring-boot:run

text

### Configuration

- Currency configurations are stored in `src/main/data/currency_denominations.txt`.
- Transactions are logged and stored in `src/main/data/transactions.txt`.
- Denominations and cashier info loaded dynamically at startup.

### Usage

- The API exposes endpoints to process cash operations.
- Operations like `LOAD`, `DEPOSIT`, `WITHDRAW`, and `RETURN_TO_VAULT` are supported.
- Errors are properly handled and returned with descriptive messages.

### Logging

- Transaction and error logs are written to the console and relevant log files.

## Troubleshooting

- Ensure all config files (`currency.txt`, `transactions.txt`) have no malformed or empty lines.
- Check logs for parsing errors or validation failures.
- Validate currency and denomination inputs to match configured values.

## Contributing

Contributions welcome! Please fork the repository and submit pull requests with clear descriptions.

## License

MIT License

## Contact

For questions or support, please contact the maintainer at miroslav.bilyarski@gmail.com.
 
