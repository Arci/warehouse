# Warehouse software

This software should hold **articles**, and the articles should contain an **identification number**, a **name** and **available stock**. 
It should be possible to **load articles** into the software **from a file**, see `data/inventory.json`.

The warehouse software should also have **products**, products are made of different articles. 
Products should have a **name**, **price** and a **list of articles** of which they are made from with a quantity. 
The products should also be **loaded from a file**, see `data/products.json`.

The warehouse should have at least the following functionality:
- Get all products and quantity of each that is an available with the current inventory
- Remove(Sell) a product and update the inventory accordingly

# Notes

during the development I've made several decisions that I've collected in [NOTES.md](./NOTES.md)