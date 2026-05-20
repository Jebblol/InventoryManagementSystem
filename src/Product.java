/**
 * Product.java — Base class representing a generic product.
 *
 * OOP Concepts demonstrated:
 *   • ENCAPSULATION – All fields are private; access is through getters/setters.
 *   • POLYMORPHISM  – getTotalValue() can be overridden by subclasses.
 *   • This class serves as the parent for ExpiringProduct (INHERITANCE).
 */
public class Product {

    // ── Private fields (Encapsulation) ──────────────────────────────
    private String productId;
    private String name;
    private String category;
    private double price;
    private int    quantity;

    // ── Constructors ────────────────────────────────────────────────

    /**
     * Default constructor.
     */
    public Product() {
    }

    /**
     * Parameterized constructor.
     *
     * @param productId unique product identifier
     * @param name      product name
     * @param category  product category
     * @param price     unit price
     * @param quantity  stock quantity
     */
    public Product(String productId, String name, String category, double price, int quantity) {
        this.productId = productId;
        this.name      = name;
        this.category  = category;
        this.price     = price;
        this.quantity  = quantity;
    }

    // ── Getters and Setters (Encapsulation) ─────────────────────────

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // ── Business Logic ──────────────────────────────────────────────

    /**
     * Calculates the total inventory value for this product.
     * POLYMORPHISM: This method is overridden in ExpiringProduct to
     * apply a discount when the product is near its expiry date.
     *
     * @return price × quantity
     */
    public double getTotalValue() {
        return price * quantity;
    }

    /**
     * Returns a human-readable representation of the product.
     */
    @Override
    public String toString() {
        return String.format("Product[id=%s, name=%s, category=%s, price=%.2f, qty=%d]",
                productId, name, category, price, quantity);
    }
}
