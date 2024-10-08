public abstract class Employee {
    protected String name;
    protected String username;
    protected String password;
    protected Logistics logistics;
    protected CSVParser parser;

    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.logistics = new Logistics();
        this.parser = new CSVParser();
    }

    public String getUsername() {
        return username;
    }

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public abstract void showMenu();
}