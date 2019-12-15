import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class ProductServer {
    static String dbfile = "C:\\Users\\Willm\\Documents\\COMP3700\\Project3\\data\\store.db";

    public static void main(String[] args) {

        int port = 1001;

        if (args.length > 0) {
            System.out.println("Running arguments: ");
            for (String arg : args)
                System.out.println(arg);
            port = Integer.parseInt(args[0]);
            dbfile = args[1];
        }

        try {
            ServerSocket server = new ServerSocket(port);

            System.out.println("Server is listening at port = " + port);

            while (true) {
                Socket pipe = server.accept();
                PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
                Scanner in = new Scanner(pipe.getInputStream());

                String command = in.nextLine();
                if (command.equals("GET")) {
                    String str = in.nextLine();
                    System.out.println("GET product with id = " + str);
                    int productID = Integer.parseInt(str);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Products WHERE ProductId = " + productID;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            out.println(rs.getString("Name")); // send back product name!
                            out.println(rs.getDouble("Price")); // send back product price!
                            out.println(rs.getDouble("Quantity")); // send back product quantity!
                        }
                        else
                            out.println("null");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }

                if (command.equals("GETC")) {
                    String str = in.nextLine();
                    System.out.println("GET customer with id = " + str);
                    int customerID = Integer.parseInt(str);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Customers WHERE CustomerId = " + customerID;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            out.println(rs.getString("Name")); // send back product name!
                            out.println(rs.getString("Address")); // send back product price!
                            out.println(rs.getString("Phone")); // send back product quantity!
                        }
                        else
                            out.println("null");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }

                if (command.equals("GETP")) {
                    String str = in.nextLine();
                    System.out.println("GET purchase with id = " + str);
                    int purchaseID = Integer.parseInt(str);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Purchases WHERE PurchaseID = " + purchaseID;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            out.println(rs.getInt("CustomerID")); // send back product name!
                            out.println(rs.getInt("ProductID")); // send back product price!
                            out.println(rs.getDouble("Quantity")); // send back product quantity!
                        }
                        else
                            out.println("null");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                }

                if (command.equals("PUT")) {
                    String id = in.nextLine();  // read all information from client
                    String name = in.nextLine();
                    String price = in.nextLine();
                    String quantity = in.nextLine();

                    System.out.println("PUT command with ProductID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Products WHERE ProductID = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("DELETE FROM Products WHERE ProductID = " + id);
                        }

                        sql = "INSERT INTO Products VALUES (" + id + ",\"" + name + "\","
                                + price + "," + quantity + ")";
                        System.out.println("SQL for PUT: " + sql);
                        stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();


                }

                if (command.equals("PUTC")) {
                    String id = in.nextLine();  // read all information from client
                    String name = in.nextLine();
                    String address = in.nextLine();
                    String phone = in.nextLine();

                    System.out.println("PUT command with CustomerID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Customers WHERE CustomerId = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("DELETE FROM Customers WHERE CustomerId = " + id);
                        }

                        sql = "INSERT INTO Customers VALUES (" + id + ",\"" + name + "\", \""
                                + address + "\", \"" + phone + "\")";
                        System.out.println("SQL for PUT: " + sql);
                        stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();


                }

                if (command.equals("PUTP")) {
                    String id = in.nextLine();  // read all information from client
                    String customerID = in.nextLine();
                    String productID = in.nextLine();
                    String quantity = in.nextLine();

                    System.out.println("PUT command with PurchaseID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Purchases WHERE PurchaseID = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("UPDATE Purchases SET CustomerID = " +  customerID + ", " +
                                    "ProductID = " + productID + ", Quantity = " + quantity + " WHERE " +
                                    "PurchaseID = " + id);
                        }

                        //sql = "INSERT INTO Products VALUES (" + id + ",\"" + name + "\","
                          //      + price + "," + quantity + ")";
                        System.out.println("SQL for PUT: " + sql);
                        //stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();


                }

                if (command.equals("PUTPU")) {
                    String id = in.nextLine();  // read all information from client
                    String customerID = in.nextLine();
                    String productID = in.nextLine();
                    String quantity = in.nextLine();

                    System.out.println("PUT command with CustomerID = " + id);

                    Connection conn = null;
                    try {
                        String url = "jdbc:sqlite:" + dbfile;
                        conn = DriverManager.getConnection(url);

                        String sql = "SELECT * FROM Purchases WHERE PurchaseID = " + id;
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            rs.close();
                            stmt.execute("DELETE FROM Purchases WHERE PurchaseID = " + id);
                        }

                        sql = "INSERT INTO Purchases VALUES (" + id + ", " + customerID + ", " + productID +
                                ", 0.0, " + quantity + ", 0.0, 0.0, 0.0, \"Today\")";
                        System.out.println("SQL for PUT: " + sql);
                        stmt.execute(sql);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();


                }

                else {
                    out.println(0); // logout unsuccessful!
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}