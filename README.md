# Project - Delivery 2

## Description
This project is a preliminary version of a sales processing system.  
It generates input files with sellers, products, and sales, and then processes them to produce reports.

- `GenerateBasicFiles.java` → Generates input data into `./input`.
- `Main.java` → Reads input files and generates reports in `./output`.
- `Seller.java` → Represents a salesman.
- `Item.java` → Represents a product.

## How to Run
1. Compile all `.java` files:
   ```bash
   mkdir bin
   javac -source 1.8 -target 1.8 -d bin src\*.java
   ```
2. Run `GenerateBasicFiles` to create random input files:
   ```bash
   java -cp bin GenerateBasicFiles
   ```
   This will create:
    - `products.txt`
    - `sellers_info.txt`
    - `sales_<DocType>_<DocNumber>.txt`

3. Run `Main` to process the input files and generate reports:
   ```bash
   java -cp bin Main
   ```
   This will create in `./output`:
    - `sellers_report.csv`
    - `items_report.csv`

## Reports
- **sellers_report.csv** → Sorted list of sellers with total sales.
- **items_report.csv** → Sorted list of products with units sold.

## Notes
- This is a **preliminary version** (Delivery 2).
- Some features may be missing or simplified.
- See `missing-elements.md` for details.
