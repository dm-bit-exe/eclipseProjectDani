/**
 * Product entity used by Main processing.
 */
public class Item {
    private int id;
    private String name;
    private double price;
    private int unitsSold = 0;

    public Item(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getUnitsSold() { return unitsSold; }
    public void addUnitsSold(int qty) { this.unitsSold += qty; }
}
