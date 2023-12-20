import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockManagementGUI extends JFrame {
    private List<ASCStockItem> stockItems;
    private static final String STOCK_CSV_FILE = "AshersSportsCollective.csv";

    // GUI components
    private JTable stockTable;
    private DefaultTableModel tableModel;

    public StockManagementGUI(List<ASCStockItem> stockItems) {
        this.stockItems = stockItems;

        setTitle("Stock Management");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();

        setLocationRelativeTo(null);

        updateStockTable();
    }

    private void initializeComponents() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Product Code");
        tableModel.addColumn("Product Title");
        tableModel.addColumn("Product Description");
        tableModel.addColumn("Unit Price (Pounds)");
        tableModel.addColumn("Unit Price (Pence)");
        tableModel.addColumn("Quantity in Stock");

        stockTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton buyButton = new JButton("Buy Stock");
        JButton sellButton = new JButton("Sell Stock");

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBuyStockDialog();
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellStock();
            }
        });

        buttonPanel.add(buyButton);
        buttonPanel.add(sellButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateStockTable() {
        tableModel.setRowCount(0); // Clear existing rows

        for (ASCStockItem item : stockItems) {
            Object[] rowData = {
                    item.getProductCode(),
                    item.getProductTitle(),
                    item.getProductDescription(),
                    item.getUnitPricePounds(),
                    item.getUnitPricePence(),
                    item.getQuantityInStock()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showBuyStockDialog() {
        JDialog buyDialog = new JDialog(this, "Buy Stock", true);
        buyDialog.setSize(300, 200);
        buyDialog.setLayout(new GridLayout(7, 2));

        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField poundsField = new JTextField();
        JTextField penceField = new JTextField();
        JTextField quantityField = new JTextField();

        buyDialog.add(new JLabel("Product Code:"));
        buyDialog.add(codeField);

        buyDialog.add(new JLabel("Product Title:"));
        buyDialog.add(titleField);

        buyDialog.add(new JLabel("Product Description:"));
        buyDialog.add(descriptionField);

        buyDialog.add(new JLabel("Unit Price (Pounds):"));
        buyDialog.add(poundsField);

        buyDialog.add(new JLabel("Unit Price (Pence):"));
        buyDialog.add(penceField);

        buyDialog.add(new JLabel("Quantity:"));
        buyDialog.add(quantityField);

        JButton addButton = new JButton("Buy");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock(codeField.getText(), titleField.getText(), descriptionField.getText(),
                        Integer.parseInt(poundsField.getText()), Integer.parseInt(penceField.getText()),
                        Integer.parseInt(quantityField.getText()));
                buyDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyDialog.dispose();
            }
        });

        buyDialog.add(addButton);
        buyDialog.add(cancelButton);
        buyDialog.setLocationRelativeTo(this);
        buyDialog.setVisible(true);
    }

    private void addStock(String productCode, String productTitle, String productDescription,
                          int pounds, int pence, int quantity) {
        boolean found = false;
        for (ASCStockItem item : stockItems) {
            if (item.getProductCode().equals(productCode)) {
                item.setQuantityInStock(item.getQuantityInStock() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            // If product code not found, create a new item
            ASCStockItem newItem = new ASCStockItem(productCode, productTitle, productDescription, pounds, pence, quantity);
            stockItems.add(newItem);
        }

        writeStockToCSV();  // Call method to write to CSV
        updateStockTable();  // Call method to update table
    }


    private void sellStock() {
        JDialog sellDialog = new JDialog(this, "Sell Stock", true);
        sellDialog.setSize(300, 114);
        sellDialog.setLayout(new GridLayout(4, 2));

        JTextField codeField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitPriceField = new JTextField();

        sellDialog.add(new JLabel("Product Code:"));
        sellDialog.add(codeField);

        sellDialog.add(new JLabel("Quantity Sold:"));
        sellDialog.add(quantityField);

        sellDialog.add(new JLabel("Unit Price:"));
        sellDialog.add(unitPriceField);

        JButton sellButton = new JButton("Sell");
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSellStock(
                        codeField.getText(),
                        Integer.parseInt(quantityField.getText()),
                        Double.parseDouble(unitPriceField.getText())
                );
                sellDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellDialog.dispose();
            }
        });

        sellDialog.add(sellButton);
        sellDialog.add(cancelButton);
        sellDialog.setLocationRelativeTo(this);
        sellDialog.setVisible(true);
    }

    private void processSellStock(String productCode, int quantitySold, double unitPrice) {
        for (ASCStockItem item : stockItems) {
            if (item.getProductCode().equals(productCode)) {
                int availableQuantity = item.getQuantityInStock();
                if (availableQuantity >= quantitySold) {
                    // Update stock quantity
                    item.setQuantityInStock(availableQuantity - quantitySold);

                    // Record sales transaction
                    recordSalesTransaction(productCode, quantitySold, unitPrice);

                    // Update CSV and stock table
                    writeStockToCSV();
                    updateStockTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock available.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
        }
    }

    private void recordSalesTransaction(String productCode, int quantitySold, double unitPrice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales_transactions.csv", true))) {
            writer.write("Date and Time: " + java.time.LocalDateTime.now());
            writer.newLine();
            writer.write("Product Code: " + productCode);
            writer.newLine();
            writer.write("Quantity Sold: " + quantitySold);
            writer.newLine();
            writer.write("Unit Price: " + unitPrice);
            writer.newLine();
            writer.newLine(); // Add an empty line to separate transactions
        } catch (IOException e) {
            System.out.println("Error writing to sales transactions CSV file: " + e.getMessage());
        }
    }


    private void writeStockToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_CSV_FILE, true))) {
            // Write each stock item
            for (ASCStockItem item : stockItems) {
                writer.write(String.format("%s,%s,%s,%d,%d,%d",
                        item.getProductCode(),
                        item.getProductTitle(),
                        item.getProductDescription(),
                        item.getUnitPricePounds(),
                        item.getUnitPricePence(),
                        item.getQuantityInStock()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating stock in CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Initialize with an empty list initially
        List<ASCStockItem> stockItems = StockLoader.loadStockFromCSV("AshersSportsCollective.csv");
        StockManagementGUI gui = new StockManagementGUI(stockItems);
        gui.setVisible(true);
    }
}