import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UpdatePurchaseUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Purchase");
    public JButton btnSave = new JButton("Save Purchase");

    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtProdID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);


    public UpdatePurchaseUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update Purchase Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("PurchaseID  "));
        line1.add(txtPurchaseID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("CustomerID "));
        line2.add(txtCustomerID);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("ProductID "));
        line3.add(txtProdID);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);


        btnLoad.addActionListener(new LoadButtonListerner());

        btnSave.addActionListener(new SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PurchaseModel purchase = new PurchaseModel();
            String id = txtPurchaseID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            // do client/server

            try {
                Socket link = new Socket("localhost", 1001);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("GETP");
                output.println(purchase.mPurchaseID);

                purchase.mCustomerID = input.nextInt();

                if (purchase.mCustomerID == -1) {
                    JOptionPane.showMessageDialog(null, "Purchase NOT exists!");
                    return;
                }

                txtCustomerID.setText(Integer.toString(purchase.mCustomerID));

                purchase.mProductID = input.nextInt();
                txtProdID.setText(Integer.toString(purchase.mProductID));

                purchase.mQuantity = input.nextDouble();
                txtQuantity.setText(Double.toString(purchase.mQuantity));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PurchaseModel purchase = new PurchaseModel();
            String id = txtPurchaseID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            try {
                purchase.mCustomerID = Integer.parseInt(txtCustomerID.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            try {
                purchase.mProductID = Integer.parseInt(txtProdID.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            String quant = txtQuantity.getText();
            try {
                purchase.mQuantity = Double.parseDouble(quant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            // all product infor is ready! Send to Server!

            try {
                Socket link = new Socket("localhost", 1001);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("PUTP");
                output.println(purchase.mPurchaseID);
                output.println(purchase.mCustomerID);
                output.println(purchase.mProductID);
                output.println(purchase.mQuantity);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
