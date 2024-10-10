import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
/** 
 *  DeliverIT - Local Logistics System
 *  Authors: John Roland Octavio, Jul Leo Javellana, Raean Chrissean Tamayo
 * 
 * 
 *  Driver Class of the whole System
*/

public class Logistics {
    private static CSVParser parser;
    private static BufferedReader reader;

    public Logistics() {
        reader = new BufferedReader(new InputStreamReader(System.in));
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
                String name = getInput("Enter Full Name");
                CSVParser.setFilePath("CSVFiles/customers.csv");
                Customer currentCustomer = manager.searchCustomerName(name);
                if (currentCustomer == null) {
                    System.out.printf("Customer name {%s} not found.\nPlease register in order to use the system.\n", name);
                    currentCustomer = manager.registerCustomer(name);
                }
                manager.showCustomerMenu(currentCustomer);
                break;
                case 3:

                break;
            default:
                System.out.println("Invalid args length!\nUSAGE: javac -cp out Logistics OR javac -cp out Logistics {role} {password} {name}");
                return;
        }
        System.out.println("Program Terminated.");
    }
    
    public static boolean checkInput(String input) { return input.length() == 0 || input == null; }

    public static String getInput(String prompt) {
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

    public static boolean checkInt(String input) {
        try {
            return CSVParser.toInt(input) > 0;
        } catch (NumberFormatException e) { return false; }
    }

    public static boolean checkDouble(String input) {
        try {
            return CSVParser.toDouble(input) > 0.0;
        } catch (NumberFormatException e) { return false; }
    }

    private Customer registerCustomer(String name) {
        System.out.println();
        String contact = getInput("Enter Contact No.");
        String address = getInput("Enter Address");
        Customer newCustomer = new Customer(CSVParser.getLatestID() + 1, name, contact, address);
        CSVParser.saveEntry(newCustomer.toCSVFormat());
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
                in = getInput("Option").toUpperCase();
            } while (in.length() != 1);
            op = in.charAt(0);
            switch(op) {
                case 'S':
                    sendPackage(customer);
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
        System.out.println("[1] Name\n[2] Contact Info\n[3] Address\n[4] Back");

        do {
            do {
                in = getInput("Option");
            } while (!checkInt(in));
            switch(CSVParser.toInt(in)) {
                case 1:
                    String newName = getInput("New Name");
                    customer.setName(newName);
                    parser.updateCustomerCSV(customer.getCustomerID(), newName, 1);
                    break;
                case 2:
                    String newInfo = getInput("New Contact No.");
                    customer.setContactInfo(newInfo);
                    parser.updateCustomerCSV(customer.getCustomerID(), newInfo, 2);
                    break;
                case 3:
                    String newAddr = getInput("New Address");
                    customer.setAddress(newAddr);
                    parser.updateCustomerCSV(customer.getCustomerID(), newAddr, 3);                    
                    break;
                case 4:
                    return;
                default:
                    valid = false;
                    break;
            }
        } while(!valid);
    }

    public Item[] getCustomerItems() {
        ArrayList<Item> currentItems = new ArrayList<>();
        boolean isDone = false;
        String name, weightStr, lenStr, widStr, heiStr;
        while(!isDone) {
            name = getInput("Item Name");
            do {
                weightStr = getInput("Weight(grams)");
            } while(!checkDouble(weightStr));
            do {
                lenStr = getInput("Length(cm)");
            } while(!checkDouble(lenStr));
            do {
                widStr = getInput("Width(cm)");
            } while(!checkDouble(widStr));
            do {
                heiStr = getInput("Height(cm)");
            } while(!checkDouble(heiStr));

            Dimension dim = new Dimension(CSVParser.toDouble(lenStr), CSVParser.toDouble(widStr), CSVParser.toDouble(heiStr));
            currentItems.add(new Item(name, CSVParser.toDouble(weightStr), dim));

            String continuePrompt;
            do {
                continuePrompt = getInput("Add more items? [Y/N]").toUpperCase();
            } while(continuePrompt.length() != 1);
            if(continuePrompt.equals("N")) {
                isDone = true;
                break;
            }
        }
        return currentItems.toArray(new Item[0]);
    }

    public void sendPackage(Customer customer) {
        Item[] items = getCustomerItems();
        String receiver = getInput("Receiver Address");
        Package pkg = new Package(CSVParser.getLatestID() + 1, items, receiver);
        pkg.displayPackageContents(); // DB
        System.out.println(pkg.toString()); // DB
        Shipment shipment = new Shipment(receiver, pkg);
        shipment.calcShipCost();
        System.out.printf("Total Shipping Cost: %.2f\n", shipment.getShipCost());
        while(true) {
            String payment = "";
            do {
                payment = getInput("Enter Cash");
            } while(!Logistics.checkDouble(payment));
            if (CSVParser.toDouble(payment) > shipment.getShipCost()) {
                System.out.printf("Change: %.2f\n", CSVParser.toDouble(payment) - shipment.getShipCost());
                break;
            }
        }
        shipment.setStatus("Paid");
        System.out.println(shipment.getShipCost()); // DB
        System.out.println(shipment.getDestination()); // DB
        System.out.println(shipment.getStatus()); // DB
        System.out.println("Shipment paid successfully. Saving your packages...");
        // Save to packages csv once paid
        CSVParser.setFilePath("CSVFiles/packages.csv");
        CSVParser.saveEntry(pkg.toCSVFormat(customer.getCustomerID()));
        // Save to items csv once paid
        CSVParser.setFilePath("CSVFiles/items.csv");
        for (Item item : items) {
            CSVParser.saveEntry(item.toCSVFormat(pkg.getId()));
        }
        // TODO: Save to Shipment CSV - still pending
        
    }

    public Customer searchCustomerName(String name) {
        String[][] csvCustomer = CSVParser.loadCSVData(CSVParser.getFilePath());
        for(int i = 0; i < csvCustomer.length; i++) {
            if(csvCustomer[i][1].toLowerCase().equals(name.toLowerCase())) {
                return Customer.toCustomer(csvCustomer, i);
            }
        }
        return null;
    }
}


class CSVParser {
    private final String[] CUSTOMER_H = {"id", "name", "contactInfo", "address"};
    private final String[] PACKAGE_H = {"cID", "pkgID", "receiverAddress", "created", "dimensionalWeight_kg", "length_cm", "width_cm", "height_cm"};
    private final String[] ITEMS_H = {"pkgID", "name", "weight_kg", "length_cm", "width_cm", "height_cm"};
    private static final String[] VEHICLES_H = {"vID", "whId", "type", "licensePlate", "driver", "cap_max", "cap_curr", "max_ship", "curr_ship", "avail"};
    private static final String[] WAREHOUSE_H = {"wID", "location", "package_capacity", "vehicle_capacity"};
    private static final String[] SHIPMENT_H = {"id","pkgId","vId","wId","dest","shipCost","confirmed","status","shipDate","eta"};
    private static String filePath; // hold the file path needed for operations

    public CSVParser() { 
        filePath = ""; 
    }

    public static void setFilePath(String file) { filePath = file; }
    public static String getFilePath() { return filePath; }
    public String[] getCustomerHeader() { return this.CUSTOMER_H; }
    public String[] getPackageHeader() { return this.PACKAGE_H; }
    public String[] getItemHeader() { return this.ITEMS_H; }
    public static String[] getVehicleHeader() { return VEHICLES_H; }
    public static String[] getWarehouseHeader() { return WAREHOUSE_H; }
    public static String[] getShipmentHeader() { return SHIPMENT_H; }

    public static int toInt(String in) { return Integer.parseInt(in); }
    public static double toDouble(String in) { return Double.parseDouble(in); }
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

    public static int getColumnCounts(String file) {
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

    public static String[][] loadCSVData(String file) {
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
        } catch (IOException e) { return new String[0][0]; }
        return data.toArray(new String[0][0]);
    }
    
    // Individual Entity - Save an entity's attributes to the CSV File
    public static void saveEntry(String[] data) {
        boolean saved = false;
        try (PrintWriter fout = new PrintWriter(new FileWriter(getFilePath(), true))) {
            String placeholder = generateFormatString(data.length);
            fout.printf(placeholder,(Object[]) data);
            saved = true;
        } catch (IOException e) { saved = false; }
        System.out.println(saved ? "Successfully saved new data." : "An error occured while saving new data.");
    }

    // Bulk Writing - Write all the current contents of the CSV after updating selected fields
    public static void writeToCSV(String[][] data, String[] header, boolean append) {
        boolean saved = false;
        try (PrintWriter fout = new PrintWriter(new FileWriter(filePath, append))) {
            // Generate string CSV structure and save the headers first
            if(!append) {
                String placeholder = generateFormatString(header.length);
                fout.printf(placeholder, (Object[]) header);
            }
            String dataPlaceholder = generateFormatString(data[0].length);
            for (String[] row : data) { fout.printf(dataPlaceholder, (Object[]) row); }
            saved = true;
        } catch (IOException e) { saved = false; }
        System.out.println(saved ? "Successfully updated new data." : "An error occured while updating new data.");
    }

    // Helper Method for CSV Structure generation for data format
    private static String generateFormatString(int length) {
        StringBuilder format = new StringBuilder();
        for(int i = 0; i < length; i++) {
            format.append("%s,");
        }
        format.setCharAt(format.length() - 1, '\n');
        return format.toString();
    }

    public static int getLatestID() {
        String[][] rawCSV = loadCSVData(getFilePath());
        return rawCSV.length == 0 ? 0 : toInt(rawCSV[rawCSV.length - 1][0]);
    }

    public void updateCustomerCSV(int id, String newAttribute, int columnIndex) {
        String[][] csvData = loadCSVData(getFilePath());
        for (int i = 0; i < csvData.length; i++) {
            if (toInt(csvData[i][0]) == id) {
                csvData[i][columnIndex] = newAttribute;
                break;
            }
        }
        // Rewrite Customer CSV
        writeToCSV(csvData, getCustomerHeader(), false); 
    }
}