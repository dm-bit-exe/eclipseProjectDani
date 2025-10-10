# Project - Final Delivery
### Daniel Fernando Matabanchoy Moreno
## Description
This project is the final version of a sales processing system.  
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
3. Run `Main` to create random output files:
   ```bash
   java -cp bin Main
   ```
