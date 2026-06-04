import javax.swing.*;
import java.awt.*;

/**
 * Home menu for choosing Manager, Check Prices, or Cashier.
 */
public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("AZKO Inventory Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.WHITE);

        add(createHeroPanel(), BorderLayout.NORTH);
        add(createMenuPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeroPanel() {
        JPanel hero = new JPanel(new BorderLayout());
        hero.setPreferredSize(new Dimension(1024, 150));
        hero.setBackground(UITheme.RED);
        hero.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JLabel logo = new JLabel("az-ko");
        logo.setForeground(UITheme.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 66));
        hero.add(logo, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Inventory and Cashier System", SwingConstants.RIGHT);
        subtitle.setForeground(UITheme.WHITE);
        subtitle.setFont(new Font("Arial", Font.BOLD, 24));
        hero.add(subtitle, BorderLayout.EAST);
        return hero;
    }

    private JPanel createMenuPanel() {
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(UITheme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(26, 0, 26, 0);

        JButton manager = UITheme.primaryButton("Manager", 56);
        JButton checkPrices = UITheme.primaryButton("Check Prices", 52);
        JButton cashier = UITheme.primaryButton("Cashier", 56);

        Dimension buttonSize = new Dimension(560, 110);
        manager.setPreferredSize(buttonSize);
        checkPrices.setPreferredSize(buttonSize);
        cashier.setPreferredSize(buttonSize);

        manager.addActionListener(e -> openScreen(new ManagerGUI()));
        checkPrices.addActionListener(e -> openScreen(new CheckPriceGUI()));
        cashier.addActionListener(e -> openScreen(new CashierGUI()));

        gbc.gridy = 0;
        menu.add(manager, gbc);
        gbc.gridy = 1;
        menu.add(checkPrices, gbc);
        gbc.gridy = 2;
        menu.add(cashier, gbc);

        return menu;
    }

    private void openScreen(JFrame frame) {
        dispose();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
