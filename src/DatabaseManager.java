import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager.java — Handles all MySQL database operations via JDBC.
 *
 * Responsibilities:
 *   • Connect to MySQL (inventory_db)
 *   • INSERT, UPDATE, DELETE, SELECT products
 *   • Convert ResultSet rows into Product / ExpiringProduct objects
 *
 * EXCEPTION HANDLING:
 *   All methods propagate or catch SQLExceptions and print meaningful messages.
 *
 * NOTE — Change DB_USER and DB_PASSWORD to match your MySQL credentials.
 */
public class DatabaseManager {

    // ── JDBC connection parameters ──────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3307/inventory_db";
    private static final String DB_USER = "root";       // ← Change if needed
    private static final String DB_PASS = "admin";   // ← Change to your MySQL password

    // ── Connection helper ───────────────────────────────────────────

    /**
     * Opens and returns a JDBC connection to the inventory_db database.
     *
     * @return Connection object
     * @throws SQLException if the connection fails
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ── SELECT all products ─────────────────────────────────────────

    /**
     * Retrieves every product from the database.
     * DATA STRUCTURE: Results are stored in an ArrayList.
     *
     * @return ArrayList of Product (or ExpiringProduct) objects
     */
    public List<Product> getAllProducts() {
        // ArrayList — data structure requirement
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }

        } catch (SQLException e) {
            // Exception handling: database / SQL errors
            System.err.println("Error fetching all products: " + e.getMessage());
        }
        return products;
    }

    // ── SELECT by ID ────────────────────────────────────────────────

    /**
     * Retrieves a single product by its primary key.
     *
     * @param productId the product ID
     * @return Product object, or null if not found
     */
    public Product getProductById(String productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToProduct(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
        }
        return null;
    }

    // ── SELECT by name (LIKE search) ────────────────────────────────

    /**
     * Searches for products whose name contains the given keyword.
     *
     * @param name search keyword
     * @return list of matching products
     */
    public List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error searching products by name: " + e.getMessage());
        }
        return products;
    }

    // ── INSERT ──────────────────────────────────────────────────────

    /**
     * Inserts a new product into the database.
     *
     * @param product the Product (or ExpiringProduct) to insert
     * @return true if the insert succeeded
     */
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO products (product_id, name, category, price, quantity, is_perishable, expiry_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getCategory());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getQuantity());

            // Polymorphism: check runtime type to decide expiring fields
            if (product instanceof ExpiringProduct) {
                pstmt.setInt(6, 1);
                pstmt.setString(7, ((ExpiringProduct) product).getExpiryDate());
            } else {
                pstmt.setInt(6, 0);
                pstmt.setString(7, null);
            }

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE ──────────────────────────────────────────────────────

    /**
     * Updates an existing product in the database.
     *
     * @param product the Product with updated fields
     * @return true if the update affected at least one row
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, category = ?, price = ?, quantity = ?, "
                   + "is_perishable = ?, expiry_date = ? WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());

            if (product instanceof ExpiringProduct) {
                pstmt.setInt(5, 1);
                pstmt.setString(6, ((ExpiringProduct) product).getExpiryDate());
            } else {
                pstmt.setInt(5, 0);
                pstmt.setString(6, null);
            }

            pstmt.setString(7, product.getProductId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId the product ID to delete
     * @return true if the deletion succeeded
     */
    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    // ── Helper: map a ResultSet row → Product or ExpiringProduct ──

    /**
     * Converts the current ResultSet row into a Product or ExpiringProduct
     * depending on the is_perishable flag.  This is another example of
     * POLYMORPHISM — the returned object's actual type varies at runtime.
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        boolean isPerishable = rs.getInt("is_perishable") == 1;

        if (isPerishable) {
            return new ExpiringProduct(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("expiry_date")
            );
        } else {
            return new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
            );
        }
    }
}
