import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationForm extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private ButtonGroup genderGroup;
    private JTextField addressField;
    private JTextField contactField;
    private JButton registerButton;
    private JButton exitButton;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public RegistrationForm() {
        // Initialize components
        idField = new JTextField(20);
        nameField = new JTextField(20);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        addressField = new JTextField(20);
        contactField = new JTextField(20);
        registerButton = new JButton("Register");
        exitButton = new JButton("Exit");

        // Initialize table
        String[] columnNames = {"ID", "Name", "Gender", "Address", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);

        // Add radio buttons to the group
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        // Set up the main panel and add components
        mainPanel = new JPanel();
        mainPanel.add(new JLabel("ID:"));
        mainPanel.add(idField);
        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Gender:"));
        mainPanel.add(maleButton);
        mainPanel.add(femaleButton);
        mainPanel.add(new JLabel("Address:"));
        mainPanel.add(addressField);
        mainPanel.add(new JLabel("Contact:"));
        mainPanel.add(contactField);
        mainPanel.add(registerButton);
        mainPanel.add(exitButton);
        mainPanel.add(new JScrollPane(dataTable));

        // Set the main panel as the content pane
        setContentPane(mainPanel);
        setTitle("Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        // Add action listener for the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        // Add action listener for the exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void register() {
        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "Male" : "Female";
        String address = addressField.getText();
        String contact = contactField.getText();

        // Add data to the table
        tableModel.addRow(new Object[]{id, name, gender, address, contact});

        // Insert data into the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO registration (id, name, gender, address, contact) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, gender);
                pstmt.setString(4, address);
                pstmt.setString(5, contact);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Clear input fields
        idField.setText("");
        nameField.setText("");
        genderGroup.clearSelection();
        addressField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm());
    }
}

class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/registration_db";
        String user = "root";
        String password = "@Sirtitus12"; 
        return DriverManager.getConnection(url, user, password);
    }
}
