# Delivery 1 - Fundamental Programming Concepts

Java 8 project that generates input files for a basic sales system.  
This corresponds to the **first delivery** of the *Fundamental Programming Concepts* module.

---

## Project Overview

This program generates sample data files to simulate a sales system:

1. **Products** – Each product has an ID, a name, and a price. Names are modern and realistic.
2. **Salesmen** – Each salesman has a document type, ID, first name, and last name.
3. **Sales records** – Each salesman has a file with a pseudo-random number of sales (product ID and quantity).

All data is pseudo-random but reproducible thanks to a fixed random seed.

---

## Project Structure

- `src/` → Java source code
    - `GenerateInfoFiles.java` → Main class to generate all input files.
- `input/` → Folder where generated files are saved:
    - `products.txt` → List of products (ID, name, price)
    - `salesmen_info.txt` → Salesmen data (document type, ID, names)
    - `sales_<DocType>_<Number>.txt` → Sales per salesman

---

## How it Works

1. The program first generates **products.txt** with 20 products.
2Finally, it generates individual sales files for each salesman, containing product IDs and quantities sold.

---

## How to Run

### Using IntelliJ IDEA
1. Open project in **IntelliJ IDEA** (`File → Open → Select project folder`).
2. Make sure `src/` is marked as **Sources Root**.
3. Open `GenerateInfoFiles.java`.
4. Right-click → **Run 'GenerateInfoFiles.main()'**.
5. Output files will appear in the `input/` folder.

### Using Command Line
```bash
# Compile the project
javac -d out src/GenerateInfoFiles.java

# Run the program
java -cp out GenerateInfoFiles
