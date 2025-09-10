import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GenerateInfoFiles
 *
 * Generates sample data files for a simple sales system:
 * - products.txt
 * - salesmen_info.txt
 * - individual sales files per salesman
 *
 * Output folder: "input"
 */

// Developed by Daniel Mata

public class GenerateInfoFiles {

    private static final String OUTPUT_DIR = "input"; // Folder to store generated files
    private static final Random RANDOM = new Random(12345); // Fixed seed for reproducible randomness

    public static void main(String[] args) {
        try {
            createOutputFolder();               // Ensure the output folder exists
            generateProducts(20);              // Generate 20 products
            generateSalesmen(10);              // Generate 10 random salesmen
            generateSalesFilesForAllSalesmen();// Generate sales files for each salesman

            System.out.println("Files successfully generated in './" + OUTPUT_DIR + "'");
        } catch (Exception e) {
            System.err.println("Error generating files: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the output folder if it does not exist
     */
    private static void createOutputFolder() throws IOException {
        Path path = Paths.get(OUTPUT_DIR);
        if (!Files.exists(path)) Files.createDirectories(path);
    }

    /**
     * Generates a products.txt file with the given number of products
     * Each product has an ID, name, and random price
     */
    private static void generateProducts(int count) throws IOException {
        String[] productNames = {
                "EcoBag", "SmartWatch", "Bluetooth Speaker", "Desk Lamp",
                "Gaming Mouse", "Water Bottle", "Yoga Mat", "Backpack",
                "Wireless Earbuds", "Notebook", "Sunglasses", "Portable Charger",
                "LED Light Strip", "Coffee Mug", "Travel Pillow", "Action Camera",
                "Desk Organizer", "Headphones", "Fitness Tracker", "Mini Projector"
        };

        File file = new File(OUTPUT_DIR, "products.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 1; i <= count; i++) {
                String name = productNames[RANDOM.nextInt(productNames.length)]; // Random product name
                double price = Math.round((10 + RANDOM.nextInt(200) + RANDOM.nextDouble()) * 100.0) / 100.0; // Random price
                writer.write(i + ";" + name + ";" + price);
                writer.newLine();
            }
        }
    }

    /**
     * Generates a salesmen_info.txt file with the given number of salesmen
     * Each salesman has document type, ID, first name, and last name
     */
    private static void generateSalesmen(int count) throws IOException {
        String[] firstNames = {"Felipe","María","Juan","Luisa","Carlos","Tatiana","Andrés","Catalina","Diego","Valentina","Josue", "Isabella", "Santiago"};
        String[] lastNames = {"González","Rodríguez","Martínez","López","Pérez","Gómez","Ramírez","Sosa","Castro","Vargas", "Roa", "Alvarado", "Gutierrez"};

        File file = new File(OUTPUT_DIR, "salesmen_info.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < count; i++) {
                String docType = getRandomDocType();  // Random document type
                long id = 10000000L + Math.abs(RANDOM.nextLong() % 90000000L); // Random ID
                String first = firstNames[RANDOM.nextInt(firstNames.length)];
                String last = lastNames[RANDOM.nextInt(lastNames.length)];
                writer.write(docType + ";" + id + ";" + first + ";" + last);
                writer.newLine();
            }
        }
    }

    /**
     * Generates individual sales files for all salesmen
     */
    private static void generateSalesFilesForAllSalesmen() throws IOException {
        List<String> salesmen = readSalesmen(); // Read all salesmen identifiers
        for (String s : salesmen) {
            String[] parts = s.split(";");
            String docType = parts[0];
            String id = parts[1];
            int salesCount = 5 + RANDOM.nextInt(46); // Random number of sales per salesman
            createSalesFile(docType, id, salesCount);
        }
    }

    /**
     * Creates a single sales file for a salesman
     * Each line has product ID and quantity sold
     */
    private static void createSalesFile(String docType, String id, int salesCount) throws IOException {
        File file = new File(OUTPUT_DIR, "sales_" + docType + "_" + id + ".txt");
        List<Integer> productIds = getProductIds(); // Get list of product IDs

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(docType + ";" + id); // First line: salesman info
            writer.newLine();
            for (int i = 0; i < salesCount; i++) {
                int productId = productIds.get(RANDOM.nextInt(productIds.size())); // Random product
                int quantity = 1 + RANDOM.nextInt(10); // Random quantity
                writer.write(productId + ";" + quantity);
                writer.newLine();
            }
        }
    }

    /**
     * Reads salesmen identifiers from salesmen_info.txt
     * Returns list of "docType;id" strings
     */
    private static List<String> readSalesmen() throws IOException {
        List<String> list = new ArrayList<>();
        Path path = Paths.get(OUTPUT_DIR, "salesmen_info.txt");
        if (!Files.exists(path)) return list;
        for (String line : Files.readAllLines(path)) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(";");
                if (parts.length >= 2) list.add(parts[0] + ";" + parts[1]);
            }
        }
        return list;
    }

    /**
     * Returns list of product IDs from products.txt
     */
    private static List<Integer> getProductIds() {
        List<Integer> ids = new ArrayList<>();
        Path path = Paths.get(OUTPUT_DIR, "products.txt");
        try {
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (!line.trim().isEmpty()) ids.add(Integer.parseInt(line.split(";")[0]));
                }
            } else {
                for (int i = 1; i <= 20; i++) ids.add(i); // Default 20 products if file missing
            }
        } catch (IOException e) {
            for (int i = 1; i <= 20; i++) ids.add(i);
        }
        return ids;
    }

    /**
     * Returns a random document type
     */
    private static String getRandomDocType() {
        String[] types = {"CC", "CE", "TI", "NIT"};
        return types[RANDOM.nextInt(types.length)];
    }
}
