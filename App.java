import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();
        products.add(new Product("Mouse", 500.0));
        products.add(new Product("Notebook", 2000.0));
        products.add(new Product("Keyboard", 300.0));

        Customer customer = new Customer("John Doe", "johndoe@mail.com");

        try {
            String invoice = generateInvoice(products, customer);
            System.out.println(invoice);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static class Product {
        private final String name;
        private final double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    static class Customer {
        private final String name;
        private final String email;

        public Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    private static String generateInvoice(List<Product> products, Customer customer) {
        // Check if products is null or empty
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        }

        double purchaseTotal = 0.0;
        double salesTax = 0.0;

        // Establish the current date
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedToday = today.format(formatter);
        // Define a StringBuilder to generate the JSON string with the current date and customer data
        StringBuilder invoice = new StringBuilder("{\"date\":\"")
                .append(formattedToday)
                .append("\",\"customer\":{\"name\":\"")
                .append(customer.getName())
                .append("\",\"email\":\"")
                .append(customer.getEmail())
                .append("\"},\"products\":{");

        // Loop through products to append them to the invoice
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            // If the product names are empty throw an exception with a user-friendly message
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty.");
            }
            // If the prices are negative throw an exception with a user-friendly message
            if (product.getPrice() <= 0.0) {
                throw new IllegalArgumentException("Product price must be positive.");
            }

            invoice.append("\"").append(i).append("\":{\"name\":\"").append(product.getName()).append("\",\"price\":").append(product.getPrice());

            // Add "}," to the end of each name - price pair otherwise add "}}" if the product - name pair is the last on products
            if (i < products.size() - 1) {
                invoice.append("},");
            } else {
                invoice.append("}}");
            }

            purchaseTotal += product.getPrice();
        }
        // Calculate the sales tax
        salesTax = 0.10 * purchaseTotal;

        purchaseTotal += salesTax;

        // Add salesTax to the JSON string
        invoice.append(",\"salesTax\":").append(salesTax);
        invoice.append(",\"total\":").append(purchaseTotal).append("}");
        return invoice.toString();
    }
}