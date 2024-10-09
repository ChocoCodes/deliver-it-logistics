public abstract class Employee {
    protected String name;
    protected String username;
    protected String password;

    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public abstract void showMenu();
}