import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CLASS Main
 * RESPONSIBLE FOR PROCESSING INPUT FILES AND GENERATING REPORTS.
 * Generates reports into ./output based on data in ./input.
 */

public class Main {

    private static final String INPUT_DIR = "input";
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        try {
            ensureOutputDir();

            Map<Integer, Item> items = loadItems();
            Map<String, Seller> sellers = loadSellers();

            processSalesFiles(items, sellers);

            generateSellersReport(sellers);
            generateItemsReport(items);

            System.out.println("Main: Reports created in ./" + OUTPUT_DIR);

        } catch (Exception e) {
            System.err.println("Error in execution: " + e.getMessage());
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

    private static Map<Integer, Item> loadItems() throws IOException {
        Map<Integer, Item> map = new HashMap<>();
        Path p = Paths.get(INPUT_DIR, "products.txt");
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    map.put(id, new Item(id, name, price));
                }
            }
        }
        return map;
    }

    private static Map<String, Seller> loadSellers() throws IOException {
        Map<String, Seller> map = new HashMap<>();
        Path p = Paths.get(INPUT_DIR, "sellers_info.txt");
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String key = parts[0] + ";" + parts[1];
                    String name = parts[2] + " " + parts[3];
                    map.put(key, new Seller(key, name));
                }
            }
        }
        return map;
    }

    private static void processSalesFiles(Map<Integer, Item> items,
                                          Map<String, Seller> sellers) throws IOException {
        File dir = new File(INPUT_DIR);
        File[] files = dir.listFiles((d, name) -> name.startsWith("sales_") && name.endsWith(".txt"));
        if (files == null) return;

        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String header = br.readLine(); // first line: DocType;DocNumber
                if (header == null) continue;
                Seller seller = sellers.get(header);
                if (seller == null) continue;

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        int pid = Integer.parseInt(parts[0]);
                        int qty = Integer.parseInt(parts[1]);
                        Item item = items.get(pid);
                        if (item != null) {
                            double total = item.getPrice() * qty;
                            seller.addSalesAmount(total);
                            item.addUnitsSold(qty);
                        }
                    }
                }
            }
        }
    }

    private static void generateSellersReport(Map<String, Seller> sellers) throws IOException {
        File f = new File(OUTPUT_DIR, "sellers_report.csv");

        Map<String, Seller> sorted = sellers.values().stream()
                .sorted(Comparator.comparingDouble((Seller s) -> s.getTotalSales()).reversed())
                .collect(Collectors.toMap(
                        Seller::getKey, s -> s,
                        (a, b) -> a, LinkedHashMap::new
                ));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (Seller s : sorted.values()) {
                bw.write(s.getName() + ";" + String.format(Locale.US, "%.2f", s.getTotalSales()));
                bw.newLine();
            }
        }
    }

    private static void generateItemsReport(Map<Integer, Item> items) throws IOException {
        File f = new File(OUTPUT_DIR, "items_report.csv");

        Map<Integer, Item> sorted = items.values().stream()
                .sorted(Comparator.comparingInt((Item p) -> p.getUnitsSold()).reversed())
                .collect(Collectors.toMap(
                        Item::getId, p -> p,
                        (a, b) -> a, LinkedHashMap::new
                ));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (Item p : sorted.values()) {
                bw.write(p.getName() + ";" + String.format(Locale.US, "%.2f", p.getPrice()) + ";" + p.getUnitsSold());
                bw.newLine();
            }
        }
    }
}
