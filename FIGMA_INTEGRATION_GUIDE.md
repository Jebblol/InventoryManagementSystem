# Figma Design Integration Guide

This guide explains how to integrate your Figma designs as backgrounds for the Inventory Management System interfaces.

## Overview

Java Swing applications can use custom images as backgrounds for panels and windows. This allows you to apply your Figma designs to enhance the visual appearance of the application.

## Step 1: Export Your Design from Figma

1. Open your Figma design
2. Select the frame or component you want to use as a background
3. Go to **File > Export** or use the export panel in the right sidebar
4. Choose the following export settings:
   - **Format**: PNG (recommended) or JPG
   - **Scale**: 1x, 2x, or 3x (higher for better quality)
   - **Suffix**: Optional (e.g., @2x)
5. Click **Export** and save the file to your project

## Step 2: Add the Image to Your Project

### Option A: Add to a resources folder (Recommended)

1. Create a `resources` folder in your project root:
   ```
   InventoryManagementSystem/
   ├── src/
   ├── resources/
   │   └── images/
   │       ├── manager_background.png
   │       ├── cashier_background.png
   │       └── login_background.png
   └── ...
   ```

2. Place your exported Figma images in the `resources/images` folder

### Option B: Add directly to src folder

1. Create an `images` folder inside `src`:
   ```
   InventoryManagementSystem/
   ├── src/
   │   ├── images/
   │   │   ├── manager_background.png
   │   │   ├── cashier_background.png
   │   │   └── login_background.png
   │   ├── LoginScreen.java
   │   ├── ManagerGUI.java
   │   └── ...
   ```

## Step 3: Load and Apply the Background Image

Here's how to add a background image to your Swing components:

### Basic Background Panel Class

Create a reusable `BackgroundPanel` class:

```java
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // Load the image
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the image to fill the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
```

### Applying to LoginScreen

Modify `LoginScreen.java` to use a background:

```java
public class LoginScreen extends JFrame {
    
    public LoginScreen() {
        setTitle("Inventory Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Use BackgroundPanel instead of regular JPanel
        BackgroundPanel mainPanel = new BackgroundPanel("/images/login_background.png");
        mainPanel.setLayout(new BorderLayout(20, 20));
        
        // Add components to the background panel
        mainPanel.add(createWelcomePanel(), BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false); // Make panel transparent
        JLabel welcomeLabel = new JLabel("Select Your Role");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE); // White text for dark backgrounds
        panel.add(welcomeLabel);
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setOpaque(false); // Make panel transparent
        
        btnManager = new JButton("Manager");
        btnManager.setFont(new Font("Arial", Font.PLAIN, 18));
        btnCashier = new JButton("Cashier");
        btnCashier.setFont(new Font("Arial", Font.PLAIN, 18));
        
        panel.add(btnManager);
        panel.add(btnCashier);
        return panel;
    }
}
```

### Applying to ManagerGUI

Modify `ManagerGUI.java` to use a background:

```java
public class ManagerGUI extends JFrame {
    
    public ManagerGUI() {
        inventoryManager = new InventoryManager();
        
        setTitle("Inventory Management System");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Use BackgroundPanel as the main container
        BackgroundPanel mainPanel = new BackgroundPanel("/images/manager_background.png");
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Add existing panels (make them transparent)
        JPanel inputPanel = createInputPanel();
        inputPanel.setOpaque(false);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        tablePanel.setOpaque(false);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        loadDataFromDatabase();
        setVisible(true);
    }
}
```

### Applying to CashierGUI

Modify `CashierGUI.java` to use a background:

```java
public class CashierGUI extends JFrame {
    
    public CashierGUI() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadFromDatabase();
        
        setTitle("Cashier - Product Removal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Use BackgroundPanel as the main container
        BackgroundPanel mainPanel = new BackgroundPanel("/images/cashier_background.png");
        mainPanel.setLayout(new BorderLayout(20, 20));
        
        // Add components (make panels transparent)
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = createContentPanel();
        contentPanel.setOpaque(false);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
}
```

## Step 4: Adjust UI Elements for Your Design

After applying the background, you may need to adjust:

1. **Text Colors**: Change text color to contrast with your background
   ```java
   label.setForeground(Color.WHITE); // For dark backgrounds
   label.setForeground(Color.BLACK); // For light backgrounds
   ```

2. **Panel Transparency**: Make panels transparent to show the background
   ```java
   panel.setOpaque(false);
   ```

3. **Button Styling**: Customize buttons to match your design
   ```java
   button.setBackground(new Color(0, 120, 215)); // Custom color
   button.setForeground(Color.WHITE);
   button.setBorderPainted(false);
   button.setFocusPainted(false);
   ```

4. **Font Styling**: Use custom fonts if needed
   ```java
   label.setFont(new Font("Segoe UI", Font.BOLD, 18));
   ```

## Step 5: Test and Refine

1. Run the application to see how the background looks
2. Adjust the image size if it doesn't fit properly
3. Tweak transparency and colors as needed
4. Ensure text remains readable against the background

## Alternative: Using Gradient Backgrounds

If you prefer not to use images, you can create gradient backgrounds programmatically:

```java
public class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    
    public GradientPanel(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
            0, 0, color1,
            getWidth(), getHeight(), color2
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
```

## Tips for Figma Integration

1. **Image Size**: Export images at the exact size of your window for best quality
2. **File Format**: Use PNG for transparency support, JPG for smaller file size
3. **Color Contrast**: Ensure your design has good contrast for text readability
4. **Performance**: Large images can slow down application startup
5. **Multiple Resolutions**: Consider providing different sizes for different screen resolutions

## Troubleshooting

**Image not loading:**
- Ensure the image path is correct
- Check that the image is in the correct folder
- Use `/images/filename.png` for resources in classpath
- Use absolute path for testing: `C:/path/to/image.png`

**Background not showing:**
- Ensure panels are set to transparent: `panel.setOpaque(false)`
- Check that the BackgroundPanel is added to the frame
- Verify the image file is not corrupted

**Text not visible:**
- Adjust text color to contrast with background
- Add semi-transparent panels behind text areas
- Use text shadows for better readability

## Example: Complete BackgroundPanel Implementation

Here's a complete, production-ready BackgroundPanel class:

```java
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean scaled = true;
    
    public BackgroundPanel(String imagePath) {
        this(imagePath, true);
    }
    
    public BackgroundPanel(String imagePath, boolean scaled) {
        this.scaled = scaled;
        loadImage(imagePath);
    }
    
    private void loadImage(String imagePath) {
        try {
            // Try loading from classpath (resources folder)
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                // Try loading from file system
                File file = new File(imagePath);
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            if (scaled) {
                // Scale image to fill panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Draw image at original size
                g.drawImage(backgroundImage, 0, 0, this);
            }
        }
    }
}
```

## Next Steps

1. Export your Figma designs
2. Add them to your project's resources folder
3. Create the BackgroundPanel class
4. Modify your GUI classes to use BackgroundPanel
5. Adjust colors and transparency as needed
6. Test the application with your new design

For more advanced customization, consider using JavaFX instead of Swing, which has better support for CSS styling and modern UI designs.
