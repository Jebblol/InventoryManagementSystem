import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * CashierGUI.java - AZKO cashier screen.
 *
 * Data structures:
 * - ArrayList stores cart rows.
 * - Queue stores completed transaction summaries.
 */
public class CashierGUI extends JFrame {
    private static final String PLACEHOLDER = "Enter Item Code";

    private final InventoryManager inventoryManager;
    private final ArrayList<CartLine> cart;
    private final Queue<String> completedTransactions;

    private JTextField txtProductCode;
    private JPanel rowsPanel;
    private JLabel lblTotal;

    public CashierGUI() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadFromDatabase();
        cart = new ArrayList<>();
        completedTransactions = new LinkedList<>();

        setTitle("AZKO - Cashier");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.WHITE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCartPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(UITheme.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(32, 28, 20, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel logo = UITheme.logoLabel(34);
        logo.setPreferredSize(new Dimension(170, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        top.add(logo, gbc);

        txtProductCode = UITheme.roundedField(PLACEHOLDER);
        txtProductCode.setPreferredSize(new Dimension(620, 62));
        txtProductCode.addActionListener(e -> addItemToCart());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        top.add(txtProductCode, gbc);

        JButton enter = UITheme.outlineButton("⌕  Enter Item", 30);
        enter.setPreferredSize(new Dimension(620, 62));
        enter.addActionListener(e -> addItemToCart());
        gbc.gridy = 1;
        top.add(enter, gbc);

        return top;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 70, 20, 70));

        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(UITheme.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.RED));
        header.add(columnHeader("Item", SwingConstants.LEFT));
        header.add(columnHeader("Quantity", SwingConstants.CENTER));
        header.add(columnHeader("Price", SwingConstants.CENTER));
        panel.add(header, BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setBackground(UITheme.WHITE);
        panel.add(rowsPanel, BorderLayout.CENTER);

        refreshCart();
        return panel;
    }

    private JLabel columnHeader(String text, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(new Font("Arial", Font.BOLD, 34));
        label.setForeground(UITheme.RED);
        label.setBorder(BorderFactory.createEmptyBorder(0, 18, 10, 18));
        return label;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UITheme.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 48, 30, 42));

        JButton back = UITheme.primaryButton("Go Back", 28);
        back.setPreferredSize(new Dimension(160, 70));
        back.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });
        footer.add(back, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 22, 0));
        right.setBackground(UITheme.WHITE);

        lblTotal = new JLabel("Total: -");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 38));
        lblTotal.setForeground(UITheme.RED);

        JButton checkout = UITheme.primaryButton("Checkout", 28);
        checkout.setPreferredSize(new Dimension(190, 70));
        checkout.addActionListener(e -> checkout());

        right.add(lblTotal);
        right.add(checkout);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }

    private void addItemToCart() {
        String code = UITheme.cleanFieldText(txtProductCode, PLACEHOLDER);
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an item code.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = inventoryManager.searchById(code);
        if (product == null) {
            JOptionPane.showMessageDialog(this,
                    "Item code not found: " + code,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            resetInput();
            return;
        }

        CartLine line = findLine(product.getProductId());
        int nextQuantity = line == null ? 1 : line.getQuantity() + 1;
        if (nextQuantity > product.getQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "Only " + product.getQuantity() + " item(s) available for " + product.getName() + ".",
                    "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
            resetInput();
            return;
        }

        if (line == null) {
            cart.add(new CartLine(product));
        } else {
            line.addOne();
        }

        resetInput();
        refreshCart();
    }

    private CartLine findLine(String productId) {
        for (CartLine line : cart) {
            if (line.getProduct().getProductId().equals(productId)) {
                return line;
            }
        }
        return null;
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cart is empty.",
                    "Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (CartLine line : cart) {
            Product product = line.getProduct();
            product.setQuantity(product.getQuantity() - line.getQuantity());
            if (!inventoryManager.updateProduct(product)) {
                JOptionPane.showMessageDialog(this,
                        "Failed to update stock for " + product.getName() + ".",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String summary = "Checkout total " + formatRupiah(calculateTotal())
                + " with " + cart.size() + " item type(s)";
        completedTransactions.add(summary);

        JOptionPane.showMessageDialog(this,
                "Checkout complete.\n" + summary,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        cart.clear();
        inventoryManager.loadFromDatabase();
        refreshCart();
    }

    private void refreshCart() {
        rowsPanel.removeAll();

        if (cart.isEmpty()) {
            JPanel emptyRow = rowPanel("-", "-", "-");
            rowsPanel.add(emptyRow);
        } else {
            for (CartLine line : cart) {
                rowsPanel.add(rowPanel(
                        line.getProduct().getName(),
                        "x" + line.getQuantity(),
                        formatRupiah(line.getLineTotal())
                ));
            }
        }

        if (lblTotal != null) {
            lblTotal.setText(cart.isEmpty() ? "Total: -" : "Total: " + formatRupiah(calculateTotal()));
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private JPanel rowPanel(String item, String quantity, String price) {
        JPanel row = new JPanel(new GridLayout(1, 3));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        row.setPreferredSize(new Dimension(840, 42));
        row.setBackground(UITheme.WHITE);

        row.add(rowLabel(item, SwingConstants.LEFT));
        row.add(rowLabel(quantity, SwingConstants.CENTER));
        row.add(rowLabel(price, SwingConstants.CENTER));
        return row;
    }

    private JLabel rowLabel(String text, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(UITheme.TEXT);
        label.setBorder(BorderFactory.createEmptyBorder(3, 18, 3, 18));
        return label;
    }

    private double calculateTotal() {
        double total = 0;
        for (CartLine line : cart) {
            total += line.getLineTotal();
        }
        return total;
    }

    private void resetInput() {
        txtProductCode.setText(PLACEHOLDER);
        txtProductCode.setForeground(UITheme.MUTED);
        txtProductCode.requestFocusInWindow();
    }

    private String formatRupiah(double value) {
        return "Rp. " + String.format("%,.0f", value).replace(',', '.');
    }

    private static class CartLine {
        private final Product product;
        private int quantity;

        CartLine(Product product) {
            this.product = product;
            this.quantity = 1;
        }

        Product getProduct() {
            return product;
        }

        int getQuantity() {
            return quantity;
        }

        void addOne() {
            quantity++;
        }

        double getLineTotal() {
            return product.getPrice() * quantity;
        }
    }
}
