# Assumptions

## Selling products

For sake of simplicity, I suppose to identify a product by its name and thus avoid to creating an identification number also for them.

With that simplification a product can be sold by specifying its name, and the desired quantity.

## Product prices

The text states that every product has a price but is not specified in the JSON format of the `products.json` example.

For simplicity, I'm assuming all the products cost the same amount (that is hardcoded in the code during adapting). 
This behaviour can be easily altered by specifying the amounts in the JSON or by fetching this information from another repository.

## Products repository

To avoid maintaining the state of the application just in memory after the initial load of the articles and the products, I've decided to update the `inventory.json` file whenever a product is sold.

## Interact with the system

System will be queried via REST APIs


# Check given specifications

## Acceptance Test

I've created an acceptance test (`WarehouseAcceptanceTest`) that uses the given files and performs some simple operations to guarantee the correctness w.r.t. the specification.

## Running the application

The application reads articles and products from `src/main/resources/data`, it starts on ost 8081 and can be queried through REST APIs. 

### List available products

```
GET http://localhost:8081/warehouse/products/available
```

example of 200 response:

```json
[
    {
        "name": "Dining Chair",
        "price": {
            "currency": "EUR",
            "amount": 42
        },
        "availableQuantity": 2
    },
    {
        "name": "Dinning Table",
        "price": {
            "currency": "EUR",
            "amount": 42
        },
        "availableQuantity": 1
    }
]
``` 

For simplicity all errors gets translated into 500s.

### Sell a product

Products can be sold by hitting the following REST API:

```
POST http://localhost:8081/warehouse/products/sell
```

with the following body:

```json

{
  "name": "Dinning Table",
  "quantity": 1
}
```

response will be 204 (`NO_CONTENT`) when sell succeeds, for simplicity all errors are translated into 500s.

# Further improvements

## Missing tests

- I haven't created the tests for `ListAvailableProductsEndpoint` and `SellProductEndpoint` since they require a bit of spring boilerplate, what should be verified is the contract that the API is declaring