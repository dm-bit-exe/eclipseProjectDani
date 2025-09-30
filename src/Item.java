public class Item {
    private final int id;
    private final String name;
    private final double price;
    private int unitsSold;

    public Item(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unitsSold = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void addUnitsSold(int units) {
        this.unitsSold += units;
    }
}
