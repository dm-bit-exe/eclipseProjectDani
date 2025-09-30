public class Seller {
    private final String key;
    private final String name;
    private double totalSales;

    public Seller(String key, String name) {
        this.key = key;
        this.name = name;
        this.totalSales = 0.0;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void addSalesAmount(double amount) {
        this.totalSales += amount;
    }
}
