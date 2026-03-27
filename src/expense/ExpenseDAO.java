package expense;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExpenseDAO {

    public void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "amount REAL," +
                     "category TEXT," +
                     "date TEXT)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.execute();

        } catch (Exception e) {
            System.out.println("Table creation error");
        }
    }

    public void addExpense(Expense expense) {

        String sql = "INSERT INTO expenses(amount, category, date) VALUES(?,?,?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, expense.getAmount());
            stmt.setString(2, expense.getCategory());
            stmt.setString(3, expense.getDate());

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Insert error");
        }
    }

    public void deleteExpense(int id) {

        String sql = "DELETE FROM expenses WHERE id=?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Delete error");
        }
    }
}