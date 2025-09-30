import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * CLASS GenerateBasicFiles
 * RESPONSIBLE FOR CREATING RANDOM INPUT FILES INTO ./input.
 */
public class GenerateBasicFiles {

    private static final String OUTPUT_DIR = "input";
    private static final Random RANDOM = new Random(12345);

    public static void main(String[] args) {
        try {
            ensureOutputDir();

            createProductsFile(15);
            createSellersInfoFile(8);

            List<String> sellers = readSellerIdentifiers();
            for (String identifier : sellers) {
                String[] parts = identifier.split(";");
                String docType = parts[0];
                String docNumber = parts[1];
                int count = 3 + RANDOM.nextInt(8);
                createSalesFile(count, "sales_" + docType + "_" + docNumber, docType, docNumber);
            }

            System.out.println("GenerateBasicFiles: Files successfully created at ./" + OUTPUT_DIR);

        } catch (Exception e) {
            System.err.println("Error generating files: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void ensureOutputDir() throws IOException {
        Path p = Paths.get(OUTPUT_DIR);
        if (!Files.exists(p)) {
            Files.createDirectories(p);
        }
    }

    public static void createSalesFile(int count, String fileBaseName, String docType, String docNumber) throws IOException {
        List<Integer> productIds = sampleProductIds();
        File f = new File(OUTPUT_DIR, fileBaseName + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(docType + ";" + docNumber);
            bw.newLine();
            for (int i = 0; i < count; i++) {
                int pid = productIds.get(RANDOM.nextInt(productIds.size()));
                int qty = 1 + RANDOM.nextInt(5);
                bw.write(pid + ";" + qty);
                bw.newLine();
            }
        }
    }

    public static void createProductsFile(int count) throws IOException {
        File f = new File(OUTPUT_DIR, "products.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (int i = 1; i <= count; i++) {
                String name = "Item_" + i;
                double price = 5.0 + RANDOM.nextInt(50) + RANDOM.nextDouble();
                bw.write(i + ";" + name + ";" + String.format(Locale.US, "%.2f", price));
                bw.newLine();
            }
        }
    }

    public static void createSellersInfoFile(int count) throws IOException {
        File f = new File(OUTPUT_DIR, "sellers_info.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (int i = 0; i < count; i++) {
                String type = pickRandomDocType();
                long id = 100000L + Math.abs(RANDOM.nextLong() % 900000L);
                String first = randomFirstName();
                String last = randomLastName();
                bw.write(type + ";" + id + ";" + first + ";" + last);
                bw.newLine();
            }
        }
    }

    private static List<Integer> sampleProductIds() throws IOException {
        List<Integer> ids = new ArrayList<>();
        Path p = Paths.get(OUTPUT_DIR, "products.txt");
        if (Files.exists(p)) {
            for (String line : Files.readAllLines(p)) {
                if (!line.trim().isEmpty()) {
                    ids.add(Integer.parseInt(line.split(";")[0]));
                }
            }
        }
        return ids;
    }

    private static List<String> readSellerIdentifiers() throws IOException {
        List<String> list = new ArrayList<>();
        Path p = Paths.get(OUTPUT_DIR, "sellers_info.txt");
        if (Files.exists(p)) {
            for (String line : Files.readAllLines(p)) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        list.add(parts[0] + ";" + parts[1]);
                    }
                }
            }
        }
        return list;
    }

    private static String pickRandomDocType() {
        String[] types = {"CC", "CE", "TI", "NIT"};
        return types[RANDOM.nextInt(types.length)];
    }

    private static String randomFirstName() {
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
        return names[RANDOM.nextInt(names.length)];
    }

    private static String randomLastName() {
        String[] names = {"Smith", "Johnson", "Brown", "Williams", "Taylor"};
        return names[RANDOM.nextInt(names.length)];
    }
}
