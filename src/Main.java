import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * MAIN
 * Reads input files and generates two CSV reports:
 * - sellers_report.csv (seller full name; total money)
 * - products_report.csv (product name; price; total units sold)
 *
 * Compatible with Java 8.
 */
public class Main {

    private static final String INPUT_DIR = "input";
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        try {
            File out = new File(OUTPUT_DIR);
            if (!out.exists()) out.mkdirs();

            Map<Integer, Item> products = loadProducts();
            Map<String, Seller> sellers = loadSellers();

            processSalesFiles(products, sellers);

            generateSellersReport(sellers);
            generateProductsReport(products);

            System.out.println("Main: reports created in ./" + OUTPUT_DIR);
        } catch (Exception e) {
            System.err.println("Error in Main: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Map<Integer, Item> loadProducts() throws IOException {
        Map<Integer, Item> map = new HashMap<Integer, Item>();
        java.nio.file.Path p = java.nio.file.Paths.get(INPUT_DIR, "products.txt");
        if (!java.nio.file.Files.exists(p)) {
            throw new IOException("Missing products.txt in input folder");
        }
        List<String> lines = java.nio.file.Files.readAllLines(p);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 3) continue;
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double price = Double.parseDouble(parts[2]);
            map.put(id, new Item(id, name, price));
        }
        return map;
    }

    private static Map<String, Seller> loadSellers() throws IOException {
        Map<String, Seller> map = new HashMap<String, Seller>();
        java.nio.file.Path p = java.nio.file.Paths.get(INPUT_DIR, "sellers_info.txt");
        if (!java.nio.file.Files.exists(p)) {
            throw new IOException("Missing sellers_info.txt in input folder");
        }
        List<String> lines = java.nio.file.Files.readAllLines(p);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 4) continue;
            String key = parts[0] + ";" + parts[1];
            String fullname = parts[2] + " " + parts[3];
            map.put(key, new Seller(key, fullname));
        }
        return map;
    }

    private static void processSalesFiles(Map<Integer, Item> products, Map<String, Seller> sellers) throws IOException {
        File dir = new File(INPUT_DIR);
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            String name = f.getName();
            if (!name.startsWith("sales_") || !name.endsWith(".txt")) continue;
            BufferedReader br = new BufferedReader(new FileReader(f));
            try {
                String header = br.readLine();
                if (header == null) continue;
                Seller s = sellers.get(header);
                if (s == null) continue;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(";");
                    if (parts.length < 2) continue;
                    int pid = Integer.parseInt(parts[0]);
                    int qty = Integer.parseInt(parts[1]);
                    Item p = products.get(pid);
                    if (p != null) {
                        double amount = p.getPrice() * qty;
                        s.addTotalSales(amount);
                        p.addUnitsSold(qty);
                    }
                }
            } finally {
                br.close();
            }
        }
    }

    private static void generateSellersReport(Map<String, Seller> sellers) throws IOException {
        List<Seller> list = new ArrayList<Seller>(sellers.values());
        Collections.sort(list, new Comparator<Seller>() {
            public int compare(Seller a, Seller b) {
                return Double.valueOf(b.getTotalSales()).compareTo(a.getTotalSales());
            }
        });
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR, "sellers_report.csv")));
        try {
            for (Seller s : list) {
                bw.write(s.getFullName() + ";" + String.format(Locale.US, "%.2f", s.getTotalSales()));
                bw.newLine();
            }
        } finally {
            bw.close();
        }
    }

    private static void generateProductsReport(Map<Integer, Item> products) throws IOException {
        List<Item> list = new ArrayList<Item>(products.values());
        Collections.sort(list, new Comparator<Item>() {
            public int compare(Item a, Item b) {
                return Integer.valueOf(b.getUnitsSold()).compareTo(a.getUnitsSold());
            }
        });
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR, "products_report.csv")));
        try {
            for (Item p : list) {
                bw.write(p.getName() + ";" + String.format(Locale.US, "%.2f", p.getPrice()) + ";" + p.getUnitsSold());
                bw.newLine();
            }
        } finally {
            bw.close();
        }
    }
}
