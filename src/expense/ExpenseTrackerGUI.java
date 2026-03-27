package expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ExpenseTrackerGUI extends JFrame {

    private JTextField amountField;
    private JTextField categoryField;
    private JTextField dateField;
    private JTable table;
    private DefaultTableModel model;
    private ExpenseDAO dao;

    public ExpenseTrackerGUI() {

        dao = new ExpenseDAO();

        setTitle("Expense Tracker Application");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dao.createTable();

        JPanel topPanel = new JPanel(new GridLayout(4,2,10,10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        topPanel.add(new JLabel("Amount"));
        amountField = new JTextField();
        topPanel.add(amountField);

        topPanel.add(new JLabel("Category"));
        categoryField = new JTextField();
        topPanel.add(categoryField);

        topPanel.add(new JLabel("Date (dd-mm-yyyy)"));
        dateField = new JTextField();
        topPanel.add(dateField);

        JButton addButton = new JButton("Add Expense");
        JButton totalButton = new JButton("Show Total");

        topPanel.add(addButton);
        topPanel.add(totalButton);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("Date");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel();

        JButton viewButton = new JButton("View");
        JButton deleteButton = new JButton("Delete");
        JButton categoryTotalButton = new JButton("Category Total");
        JButton exitButton = new JButton("Exit");

        bottomPanel.add(viewButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(categoryTotalButton);
        bottomPanel.add(exitButton);

        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addExpense());
        viewButton.addActionListener(e -> loadExpenses());
        deleteButton.addActionListener(e -> deleteExpense());
        totalButton.addActionListener(e -> showTotal());
        categoryTotalButton.addActionListener(e -> showCategoryTotal());
        exitButton.addActionListener(e -> System.exit(0));

        loadExpenses();
    }

    private void addExpense() {

        try {

            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            String date = dateField.getText();

            Expense exp = new Expense(amount, category, date);

            dao.addExpense(exp);

            loadExpenses();

            amountField.setText("");
            categoryField.setText("");
            dateField.setText("");

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,"Enter valid data");
        }
    }

    private void loadExpenses() {

        model.setRowCount(0);

        try(Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM expenses")) {

            while(rs.next()) {

                model.addRow(new Object[] {

                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("date")

                });
            }

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,"Load error");
        }
    }

    private void deleteExpense() {

        int row = table.getSelectedRow();

        if(row == -1) {

            JOptionPane.showMessageDialog(this,"Select row");
            return;
        }

        int id = (int) model.getValueAt(row,0);

        dao.deleteExpense(id);

        loadExpenses();
    }

    private void showTotal() {

        try(Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(amount) AS total FROM expenses")) {

            JOptionPane.showMessageDialog(this,
                    "Total = " + rs.getDouble("total"));

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,"Error");
        }
    }

    private void showCategoryTotal() {

        try(Connection conn = DBconnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT category, SUM(amount) total FROM expenses GROUP BY category")) {

            String msg = "";

            while(rs.next()) {

                msg += rs.getString("category")
                        + " = "
                        + rs.getDouble("total")
                        + "\n";
            }

            JOptionPane.showMessageDialog(this,msg);

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this,"Error");
        }
    }
}