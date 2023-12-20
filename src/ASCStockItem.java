import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ASCStockItem implements StockItem {
    private String productCode;
    private String productTitle;
    private String productDescription;
    private int unitPricePounds;
    private int unitPricePence;
    private int quantityInStock;
    private List<StockObserver> observers = new ArrayList<>();

    // Constructor
    public ASCStockItem(String productCode, String productTitle, String productDescription,
                        int unitPricePounds, int unitPricePence, int quantityInStock) {
        this.productCode = productCode;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.unitPricePounds = unitPricePounds;
        this.unitPricePence = unitPricePence;
        this.quantityInStock = quantityInStock;
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    // Notify observers when stock is updated
    public void notifyObservers() {
        for (StockObserver observer : observers) {
            observer.updateLowStock(getProductCode(), getQuantityInStock());
        }
    }

    // Getters and Setters
    public String getProductCode() {
        return productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getUnitPricePounds() {
        return unitPricePounds;
    }

    public void setUnitPricePounds(int unitPricePounds) {
        this.unitPricePounds = unitPricePounds;
    }

    public int getUnitPricePence() {
        return unitPricePence;
    }

    public void setUnitPricePence(int unitPricePence) {
        this.unitPricePence = unitPricePence;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    // Method to record sales transaction
    public void recordSalesTransaction(int quantitySold, Date dateTime) {
        if (quantitySold > 0 && quantitySold <= quantityInStock) {
            // Update quantity in stock
            quantityInStock -= quantitySold;

            // Log sales transaction
            System.out.println("Sales Transaction Recorded:");
            System.out.println("Date and Time: " + dateTime);
            System.out.println("Product Code: " + productCode);
            System.out.println("Quantity Sold: " + quantitySold);
            System.out.println("Unit Price: " + unitPricePounds + "." + unitPricePence);
        } else {
            System.out.println("Invalid quantity for sales transaction.");
        }
    }
}
