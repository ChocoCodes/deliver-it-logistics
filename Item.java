public class Item {
    private final String[] ITEMS_H = {"pkgID", "name", "weight_kg", "length_cm", "width_cm", "height_cm"};
    private String name;
    private double weight; // in kilograms
    private Dimension dimension;

    public Item(String name, double weight, Dimension dimension) {
        this.name = name;
        this.weight = toKilograms(weight);
        this.dimension = dimension;
    }

    public String getName() { return this.name; }
    public double getWeight() { return this.weight; }
    public Dimension getDimensions() { return this.dimension; }
    public String[] getItemHeader() { return this.ITEMS_H; }
    @Override
    public String toString() {
        Dimension dim = getDimensions();
        return String.format("| %s | %.2f | %dx%dx%d |",
            getName(),
            getWeight(),
            (int) Math.round(dim.getLength()),
            (int) Math.round(dim.getWidth()),
            (int) Math.round(dim.getHeight())
        );
    }

    public double toKilograms(double grams) {
        return grams / 1000.0;
    }

    public String[] toCSVFormat(int pkgID) { 
        Dimension dim = getDimensions();
        return new String[] {
            String.valueOf(pkgID),
            getName(), 
            String.valueOf(getWeight()),  
            String.valueOf(dim.getLength()), 
            String.valueOf(dim.getWidth()), 
            String.valueOf(dim.getHeight())
        }; 
    }

    public static Item toItem(String[][] raw, int idx) {
        return new Item(
            raw[idx][1],
            CSVParser.toDouble(raw[idx][2]),
            Dimension.toDimension(raw[idx], 3)
        );
    }
}