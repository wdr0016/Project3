import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UpdateCustomerUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Customer");
    public JButton btnSave = new JButton("Save Customer");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);


    public UpdateCustomerUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update Customer Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("CustomerID "));
        line1.add(txtCustomerID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Address "));
        line3.add(txtAddress);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Phone "));
        line4.add(txtPhone);
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
            CustomerModel customer = new CustomerModel();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            // do client/server

            try {
                Socket link = new Socket("localhost", 1001);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("GETC");
                output.println(customer.mCustomerID);

                customer.mName = input.nextLine();

                if (customer.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Customer NOT exists!");
                    return;
                }

                txtName.setText(customer.mName);

                customer.mAddress = input.nextLine();
                txtAddress.setText(customer.mAddress);

                customer.mPhone = input.nextLine();
                txtPhone.setText(customer.mPhone);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer name cannot be empty!");
                return;
            }

            customer.mName = name;

            String address = txtAddress.getText();
            try {
                customer.mAddress = address;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Address is invalid!");
                return;
            }

            String phone = txtPhone.getText();
            try {
                customer.mPhone = phone;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Phone is invalid!");
                return;
            }

            // all customer infor is ready! Send to Server!

            try {
                Socket link = new Socket("localhost", 1001);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("PUTC");
                output.println(customer.mCustomerID);
                output.println(customer.mName);
                output.println(customer.mAddress);
                output.println(customer.mPhone);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
