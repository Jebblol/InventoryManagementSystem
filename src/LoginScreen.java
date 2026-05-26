import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginScreen.java — Role selection screen for the Inventory Management System.
 *
 * Allows users to choose between Manager and Cashier roles.
 * Manager has full access to inventory management.
 * Cashier can only remove items by product code.
 */
public class LoginScreen extends JFrame {

    private JButton btnManager;
    private JButton btnCashier;

    public LoginScreen() {
        setTitle("Inventory Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // Create welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("Select Your Role");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        btnManager = new JButton("Manager");
        btnManager.setFont(new Font("Arial", Font.PLAIN, 18));
        btnCashier = new JButton("Cashier");
        btnCashier.setFont(new Font("Arial", Font.PLAIN, 18));

        buttonPanel.add(btnManager);
        buttonPanel.add(btnCashier);
        add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        btnManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManagerGUI();
            }
        });

        btnCashier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCashierGUI();
            }
        });

        setVisible(true);
    }

    private void openManagerGUI() {
        dispose(); // Close login screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerGUI();
            }
        });
    }

    private void openCashierGUI() {
        dispose(); // Close login screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CashierGUI();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen();
            }
        });
    }
}
