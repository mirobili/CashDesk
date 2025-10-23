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

- Currency and denominations configuration is stored in `src/main/data/currency_denominations.txt`.
- Transactions are logged and stored in `src/main/data/transactions.txt`.
- Transactions(and cashier info) are persisted in src/main/data/transactions.txt. They are not cleaned automatically.. If you need to start fresh you can delete the content of the src/main/data/transactions.txt.

- ### CASHIER BALANCE AND DENOMINATIONS are NOT STORED SEPARATELY. THEY ARE DYNAMICALLY CALCULATED ON DEMAND USING THE TRANSACTIONS LIST... This way we avoid data missmatch and dissynchronization, etc. WE DO NOT STORE AND UPDATE CASHIER BALANCE AND DENOMINATIONS as FIELDS, but CALCULATE THEM ON DEMAND USING THE APPEND ONLY LEDGER
  
- DEPOSIT accepts Denominations and returns Amount -  Deposit.amount is a calculated field from the denominations presented..
- WITHDRAW accepts Amount and returns Denominations  



- The accepted Currencies and Denominations are configured in currency_denominations.txt
- you can add more currencies to the currency_denominations.txt file (ISO 4217)
- 
```
  BGN|1,2,5,10,20,50,100|LOCAL
  EUR|1,2,5,10,20,50,100,500|FOREIGN
```
  format:
  
``` CurrencyCode | AcceptedDenemonations | LOCAL/FOREIGN ``` 
LOCAL (will be used to decide different withdraw strategies ;)  


### Transactions Persistance Storage

Transactions are stored in a ```src/main/data/transactions.txt``` flat file (configurable in application.properties)

```csv
TransactionId|timestamp|CASHIER|AMOUNT|CURRENCY|OPERATION|DENOMINATIONS
```

```csv
3d72b6c4-ee17-4ca0-9293-0179db36abea|2025-10-21T10:51:03.1791008|MARTINA|1000|BGN|LOAD|10:50,50:10
0e259533-7025-44e9-b7c2-01c819b0189c|2025-10-21T10:51:11.8967321|PETER|1000|BGN|LOAD|10:50,50:10
bdea85df-359a-4079-bc29-235a7d9dc7f0|2025-10-21T10:51:32.0974839|LINDA|1000|BGN|LOAD|10:50,50:10
c9147c4f-537f-42af-b638-0d5f162e5737|2025-10-21T10:54:23.9462059|LINDA|2000|EUR|LOAD|10:100,50:20
e78e2aba-6f1b-4797-bc8e-e6b273f12a3c|2025-10-21T10:54:41.3034785|MARTINA|2000|EUR|LOAD|10:100,50:20
50caf06e-d110-4525-afe9-2da9c16e9741|2025-10-21T10:54:46.9096324|PETER|2000|EUR|LOAD|10:100,50:20
ff347285-a8e6-4c04-8a01-1878c48de9bd|2025-10-22T23:21:17.7129626|MARTINA|100|BGN|WITHDRAW|10:5,50:1
2c8d1700-4ebd-4fad-8d13-ca7c186e2ab0|2025-10-22T23:21:24.4936187|MARTINA|100|BGN|WITHDRAW|10:5,50:1
60a6b799-04a9-4cf8-a92b-bdda0a498710|2025-10-22T23:21:26.8404634|MARTINA|100|BGN|WITHDRAW|10:5,50:1
48288cdc-720f-4394-88eb-3a997ea1f499|2025-10-22T23:21:38.2246154|MARTINA|120|BGN|WITHDRAW|10:7,50:1
4aa7e5f9-09f3-4b75-b348-81beeffb5e43|2025-10-23T09:20:55.3741439|MARTINA|200|BGN|DEPOSIT|10:10,50:2
```

### Usage

- The API exposes endpoints to process cash operations.
- Operations like debit :`LOAD`, `DEPOSIT` and credit:`WITHDRAW`, and `RETURN_TO_VAULT`() are supported.
- Errors are properly handled and returned with descriptive messages.

- Use

- Initialisation of cashiers is done using the LOAD cash operation ... it is the debit operation the same as DEPOSIT , but there are semantics , and I decided to introduce new operation to distinguish them.

  ```{{baseUrl}}/api/v1/cash-operation```
```json

{
    "cashier": "MARTINA",
    "type": "LOAD",
    "currency": "EUR",
    "denominations": {
        "10": 100,
        "50": 20
    }
} 
```
 and 
 ``` {{baseUrl}}/api/v1/cash-operation```
```json
  {
    "cashier": "MARTINA",
    "type": "LOAD",
    "currency": "BGN",
    "denominations": {
        "10": 50,
        "50": 10
    }
} 

```

- Deposit Request
 ``` {{baseUrl}}/api/v1/cash-operation```
```json
{
    "cashier": "MARTINA",
    "type": "DEPOSIT",
    "currency": "EUR",
    "denominations": {
        "10":1,
        "50":1,
        "100":1
      }

}

```
Deposit Response :
```json
{
    "transactionId": "b2011bf7-d598-4876-8ae7-b2a11413593d",
    "cashier": "MARTINA",
    "amount": 160,
    "currency": "EUR",
    "type": "DEPOSIT",
    "timestamp": "2025-10-21T10:04:33.8541665",
    "denominations": {
        "10": 1,
        "50": 1,
        "100": 1
    },
    "success": false,
    "message": null,
    "data": null
}

```

-  Withdraw Request
``` {{baseUrl}}/api/v1/cash-operation```

```json
{
    "cashier": "Martina",
    "type": "WITHDRAW",
    "currency": "BGN",
    "amount":100

}
```
- Withdraw Response:
```json 
{
    "transactionId": "44bdc4eb-06eb-48c6-ae09-44a8024a2f22",
    "cashier": "Martina",
    "amount": 100,
    "currency": "BGN",
    "type": "WITHDRAW",
    "timestamp": "2025-10-21T11:07:31.4303789",
    "denominations": {
        "50": 1,
        "10": 5
    },
    "success": true,
    "message": null
}
```


- ```{{baseUrl}}/api/v1/cash-balance``` - list current denominations bay cashier

```json
 [
    {
        "timestamp": "2025-10-21T10:51:03.1791008",
        "cashier": "MARTINA",
        "currency": "BGN",
        "amount": 1000,
        "denominations": {
            "50": 10,
            "10": 50
        }
    },
    {
        "timestamp": "2025-10-21T10:54:41.3034785",
        "cashier": "MARTINA",
        "currency": "EUR",
        "amount": 2000,
        "denominations": {
            "50": 20,
            "10": 100
        }
    },
    {
        "timestamp": "2025-10-21T12:07:13.8424772",
        "cashier": "MARTINA",
        "currency": "EUR",
        "amount": 1800,
        "denominations": {
            "50": 16,
            "10": 100
        }
    }
]
```
# TODO :
  - some cleanup (Done)
  - reports improvement (Done)

### Logging

- Transaction and error logs are written to the console and relevant log files.

## Troubleshooting

- Ensure all config files (`currency_denominations.txt`, `transactions.txt`) have no malformed or empty lines.
- Check logs for parsing errors or validation failures.
- Validate currency and denomination inputs to match configured values.

## Contributing

Contributions welcome! Please fork the repository and submit pull requests with clear descriptions.

## License

MIT License

## Contact

For questions or support, please contact the maintainer at miroslav.bilyarski@gmail.com.
 
