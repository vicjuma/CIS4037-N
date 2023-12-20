import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StockLoader {
    public static List<ASCStockItem> loadStockFromCSV(String filePath) {
        List<ASCStockItem> stockItems = new ArrayList<>();
        Set<String> uniqueProductCodes = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the header line (if any)
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String productCode = parts[0].trim();

                    // Check if the product code is unique
                    if (!uniqueProductCodes.contains(productCode)) {
                        uniqueProductCodes.add(productCode);

                        // Parse and create a stock item
                        String productTitle = parts[1].trim();
                        String productDescription = parts[2].trim();
                        int unitPricePounds = Integer.parseInt(parts[3].trim());
                        int unitPricePence = Integer.parseInt(parts[4].trim());
                        int quantityInStock = Integer.parseInt(parts[5].trim());

                        ASCStockItem stockItem = new ASCStockItem(
                                productCode, productTitle, productDescription,
                                unitPricePounds, unitPricePence, quantityInStock
                        );

                        // Add the loaded stock item to the list
                        stockItems.add(stockItem);
                    }
                } else {
                    System.out.println("Invalid CSV format. Skipping line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading stock from CSV file: " + e.getMessage());
        }

        return stockItems;
    }
}
