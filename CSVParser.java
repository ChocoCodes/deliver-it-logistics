import java.io.*;
import java.util.ArrayList;

class CSVParser {
    public int getColumnCounts(String file) {
        int counts = -1;
        try (BufferedReader fin = new BufferedReader(new FileReader(file))) {
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
        try (BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String buffer;
            if ((buffer = fin.readLine()) != null) {}
            while ((buffer = fin.readLine()) != null) {
                String[] tmp = buffer.split(",");
                // Skip invalid columns: columns that does not have the same counts as the header
                if(tmp.length != getColumnCounts(file)) {
                    continue;
                }
                data.add(tmp);
            }
        } catch (IOException e) { return new String[0][0]; }
        return data.toArray(new String[0][0]);
    }

    public Customer[] toCustomer(String[][] raw) {
        Customer[] customers = new Customer[raw.length];
        for (int i = 0; i < raw.length; i++) {
            customers[i] = new Customer(
                Integer.parseInt(raw[i][0]),
                raw[i][1],
                raw[i][2],
                raw[i][3]
            );
        }
        return customers;
    }

    public Vehicle[] toVehicle(String[][] raw) {
        Vehicle[] vehicles = new Vehicle[raw.length];
        for (int i = 0; i < raw.length; i++) {
            vehicles[i] = new Vehicle(
                Integer.parseInt(raw[i][0]),
                raw[i][1],
                raw[i][2],
                raw[i][3],
                Double.parseDouble(raw[i][3]),
                Boolean.parseBoolean(raw[i][4])
            );
        }
        return vehicles;
    }

    // Test Code
    public static void main(String[] args) {
        CSVParser parser = new CSVParser();
        String[][] rawCustomerData = parser.loadCSVData("CSVFiles/customers.csv");
        Customer[] data = parser.toCustomer(rawCustomerData);
        for (Customer customer : data) {
            System.out.println(customer.toString());
        }
        System.err.println("CSV Header Count: " + parser.getColumnCounts("CSVFiles/customers.csv"));
    }
}