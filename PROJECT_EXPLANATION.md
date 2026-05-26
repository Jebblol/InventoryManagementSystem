# Inventory Management System
## Project Explanation Document

---

### 1. PROJECT OVERVIEW

The Inventory Management System is a desktop application designed to help small store owners manage their product inventory efficiently. It allows users to add, update, delete, search, and sort products through a user-friendly graphical interface. All product data is stored permanently in a MySQL database, ensuring that information is not lost when the application closes.

**Why this system is useful:**

- Helps store owners track stock levels to avoid overstocking or running out of products
- Provides quick search functionality to find products instantly
- Allows sorting by name or price for better organization
- Calculates total inventory value automatically
- Stores data securely in a database for long-term use

**Technologies used:**

- **Java** - The programming language used to build the entire application
- **Swing GUI** - Java's built-in library for creating graphical user interfaces with windows, buttons, and tables
- **MySQL** - A relational database management system that stores all product data
- **JDBC** - Java Database Connectivity, a standard API that allows Java programs to connect to databases
- **ArrayList** - A dynamic array data structure that stores products in an ordered list
- **HashMap** - A key-value data structure that enables fast product lookup by ID

---

### 2. SYSTEM FEATURES

The system implements the following features:

**Add Products**
- Users can enter product details (ID, name, category, price, quantity) and add them to the database
- For perishable items like food, users can specify an expiry date
- The system prevents duplicate product IDs

**Update Products**
- Users can modify existing product information
- Changes are saved to both the database and the in-memory data structures

**Delete Products**
- Users can remove products from the system by entering the product ID
- A confirmation dialog prevents accidental deletions

**Search Products**
- Search by product ID for instant lookup using HashMap (very fast)
- Search by product name using linear search through the ArrayList
- Search is case-insensitive for convenience

**Sort Products**
- Sort products alphabetically by name
- Sort products by price from lowest to highest
- Sorting uses Java's Collections.sort() with custom comparators

**Display Products**
- All products are displayed in a table format (JTable)
- The table shows product ID, name, category, price, quantity, expiry status, expiry date, and total value
- Users can click on a table row to populate the input fields for editing

**Database Storage**
- All product data is stored in a MySQL database named `inventory_db`
- Data persists even after closing the application
- The system uses prepared statements for safe SQL operations

---

### 3. CLASS EXPLANATIONS

#### Product Class

**Purpose:** The Product class is the base class that represents a generic product in the inventory. It contains all the basic information that every product needs.

**Main attributes:**
- `productId` (String) - A unique identifier for the product
- `name` (String) - The product name
- `category` (String) - The category the product belongs to (e.g., Electronics, Food)
- `price` (double) - The unit price of the product
- `quantity` (int) - The number of items in stock

**Main methods:**
- Getters and setters for all attributes (e.g., `getName()`, `setPrice()`)
- `getTotalValue()` - Calculates the total value of this product (price × quantity)
- `toString()` - Returns a readable string representation of the product

**Why this class is important:** This class serves as the foundation for the entire system. All products, whether regular or perishable, are based on this class. It demonstrates encapsulation by keeping all fields private and providing controlled access through getters and setters.

**How it interacts with other classes:** The Product class is used by InventoryManager to store products, by DatabaseManager to save and retrieve data, and by MainGUI to display product information in the table.

---

#### ExpiringProduct Class

**Purpose:** The ExpiringProduct class represents products that have an expiry date, such as food items or medicines. It extends the Product class to add expiry-specific functionality.

**Main attributes:**
- `expiryDate` (String) - The date when the product expires (format: YYYY-MM-DD)
- Inherits all attributes from Product (productId, name, category, price, quantity)

**Main methods:**
- `getExpiryDate()` and `setExpiryDate()` - Accessor methods for the expiry date
- `getTotalValue()` - Overrides the parent method to apply a 20% discount (simulating near-expiry markdown)
- `toString()` - Overrides to include expiry information

**Why this class is important:** This class demonstrates inheritance by reusing all the code from Product while adding new functionality specific to perishable items. It also demonstrates polymorphism through the overridden `getTotalValue()` method.

**How it interacts with other classes:** ExpiringProduct is used wherever Product is used, but with special behavior. The DatabaseManager checks if a product is an ExpiringProduct to save the expiry date, and MainGUI displays the expiry information in the table.

---

#### InventoryManager Class

**Purpose:** The InventoryManager is the central controller that manages all inventory operations. It acts as the bridge between the GUI and the database.

**Main attributes:**
- `productList` (ArrayList<Product>) - Stores all products in an ordered list
- `productMap` (HashMap<String, Product>) - Maps product IDs to Product objects for fast lookup
- `dbManager` (DatabaseManager) - Handles all database operations

**Main methods:**
- `loadFromDatabase()` - Loads all products from the database into memory
- `addProduct(Product)` - Adds a new product to the database and memory
- `updateProduct(Product)` - Updates an existing product
- `deleteProduct(String)` - Removes a product by ID
- `searchById(String)` - Searches for a product by ID using HashMap
- `searchByName(String)` - Searches for products by name using linear search
- `sortByName()` - Sorts products alphabetically
- `sortByPrice()` - Sorts products by price
- `getProductList()` and `getProductMap()` - Returns the data structures

**Why this class is important:** This class implements the Searchable interface and uses both ArrayList and HashMap data structures. It demonstrates how to choose the right data structure for different operations - HashMap for fast ID lookup, ArrayList for ordered storage and iteration.

**How it interacts with other classes:** InventoryManager is called by MainGUI to perform all operations. It delegates database work to DatabaseManager and uses ProductComparator for sorting. It implements the Searchable interface to provide search functionality.

---

#### DatabaseManager Class

**Purpose:** The DatabaseManager handles all direct communication with the MySQL database. It contains all the SQL code needed to store and retrieve product data.

**Main attributes:**
- `DB_URL` (String) - The JDBC connection string (e.g., `jdbc:mysql://localhost:3307/inventory_db`)
- `DB_USER` (String) - MySQL username
- `DB_PASS` (String) - MySQL password

**Main methods:**
- `getConnection()` - Opens a connection to the MySQL database
- `getAllProducts()` - Retrieves all products from the database using SELECT
- `getProductById(String)` - Retrieves a single product by ID
- `getProductsByName(String)` - Searches for products by name using SQL LIKE
- `insertProduct(Product)` - Adds a new product using INSERT
- `updateProduct(Product)` - Updates an existing product using UPDATE
- `deleteProduct(String)` - Removes a product using DELETE
- `mapRowToProduct(ResultSet)` - Converts database rows into Product or ExpiringProduct objects

**Why this class is important:** This class encapsulates all database logic, making the rest of the application independent of database details. It uses PreparedStatement to prevent SQL injection attacks and demonstrates proper exception handling for database errors.

**How it interacts with other classes:** DatabaseManager is used exclusively by InventoryManager. It converts database results into Product or ExpiringProduct objects using polymorphism - checking the `is_perishable` flag to decide which type to create.

---

#### ProductComparator Class

**Purpose:** The ProductComparator class provides custom sorting logic for products. It implements the Comparator interface to define how products should be compared.

**Main methods:**
- `byName()` - Returns a Comparator that sorts products alphabetically by name (case-insensitive)
- `byPrice()` - Returns a Comparator that sorts products by price from lowest to highest

**Why this class is important:** This class demonstrates how to use the Comparator interface for custom sorting. It allows flexible sorting without modifying the Product class itself. The sorting is passed to `Collections.sort()` which uses an efficient sorting algorithm internally.

**How it interacts with other classes:** InventoryManager uses ProductComparator to sort the product list. The comparators are passed to `Collections.sort()` along with the ArrayList to be sorted.

---

#### MainGUI Class

**Purpose:** The MainGUI class creates and manages the graphical user interface. It provides all the visual components that users interact with.

**Main attributes:**
- `inventoryManager` (InventoryManager) - The controller that handles business logic
- Various JTextFields for input (txtProductId, txtName, txtCategory, etc.)
- `productTable` (JTable) - Displays products in a table format
- `tableModel` (DefaultTableModel) - Manages the table data
- Various JButtons for actions (btnAdd, btnUpdate, btnDelete, etc.)

**Main methods:**
- `createInputPanel()` - Creates the panel with input fields
- `createTablePanel()` - Creates the panel with the product table
- `createButtonPanel()` - Creates the panel with action buttons
- `addProduct()` - Handles adding a new product
- `updateProduct()` - Handles updating an existing product
- `deleteProduct()` - Handles deleting a product
- `searchProduct()` - Handles searching by ID or name
- `sortByName()` and `sortByPrice()` - Handle sorting operations
- `validateFields()` - Checks that all required fields are filled
- `createProductFromFields()` - Creates a Product or ExpiringProduct from input fields
- `refreshTable(List<Product>)` - Updates the table display

**Why this class is important:** This class makes the system user-friendly by providing a visual interface instead of command-line input. It demonstrates event-driven programming through action listeners and proper exception handling for user input errors.

**How it interacts with other classes:** MainGUI creates an InventoryManager instance and calls its methods for all operations. It displays Product and ExpiringProduct objects in the table and uses JOptionPane for error messages and confirmations.

---

#### MainApp Class

**Purpose:** The MainApp class is the entry point of the application. It contains the main method that starts the program.

**Main methods:**
- `main(String[] args)` - The starting point of the program

**Why this class is important:** This class initializes the application and launches the GUI on the Event Dispatch Thread (EDT), which is the correct way to start Swing applications to ensure thread safety.

**How it interacts with other classes:** MainApp simply creates an instance of MainGUI, which then initializes the rest of the system including InventoryManager and DatabaseManager.

---

#### Searchable Interface

**Purpose:** The Searchable interface defines the contract for search operations. Any class that implements this interface must provide methods for searching by ID and by name.

**Methods:**
- `searchById(String productId)` - Returns a Product matching the given ID
- `searchByName(String name)` - Returns a list of Products whose names contain the given keyword

**Why this interface is important:** This demonstrates abstraction - it defines what search operations should look like without specifying how they should be implemented. InventoryManager implements this interface, providing concrete search logic using HashMap and ArrayList.

**How it interacts with other classes:** InventoryManager implements Searchable, which means it must provide implementations for both search methods. This makes the code more flexible and easier to maintain.

---

### 4. OBJECT-ORIENTED PROGRAMMING CONCEPTS

#### ENCAPSULATION

Encapsulation is the practice of keeping data private and providing controlled access through public methods. In this project, encapsulation is used throughout:

**Use of private fields:**
- All fields in the Product class are private (productId, name, category, price, quantity)
- The ExpiringProduct class has a private expiryDate field
- The InventoryManager class has private data structures (productList, productMap)

**Use of getters and setters:**
- Each private field has a public getter method (e.g., `getName()`, `getPrice()`)
- Each private field has a public setter method (e.g., `setName()`, `setPrice()`)
- This allows controlled access to the data - the class can validate input before setting values

**Why encapsulation protects data:**
- Prevents external code from directly modifying internal state
- Allows the class to enforce rules (e.g., preventing negative prices)
- Makes it easier to change the internal implementation without affecting other code
- Provides a clear public API for interacting with the object

For example, in the Product class, the price field is private. If we wanted to add validation to prevent negative prices, we could modify the `setPrice()` method without changing any code that uses Product objects.

---

#### INHERITANCE

Inheritance allows a new class to acquire the properties and methods of an existing class. This reduces code duplication and creates a logical hierarchy.

**ExpiringProduct extends Product:**
- ExpiringProduct inherits all fields and methods from Product
- It adds one new field: expiryDate
- It adds getter and setter methods for expiryDate
- It overrides the `getTotalValue()` method to apply a discount

**Why inheritance reduces code duplication:**
- ExpiringProduct doesn't need to redefine productId, name, category, price, or quantity
- It doesn't need to rewrite all the getters and setters for these inherited fields
- It can reuse the constructor logic by calling `super()`
- Any changes to Product automatically benefit ExpiringProduct

For example, if we add a new field to Product (like a description), ExpiringProduct automatically gets it without any additional code. This makes the system easier to maintain and extend.

---

#### POLYMORPHISM

Polymorphism allows objects of different types to be treated as objects of a common type. The same method call can behave differently depending on the actual object type.

**Method overriding:**
- ExpiringProduct overrides the `getTotalValue()` method from Product
- Product's version returns: `price × quantity`
- ExpiringProduct's version returns: `price × quantity × 0.80` (20% discount)

**Example using getTotalValue():**
```java
Product p1 = new Product("P001", "Laptop", "Electronics", 999.99, 10);
Product p2 = new ExpiringProduct("P003", "Milk", "Dairy", 3.49, 100, "2026-04-20");

double value1 = p1.getTotalValue(); // Returns 9999.90
double value2 = p2.getTotalValue(); // Returns 279.20 (with 20% discount)
```

Even though both variables are declared as type Product, the actual behavior depends on whether the object is a Product or an ExpiringProduct. This is polymorphism in action.

**Different behavior between Product and ExpiringProduct:**
- When MainGUI calls `getTotalValue()` on each product in the table, it gets different results for regular products vs. perishable products
- This allows the system to handle both types uniformly in the code while providing specialized behavior where needed

---

#### ABSTRACTION

Abstraction hides complex implementation details and shows only the necessary features of an object. Interfaces are a key way to achieve abstraction in Java.

**The Searchable interface:**
- Declares two methods: `searchById()` and `searchByName()`
- Does not provide any implementation - just the method signatures
- Any class that implements Searchable must provide concrete implementations

**Why interfaces are useful:**
- They define a contract that implementing classes must follow
- They allow for loose coupling - code can depend on the interface rather than a specific class
- They make it easy to add new implementations without changing existing code
- They improve code organization and readability

**How abstraction hides implementation details:**
- When MainGUI calls `inventoryManager.searchById()`, it doesn't need to know whether the search uses a HashMap, a database query, or some other method
- The implementation can change completely without affecting the calling code
- This makes the system more flexible and easier to maintain

For example, if we wanted to change the search implementation to use a binary search tree instead of a HashMap, we would only need to modify InventoryManager - MainGUI wouldn't need any changes.

---

### 5. DATA STRUCTURES

#### Why ArrayList is used

The ArrayList is used in the InventoryManager class to store the master list of all products.

**Characteristics of ArrayList:**
- Dynamic array that grows automatically as elements are added
- Maintains insertion order - products stay in the order they were added
- Allows duplicates
- Provides indexed access - can get the 5th product directly
- Iteration is fast and straightforward

**Advantages for this project:**
- Perfect for displaying products in a table in a consistent order
- Easy to iterate through when searching by name or displaying all products
- Works well with Collections.sort() for sorting operations
- Simple to use and understand

**How it's used:**
```java
private ArrayList<Product> productList;

// Adding a product
productList.add(product);

// Iterating through all products
for (Product p : productList) {
    System.out.println(p.getName());
}

// Sorting
Collections.sort(productList, ProductComparator.byName());
```

---

#### Why HashMap is used

The HashMap is used in the InventoryManager class to map product IDs to Product objects for fast lookup.

**Characteristics of HashMap:**
- Stores key-value pairs (product ID → Product object)
- Does not maintain any specific order
- Does not allow duplicate keys
- Provides very fast lookup by key - average O(1) time complexity
- Each key must be unique

**Advantages for this project:**
- Instant lookup by product ID - much faster than searching through an ArrayList
- Perfect for the `searchById()` operation
- Efficient for checking if a product ID already exists
- Reduces the time complexity of ID search from O(n) to O(1)

**How it's used:**
```java
private HashMap<String, Product> productMap;

// Adding a product
productMap.put(product.getProductId(), product);

// Searching by ID (very fast)
Product found = productMap.get("P001");

// Removing a product
productMap.remove("P001");
```

**Performance comparison:**
- Searching an ArrayList with 1000 products by ID: up to 1000 comparisons
- Searching a HashMap with 1000 products by ID: 1 lookup on average

This dramatic performance improvement is why both data structures are used together - HashMap for fast ID lookup, ArrayList for ordered display and sorting.

---

### 6. SEARCHING AND SORTING ALGORITHMS

#### Linear Search

Linear search is the simplest searching algorithm. It checks each element in a list one by one until the target is found or the end of the list is reached.

**How it works in this project:**
- Used in the `searchByName()` method of InventoryManager
- Iterates through the ArrayList of products
- Checks if each product's name contains the search keyword (case-insensitive)
- Collects all matching products into a result list

**Example:**
```java
public List<Product> searchByName(String name) {
    List<Product> results = new ArrayList<>();
    
    // Linear search through the ArrayList
    for (Product product : productList) {
        if (product.getName().toLowerCase().contains(name.toLowerCase())) {
            results.add(product);
        }
    }
    return results;
}
```

**Time complexity:** O(n) - where n is the number of products. In the worst case, it checks every product.

**Why linear search is used here:**
- Simple to implement and understand
- Works well for partial matches (searching for "mouse" finds "Wireless Mouse")
- Sufficient for small to medium-sized inventories
- The ArrayList doesn't support faster search by name (unlike ID search with HashMap)

---

#### Comparator Sorting

A Comparator is an object that defines a custom ordering for objects. It provides a `compare()` method that determines which of two objects should come first.

**How it works in this project:**
- ProductComparator class provides two static methods that return Comparator objects
- `byName()` returns a Comparator that compares products by name alphabetically
- `byPrice()` returns a Comparator that compares products by price numerically
- These comparators are passed to `Collections.sort()`

**Example:**
```java
public static Comparator<Product> byName() {
    return new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
    };
}
```

The `compare()` method returns:
- A negative number if p1 should come before p2
- Zero if they are equal
- A positive number if p1 should come after p2

---

#### Collections.sort()

`Collections.sort()` is a built-in Java method that sorts a List using an efficient sorting algorithm (typically a modified merge sort called Timsort).

**How it's used in this project:**
```java
// Sort by name
Collections.sort(productList, ProductComparator.byName());

// Sort by price
Collections.sort(productList, ProductComparator.byPrice());
```

**Time complexity:** O(n log n) - much faster than simple algorithms like bubble sort.

**Why sorting is useful in inventory systems:**
- Helps users find products quickly when browsing
- Allows price-based analysis (cheapest to most expensive)
- Makes reports and summaries more readable
- Essential for generating organized catalogs

---

### 7. JDBC AND DATABASE CONNECTION

#### What JDBC is

JDBC (Java Database Connectivity) is a standard Java API that allows Java programs to connect to and interact with databases. It provides a uniform interface for working with different database systems (MySQL, Oracle, PostgreSQL, etc.).

**Why JDBC is needed:**
- Java cannot directly communicate with databases without a driver
- JDBC provides the bridge between Java code and SQL databases
- It handles connection management, query execution, and result processing
- It's part of the Java standard library, making it widely available

---

#### How Java connects to MySQL

The connection process involves several steps:

1. **Load the JDBC driver** - The MySQL Connector/J driver is loaded (automatically in modern JDBC)
2. **Establish a connection** - Using `DriverManager.getConnection()` with connection parameters
3. **Execute SQL queries** - Using Statement or PreparedStatement objects
4. **Process results** - Reading data from ResultSet objects
5. **Close resources** - Properly closing connections, statements, and result sets

**Connection code in DatabaseManager:**
```java
public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
}
```

---

#### The role of DatabaseManager

The DatabaseManager class encapsulates all database operations. It serves as the data access layer, separating database logic from business logic.

**Responsibilities:**
- Opening and closing database connections
- Executing SQL queries (SELECT, INSERT, UPDATE, DELETE)
- Converting database rows into Java objects
- Handling database exceptions
- Managing prepared statements for security

**Why this separation is important:**
- Makes the code easier to maintain - database changes only affect this class
- Improves testability - can mock DatabaseManager for testing
- Follows the single responsibility principle
- Makes the application more flexible (could switch databases with minimal changes)

---

#### SQL operations used

**INSERT - Adding new products:**
```java
String sql = "INSERT INTO products (product_id, name, category, price, quantity, is_perishable, expiry_date) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?)";
```
Adds a new row to the products table with the provided values.

**UPDATE - Modifying existing products:**
```java
String sql = "UPDATE products SET name = ?, category = ?, price = ?, quantity = ?, "
           + "is_perishable = ?, expiry_date = ? WHERE product_id = ?";
```
Updates an existing row identified by the product_id.

**DELETE - Removing products:**
```java
String sql = "DELETE FROM products WHERE product_id = ?";
```
Removes a row from the products table.

**SELECT - Retrieving products:**
```java
String sql = "SELECT * FROM products";
String sql = "SELECT * FROM products WHERE product_id = ?";
String sql = "SELECT * FROM products WHERE name LIKE ?";
```
Retrieves data from the products table, optionally filtered by conditions.

---

#### Understanding the JDBC connection string

The connection string used in this project is:
```
jdbc:mysql://localhost:3307/inventory_db
```

**Breakdown:**
- `jdbc:` - The protocol identifier, telling Java this is a JDBC connection
- `mysql:` - The sub-protocol, specifying MySQL as the database type
- `localhost` - The hostname where the database server is running (local machine)
- `3307` - The port number where MySQL is listening (default is 3306, but this uses 3307)
- `inventory_db` - The name of the specific database to connect to

**Why these components matter:**
- The protocol tells Java which driver to use
- The hostname could be an IP address for remote databases
- The port must match where MySQL is actually running
- The database name specifies which database to use within the MySQL server

---

#### PreparedStatement and security

PreparedStatement is a precompiled SQL statement that can be executed multiple times with different parameters. It's safer than regular Statement because it prevents SQL injection attacks.

**Why PreparedStatement is safer:**
- User input is treated as data, not as executable SQL code
- The database automatically escapes special characters
- Attackers cannot inject malicious SQL through input fields

**Example of PreparedStatement usage:**
```java
String sql = "SELECT * FROM products WHERE product_id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, productId);  // Safely sets the parameter
ResultSet rs = pstmt.executeQuery();
```

**SQL injection prevention:**
- Without PreparedStatement, a user could enter: `P001' OR '1'='1` as a product ID
- This could manipulate the SQL query to return all products
- With PreparedStatement, the input is treated as a literal string, preventing this attack

---

### 8. GRAPHICAL USER INTERFACE (GUI)

#### GUI Components Used

**JFrame**
- The main application window
- Provides the title bar, close button, and window controls
- All other components are added to the JFrame
- In this project, MainGUI extends JFrame

**JPanel**
- A container that groups related components
- Used to organize the layout (input panel, table panel, button panel)
- Uses layout managers like GridBagLayout and BorderLayout for positioning
- Makes the interface organized and visually appealing

**JTable**
- Displays data in a tabular format with rows and columns
- Shows product information in a structured way
- Supports selection, scrolling, and column headers
- Uses DefaultTableModel to manage the data

**JTextField**
- Text input fields where users type data
- Used for product ID, name, category, price, quantity, expiry date, and search
- Allows users to enter and edit product information
- Can have tooltips for guidance (e.g., expiry date format)

**JButton**
- Clickable buttons that trigger actions
- Used for Add, Update, Delete, Search, Sort, Clear, and Refresh operations
- Each button has an ActionListener that defines what happens when clicked
- Provides clear visual cues for available actions

**JScrollPane**
- A scrollable container that wraps the JTable
- Allows users to scroll through many products
- Automatically shows scrollbars when needed
- Improves usability when there are many records

**JOptionPane**
- Dialog boxes for messages and confirmations
- Used for error messages, success messages, and confirmation dialogs
- Provides a standard way to communicate with the user
- Examples: "Product added successfully!", "Are you sure you want to delete?"

**JCheckBox**
- A checkbox that can be checked or unchecked
- Used to indicate whether a product has an expiry date
- Toggles the visibility of the expiry date field
- Provides a simple yes/no choice

---

#### How the GUI improves usability

**Visual feedback:**
- Users can see all products at a glance in the table
- Color-coded messages (errors in red, warnings in yellow)
- Clear button labels indicate what each action does

**Ease of input:**
- Labeled fields guide users on what to enter
- Tooltips provide additional guidance (e.g., date format)
- Validation prevents invalid data from being submitted

**Efficiency:**
- Click on a table row to populate fields for editing
- Search functionality finds products instantly
- Sort buttons organize data with one click

**Error prevention:**
- Confirmation dialogs prevent accidental deletions
- Input validation catches errors before database operations
- Clear error messages explain what went wrong

**Consistency:**
- Standard Swing components behave as users expect
- Familiar window controls (close, minimize, maximize)
- Consistent layout and design throughout

---

### 9. EXCEPTION HANDLING

#### Types of Exceptions Handled

**NumberFormatException**
- Occurs when trying to convert a string to a number but the string is not a valid number
- Example: User enters "abc" in the Price field
- Handled in MainGUI when parsing price and quantity:
```java
try {
    double price = Double.parseDouble(txtPrice.getText().trim());
    int quantity = Integer.parseInt(txtQuantity.getText().trim());
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Invalid numeric input...");
}
```

**SQLException**
- Occurs when there's an error with database operations
- Examples: Connection failed, SQL syntax error, constraint violation
- Handled in DatabaseManager for all database operations:
```java
try {
    // Database operation
} catch (SQLException e) {
    System.err.println("Error: " + e.getMessage());
}
```

**Invalid input handling**
- Occurs when required fields are empty or invalid
- Example: Trying to add a product without a name
- Handled by the `validateFields()` method in MainGUI:
```java
if (txtProductId.getText().trim().isEmpty() || ...) {
    JOptionPane.showMessageDialog(this, "Please fill in all required fields...");
    return false;
}
```

---

#### Error dialogs using JOptionPane

JOptionPane is used to display error messages to the user in a user-friendly way:

**Error messages:**
```java
JOptionPane.showMessageDialog(this, 
    "Failed to add product to the database.",
    "Error", JOptionPane.ERROR_MESSAGE);
```

**Warning messages:**
```java
JOptionPane.showMessageDialog(this,
    "Product ID already exists!",
    "Duplicate ID", JOptionPane.WARNING_MESSAGE);
```

**Confirmation dialogs:**
```java
int confirm = JOptionPane.showConfirmDialog(this,
    "Are you sure you want to delete product " + productId + "?",
    "Confirm Delete", JOptionPane.YES_NO_OPTION);
```

---

#### Why exception handling is important

**Prevents application crashes:**
- Without exception handling, errors would cause the program to terminate
- Users would lose unsaved work
- The application would appear unreliable

**Provides helpful feedback:**
- Users understand what went wrong
- Clear messages guide users to correct their input
- Improves the user experience

**Maintains data integrity:**
- Prevents invalid data from being saved to the database
- Catches database errors before they cause corruption
- Ensures operations complete successfully or fail gracefully

**Improves debugging:**
- Error messages help developers identify issues
- Stack traces show where errors occurred
- Makes maintenance easier

**Professional quality:**
- Robust applications handle errors gracefully
- Users expect applications to be stable
- Exception handling is a mark of quality software

---

### 10. SYSTEM WORKFLOW

The system follows a clear flow from user interaction to database storage:

**User → GUI → InventoryManager → DatabaseManager → MySQL Database**

**Detailed workflow for adding a product:**

1. **User enters data** - User types product information into the text fields in MainGUI
2. **User clicks "Add Product"** - The button's ActionListener is triggered
3. **GUI validates input** - MainGUI calls `validateFields()` to check all required fields
4. **GUI creates Product object** - MainGUI calls `createProductFromFields()` to create a Product or ExpiringProduct
5. **GUI checks for duplicates** - MainGUI calls `inventoryManager.searchById()` to ensure the ID doesn't exist
6. **GUI calls InventoryManager** - MainGUI calls `inventoryManager.addProduct(product)`
7. **InventoryManager calls DatabaseManager** - InventoryManager calls `dbManager.insertProduct(product)`
8. **DatabaseManager executes SQL** - DatabaseManager creates a PreparedStatement and executes the INSERT statement
9. **MySQL stores the data** - The database saves the product in the products table
10. **DatabaseManager returns success** - DatabaseManager returns true if the insert succeeded
11. **InventoryManager updates memory** - InventoryManager adds the product to both ArrayList and HashMap
12. **InventoryManager returns success** - InventoryManager returns true to MainGUI
13. **GUI refreshes table** - MainGUI calls `refreshTable()` to update the display
14. **GUI shows success message** - MainGUI displays a JOptionPane success dialog
15. **GUI clears fields** - MainGUI calls `clearFields()` to prepare for the next entry

**Similar workflows exist for:**
- **Update:** User enters modified data → GUI validates → InventoryManager updates → DatabaseManager executes UPDATE → MySQL saves changes
- **Delete:** User enters ID → GUI confirms → InventoryManager deletes → DatabaseManager executes DELETE → MySQL removes row
- **Search:** User enters keyword → GUI searches → InventoryManager uses HashMap/ArrayList → DatabaseManager queries (optional) → Results displayed
- **Sort:** User clicks sort button → GUI calls InventoryManager → InventoryManager sorts using Comparator → GUI refreshes table

This layered architecture ensures separation of concerns and makes the system maintainable and testable.

---

### 11. ASSIGNMENT REQUIREMENTS CHECKLIST

| Requirement | How the project satisfies it |
|-------------|------------------------------|
| **Object-Oriented Programming** | The entire system is built using OOP principles with 8 classes demonstrating proper OOP design |
| **Encapsulation** | All fields in Product and ExpiringProduct are private with getters/setters. InventoryManager and DatabaseManager also use private fields |
| **Inheritance** | ExpiringProduct extends Product, inheriting all fields and methods while adding expiry-specific functionality |
| **Polymorphism** | ExpiringProduct overrides `getTotalValue()` to apply a discount. The same method call behaves differently based on object type |
| **Abstraction** | Searchable interface defines search behavior without implementation. InventoryManager implements this interface |
| **5+ Classes** | The project has 8 classes: Product, ExpiringProduct, InventoryManager, DatabaseManager, ProductComparator, MainGUI, MainApp, Searchable (interface) |
| **Data Structures - ArrayList** | InventoryManager uses ArrayList<Product> to store products in ordered list for display and iteration |
| **Data Structures - HashMap** | InventoryManager uses HashMap<String, Product> for O(1) lookup by product ID |
| **Searching Algorithm** | Linear search is implemented in `searchByName()` which iterates through the ArrayList |
| **Sorting Algorithm** | Sorting is implemented using Collections.sort() with custom Comparators from ProductComparator |
| **GUI - JFrame** | MainGUI extends JFrame to create the main application window |
| **GUI - JPanel** | Multiple JPanels organize the layout (input panel, table panel, button panel) |
| **GUI - JTextField** | JTextFields are used for all user input (ID, name, category, price, quantity, expiry date, search) |
| **GUI - JButton** | JButtons trigger all actions (Add, Update, Delete, Search, Sort, Clear, Refresh) |
| **GUI - JTable** | JTable displays all products in a tabular format with columns for each attribute |
| **GUI - JScrollPane** - JScrollPane wraps the JTable to enable scrolling when there are many products |
| **GUI - JOptionPane** | JOptionPane displays error messages, success messages, and confirmation dialogs |
| **JDBC Connectivity** | DatabaseManager uses JDBC to connect to MySQL with DriverManager.getConnection() |
| **Database - MySQL** | The system uses MySQL as the database backend with the inventory_db database |
| **Database - INSERT** | DatabaseManager.insertProduct() uses PreparedStatement to add new products |
| **Database - UPDATE** | DatabaseManager.updateProduct() uses PreparedStatement to modify existing products |
| **Database - DELETE** | DatabaseManager.deleteProduct() uses PreparedStatement to remove products |
| **Database - SELECT** | DatabaseManager.getAllProducts(), getProductById(), and getProductsByName() use SELECT queries |
| **Exception Handling** | NumberFormatException, SQLException, and invalid input are caught and handled with appropriate error messages |

---

### 12. CONCLUSION

#### What was learned from this project

This project demonstrated the practical application of object-oriented programming concepts in building a real-world application. Key learning outcomes include:

- **OOP in practice** - Understanding how encapsulation, inheritance, polymorphism, and abstraction work together in a complete system
- **Data structure selection** - Learning to choose the right data structure (ArrayList vs HashMap) based on the operation being performed
- **Database integration** - Gaining experience with JDBC and SQL for persistent data storage
- **GUI development** - Learning to create user-friendly interfaces with Java Swing
- **Algorithm implementation** - Implementing searching and sorting algorithms in a practical context
- **Exception handling** - Understanding the importance of robust error handling for professional applications
- **Software architecture** - Learning to separate concerns with distinct layers (GUI, business logic, data access)

---

#### Why object-oriented programming is useful

Object-oriented programming provides several key benefits that make it ideal for this type of project:

**Code reusability:** Through inheritance, ExpiringProduct reused all the code from Product without duplication. This reduces development time and makes maintenance easier.

**Modularity:** Each class has a single, well-defined responsibility. Product handles product data, DatabaseManager handles database operations, MainGUI handles the user interface. This makes the code easier to understand and test.

**Flexibility:** Polymorphism allows the system to handle different types of products uniformly. The same code works for both Product and ExpiringProduct objects.

**Maintainability:** Encapsulation protects internal implementation details. Changes to one class don't necessarily require changes to other classes.

**Scalability:** The OOP design makes it easy to add new features. For example, adding a new product type would simply require creating a new subclass of Product.

---

#### Why databases are important in management systems

Databases are essential for inventory management systems for several reasons:

**Persistence:** Data stored in a database survives application restarts and system shutdowns. Unlike in-memory storage, database data is permanent.

**Data integrity:** Databases enforce constraints (like primary keys) to ensure data consistency. The product_id being a primary key prevents duplicate IDs.

**Concurrent access:** Multiple users can access the database simultaneously without conflicts. This is crucial for multi-user environments.

**Query capabilities:** Databases provide powerful query languages (SQL) for searching, filtering, and aggregating data. This enables complex reporting and analysis.

**Scalability:** Databases can handle large amounts of data efficiently. As the inventory grows, the database performance remains acceptable.

**Backup and recovery:** Databases support backup and recovery mechanisms to protect against data loss.

**Transaction support:** Databases ensure that operations either complete fully or not at all. For example, updating a product's quantity and price happens as a single atomic operation.

This project demonstrates how a well-designed Java application can leverage the power of databases to create a robust, professional-grade inventory management system.

---

## PRESENTATION SUMMARY

### Key Points for Presentation

1. **System Overview:** A desktop inventory management application built with Java, Swing GUI, and MySQL database for small store owners.

2. **OOP Implementation:**
   - Encapsulation: Private fields with getters/setters in all classes
   - Inheritance: ExpiringProduct extends Product
   - Polymorphism: Overridden getTotalValue() method
   - Abstraction: Searchable interface defines search behavior

3. **Data Structures:**
   - ArrayList for ordered storage and display
   - HashMap for O(1) product lookup by ID

4. **Algorithms:**
   - Linear search for name-based searching
   - Comparator-based sorting with Collections.sort()

5. **Database Integration:**
   - JDBC connection to MySQL
   - Prepared statements for security
   - CRUD operations (INSERT, UPDATE, DELETE, SELECT)

6. **GUI Components:**
   - JFrame, JPanel, JTable, JTextField, JButton
   - JOptionPane for user feedback
   - Event-driven programming with ActionListeners

7. **Exception Handling:**
   - NumberFormatException for invalid numeric input
   - SQLException for database errors
   - Input validation for missing fields

---

## POSSIBLE LECTURER QUESTIONS AND ANSWERS

### Q: Why did you choose ArrayList over LinkedList for storing products?

**A:** ArrayList was chosen because:
- It provides indexed access, which is useful for displaying products in a table
- It has better performance for sequential access and iteration
- It works efficiently with Collections.sort() for sorting operations
- Memory overhead is lower than LinkedList
- For this use case (displaying and iterating through products), ArrayList's characteristics are a better fit than LinkedList's strength in frequent insertions/deletions at arbitrary positions.

### Q: How does polymorphism benefit your system?

**A:** Polymorphism allows the system to handle different product types uniformly:
- The same code can work with both Product and ExpiringProduct objects
- When MainGUI calls getTotalValue(), it automatically gets the correct calculation (regular for Product, discounted for ExpiringProduct)
- This makes the code more flexible and easier to extend - adding new product types doesn't require changing existing code
- It demonstrates the "program to an interface, not an implementation" principle

### Q: Why use PreparedStatement instead of regular Statement?

**A:** PreparedStatement provides two key benefits:
1. **Security:** It prevents SQL injection attacks by treating user input as data, not executable SQL code. This is crucial for any application that handles user input.
2. **Performance:** PreparedStatement statements are precompiled by the database, making them faster when executed multiple times with different parameters.
3. **Readability:** Parameters are clearly separated from the SQL query, making the code easier to read and maintain.

### Q: What is the difference between ArrayList and HashMap, and why use both?

**A:** 
- **ArrayList** is an ordered list that maintains insertion order and allows indexed access. It's used for displaying products in a table and for iteration when sorting or searching by name.
- **HashMap** is a key-value mapping that provides O(1) average lookup time by key. It's used for fast product lookup by ID.

**Why both:** Each data structure excels at different operations. Using both together gives the best of both worlds - fast ID lookup with HashMap and ordered display with ArrayList. This demonstrates understanding of choosing the right tool for each job.

### Q: How does inheritance reduce code duplication in your project?

**A:** ExpiringProduct inherits all fields and methods from Product:
- It doesn't need to redefine productId, name, category, price, or quantity
- It doesn't need to rewrite all the getters and setters for these fields
- It reuses the parent constructor logic by calling super()
- Any future changes to Product automatically apply to ExpiringProduct

This reduces code from approximately 100 lines to about 30 lines for the subclass, making the codebase smaller and easier to maintain.

### Q: What is the time complexity of your search operations?

**A:**
- **Search by ID:** O(1) average time complexity because it uses HashMap lookup. This is constant time regardless of the number of products.
- **Search by name:** O(n) time complexity because it uses linear search through the ArrayList. It checks each product until a match is found or the end is reached.

The different time complexities show why choosing the right data structure matters - HashMap provides much faster performance for ID-based searches.

### Q: How does your GUI handle invalid user input?

**A:** The GUI uses multiple layers of input validation:
1. **Field validation:** The validateFields() method checks that all required fields are filled before any operation
2. **Type validation:** try-catch blocks catch NumberFormatException when parsing numeric fields (price, quantity)
3. **Business logic validation:** Checks for duplicate product IDs before adding
4. **User feedback:** JOptionPane dialogs display clear error messages explaining what went wrong
5. **Confirmation dialogs:** Prevent accidental deletions with yes/no confirmation

This comprehensive approach ensures data integrity and provides a good user experience.

### Q: Why is abstraction important in software design?

**A:** Abstraction provides several benefits:
- **Simplifies complexity:** Hides implementation details and shows only necessary features
- **Improves flexibility:** The Searchable interface allows different search implementations without changing calling code
- **Enables loose coupling:** Code depends on interfaces rather than concrete classes, making it easier to change implementations
- **Improves maintainability:** Changes to implementation don't affect code that uses the abstraction
- **Facilitates testing:** Interfaces can be mocked for unit testing

In this project, the Searchable interface abstracts search behavior, allowing InventoryManager to provide its own implementation while MainGUI doesn't need to know how searching actually works.

### Q: What happens if the database connection fails?

**A:** The system has comprehensive exception handling:
1. DatabaseManager catches SQLException in all database methods
2. Error messages are printed to the console for debugging
3. MainGUI catches exceptions when calling InventoryManager methods
4. JOptionPane displays user-friendly error messages
5. The application continues running (doesn't crash) but the operation fails gracefully

This ensures the application remains stable even when the database is unavailable, though database operations won't succeed until the connection is restored.

### Q: How would you extend this system for a multi-user environment?

**A:** Several modifications would be needed:
1. **User authentication:** Add a users table with login functionality
2. **Session management:** Track which user is logged in and their permissions
3. **Concurrent access control:** Use database transactions and locking to prevent conflicts
4. **Audit logging:** Track who made what changes and when
5. **Network deployment:** Move from local MySQL to a network-accessible database server
6. **Connection pooling:** Use a connection pool for better performance with multiple users

The current single-user design would need these enhancements to support multiple users safely and efficiently.
