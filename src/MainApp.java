import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * MainApp.java — Entry point of the Inventory Management System.
 *
 * This class launches the Swing GUI on the Event Dispatch Thread (EDT)
 * to ensure thread-safe GUI operations.
 */
public class MainApp {

    public static void main(String[] args) {

        // Set the look-and-feel to the system default for a native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look-and-feel if the system one is not available
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Launch the Login Screen on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen();
            }
        });
    }
}
