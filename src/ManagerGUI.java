import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * ManagerGUI.java — Java Swing graphical user interface for the Inventory Management System.
 *
 * This is the manager interface with full access to inventory management features.
 *
 * GUI Components used:
 *   • JFrame       — main application window
 *   • JPanel       — layout containers
 *   • JTextField   — input fields for product data
 *   • JButton      — action buttons (Add, Update, Delete, Search, Sort)
 *   • JTable       — displays product records in a tabular format
 *   • JScrollPane  — scrollable wrapper for the table
 *   • JOptionPane  — dialog messages for errors and confirmations
 *   • JComboBox    — dropdown for sort criteria
 *   • JCheckBox    — expiring toggle
 *
 * EXCEPTION HANDLING:
 *   Invalid inputs (non-numeric price/quantity, missing fields) are caught
 *   and reported to the user via JOptionPane dialogs.
 */
public class ManagerGUI extends JFrame {

    // ── Controller ──────────────────────────────────────────────────
    private InventoryManager inventoryManager;

    // ── Input fields ────────────────────────────────────────────────
    private JTextField txtProductId;
    private JTextField txtName;
    private JTextField txtCategory;
    private JTextField txtPrice;
    private JTextField txtQuantity;
    private JTextField txtExpiryDate;
    private JTextField txtSearch;
    private JCheckBox  chkExpiring;

    // ── Table ───────────────────────────────────────────────────────
    private JTable          productTable;
    private DefaultTableModel tableModel;

    // ── Buttons ─────────────────────────────────────────────────────
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnSortName;
    private JButton btnSortPrice;
    private JButton btnClear;
    private JButton btnRefresh;
    private JButton btnSaveBackup;
    private JButton btnLoadBackup;

    // ── Table column names ──────────────────────────────────────────
    private final String[] COLUMNS = {
            "Product ID", "Name", "Category", "Price", "Quantity", "Expires", "Expiry Date", "Total Value"
    };

    // ═══════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public ManagerGUI() {
        inventoryManager = new InventoryManager();

        // ── JFrame settings ─────────────────────────────────────────
        setTitle("AZKO - Manager Dashboard");
        setSize(1120, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        getContentPane().setBackground(UITheme.WHITE);

        // ── Build panels ────────────────────────────────────────────
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // ── Load data from database on startup ──────────────────────
        loadDataFromDatabase();

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 38, 20, 0));

        JLabel logo = UITheme.logoLabel(34);
        logo.setPreferredSize(new Dimension(180, 90));
        panel.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("MANAGER | DASHBOARD", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(UITheme.RED);
        title.setForeground(UITheme.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 44));
        title.setBorder(new UITheme.RoundedBorder(UITheme.RED, 0, 18));
        title.setPreferredSize(new Dimension(740, 120));
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════
    //  PANEL BUILDERS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Creates the input panel with labeled text fields.
     * Uses JPanel with GridBagLayout for clean alignment.
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 12));
        panel.setPreferredSize(new Dimension(350, 520));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 — Product ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(formLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtProductId = formField(16);
        panel.add(txtProductId, gbc);

        // Row 0 — Name
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(formLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtName = formField(16);
        panel.add(txtName, gbc);

        // Row 0 — Category
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(formLabel("Category:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtCategory = formField(16);
        panel.add(txtCategory, gbc);

        // Row 1 — Price
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(formLabel("Price:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtPrice = formField(16);
        panel.add(txtPrice, gbc);

        // Row 1 — Quantity
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0;
        panel.add(formLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtQuantity = formField(16);
        panel.add(txtQuantity, gbc);

        // Row 1 — Perishable checkbox
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        chkExpiring = new JCheckBox("Expiry Date");
        chkExpiring.setBackground(UITheme.WHITE);
        chkExpiring.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(chkExpiring, gbc);
        gbc.gridwidth = 1;

        // Row 1 — Expiry Date
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.weightx = 0;
        panel.add(formLabel("Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtExpiryDate = formField(16);
        txtExpiryDate.setToolTipText("YYYY-MM-DD (only for expiring products)");
        panel.add(txtExpiryDate, gbc);

        // Row 2 — Search
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.weightx = 0;
        panel.add(formLabel("Search:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        gbc.weightx = 1;
        txtSearch = formField(16);
        panel.add(txtSearch, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1; gbc.gridy = 8;
        gbc.weightx = 1;
        btnSearch = UITheme.primaryButton("Search", 14);
        btnSearch.setPreferredSize(new Dimension(140, 46));
        panel.add(btnSearch, gbc);

        // Search button action
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct();
            }
        });

        return panel;
    }

    private JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextField formField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(new UITheme.RoundedBorder(UITheme.RED, 3, 18));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    /**
     * Creates the JTable wrapped in a JScrollPane.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 30));

        // JTable + DefaultTableModel
        tableModel   = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(24);
        productTable.getTableHeader().setBackground(UITheme.WHITE);
        productTable.getTableHeader().setForeground(UITheme.TEXT);
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.setSelectionBackground(new Color(255, 226, 220));
        productTable.setSelectionForeground(Color.BLACK);

        // Click a row → populate input fields
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    populateFieldsFromTable(row);
                }
            }
        });

        // JScrollPane wrapping the table
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the bottom button panel.
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        panel.setBackground(UITheme.WHITE);
        panel.setOpaque(true);

        btnAdd       = UITheme.primaryButton("Add Item", 15);
        btnUpdate    = UITheme.primaryButton("Edit Item", 15);
        btnDelete    = UITheme.primaryButton("Delete Item", 15);
        btnSortName  = UITheme.outlineButton("Sort By Name", 14);
        btnSortPrice = UITheme.outlineButton("Sort By Price", 14);
        btnClear     = UITheme.outlineButton("Clear", 14);
        btnRefresh   = UITheme.outlineButton("Refresh", 14);
        btnSaveBackup = UITheme.outlineButton("Save Backup", 14);
        btnLoadBackup = UITheme.outlineButton("Load Backup", 14);
        JButton btnBack = UITheme.primaryButton("Go Back", 24);

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSortName);
        panel.add(btnSortPrice);
        panel.add(btnClear);
        panel.add(btnRefresh);
        panel.add(btnSaveBackup);
        panel.add(btnLoadBackup);
        panel.add(btnBack);

        // ── Action listeners ────────────────────────────────────────
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { addProduct(); }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { updateProduct(); }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { deleteProduct(); }
        });

        btnSortName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { sortByName(); }
        });

        btnSortPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { sortByPrice(); }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { clearFields(); }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { loadDataFromDatabase(); }
        });

        btnSaveBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { saveBackup(); }
        });

        btnLoadBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { loadBackup(); }
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

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════
    //  CRUD + SEARCH + SORT OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Loads all products from the database and refreshes the table.
     * Called at application startup and when the Refresh button is pressed.
     */
    private void loadDataFromDatabase() {
        try {
            inventoryManager.loadFromDatabase();
            refreshTable(inventoryManager.getProductList());
        } catch (Exception e) {
            // Exception handling: database connection error
            JOptionPane.showMessageDialog(this,
                    "Failed to load data from the database.\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new product using data from the input fields.
     */
    private void addProduct() {
        try {
            // Validate required fields
            if (!validateFields()) return;

            Product product = createProductFromFields();

            if (inventoryManager.searchById(product.getProductId()) != null) {
                JOptionPane.showMessageDialog(this,
                        "Product ID already exists!",
                        "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (inventoryManager.addProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                refreshTable(inventoryManager.getProductList());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add product to the database.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            // Exception handling: invalid numeric input
            JOptionPane.showMessageDialog(this,
                    "Invalid numeric input for Price or Quantity.\nPlease enter valid numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the selected product with data from the input fields.
     */
    private void updateProduct() {
        try {
            if (!validateFields()) return;

            Product product = createProductFromFields();

            if (inventoryManager.searchById(product.getProductId()) == null) {
                JOptionPane.showMessageDialog(this,
                        "Product ID not found! Cannot update a non-existing product.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (inventoryManager.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                refreshTable(inventoryManager.getProductList());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update product.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid numeric input for Price or Quantity.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the product whose ID is in the Product ID field.
     */
    private void deleteProduct() {
        String productId = txtProductId.getText().trim();

        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the Product ID to delete.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete product " + productId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (inventoryManager.deleteProduct(productId)) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                refreshTable(inventoryManager.getProductList());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete product. ID may not exist.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Searches for products by ID or name.
     * Uses HashMap for ID search and linear search for name search.
     */
    private void searchProduct() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            // If search is cleared, show all products
            refreshTable(inventoryManager.getProductList());
            return;
        }

        // Try HashMap-based search by ID first
        Product byId = inventoryManager.searchById(keyword);
        if (byId != null) {
            List<Product> result = new java.util.ArrayList<>();
            result.add(byId);
            refreshTable(result);
            return;
        }

        // Fall back to linear search by name
        List<Product> byName = inventoryManager.searchByName(keyword);
        if (!byName.isEmpty()) {
            refreshTable(byName);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No products found matching: " + keyword,
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Sorts products by name and refreshes the table.
     */
    private void sortByName() {
        inventoryManager.sortByName();
        refreshTable(inventoryManager.getProductList());
    }

    /**
     * Sorts products by price and refreshes the table.
     */
    private void sortByPrice() {
        inventoryManager.sortByPrice();
        refreshTable(inventoryManager.getProductList());
    }

    private void saveBackup() {
        try {
            inventoryManager.saveToFile("inventory_backup.csv");
            JOptionPane.showMessageDialog(this,
                    "Inventory saved to inventory_backup.csv",
                    "File Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save inventory backup.\n" + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBackup() {
        try {
            inventoryManager.loadFromFile("inventory_backup.csv");
            refreshTable(inventoryManager.getProductList());
            JOptionPane.showMessageDialog(this,
                    "Inventory loaded from inventory_backup.csv",
                    "File Loaded", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load inventory backup.\n" + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Validates that all required input fields are filled.
     *
     * @return true if validation passes
     */
    private boolean validateFields() {
        if (txtProductId.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty()      ||
            txtCategory.getText().trim().isEmpty()   ||
            txtPrice.getText().trim().isEmpty()      ||
            txtQuantity.getText().trim().isEmpty()) {

            // Exception handling: missing input fields
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (ID, Name, Category, Price, Quantity).",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (chkExpiring.isSelected() && txtExpiryDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an Expiry Date for expiring products (YYYY-MM-DD).",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Creates a Product or ExpiringProduct from the current input fields.
     * Demonstrates POLYMORPHISM — the returned object can be either type.
     *
     * @return Product or ExpiringProduct
     * @throws NumberFormatException if price or quantity are not valid numbers
     */
    private Product createProductFromFields() throws NumberFormatException {
        String id       = txtProductId.getText().trim();
        String name     = txtName.getText().trim();
        String category = txtCategory.getText().trim();
        double price    = Double.parseDouble(txtPrice.getText().trim());
        int    quantity = Integer.parseInt(txtQuantity.getText().trim());

        if (chkExpiring.isSelected()) {
            String expiry = txtExpiryDate.getText().trim();
            // Inheritance: ExpiringProduct extends Product
            return new ExpiringProduct(id, name, category, price, quantity, expiry);
        } else {
            return new Product(id, name, category, price, quantity);
        }
    }

    /**
     * Refreshes the JTable with the given product list.
     * Demonstrates POLYMORPHISM: getTotalValue() returns different results
     * depending on whether the object is a Product or ExpiringProduct.
     */
    private void refreshTable(List<Product> products) {
        tableModel.setRowCount(0); // clear existing rows

        for (Product p : products) {
            boolean expiring = p instanceof ExpiringProduct;
            String  expiry   = expiring ? ((ExpiringProduct) p).getExpiryDate() : "N/A";

            // Polymorphism: getTotalValue() behaves differently for ExpiringProduct
            Object[] row = {
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity(),
                    expiring ? "Yes" : "No",
                    expiry,
                    String.format("%.2f", p.getTotalValue())
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Populates the input fields from the selected table row.
     */
    private void populateFieldsFromTable(int row) {
        txtProductId.setText((String) tableModel.getValueAt(row, 0));
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtCategory.setText((String) tableModel.getValueAt(row, 2));
        txtPrice.setText((String) tableModel.getValueAt(row, 3));
        txtQuantity.setText(String.valueOf(tableModel.getValueAt(row, 4)));

        String expiring = (String) tableModel.getValueAt(row, 5);
        chkExpiring.setSelected("Yes".equals(expiring));

        String expiry = (String) tableModel.getValueAt(row, 6);
        txtExpiryDate.setText("N/A".equals(expiry) ? "" : expiry);
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtProductId.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtExpiryDate.setText("");
        txtSearch.setText("");
        chkExpiring.setSelected(false);
        productTable.clearSelection();
    }
}
