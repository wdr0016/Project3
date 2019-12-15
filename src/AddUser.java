import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser {

    public JFrame view;

    public JButton btnAdd = new JButton("Add");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtUsername = new JTextField(20);
    public JTextField txtPassword = new JTextField(20);
    public JTextField txtFullName = new JTextField(20);
    public JTextField txtType = new JTextField(20);


    public AddUser()   {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Add User");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        String[] labels = {"Username ", "Password ", "Fullname ", "Type "};

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Username: "));
        line1.add(txtUsername);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Password: "));
        line2.add(txtPassword);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Name: "));
        line3.add(txtFullName);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("User Type: "));
        line4.add(txtType);
        view.getContentPane().add(line4);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdd);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        btnAdd.addActionListener(new AddButtonListerner());

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                view.dispose();
            }
        });

    }

    public void run() {
        view.setVisible(true);
    }

    class AddButtonListerner implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();

            String username = txtUsername.getText();

            if (username.length() == 0) {
                JOptionPane.showMessageDialog(null, "Username cannot be null!");
                return;
            }
            user.mUsername = username;

            String password = txtPassword.getText();
            if (password.length() == 0) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                return;
            }
            user.mPassword = password;

            String fullname = txtFullName.getText();
            if (fullname.length() == 0) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!");
                return;
            }
            user.mFullname = fullname;

            String type = txtType.getText();
            if (type.length() == 0) {
                JOptionPane.showMessageDialog(null, "Type cannot be empty!");
                return;
            }
            try {
                user.mUserType = Integer.parseInt(type);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "User Type is invalid!");
                return;
            }

            switch (StoreManager.getInstance().getDataAdapter().saveUser(user)) {
                case SQLiteDataAdapter.CUSTOMER_DUPLICATE_ERROR:
                    JOptionPane.showMessageDialog(null, "User NOT added successfully! Duplicate username!");
                default:
                    JOptionPane.showMessageDialog(null, "User added successfully!" + user);
            }
        }
    }

}