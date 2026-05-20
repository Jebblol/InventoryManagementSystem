# Inventory Management System

A **Java Swing-based Inventory Management System** that manages products in a small store using **MySQL** as the database backend. Built as a university project demonstrating Object-Oriented Programming, Data Structures, Algorithms, and GUI development.

---

## Project Structure

```
InventoryManagementSystem/
├── README.md                          ← This file
├── sql/
│   └── create_database.sql            ← SQL script to set up the database
└── src/
    ├── MainApp.java                   ← Application entry point
    ├── Product.java                   ← Base product class (Encapsulation)
    ├── ExpiringProduct.java           ← Subclass (Inheritance, Polymorphism)
    ├── Searchable.java                ← Interface (Abstraction)
    ├── InventoryManager.java          ← Controller (ArrayList, HashMap, Searchable)
    ├── DatabaseManager.java           ← JDBC database operations
    ├── ProductComparator.java         ← Comparator for sorting
    └── MainGUI.java                   ← Java Swing GUI
```

---

## Prerequisites

1. **Java JDK 8 or later** — [Download](https://www.oracle.com/java/technologies/downloads/)
2. **MySQL Server 8.x** — [Download](https://dev.mysql.com/downloads/mysql/)
3. **MySQL Workbench** — [Download](https://dev.mysql.com/downloads/workbench/)
4. **MySQL Connector/J (JDBC Driver)** — [Download](https://dev.mysql.com/downloads/connector/j/)
5. **IDE** — NetBeans, IntelliJ IDEA, or Eclipse

---

## Setup Instructions

### Step 1: Create the Database

1. Open **MySQL Workbench** and connect to your local MySQL server.
2. Open the file `sql/create_database.sql`.
3. Execute the entire script. This will:
   - Create the `inventory_db` database.
   - Create the `products` table.
   - Insert sample data.

### Step 2: Configure the JDBC Driver

#### Option A: NetBeans
1. Right-click your project → **Properties** → **Libraries**.
2. Click **Add JAR/Folder** and browse to the downloaded `mysql-connector-j-X.X.XX.jar`.
3. Click **OK**.

#### Option B: IntelliJ IDEA
1. Go to **File → Project Structure → Libraries**.
2. Click **+** → **Java** and browse to the `mysql-connector-j-X.X.XX.jar`.
3. Click **OK** → **Apply**.

#### Option C: Eclipse
1. Right-click project → **Build Path** → **Add External Archives…**
2. Select the `mysql-connector-j-X.X.XX.jar`.

#### Option D: Command Line
```bash
# Place the JAR in a lib/ folder and compile/run with classpath:
javac -cp "lib/mysql-connector-j-X.X.XX.jar" -d out src/*.java
java  -cp "out;lib/mysql-connector-j-X.X.XX.jar" MainApp
```
> On macOS/Linux use `:` instead of `;` as the classpath separator.

### Step 3: Update Database Credentials

Open `src/DatabaseManager.java` and update lines 22–23:

```java
private static final String DB_USER = "root";       // ← Your MySQL username
private static final String DB_PASS = "password";   // ← Your MySQL password
```

### Step 4: Run the Application

Run `MainApp.java` from your IDE. The application will:
- Connect to MySQL
- Load all products into the GUI table
- Allow full CRUD operations, search, and sort

---

## UML Class Diagram (Text)

```
┌──────────────────────────────┐
│      «interface»             │
│        Searchable            │
├──────────────────────────────┤
│ + searchById(id): Product    │
│ + searchByName(name): List   │
└──────────────┬───────────────┘
               │ implements
               ▼
┌──────────────────────────────┐       ┌──────────────────────────────┐
│     InventoryManager         │──────▶│      DatabaseManager         │
├──────────────────────────────┤ uses  ├──────────────────────────────┤
│ - productList: ArrayList     │       │ - DB_URL: String             │
│ - productMap: HashMap        │       │ - DB_USER: String            │
│ - dbManager: DatabaseManager │       │ - DB_PASS: String            │
├──────────────────────────────┤       ├──────────────────────────────┤
│ + loadFromDatabase()         │       │ + getConnection(): Connection│
│ + addProduct(p): boolean     │       │ + getAllProducts(): List      │
│ + updateProduct(p): boolean  │       │ + getProductById(id): Product│
│ + deleteProduct(id): boolean │       │ + getProductsByName(): List   │
│ + searchById(id): Product    │       │ + insertProduct(p): boolean  │
│ + searchByName(n): List      │       │ + updateProduct(p): boolean  │
│ + sortByName()               │       │ + deleteProduct(id): boolean │
│ + sortByPrice()              │       └──────────────────────────────┘
└──────────────────────────────┘
               ▲
               │ uses
┌──────────────┴───────────────┐
│          MainGUI             │
├──────────────────────────────┤
│ - inventoryManager           │
│ - txtProductId: JTextField   │
│ - productTable: JTable       │
│ - tableModel: DefaultTable   │
│ - btnAdd, btnUpdate, etc.    │
├──────────────────────────────┤
│ + addProduct()               │
│ + updateProduct()            │
│ + deleteProduct()            │
│ + searchProduct()            │
│ + sortByName() / sortByPrice │
│ + refreshTable()             │
└──────────────────────────────┘

┌──────────────────────────────┐
│          Product             │  (Base class)
├──────────────────────────────┤
│ - productId: String          │
│ - name: String               │
│ - category: String           │
│ - price: double              │
│ - quantity: int              │
├──────────────────────────────┤
│ + getters/setters            │
│ + getTotalValue(): double    │
│ + toString(): String         │
└──────────────┬───────────────┘
               │ extends (Inheritance)
               ▼
┌──────────────────────────────┐
│      ExpiringProduct         │  (Subclass)
├──────────────────────────────┤
│ - expiryDate: String         │
├──────────────────────────────┤
│ + getExpiryDate()            │
│ + setExpiryDate()            │
│ + getTotalValue(): double    │  ← Overridden (Polymorphism)
│ + toString(): String         │
└──────────────────────────────┘

┌──────────────────────────────┐
│     ProductComparator        │
├──────────────────────────────┤
│ + byName(): Comparator       │
│ + byPrice(): Comparator      │
└──────────────────────────────┘

┌──────────────────────────────┐
│          MainApp             │
├──────────────────────────────┤
│ + main(args): void           │
└──────────────────────────────┘
```

---

## How Each Requirement Is Satisfied

### Functional Features

| # | Requirement | Implementation |
|---|-------------|----------------|
| 1 | Add new product | `MainGUI.addProduct()` → `InventoryManager.addProduct()` → `DatabaseManager.insertProduct()` |
| 2 | Update existing product | `MainGUI.updateProduct()` → `InventoryManager.updateProduct()` → `DatabaseManager.updateProduct()` |
| 3 | Delete product | `MainGUI.deleteProduct()` → `InventoryManager.deleteProduct()` → `DatabaseManager.deleteProduct()` |
| 4 | Display all products in JTable | `MainGUI.refreshTable()` populates a `DefaultTableModel` shown in a `JTable` |
| 5 | Search by ID or name | `MainGUI.searchProduct()` → HashMap lookup (ID) / linear search (name) |
| 6 | Sort by name or price | `InventoryManager.sortByName()` / `sortByPrice()` using `Collections.sort()` with `ProductComparator` |
| 7 | Load data on startup | `MainGUI` constructor calls `loadDataFromDatabase()` |

### Database Requirements

| Requirement | Implementation |
|-------------|----------------|
| MySQL database `inventory_db` | Created via `sql/create_database.sql` |
| Table `products` with required columns | Defined in SQL script with `product_id`, `name`, `category`, `price`, `quantity` |
| INSERT / UPDATE / DELETE / SELECT | All implemented in `DatabaseManager.java` using `PreparedStatement` |
| JDBC connectivity | `DatabaseManager.getConnection()` using `jdbc:mysql://localhost:3306/inventory_db` |

### OOP Requirements

| Concept | Implementation |
|---------|----------------|
| **Inheritance** | `ExpiringProduct extends Product` |
| **Polymorphism** | `getTotalValue()` overridden in `ExpiringProduct` (applies 20% discount) |
| **Abstraction** | `Searchable` interface with `searchById()` and `searchByName()` |
| **Encapsulation** | All fields in `Product` and `ExpiringProduct` are `private` with getters/setters |
| **5+ classes** | `Product`, `ExpiringProduct`, `InventoryManager`, `DatabaseManager`, `ProductComparator`, `MainGUI`, `MainApp`, `Searchable` (8 total) |

### Data Structures

| Structure | Usage |
|-----------|-------|
| **ArrayList** | `InventoryManager.productList` — stores ordered list of products |
| **HashMap** | `InventoryManager.productMap` — maps product ID → Product for O(1) lookup |

### Algorithms

| Algorithm | Usage |
|-----------|-------|
| **Linear search** | `InventoryManager.searchByName()` iterates through `ArrayList` |
| **Sorting with Comparator** | `ProductComparator.byName()` and `byPrice()` used with `Collections.sort()` |

### GUI Components

| Component | Usage |
|-----------|-------|
| **JFrame** | Main application window (`MainGUI extends JFrame`) |
| **JPanel** | Input panel, table panel, button panel |
| **JTextField** | Product ID, Name, Category, Price, Quantity, Expiry Date, Search |
| **JButton** | Add, Update, Delete, Search, Sort by Name, Sort by Price, Clear, Refresh |
| **JTable** | Displays all product records |
| **JScrollPane** | Wraps the JTable for scrolling |
| **JOptionPane** | Error/success/confirmation dialogs |
| **JCheckBox** | Expiring product toggle |

### Exception Handling

| Error Type | Handling |
|------------|----------|
| Invalid numeric input | `NumberFormatException` caught → `JOptionPane` error dialog |
| Database connection errors | `SQLException` caught → error dialog shown |
| SQL exceptions | All DB methods use try-catch with `SQLException` |
| Missing input fields | `validateFields()` checks all required fields before any operation |

---

## License

This project is created for educational purposes as a university assignment.
