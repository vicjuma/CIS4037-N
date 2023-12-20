import javax.swing.*;
import java.awt.*;
interface StockObserver {
    void updateLowStock(String productCode, int currentStockLevel);
}

public class PurchasingDepartment implements StockObserver {
    @Override
    public void updateLowStock(String productCode, int currentStockLevel) {
        if (currentStockLevel < 5) {
            // Notify the purchasing department with a warning popup
            String message = "Low stock alert: Product Code " + productCode +
                    " has stock below 5 units. Please reorder.";

            Point topLeft = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds().getLocation();
            // Show a warning dialog
            JFrame dummyFrame = new JFrame();
            dummyFrame.setLocation(0, 0);
            JOptionPane.showMessageDialog(dummyFrame, message, "Low Stock Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
