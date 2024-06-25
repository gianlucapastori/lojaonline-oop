package lojaonline;

import java.util.List;

public interface Persistence {
    void saveProducts(List<Product> products);
    List<Product> loadProducts();

    void saveCustomers(List<Customer> customers);
    List<Customer> loadCustomers();

    void saveOrders(List<Order> orders);
    List<Order> loadOrders();
}
