import java.util.Comparator;

/**
 * ProductComparator.java — Provides Comparator implementations for sorting.
 *
 * ALGORITHM demonstrated:
 *   • Sorting via Comparator passed to Collections.sort().
 *
 * Two static factory methods return ready-to-use comparators:
 *   1. byName()  – alphabetical (case-insensitive)
 *   2. byPrice() – ascending numeric order
 */
public class ProductComparator {

    /**
     * Returns a Comparator that sorts products by name (A-Z, case-insensitive).
     *
     * @return Comparator for sorting by product name
     */
    public static Comparator<Product> byName() {
        return new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        };
    }

    /**
     * Returns a Comparator that sorts products by price (low → high).
     *
     * @return Comparator for sorting by product price
     */
    public static Comparator<Product> byPrice() {
        return new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        };
    }
}
