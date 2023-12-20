import java.util.Date;

public interface StockItem {
    String getProductCode();

    String getProductTitle();

    void setProductTitle(String productTitle);

    String getProductDescription();

    void setProductDescription(String productDescription);

    int getUnitPricePounds();

    void setUnitPricePounds(int unitPricePounds);

    int getUnitPricePence();

    void setUnitPricePence(int unitPricePence);

    int getQuantityInStock();

    void setQuantityInStock(int quantityInStock);

    void addObserver(StockObserver observer);

    void removeObserver(StockObserver observer);

    void notifyObservers();

    void recordSalesTransaction(int quantitySold, Date dateTime);
}
