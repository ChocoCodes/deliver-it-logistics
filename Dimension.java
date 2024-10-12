// All measurements in centimeters(cm) for accurate calculations later
public class Dimension {
    private double height;
    private double width;
    private double length;

    public Dimension(double height, double width, double length) {
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public double getLength() { return this.length; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }

    public void setLength(double length) { this.length = length; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }

    // Dimensions of an item/
    public static Dimension toDimension(String[] raw, int start) {
        double l = CSVParser.toDouble(raw[start]),
        w = CSVParser.toDouble(raw[start + 1]),
        h = CSVParser.toDouble(raw[start + 2]);
        return new Dimension(h, w, l);
    }

    @Override
    public String toString() {
        return String.format("%.2fx%.2fx%.2f", getLength(), getWidth(), getHeight());
    }
}