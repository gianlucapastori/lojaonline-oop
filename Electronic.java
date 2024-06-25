package lojaonline;

public class Electronic extends Product {
    public Electronic(String name, double price) {
        super(name, price);
    }

    @Override
    public double discountedPrice() {
        return this.price * 0.9;
    }
}