import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * InventoryManager.java — Central controller for product inventory operations.
 *
 * OOP Concepts demonstrated:
 *   • ABSTRACTION – Implements the Searchable interface.
 *   • ENCAPSULATION – Private data structures with controlled access.
 *
 * Data Structures used:
 *   • ArrayList — stores the master list of products.
 *   • HashMap   — maps product ID → Product for O(1) lookups.
 *
 * Algorithms:
 *   • Linear search (searchByName iterates through the ArrayList).
 *   • Sorting via Collections.sort() with ProductComparator.
 */
public class InventoryManager implements Searchable {

    // ── Data structures (ArrayList + HashMap) ───────────────────────
    private ArrayList<Product> productList;         // ArrayList for ordered storage
    private HashMap<String, Product> productMap;    // HashMap for quick ID lookup
    private DatabaseManager dbManager;              // Handles database operations

    // ── Constructor ─────────────────────────────────────────────────

    /**
     * Initializes the data structures and loads products from the database.
     */
    public InventoryManager() {
        productList = new ArrayList<>();
        productMap  = new HashMap<>();
        dbManager   = new DatabaseManager();
    }

    // ── Load from database ──────────────────────────────────────────

    /**
     * Loads all products from the database into the ArrayList and HashMap.
     * Called when the application starts.
     */
    public void loadFromDatabase() {
        productList.clear();
        productMap.clear();

        List<Product> dbProducts = dbManager.getAllProducts();

        for (Product p : dbProducts) {
            // Add to ArrayList (data structure requirement)
            productList.add(p);
            // Add to HashMap for fast ID lookup (data structure requirement)
            productMap.put(p.getProductId(), p);
        }
    }

    // ── Add product ─────────────────────────────────────────────────

    /**
     * Adds a new product to the database and in-memory collections.
     *
     * @param product the product to add
     * @return true if the operation succeeded
     */
    public boolean addProduct(Product product) {
        if (dbManager.insertProduct(product)) {
            productList.add(product);                        // ArrayList add
            productMap.put(product.getProductId(), product);  // HashMap put
            return true;
        }
        return false;
    }

    // ── Update product ──────────────────────────────────────────────

    /**
     * Updates an existing product in the database and in-memory collections.
     *
     * @param product the product with updated fields
     * @return true if the operation succeeded
     */
    public boolean updateProduct(Product product) {
        if (dbManager.updateProduct(product)) {
            // Update HashMap
            productMap.put(product.getProductId(), product);

            // Update ArrayList — remove old entry, add updated one
            productList.removeIf(p -> p.getProductId().equals(product.getProductId()));
            productList.add(product);
            return true;
        }
        return false;
    }

    // ── Delete product ──────────────────────────────────────────────

    /**
     * Removes a product from the database and in-memory collections.
     *
     * @param productId the ID of the product to remove
     * @return true if the operation succeeded
     */
    public boolean deleteProduct(String productId) {
        if (dbManager.deleteProduct(productId)) {
            productMap.remove(productId);                              // HashMap remove
            productList.removeIf(p -> p.getProductId().equals(productId)); // ArrayList remove
            return true;
        }
        return false;
    }

    // ── Search by ID (HashMap lookup) ───────────────────────────────

    /**
     * Searches for a product by ID using the HashMap for O(1) average lookup.
     * Implements Searchable interface (Abstraction).
     *
     * @param productId the product ID
     * @return matching Product or null
     */
    @Override
    public Product searchById(String productId) {
        // HashMap lookup — fast search by key
        return productMap.get(productId);
    }

    // ── Search by name (Linear Search algorithm) ────────────────────

    /**
     * Performs a LINEAR SEARCH through the ArrayList to find products
     * whose name contains the given keyword (case-insensitive).
     * Implements Searchable interface (Abstraction).
     *
     * ALGORITHM: Linear search — iterates through every element in the list.
     *
     * @param name the search keyword
     * @return list of matching products
     */
    @Override
    public List<Product> searchByName(String name) {
        List<Product> results = new ArrayList<>();

        // Linear search algorithm through the ArrayList
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    // ── Sort by name ────────────────────────────────────────────────

    /**
     * Sorts the product list alphabetically by name using Collections.sort()
     * with a Comparator from ProductComparator.
     */
    public void sortByName() {
        // Sorting algorithm using Comparator (Collections.sort)
        Collections.sort(productList, ProductComparator.byName());
    }

    // ── Sort by price ───────────────────────────────────────────────

    /**
     * Sorts the product list by price (ascending) using Collections.sort()
     * with a Comparator from ProductComparator.
     */
    public void sortByPrice() {
        // Sorting algorithm using Comparator (Collections.sort)
        Collections.sort(productList, ProductComparator.byPrice());
    }

    // ── Getters ─────────────────────────────────────────────────────

    /**
     * Returns the current product list (ArrayList).
     *
     * @return ArrayList of products
     */
    public ArrayList<Product> getProductList() {
        return productList;
    }

    /**
     * Returns the product map (HashMap).
     *
     * @return HashMap of productId → Product
     */
    public HashMap<String, Product> getProductMap() {
        return productMap;
    }

    /**
     * Saves current products to a CSV file for the file I/O requirement.
     */
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("product_id,name,category,price,quantity,is_perishable,expiry_date");
            writer.newLine();
            for (Product product : productList) {
                boolean expiring = product instanceof ExpiringProduct;
                String expiryDate = expiring ? ((ExpiringProduct) product).getExpiryDate() : "";
                writer.write(csv(product.getProductId()) + ","
                        + csv(product.getName()) + ","
                        + csv(product.getCategory()) + ","
                        + product.getPrice() + ","
                        + product.getQuantity() + ","
                        + expiring + ","
                        + csv(expiryDate));
                writer.newLine();
            }
        }
    }

    /**
     * Loads products from a CSV backup and inserts/updates them in the database.
     */
    public void loadFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 7) {
                    continue;
                }
                Product product;
                if (Boolean.parseBoolean(parts[5])) {
                    product = new ExpiringProduct(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[6]);
                } else {
                    product = new Product(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
                }

                if (searchById(product.getProductId()) == null) {
                    addProduct(product);
                } else {
                    updateProduct(product);
                }
            }
        }
        loadFromDatabase();
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ");
    }
}
