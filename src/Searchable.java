import java.util.List;

/**
 * Searchable.java — Interface that defines search operations.
 *
 * OOP Concept demonstrated:
 *   • ABSTRACTION – This interface declares search behaviour without
 *     providing an implementation.  Any class that implements Searchable
 *     must provide its own concrete logic for these methods.
 */
public interface Searchable {

    /**
     * Search for a product by its unique ID.
     *
     * @param productId the product ID to look up
     * @return the matching Product, or null if not found
     */
    Product searchById(String productId);

    /**
     * Search for products whose name contains the given keyword
     * (case-insensitive linear search).
     *
     * @param name the keyword to search for
     * @return list of matching products
     */
    List<Product> searchByName(String name);
}
