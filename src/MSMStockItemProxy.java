import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MSMStockItemProxy implements StockItem {
    private final MSMStockItem adaptee;
    private final List<StockObserver> observers = new ArrayList<>();

    public MSMStockItemProxy(MSMStockItem adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String getProductCode() {
        return adaptee.getCode();
    }

    @Override
    public String getProductTitle() {
        return adaptee.getName();
    }

    @Override
    public void setProductTitle(String productTitle) {
        // Implement as needed
    }

    @Override
    public String getProductDescription() {
        return adaptee.getDescription();
    }

    @Override
    public void setProductDescription(String productDescription) {
        // Implement as needed
    }

    @Override
    public int getUnitPricePounds() {
        return adaptee.getUnitPrice() / 100;
    }

    @Override
    public void setUnitPricePounds(int unitPricePounds) {
        // Implement as needed
    }

    @Override
    public int getUnitPricePence() {
        return adaptee.getUnitPrice() % 100;
    }

    @Override
    public void setUnitPricePence(int unitPricePence) {
        // Implement as needed
    }

    @Override
    public int getQuantityInStock() {
        return adaptee.getQuantityInStock();
    }

    @Override
    public void setQuantityInStock(int quantityInStock) {
        // Implement as needed
    }

    @Override
    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (StockObserver observer : observers) {
            observer.updateLowStock(getProductCode(), getQuantityInStock());
        }
    }

    @Override
    public void recordSalesTransaction(int quantitySold, Date dateTime) {
        adaptee.setQuanity(adaptee.getQuantityInStock() - quantitySold);

        System.out.println("Sales Transaction Recorded:");
        System.out.println("Date and Time: " + dateTime);
        System.out.println("Product Code: " + getProductCode());
        System.out.println("Quantity Sold: " + quantitySold);
        System.out.println("Unit Price: " + getUnitPricePounds() + "." + getUnitPricePence());
    }
}