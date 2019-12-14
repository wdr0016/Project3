
import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class SQLiteDataAdapter implements IDataAdapter {

    Connection conn = null;

    public int connect(String dbfile) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbfile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

    @Override
    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        return CONNECTION_CLOSE_OK;
    }




    @Override
    public PurchaseListModel loadPurchaseHistory(int id) {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases WHERE CustomerId = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = id;
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getDouble("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    @Override
    public ProductListModel searchProduct(String name, double minPrice, double maxPrice) {
        ProductListModel res = new ProductListModel();
        try {
            String sql = "SELECT * FROM Products WHERE Name LIKE \'%" + name + "%\' "
                    + "AND Price >= " + minPrice + " AND Price <= " + maxPrice;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("ProductID");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
                res.products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    public UserModel loadUser(String username) {
        UserModel user = null;

        try {
            String sql = "SELECT * FROM Users WHERE Username = \"" + username + "\"";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new UserModel();
                user.mUsername = username;
                user.mPassword = rs.getString("Password");
                user.mFullname = rs.getString("Fullname");
                user.mUserType = rs.getInt("Usertype");
                if (user.mUserType == UserModel.CUSTOMER)
                    user.mCustomerID = rs.getInt("CustomerID");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }


    public ProductModel loadProduct(int productID) {
        ProductModel product = new ProductModel();

        try {
            Socket link = new Socket("localhost", 1000);
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            output.println("GET");
            output.println(productID);

            product.mName = input.nextLine();

            if (product.mName.equals("null")) {
                JOptionPane.showMessageDialog(null, "Product NOT exists!");
                return null;
            }

            product.mPrice = input.nextDouble();

            product.mQuantity = input.nextDouble();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            Socket link = new Socket("localhost", 1000);
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            output.println("PUT");
            output.println(product.mProductID);
            output.println(product.mName);
            output.println(product.mPrice);
            output.println(product.mQuantity);
            return PRODUCT_SAVE_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return PRODUCT_DUPLICATE_ERROR;
        }
    }

    @Override
    public int savePurchase(PurchaseModel purchase) {
        try {
            Socket link = new Socket("localhost", 1000);
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            output.println("PUTPU");
            output.println(purchase.mPurchaseID);
            output.println(purchase.mCustomerID);
            output.println(purchase.mProductID);
            output.println(purchase.mQuantity);
            return PURCHASE_SAVED_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return PRODUCT_DUPLICATE_ERROR;
        }

    }

    public int saveCustomer(CustomerModel customer) {
        try {
            Socket link = new Socket("localhost", 1000);
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            output.println("PUTC");
            output.println(customer.mCustomerID);
            output.println(customer.mName);
            output.println(customer.mAddress);
            output.println(customer.mPhone);

            return CUSTOMER_SAVED_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return CUSTOMER_DUPLICATE_ERROR;
        }
    }

    public CustomerModel loadCustomer(int id) {
        CustomerModel customer = new CustomerModel();

        try {
            Socket link = new Socket("localhost", 1000);
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            output.println("GETC");
            output.println(id);

            customer.mName = input.nextLine();

            if (customer.mName.equals("null")) {
                JOptionPane.showMessageDialog(null, "Customer NOT exists!");
                return null;
            }

            customer.mAddress = input.nextLine();

            customer.mPhone = input.nextLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

}
