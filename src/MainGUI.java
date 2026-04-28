import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * MainGUI.java — Java Swing graphical user interface for the Inventory Management System.
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
 *   • JCheckBox    — perishable toggle
 *
 * EXCEPTION HANDLING:
 *   Invalid inputs (non-numeric price/quantity, missing fields) are caught
 *   and reported to the user via JOptionPane dialogs.
 */
public class MainGUI extends JFrame {

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
    private JCheckBox  chkPerishable;

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

    // ── Table column names ──────────────────────────────────────────
    private final String[] COLUMNS = {
            "Product ID", "Name", "Category", "Price", "Quantity", "Perishable", "Expiry Date", "Total Value"
    };

    // ═══════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public MainGUI() {
        inventoryManager = new InventoryManager();

        // ── JFrame settings ─────────────────────────────────────────
        setTitle("Inventory Management System");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ── Build panels ────────────────────────────────────────────
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // ── Load data from database on startup ──────────────────────
        loadDataFromDatabase();

        setVisible(true);
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
        panel.setBorder(BorderFactory.createTitledBorder("Product Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 — Product ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        txtProductId = new JTextField(12);
        panel.add(txtProductId, gbc);

        // Row 0 — Name
        gbc.gridx = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        txtName = new JTextField(12);
        panel.add(txtName, gbc);

        // Row 0 — Category
        gbc.gridx = 4;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 5;
        txtCategory = new JTextField(12);
        panel.add(txtCategory, gbc);

        // Row 1 — Price
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(12);
        panel.add(txtPrice, gbc);

        // Row 1 — Quantity
        gbc.gridx = 2;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3;
        txtQuantity = new JTextField(12);
        panel.add(txtQuantity, gbc);

        // Row 1 — Perishable checkbox
        gbc.gridx = 4;
        chkPerishable = new JCheckBox("Perishable");
        panel.add(chkPerishable, gbc);

        // Row 1 — Expiry Date
        gbc.gridx = 5;
        txtExpiryDate = new JTextField(12);
        txtExpiryDate.setToolTipText("YYYY-MM-DD (only for perishable)");
        panel.add(txtExpiryDate, gbc);

        // Row 2 — Search
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Search (ID/Name):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtSearch = new JTextField(20);
        panel.add(txtSearch, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3;
        btnSearch = new JButton("Search");
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

    /**
     * Creates the JTable wrapped in a JScrollPane.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Product Records"));

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd       = new JButton("Add Product");
        btnUpdate    = new JButton("Update Product");
        btnDelete    = new JButton("Delete Product");
        btnSortName  = new JButton("Sort by Name");
        btnSortPrice = new JButton("Sort by Price");
        btnClear     = new JButton("Clear Fields");
        btnRefresh   = new JButton("Refresh");

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSortName);
        panel.add(btnSortPrice);
        panel.add(btnClear);
        panel.add(btnRefresh);

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

        if (chkPerishable.isSelected() && txtExpiryDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an Expiry Date for perishable products (YYYY-MM-DD).",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Creates a Product or PerishableProduct from the current input fields.
     * Demonstrates POLYMORPHISM — the returned object can be either type.
     *
     * @return Product or PerishableProduct
     * @throws NumberFormatException if price or quantity are not valid numbers
     */
    private Product createProductFromFields() throws NumberFormatException {
        String id       = txtProductId.getText().trim();
        String name     = txtName.getText().trim();
        String category = txtCategory.getText().trim();
        double price    = Double.parseDouble(txtPrice.getText().trim());
        int    quantity = Integer.parseInt(txtQuantity.getText().trim());

        if (chkPerishable.isSelected()) {
            String expiry = txtExpiryDate.getText().trim();
            // Inheritance: PerishableProduct extends Product
            return new PerishableProduct(id, name, category, price, quantity, expiry);
        } else {
            return new Product(id, name, category, price, quantity);
        }
    }

    /**
     * Refreshes the JTable with the given product list.
     * Demonstrates POLYMORPHISM: getTotalValue() returns different results
     * depending on whether the object is a Product or PerishableProduct.
     */
    private void refreshTable(List<Product> products) {
        tableModel.setRowCount(0); // clear existing rows

        for (Product p : products) {
            boolean perishable = p instanceof PerishableProduct;
            String  expiry     = perishable ? ((PerishableProduct) p).getExpiryDate() : "N/A";

            // Polymorphism: getTotalValue() behaves differently for PerishableProduct
            Object[] row = {
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity(),
                    perishable ? "Yes" : "No",
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

        String perishable = (String) tableModel.getValueAt(row, 5);
        chkPerishable.setSelected("Yes".equals(perishable));

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
        chkPerishable.setSelected(false);
        productTable.clearSelection();
    }
}
