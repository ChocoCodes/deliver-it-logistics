import java.io.*;
import java.util.ArrayList;


/** 
 *  DeliverIT - Local Logistics System
 *  Authors: John Roland Octavio, Jul Leo Javellana, Raean Chrissean Tamayo
 * 
 * 
 *  Driver Class of the whole System
*/

public class Logistics {
    private CSVParser parser;
    private BufferedReader reader;

    public Logistics() {
        this.parser = new CSVParser();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }
    public static void main(String[] args) {
        // Instantiate Logistics Class - contains all methods
        Logistics manager = new Logistics();
        switch(args.length) {
            case 0:
                // Customer Module
                System.out.println("=====================================================");
                System.out.println("\t\t Welcome to DeliverIT! \t\t");
                System.out.println("=====================================================");
                String name = manager.getInput("Enter Full Name");
                manager.parser.setFilePath("CSVFiles/customers.csv");
                Customer currentCustomer = manager.parser.searchCustomerName(name);
                if (currentCustomer == null) {
                    System.out.printf("Customer name {%s} not found.\nPlease register in order to use the system.\n", name);
                    currentCustomer = manager.registerCustomer(name);
                }
                manager.showCustomerMenu(currentCustomer);
                break;
            case 2:
                // Admin/Employee Module
                System.out.println("Admin/Employee Module in progress");
                break;
            default:
                System.out.println("Invalid args length!\nUSAGE: javac -cp out Logistics OR javac -cp out Logistics {arg1} {arg2}");
                return;
        }
        System.out.println("Program Terminated.");
    }
    
    private boolean checkInput(String input) { return input.length() == 0 || input == null; }

    private String getInput(String prompt) {
        boolean exceptionOccured = false; 
        String input = "";
        do {
            try {
                System.out.printf("%s: ", prompt);
                input = reader.readLine();
            } catch (IOException e) { exceptionOccured = true; }
        } while(checkInput(input) || exceptionOccured);
        return input;
    }

    private boolean checkNumerics(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) { return false; }
    }

    private Customer registerCustomer(String name) {
        System.out.println();
        String contact = getInput("Enter Contact No.");
        String address = getInput("Enter Address");
        int id = parser.getLatestID() + 1;
        Customer newCustomer = new Customer(id, name, contact, address);
        parser.saveCustomerEntry(newCustomer);
        return newCustomer;
    }

    private void showCustomerMenu(Customer customer) {
        String in;
        char op;
        boolean valid = true;

        System.out.printf("Welcome, %s!\n", customer.getName());
        System.out.println("[S]end Package\n[E]dit Information\n[T]rack Package\n[Q]uit");
        do {
            do {
                in = getInput("Enter Option").toUpperCase();
            } while (in.length() != 1);
            op = in.charAt(0);
            switch(op) {
                case 'S':
                System.out.println("Send Package in Progress");
                break;
                case 'E':   
                editCustomerInfo(customer);
                break;
                case 'T':
                System.out.println("In progress.");
                break;
                case 'Q':
                return;
                default:
                valid = false;
                break;
            }
        } while(!valid);
    }

    private void editCustomerInfo(Customer customer) {
        String in;
        boolean valid = true;
        System.out.println("Edit Customer Information");
        System.out.println("[1] Name\n[2] Contact Info\n[3] Address\n[4] Quit");

        do {
            do {
                in = getInput("Enter Option");
            } while (!checkNumerics(in));
            switch(Integer.parseInt(in)) {
                case 1:
                    String newName = getInput("Enter New Name");
                    customer.setName(newName);
                    parser.updateCustomerCSV(customer.getCustomerID(), newName, 1);
                    break;
                case 2:
                    String newInfo = getInput("Enter New Contact No.");
                    customer.setContactInfo(newInfo);
                    parser.updateCustomerCSV(customer.getCustomerID(), newInfo, 2);
                    break;
                case 3:
                    String newAddr = getInput("Enter New Address");
                    customer.setAddress(newAddr);
                    parser.updateCustomerCSV(customer.getCustomerID(), newAddr, 3);                    
                    break;
                default:
                    valid = false;
                    break;
            }
        } while(!valid);
    }
}


class CSVParser {
    private String file;
    private final String[] CUSTOMER_HEADER = {"id", "name", "contactInfo", "address"};

    public CSVParser() { file = ""; }
    public void setFilePath(String file) { this.file = file; }
    public String getFilePath() { return this.file; }

    public int getColumnCounts(String file) {
        int counts = -1;
        try (BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String buffer;
            if ((buffer = fin.readLine()) != null) {
                String[] header = buffer.split(",");
                counts = header.length;
            }
            fin.close();
        } catch(IOException e) { return -1; }
        return counts;
    }

    public String[][] loadCSVData(String file) {
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String buffer;
            // Skip header column
            if ((buffer = fin.readLine()) != null) {}
            while ((buffer = fin.readLine()) != null) {
                String[] tmp = buffer.split(",");
                // Skip invalid columns: columns that does not have the same counts as the header
                if(tmp.length != getColumnCounts(file)) { continue; }
                data.add(tmp);
            }
            fin.close();
        } catch (IOException e) { return new String[0][0]; }
        return data.toArray(new String[0][0]);
    }

    public void saveCustomerEntry(Customer customer) {
        boolean saved = false;
        try (PrintWriter fout = new PrintWriter(new FileWriter(getFilePath(), true))) {
            fout.printf ("%d,%s,%s,%s\n", 
            customer.getCustomerID(), 
            customer.getName(), 
            customer.getContactInfo(), 
            customer.getAddress());
            saved = true;
        } catch (IOException e) { saved = false; }
        System.out.println(saved ? "Successfully saved new customer data." : "An error occured while saving customer data.");
    }

    public void overwrite(String file, String[][] data) {
        boolean saved = false;
        try (PrintWriter fout = new PrintWriter(new FileWriter(file))) {
            fout.printf("%s,%s,%s,%s\n", CUSTOMER_HEADER[0], CUSTOMER_HEADER[1], CUSTOMER_HEADER[2], CUSTOMER_HEADER[3]);
            for(String[] datum: data) {
                fout.printf("%s,%s,%s,%s\n", 
                datum[0], 
                datum[1], 
                datum[2], 
                datum[3]);
            }
            saved = true;
        } catch (IOException e) { saved = false; }
        System.out.println(saved ? "Successfully updated customer data." : "An error occured while updating customer data.");
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

    public Customer toCustomer(String[][] raw, int idx) { 
        return new Customer(
            Integer.parseInt(raw[idx][0]), 
            raw[idx][1], 
            raw[idx][2], 
            raw[idx][3]
        ); 
    }

    public Vehicle[] toVehicle(String[][] raw) {
        Vehicle[] vehicles = new Vehicle[raw.length];
        for (int i = 0; i < raw.length; i++) {
            vehicles[i] = new Vehicle(
                Integer.parseInt(raw[i][0]),
                raw[i][1],
                raw[i][2],
                raw[i][3],
                Double.parseDouble(raw[i][4]),
                Boolean.parseBoolean(raw[i][5])
            );
        }
        return vehicles;
    }

    public Vehicle toVehicle(String[][] raw, int idx) { 
        return new Vehicle(
            Integer.parseInt(raw[idx][0]), 
            raw[idx][1], 
            raw[idx][2], 
            raw[idx][3],
            Double.parseDouble(raw[idx][4]),
            Boolean.parseBoolean(raw[idx][5])
        ); 
    }

    public Customer searchCustomerName(String name) {
        String[][] csvCustomer = loadCSVData(getFilePath());
        for(int i = 0; i < csvCustomer.length; i++) {
            if(csvCustomer[i][1].toLowerCase().equals(name.toLowerCase())) {
                return toCustomer(csvCustomer, i);
            }
        }
        return null;
    }

    public int getLatestID() {
        String[][] rawCSV = loadCSVData(getFilePath());
        return Integer.parseInt(rawCSV[rawCSV.length - 1][0]);
    }

    public void updateCustomerCSV(int id, String newAttribute, int columnIndex) {
        String[][] csvData = loadCSVData(getFilePath());
        for (int i = 0; i < csvData.length; i++) {
            if (Integer.parseInt(csvData[i][0]) == id) {
                csvData[i][columnIndex] = newAttribute;
                break;
            }
        }
        // Rewrite Customer CSV
        overwrite(getFilePath(), csvData); 
    }
}