# Assumptions

## Selling products

For sake of simplicity, I suppose to identify a product by its name and thus avoid to creating an identification number also for them.

With that simplification a product can be sold by specifying its name, and the desired quantity.

## Product prices

The text states that every product has a price but is not specified in the JSON format of the `products.json` example.

For simplicity, I'm assuming all the products cost the same amount (that is hardcoded in the code during adapting). 
This behaviour can be easily altered by specifying the amounts in the JSON or by fetching this information from another repository.

## Query available products

System will be queried via REST APIs, for listing available products I've created the following one:

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