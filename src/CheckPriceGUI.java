import javax.swing.*;
import java.awt.*;

/**
 * Customer-facing price lookup screen.
 */
public class CheckPriceGUI extends JFrame {
    private static final String PLACEHOLDER = "Type Code";

    private final InventoryManager inventoryManager;
    private JTextField txtCode;
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblCategory;
    private JPanel imagePanel;

    public CheckPriceGUI() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadFromDatabase();

        setTitle("AZKO - Check Prices");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.WHITE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createResultPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
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

        txtCode = UITheme.roundedField(PLACEHOLDER);
        txtCode.setPreferredSize(new Dimension(620, 62));
        txtCode.addActionListener(e -> lookupProduct());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        top.add(txtCode, gbc);

        JButton check = UITheme.outlineButton("⌕  Check Price", 30);
        check.setPreferredSize(new Dimension(620, 62));
        check.addActionListener(e -> lookupProduct());
        gbc.gridy = 1;
        top.add(check, gbc);
        return top;
    }

    private JPanel createResultPanel() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(60, 78, 30, 78));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        textPanel.setBackground(UITheme.WHITE);
        lblName = new JLabel("-", SwingConstants.LEFT);
        lblName.setFont(new Font("Arial", Font.BOLD, 48));
        lblName.setForeground(UITheme.RED);
        lblPrice = new JLabel("-", SwingConstants.LEFT);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 42));
        lblPrice.setForeground(UITheme.RED);
        lblCategory = new JLabel("", SwingConstants.LEFT);
        lblCategory.setFont(new Font("Arial", Font.BOLD, 24));
        lblCategory.setForeground(UITheme.TEXT);
        textPanel.add(lblName);
        textPanel.add(lblPrice);
        textPanel.add(lblCategory);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        content.add(textPanel, gbc);

        imagePanel = new ProductImagePanel();
        imagePanel.setPreferredSize(new Dimension(300, 300));
        gbc.gridx = 1;
        gbc.weightx = 0;
        content.add(imagePanel, gbc);

        return content;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 48, 28));
        footer.setBackground(UITheme.WHITE);
        JButton back = UITheme.primaryButton("Go Back", 28);
        back.setPreferredSize(new Dimension(160, 70));
        back.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });
        footer.add(back);
        return footer;
    }

    private void lookupProduct() {
        String code = UITheme.cleanFieldText(txtCode, PLACEHOLDER);
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product code.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = inventoryManager.searchById(code);
        if (product == null) {
            lblName.setText("-");
            lblPrice.setText("-");
            lblCategory.setText("Product code not found");
            JOptionPane.showMessageDialog(this, "No product found for code: " + code, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        lblName.setText("<html>" + product.getName().toUpperCase() + "</html>");
        lblPrice.setText(formatRupiah(product.getPrice()));
        lblCategory.setText(product.getCategory() + " | Stock: " + product.getQuantity());
    }

    private String formatRupiah(double value) {
        return "Rp. " + String.format("%,.0f", value).replace(',', '.');
    }

    private static class ProductImagePanel extends JPanel {
        ProductImagePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(UITheme.RED);
            g2.fillRoundRect(10, 10, w - 20, h - 20, 24, 24);
            g2.setColor(new Color(246, 91, 67));
            g2.fillRect(36, 36, w - 72, h - 72);
            g2.setColor(new Color(184, 26, 0));
            g2.fillRoundRect(74, 84, w - 148, h - 168, 18, 18);
            g2.setColor(new Color(246, 91, 67));
            g2.fillOval(w / 2 - 22, h / 2 - 62, 44, 44);
            int[] x = {96, 150, 220, w - 86, w - 86, 96};
            int[] y = {h - 98, h - 150, h - 98, h - 98, h - 74, h - 74};
            g2.fillPolygon(x, y, x.length);
            g2.dispose();
        }
    }
}
