import java.io.*;
import java.util.ArrayList;

class CSVParser {
    public int getColumnCounts(String file) {
        int counts = -1;
        try(BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String buffer;
            if ((buffer = fin.readLine()) != null) {
                String[] header = buffer.split(",");
                counts = header.length;
            }
        } catch(IOException e) { return -1; }
        return counts;
    }

    public String[][] loadCSVData(String file) {
        ArrayList<String[]> data = new ArrayList<>();
        try(BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String buffer;
            if((buffer = fin.readLine()) != null) {}
            while((buffer = fin.readLine()) != null) {
                String[] tmp = buffer.split(",");
                // Skip invalid columns: columns that does not have the same counts as the header
                if(tmp.length != getColumnCounts(file)) {
                    continue;
                }
                data.add(tmp);
            }
        } catch(IOException e) { return new String[0][0]; }
        return data.toArray(new String[0][0]);
    }

    public Customer[] toCustomer(String[][] raw) {
        Customer[] customers = new Customer[raw.length];
        for(int i = 0; i < raw.length; i++) {
            customers[i] = new Customer(
                Integer.parseInt(raw[i][0]),
                raw[i][1],
                raw[i][2],
                raw[i][3]
            );
        }
        return customers;
    }

    public static void main(String[] args) {
        CSVParser parser = new CSVParser();
        String[][] rawCustomerData = parser.loadCSVData("CSVFiles/customers.csv");
        Customer[] data = parser.toCustomer(rawCustomerData);
        for(Customer customer : data) {
            System.out.println(customer.toString());
        }
        System.err.println("CSV Header Count: " + parser.getColumnCounts("CSVFiles/customers.csv"));
    }
}

class Customer {
    private int id;
    private String name;
    private String contactNo;
    private String address;

    public Customer(int id, String name, String contactNo, String address) {
        this.id = id;
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("ID: %d\nName: %s\nContact Number: %s\nAddress: %s\n", 
        this.id, 
        this.name, 
        this.contactNo, 
        this.address
        );
    }
}