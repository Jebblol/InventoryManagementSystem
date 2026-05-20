/**
 * ExpiringProduct.java — Subclass of Product for items with an expiry date.
 *
 * OOP Concepts demonstrated:
 *   • INHERITANCE   – Extends the Product base class.
 *   • POLYMORPHISM  – Overrides getTotalValue() to apply a near-expiry discount.
 *   • ENCAPSULATION – Private expiryDate field with getter/setter.
 */
public class ExpiringProduct extends Product {

    // ── Private field (Encapsulation) ───────────────────────────────
    private String expiryDate; // Format: YYYY-MM-DD

    // ── Constructors ────────────────────────────────────────────────

    /**
     * Default constructor.
     */
    public ExpiringProduct() {
        super();
    }

    /**
     * Parameterized constructor.
     *
     * @param productId  unique identifier
     * @param name       product name
     * @param category   product category
     * @param price      unit price
     * @param quantity   stock quantity
     * @param expiryDate expiry date in YYYY-MM-DD format
     */
    public ExpiringProduct(String productId, String name, String category,
                             double price, int quantity, String expiryDate) {
        // Call parent constructor (Inheritance)
        super(productId, name, category, price, quantity);
        this.expiryDate = expiryDate;
    }

    // ── Getter / Setter ─────────────────────────────────────────────

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    // ── Overridden Method (Polymorphism) ────────────────────────────

    /**
     * Calculates total inventory value with a 20 % discount to simulate
     * near-expiry markdown.  This demonstrates POLYMORPHISM because the
     * same method signature behaves differently than in the parent class.
     *
     * @return discounted total value (price × quantity × 0.80)
     */
    @Override
    public double getTotalValue() {
        // Apply a 20% discount for expiring products (near-expiry markdown)
        return getPrice() * getQuantity() * 0.80;
    }

    /**
     * String representation including expiry information.
     */
    @Override
    public String toString() {
        return String.format(
                "ExpiringProduct[id=%s, name=%s, category=%s, price=%.2f, qty=%d, expiry=%s]",
                getProductId(), getName(), getCategory(), getPrice(), getQuantity(), expiryDate);
    }
}
