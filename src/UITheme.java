import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Shared AZKO-inspired Swing styling helpers.
 */
public class UITheme {
    public static final Color RED = new Color(238, 35, 0);
    public static final Color RED_DARK = new Color(190, 24, 0);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT = Color.BLACK;
    public static final Color MUTED = new Color(205, 205, 205);

    private UITheme() {
    }

    public static JLabel logoLabel(int fontSize) {
        JLabel label = new JLabel("az-ko", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(RED);
        label.setForeground(WHITE);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setBorder(new RoundedBorder(RED, 0, 18));
        return label;
    }

    public static JButton primaryButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setForeground(TEXT);
        button.setBackground(RED);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(RED, 0, 18));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(RED_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(RED);
            }
        });
        return button;
    }

    public static JButton outlineButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setForeground(TEXT);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(RED, 3, 18));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JTextField roundedField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 22));
        field.setForeground(MUTED);
        field.setBorder(new RoundedBorder(RED, 3, 28));
        field.setMargin(new Insets(8, 24, 8, 24));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(MUTED);
                }
            }
        });
        return field;
    }

    public static String cleanFieldText(JTextField field, String placeholder) {
        String value = field.getText().trim();
        return value.equals(placeholder) ? "" : value;
    }

    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(Math.max(1, thickness)));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                    width - thickness - 1, height - thickness - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int pad = Math.max(8, radius / 2);
            return new Insets(pad, pad, pad, pad);
        }
    }
}
