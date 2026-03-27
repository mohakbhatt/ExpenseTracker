package expense;

public class Expense {

    private int id;
    private double amount;
    private String category;
    private String date;

    public Expense(double amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}