public class Customer {
    private final String[] CUSTOMER_H = {"id", "name", "contactInfo", "address"};
    private int id;
    private String name;
    private String contactInfo;
    private String address;

    // Customer Constructor
    public Customer(int id, String name, String contactInfo, String address) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    // Getters
    public int getCustomerID() { return this.id; }
    public String getName() { return this.name; }
    public String getContactInfo() { return this.contactInfo; }
    public String getAddress() { return this.address; }
    public String[] getCustomerHeader() { return this.CUSTOMER_H; }
    
    public void setName(String name) { this.name = name; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("ID: %d\nName: %s\nContact Number: %s\nAddress: %s\n", 
        getCustomerID(), 
        getName(), 
        getContactInfo(), 
        getAddress()
        );
    }

    public String[] toCSVFormat() {
        return new String[] {
            String.valueOf(getCustomerID()),
            getName(),
            getContactInfo(),
            getAddress()
        };
    }

    public static Customer[] toCustomer(String[][] raw) {
        Customer[] customers = new Customer[raw.length];
        for (int i = 0; i < raw.length; i++) {
            customers[i] = new Customer(
                CSVParser.toInt(raw[i][0]),
                raw[i][1],
                raw[i][2],
                raw[i][3]
            );
        }
        return customers;
    }

    public static Customer toCustomer(String[][] raw, int idx) { 
        return new Customer(
            CSVParser.toInt(raw[idx][0]), 
            raw[idx][1], 
            raw[idx][2], 
            raw[idx][3]
        ); 
    }
}
