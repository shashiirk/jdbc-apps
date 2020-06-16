import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Signup implements ActionListener {

    private Connection con;
    private PreparedStatement ps;
    private int i = 0;

    private JFrame f;
    private JLabel l1, l2 ,l3 ,l4 ,l5;
    private JTextField tf1, tf2;
    private JPasswordField pf1, pf2;
    private JButton b;

    //Constructor.
    private Signup() {
        boolean connected = establishConnection();
        if(connected) {
            f = new JFrame("Signup");

            l5 = new JLabel("Sign Up");
            l5.setFont(new Font("Corbel", Font.PLAIN, 35));
            l5.setBounds(30,30,120,40);

            l1 = new JLabel("Name");
            l1.setFont(new Font("Ariel", Font.BOLD, 18));
            l1.setBounds(30,90,60,18);

            tf1 = new JTextField();
            tf1.setFont(new Font("Ariel", Font.PLAIN, 15));
            tf1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tf1.setBounds(30,115,200,30);

            l2 = new JLabel("Email");
            l2.setFont(new Font("Ariel", Font.BOLD, 18));
            l2.setBounds(30,170,60,18);

            tf2 = new JTextField();
            tf2.setFont(new Font("Ariel", Font.PLAIN, 15));
            tf2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tf2.setBounds(30,195,200,30);

            l3 = new JLabel("Password");
            l3.setFont(new Font("Ariel", Font.BOLD, 18));
            l3.setBounds(30,250,90,18);

            pf1 = new JPasswordField();
            pf1.setFont(new Font("Ariel", Font.PLAIN, 15));
            pf1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            pf1.setBounds(30,275,200,30);

            l4 = new JLabel("Confirmation");
            l4.setFont(new Font("Ariel", Font.BOLD, 18));
            l4.setBounds(30,330,120,18);

            pf2 = new JPasswordField();
            pf2.setFont(new Font("Ariel", Font.PLAIN, 15));
            pf2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            pf2.setBounds(30,355,200,30);

            b = new JButton("Submit");
            b.setFont(new Font("Ariel", Font.PLAIN, 18));
            b.setForeground(Color.WHITE);
            b.setBackground(Color.BLACK);
            b.setBounds(30,420,80,40);
            b.setFocusable(false);
            b.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            b.addActionListener(this);

            f.add(l1);
            f.add(tf1);
            f.add(l2);
            f.add(tf2);
            f.add(l3);
            f.add(pf1);
            f.add(l4);
            f.add(pf2);
            f.add(l5);
            f.add(b);

            f.addWindowListener(new WindowAdapter(){
                //Close the window.
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        int a = JOptionPane.showConfirmDialog(f, "Are you sure to quit?");
                        if(a == JOptionPane.YES_OPTION){
                            con.close();
                            System.out.println(i + " record(s) inserted");
                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
                        }
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });

            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.setSize(400,550);
            f.getContentPane().setBackground(new Color(0,255,255));
            f.setLayout(null);
            f.setVisible(true);
        }
        else {
            System.out.println("Error: Failed to connect to the database.");
        }
    }

    //Define the action performed when clicking the buttons.
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = tf1.getText();
        boolean b1 = validateName(name);
        
        String email = tf2.getText();
        boolean b2 = validateEmail(email);
        
        String pass = String.valueOf(pf1.getPassword());
        boolean b3 = validatePassword(pass);
        
        String conf = String.valueOf(pf2.getPassword());
        boolean b4 = validateConfirmPassword(pass,conf);

        if(b1 && b2 && b3 && b4) {
            boolean b = addMember(name, email, pass);
            if(b) {
                tf1.setText("");
                tf2.setText("");
                pf1.setText("");
                pf2.setText("");
                JOptionPane.showMessageDialog(f, "Your account has been created successfully.");
            }
        }
        else {
            if(!b1) {
                if(name.isEmpty()) {
                    JOptionPane.showMessageDialog(f,
                        "Name field cannot be left blank.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
                else {
                    tf1.setText("");
                    JOptionPane.showMessageDialog(f,
                        "Please enter a valid name.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }

            }
            else if(!b2) {
                if(email.isEmpty()) {
                    JOptionPane.showMessageDialog(f,
                        "Email field cannot be left blank.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
                else {
                    tf2.setText("");
                    JOptionPane.showMessageDialog(f,
                        "Please enter a valid email address.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(!b3) {
                if(pass.isEmpty()) {
                    JOptionPane.showMessageDialog(f,
                        "Password field cannot be left blank.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
                else {
                    pf1.setText("");
                    JOptionPane.showMessageDialog(f,
                        "Password must have atleast 6 characters.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(!b4) {
                pf1.setText("");
                pf2.setText("");
                JOptionPane.showMessageDialog(f,
                    "Please provide same passwords.","Alert",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Validate the name provided by the user.
    private boolean validateName(String name) {
        char arr[] = name.toCharArray();
        for(int i=0;i<arr.length;i++) {
            if((arr[i] >= 'A' && arr[i] <= 'Z') || (arr[i] >= 'a' && arr[i] <= 'z') || arr[i] == ' ') {
                continue;
            }
            else
                return false;
        }
        if(name.isEmpty()) {
            return false;
        }
        return true;
    }

    //Validate the email provided by the user.
    private boolean validateEmail(String email) {
        String emailPattern = "^([a-z0-9]{1}[a-z0-9-_.]+)@(?:[a-zA-Z0-9-]+\\.)+([a-zA-Z]{2,6})$";
        return email.toLowerCase().matches(emailPattern);
    }

    //Validate the password provided by the user.
    private boolean validatePassword(String pass) {
        return pass.length() >= 6;
    }

    //Validate the password confirmation.
    private boolean validateConfirmPassword(String pass, String conf) {
        return pass.equals(conf);
    }

    //Database connection.
    private boolean establishConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/java_db";
            String username = "<username>";
            String password = "<password>";
            con = DriverManager.getConnection(url, username, password);
            return true;
        }
        catch(SQLException se) {}
        return false;
    }

    //Add the member details into the database.
    private boolean addMember(String name, String mail, String pass) {
        try {
            ps = con.prepareStatement("INSERT INTO members (name,email,password) VALUES (?,?,?);");
            ps.setString(1, name);
            ps.setString(2, mail);
            ps.setString(3, pass);
            i = ps.executeUpdate();
            return true;
        }
        catch(SQLIntegrityConstraintViolationException sie) {
            tf2.setText("");
            JOptionPane.showMessageDialog(f,
                "An account with this email already exists.","Alert",
                JOptionPane.WARNING_MESSAGE);
        }
        catch(SQLException se) {
            System.out.println(se);
        }
        return false;
    }

    //Main method.
    public static void main(String args[]) {
        new Signup();
    }
}
