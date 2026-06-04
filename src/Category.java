/**
 * Category.java — Domain model class representing a product category.
 *
 * OOP Concepts demonstrated:
 *   • ENCAPSULATION – All fields are private; access is through getters/setters.
 *   • This class serves as a standalone domain/entity class for the inventory system.
 */
public class Category {

    // ── Private fields (Encapsulation) ──────────────────────────────
    private String categoryId;
    private String categoryName;

    // ── Constructors ────────────────────────────────────────────────

    /**
     * Default constructor.
     */
    public Category() {
    }

    /**
     * Parameterized constructor.
     *
     * @param categoryId   unique category identifier
     * @param categoryName category name
     */
    public Category(String categoryId, String categoryName) {
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
    }

    // ── Getters and Setters (Encapsulation) ─────────────────────────

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // ── String representation ────────────────────────────────────────

    /**
     * Returns a human-readable representation of the category.
     */
    @Override
    public String toString() {
        return String.format("Category[id=%s, name=%s]", categoryId, categoryName);
    }
}
