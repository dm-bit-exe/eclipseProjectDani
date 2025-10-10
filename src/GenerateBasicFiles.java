import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Locale;

/**
 * GENERATE DATA FILES
 * Generates products.txt, sellers_info.txt and multiple seller sales files
 * in the 'input' folder. No user interaction required.
 * Compatible with Java 8.
 */
public class GenerateBasicFiles {

    private static final String OUTPUT_DIR = "input";
    private static final Random RAND = new Random(123456);

    public static void main(String[] args) {
        try {
            File d = new File(OUTPUT_DIR);
            if (!d.exists()) d.mkdirs();

            createProductsFile(20);
            createSellersInfoFile(10);
            createSalesFilesForSellers();

            System.out.println("GenerateDataFiles: files created in ./" + OUTPUT_DIR);
        } catch (Exception e) {
            System.err.println("Error generating files: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void createProductsFile(int count) throws IOException {
        File f = new File(OUTPUT_DIR, "products.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (int i = 1; i <= count; i++) {
                String name = "Product_" + i;
                double price = 1.0 + RAND.nextInt(200) + RAND.nextDouble();
                price = Math.round(price * 100.0) / 100.0;
                bw.write(i + ";" + name + ";" + String.format(Locale.US, "%.2f", price));
                bw.newLine();
            }
        }
    }

    private static void createSellersInfoFile(int count) throws IOException {
        File f = new File(OUTPUT_DIR, "sellers_info.txt");
        String[] firstNames = {"Daniel","Laura","Carlos","Ana","Sergio","Mariana","Felipe","Sofia","Jorge","Camila"};
        String[] lastNames = {"Matabanchoy","Gomez","Lopez","Perez","Rodriguez","Ramirez","Santos","Diaz","Torres","Morales"};
        String[] types = {"CC","CE","TI"};
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (int i = 0; i < count; i++) {
                String tipo = types[RAND.nextInt(types.length)];
                long number = 10000000L + Math.abs(RAND.nextLong() % 90000000L);
                String first = firstNames[RAND.nextInt(firstNames.length)];
                String last = lastNames[RAND.nextInt(lastNames.length)];
                bw.write(tipo + ";" + number + ";" + first + ";" + last);
                bw.newLine();
            }
        }
    }

    private static void createSalesFilesForSellers() throws IOException {
        // read sellers_info.txt to create one sales file per seller
        java.nio.file.Path p = java.nio.file.Paths.get(OUTPUT_DIR, "sellers_info.txt");
        java.util.List<String> lines = java.nio.file.Files.readAllLines(p);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 2) continue;
            String tipo = parts[0];
            String numero = parts[1];
            int salesCount = 5 + RAND.nextInt(11); // 5-15 sales
            File f = new File(OUTPUT_DIR, "sales_" + tipo + "_" + numero + ".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(tipo + ";" + numero);
                bw.newLine();
                for (int i = 0; i < salesCount; i++) {
                    int pid = 1 + RAND.nextInt(20);
                    int qty = 1 + RAND.nextInt(10);
                    bw.write(pid + ";" + qty);
                    bw.newLine();
                }
            }
        }
    }
}
