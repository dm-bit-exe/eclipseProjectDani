/**
 * Seller entity used by Main processing.
 */
public class Seller {
    private String key; // tipo;numero
    private String fullName;
    private double totalSales = 0.0;

    public Seller(String key, String fullName) {
        this.key = key;
        this.fullName = fullName;
    }

    public String getKey() { return key; }
    public String getFullName() { return fullName; }
    public double getTotalSales() { return totalSales; }
    public void addTotalSales(double amt) { this.totalSales += amt; }
}
