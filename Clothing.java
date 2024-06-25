package lojaonline;

public class Clothing extends Product {
    public Clothing(String name, double price) {
        super(name, price);
    }

    @Override
    public double discountedPrice() {
        return this.price * 0.8;
    }
}