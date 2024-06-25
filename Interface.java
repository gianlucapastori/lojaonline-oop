package lojaonline;

import java.util.*;

public class Interface {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Product> products = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static Persistence persistence = new FilePersistence();

    public static void main(String[] args) {
        loadData();

        boolean exit = false;
        while (!exit) {
            System.out.println("=== Online Store Management System ===");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. List Products");
            System.out.println("4. Add Customer");
            System.out.println("5. Place Order");
            System.out.println("6. List Orders");
            System.out.println("7. List Customers");
            System.out.println("8. Save and Exit");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    removeProduct();
                    break;
                case 3:
                    listProducts();
                    break;
                case 4:
                    addCustomer();
                    break;
                case 5:
                    placeOrder();
                    break;
                case 6:
                    listOrders();
                    break;
                case 7:
                    listCustomers();
                    break;
                case 8:
                    saveData();
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        }

        scanner.close();
    }

    private static void addProduct() {
        System.out.println("Enter product details:");
        String name = getStringInput("Name: ");
        double price = getDoubleInput("Price: ");

        try {
            validateProduct(name, price);
            Product product = createProduct(name, price);
            products.add(product);
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeProduct() {
        listProducts();
        if (products.isEmpty()) {
            System.out.println("No products to remove.");
            return;
        }

        String name = getStringInput("Enter product name to remove: ");
        Product productToRemove = findProductByName(name);
        if (productToRemove == null) {
            System.out.println("Product not found.");
            return;
        }

        products.remove(productToRemove);
        System.out.println("Product removed successfully.");
    }

    private static void listProducts() {
        System.out.println("List of Products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void addCustomer() {
        System.out.println("Enter customer details:");
        String cpf = getStringInput("CPF: ");
        String name = getStringInput("Name: ");

        try {
            validateCustomer(cpf);
            Customer customer = new Customer(cpf, name);
            customers.add(customer);
            System.out.println("Customer added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void placeOrder() {
        listCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers available to place order.");
            return;
        }

        String cpf = getStringInput("Enter customer CPF to place order: ");
        Customer customer = findCustomerByCpf(cpf);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        listProducts();
        if (products.isEmpty()) {
            System.out.println("No products available to place order.");
            return;
        }

        String productName = getStringInput("Enter product name to add to order: ");
        Product product = findProductByName(productName);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        int quantity = getIntInput("Enter quantity: ");
        Order order = findOrderForCustomer(customer);
        if (order == null) {
            order = new Order(customer);
            orders.add(order);
        }

        order.addProduct(product, quantity);
        System.out.println("Product added to order.");
    }

    private static void listOrders() {
        System.out.println("List of Orders:");
        for (Order order : orders) {
            System.out.println(order);
            System.out.println("Total: " + order.calculateTotal());
        }
    }

    private static void listCustomers() {
        System.out.println("List of Customers:");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private static void validateProduct(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (findProductByName(name) != null) {
            throw new IllegalArgumentException("Product with the same name already exists.");
        }
    }

    private static void validateCustomer(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be empty.");
        }
        if (findCustomerByCpf(cpf) != null) {
            throw new IllegalArgumentException("Customer with the same CPF already exists.");
        }
    }

    private static Product createProduct(String name, double price) {
    	int choice = getIntInput("1. Eletronic\n2. Clothing\nEnter product type: ");
    	if (choice == 1) {
            return new Electronic(name, price);
        } else if (choice == 2) {
            return new Clothing(name, price);
        }
		return null;
    }

    private static Product findProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    private static Customer findCustomerByCpf(String cpf) {
        for (Customer customer : customers) {
            if (customer.getCpf().equalsIgnoreCase(cpf)) {
                return customer;
            }
        }
        return null;
    }

    private static Order findOrderForCustomer(Customer customer) {
        for (Order order : orders) {
            if (order.getCustomer().equals(customer)) {
                return order;
            }
        }
        return null;
    }

    private static void saveData() {
        persistence.saveProducts(products);
        persistence.saveCustomers(customers);
        persistence.saveOrders(orders);
    }

    private static void loadData() {
        products = persistence.loadProducts();
        customers = persistence.loadCustomers();
        orders = persistence.loadOrders();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); 
        }
        return scanner.nextInt();
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); 
        }
        return scanner.nextDouble();
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }
}
