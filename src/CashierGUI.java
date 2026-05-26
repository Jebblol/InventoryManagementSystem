import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CashierGUI.java — Simple interface for cashiers to remove items from inventory.
 *
 * Cashiers can only:
 *   • Input a product code
 *   • Specify quantity to remove
 *   • Reduce item quantity in the database
 *   • View confirmation of removal
 *
 * This is a simplified interface designed for quick checkout operations.
 */
public class CashierGUI extends JFrame {

    private InventoryManager inventoryManager;
    private JTextField txtProductCode;
    private JTextField txtQuantity;
    private JButton btnRemove;
    private JButton btnBack;
    private JLabel lblProductName;
    private JLabel lblProductInfo;

    public CashierGUI() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadFromDatabase();

        setTitle("Cashier - Product Removal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // Create header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Cashier Interface");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Product Code Input
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Product Code:"), gbc);
        gbc.gridx = 1;
        txtProductCode = new JTextField(20);
        mainPanel.add(txtProductCode, gbc);

        // Quantity Input
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Quantity to Remove:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(20);
        mainPanel.add(txtQuantity, gbc);

        // Product Name Display
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        lblProductName = new JLabel("N/A");
        lblProductName.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(lblProductName, gbc);

        // Product Info Display
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Product Info:"), gbc);
        gbc.gridx = 1;
        lblProductInfo = new JLabel("N/A");
        lblProductInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(lblProductInfo, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnRemove = new JButton("Remove Item");
        btnRemove.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRemove.setPreferredSize(new Dimension(150, 40));

        btnBack = new JButton("Back to Login");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 16));
        btnBack.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(btnRemove);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        txtProductCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lookupProduct();
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProduct();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new LoginScreen();
                    }
                });
            }
        });

        setVisible(true);
    }

    private void lookupProduct() {
        String productCode = txtProductCode.getText().trim();
        
        if (productCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a product code.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = inventoryManager.searchById(productCode);
        
        if (product != null) {
            lblProductName.setText(product.getName());
            String info = String.format("Category: %s | Price: $%.2f | Qty: %d",
                    product.getCategory(), product.getPrice(), product.getQuantity());
            lblProductInfo.setText(info);
        } else {
            lblProductName.setText("Not Found");
            lblProductInfo.setText("Product code does not exist in database");
        }
    }

    private void removeProduct() {
        String productCode = txtProductCode.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        
        if (productCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a product code.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the quantity to remove.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantityToRemove;
        try {
            quantityToRemove = Integer.parseInt(quantityStr);
            if (quantityToRemove <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity must be greater than 0.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for quantity.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = inventoryManager.searchById(productCode);
        
        if (product == null) {
            JOptionPane.showMessageDialog(this,
                    "Product code does not exist in database.",
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantityToRemove > product.getQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot remove " + quantityToRemove + " items. Only " + product.getQuantity() + " available.",
                    "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove " + quantityToRemove + " x " + product.getName() + " (" + productCode + ")?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Reduce the quantity
            int newQuantity = product.getQuantity() - quantityToRemove;
            product.setQuantity(newQuantity);
            
            // Update the product in database
            if (inventoryManager.updateProduct(product)) {
                JOptionPane.showMessageDialog(this,
                        "Successfully removed " + quantityToRemove + " items. Remaining: " + newQuantity,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the display
                lookupProduct();
                txtQuantity.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update product quantity.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
