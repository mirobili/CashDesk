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
  BGN|1,2,5,10,20,50,100|LOCAL

  EUR|1,2,5,10,20,50,100,500|FOREIGN

  format:

  CurrencyCode|AcceptedDenemonations|LOCAL/FOREIGN(will be used to decide different withdraw strategies ;),, for now it is hardcoded to BGN or not BGN ) 

### Usage

- The API exposes endpoints to process cash operations.
- Operations like debit :`LOAD`, `DEPOSIT` and credit:`WITHDRAW`, and `RETURN_TO_VAULT`() are supported.
- Errors are properly handled and returned with descriptive messages.

- Use

- Initialisation of cashiers is done using the LOAD cash operation ... it is the debit operation the same as DEPOSIT , but there are semantics , and I decided to introduce new operation to distinguish them.

  {{baseUrl}}/api/v1/cash-operation
  {
    "cashierName": "MARTINA",
    "type": "LOAD",
    "currency": "EUR",
    "denominations": {
        "10": 100,
        "50": 20
    }
} 

 and 
  {{baseUrl}}/api/v1/cash-operation
  {
    "cashierName": "MARTINA",
    "type": "LOAD",
    "currency": "BGN",
    "denominations": {
        "10": 50,
        "50": 10
    }
} 



- Deposit Request
  {{baseUrl}}/api/v1/cash-operation
 {
    "cashierName": "MARTINA",
    "type": "DEPOSIT",
    "currency": "EUR",
    "denominations": {
        "10":1,
        "50":1,
        "100":1
      }

}
Deposit Response :
{
    "transactionId": "b2011bf7-d598-4876-8ae7-b2a11413593d",
    "cashierName": "MARTINA",
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
-  Withdraw Request
 {{baseUrl}}/api/v1/cash-operation
 {
    "cashierName": "Martina",
    "type": "WITHDRAW",
    "currency": "BGN",
    "amount":100

}
- Withdraw Response:
- {
    "transactionId": "44bdc4eb-06eb-48c6-ae09-44a8024a2f22",
    "cashierName": "Martina",
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


- {{baseUrl}}/api/v1/cash-balance/report - list current denominations bay cashier
- {
    "2025-10-21": {
        "MARTINA": {
            "EUR": {
                "amount": 2000,
                "denominations": {
                    "50": 20,
                    "10": 100
                }
            },
            "BGN": {
                "amount": 1000,
                "denominations": {
                    "50": 10,
                    "10": 50
                }
            }
        },
        "Martina": {
            "BGN": {
                "amount": -100,
                "denominations": {
                    "50": -1,
                    "10": -5
                }
            }
        },
        "PETER": {
            "EUR": {
                "amount": 2000,
                "denominations": {
                    "50": 20,
                    "10": 100
                }
            },
            "BGN": {
                "amount": 1000,
                "denominations": {
                    "50": 10,
                    "10": 50
                }
            }
        },
        "LINDA": {
            "EUR": {
                "amount": 2000,
                "denominations": {
                    "50": 20,
                    "10": 100
                }
            },
            "BGN": {
                "amount": 1000,
                "denominations": {
                    "50": 10,
                    "10": 50
                }
            }
        }
    }
}
- {{baseUrl}}/api/v1/cash-balance/list - list all the transactions

# TODO :
  - some cleanup
  - reports improvement

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
 
