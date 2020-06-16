import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginApp implements ActionListener {

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    private JFrame f, f2, f3;
    private JLabel l1, l2 ,l3 ,l4 ,l5, l6, l7, l8, l9, l10, l11, l12;
    private JTextField tf1, tf2, tf3;
    private JPasswordField pf1, pf2, pf3;
    private JButton b, b2, b3;

    //Constructor.
    private LoginApp() {

        boolean connected = establishConnection();
        if(connected) {
            //Login Frame
            f2 = new JFrame("Login");

            l8 = new JLabel("<3");
            l8.setFont(new Font("Ariel", Font.BOLD, 80));
            l8.setForeground(new Color(255,51,51));
            l8.setBounds(130,40,100,80);

            l6 = new JLabel("Email");
            l6.setFont(new Font("Ariel", Font.BOLD, 18));
            l6.setBounds(85,180,60,18);

            tf3 = new JTextField();
            tf3.setFont(new Font("Ariel", Font.PLAIN, 15));
            tf3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tf3.setBounds(85,205,200,30);

            l7 = new JLabel("Password");
            l7.setFont(new Font("Ariel", Font.BOLD, 18));
            l7.setBounds(85,260,90,18);

            pf3 = new JPasswordField();
            pf3.setFont(new Font("Ariel", Font.PLAIN, 15));
            pf3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            pf3.setBounds(85,285,200,30);

            b2 = new JButton("Login");
            b2.setFont(new Font("Ariel", Font.PLAIN, 18));
            b2.setForeground(Color.WHITE);
            b2.setBackground(new Color(51,255,51));
            b2.setBounds(85,340,95,40);
            b2.setFocusable(false);
            b2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            b2.addActionListener(this);

            b3 = new JButton("Signup");
            b3.setFont(new Font("Ariel", Font.PLAIN, 18));
            b3.setForeground(Color.WHITE);
            b3.setBackground(new Color(51,51,255));
            b3.setBounds(190,340,95,40);
            b3.setFocusable(false);
            b3.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            b3.addActionListener(this);

            f2.add(l8);
            f2.add(l6);
            f2.add(tf3);
            f2.add(l7);
            f2.add(pf3);
            f2.add(b2);
            f2.add(b3);

            f2.addWindowListener(new WindowAdapter(){
                //Close the entire application.
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        int a = JOptionPane.showConfirmDialog(f2, "Are you sure to quit?");
                        if(a == JOptionPane.YES_OPTION){
                            con.close();
                            f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                        }
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });

            f2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f2.setSize(400,550);
            f2.getContentPane().setBackground(new Color(204,255,204));
            f2.setLayout(null);
            f2.setVisible(true);

            //Dashboard Frame
            f3 = new JFrame("Dashboard");

            l9 = new JLabel("Hello");
            l9.setFont(new Font("Consolas", Font.PLAIN, 25));
            l9.setBounds(20,120,100,30);

            l10 = new JLabel("");
            l10.setFont(new Font("Consolas", Font.BOLD, 45));
            l10.setForeground(new Color (204,0,204));
            l10.setBounds(20,160,350,60);

            l11 = new JLabel("<html>Thanks for trying out<br>" +
                            "</br> this application :)</html>");
            l11.setFont(new Font("Consolas", Font.PLAIN, 25));
            l11.setBounds(20,230,350,60);

            l12 = new JLabel("....");
            l12.setFont(new Font("Consolas", Font.PLAIN, 50));
            l12.setBounds(130,380,120,60);

            f3.add(l9);
            f3.add(l10);
            f3.add(l11);
            f3.add(l12);

            f3.addWindowListener(new WindowAdapter(){
                //Close the Dashboard window.
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        int a = JOptionPane.showConfirmDialog(f3, "Are you sure to logout?");
                        if(a == JOptionPane.YES_OPTION){
                            f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            l10.setText("");
                            tf3.setText("");
                            pf3.setText("");
                            getLoginFrame();
                        }
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });

            f3.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f3.setSize(400,550);
            f3.getContentPane().setBackground(new Color(204,255,204));
            f3.setLayout(null);
            f3.setVisible(false);

            //Signup Frame
            f = new JFrame("Signup");

            l5 = new JLabel("Sign Up");
            l5.setFont(new Font("Corbel", Font.PLAIN, 35));
            l5.setForeground(new Color(0,128,255));
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
            b.setBackground(new Color(0,128,255));
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
                //Close the Signup window.
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        int a = JOptionPane.showConfirmDialog(f, "Are you sure to not Signup now?");
                        if(a == JOptionPane.YES_OPTION){
                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            getLoginFrame();
                        }
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }
                }
            });

            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.setSize(400,550);
            f.getContentPane().setBackground(new Color(204,255,204));
            f.setLayout(null);
            f.setVisible(false);
        }
        else {
            System.out.println("Error: Failed to connect to the database.");
        }
    }

    //Define the action performed when clicking the buttons.
    @Override
    public void actionPerformed(ActionEvent e) {
        Object button = e.getSource();
        
        if(button == b2) {
            String email = tf3.getText();
            boolean bool1 = validateEmail(email);
            
            String pass = String.valueOf(pf3.getPassword());
            boolean bool2 = !pass.isEmpty();

            boolean bool3 = verifyUser(email, pass);

            if(bool1 && bool2 && bool3) {
                getDashboardFrame();
            }
            else {
                if(!bool1) {
                    if(email.isEmpty()) {
                        JOptionPane.showMessageDialog(f2,
                            "Email field cannot be left blank.","Alert",
                             JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        tf2.setText("");
                        JOptionPane.showMessageDialog(f2,
                            "Please enter a valid email address.","Alert",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(!bool2) {
                    JOptionPane.showMessageDialog(f2,
                        "Password field cannot be left blank.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
                else if(!bool3) {
                    JOptionPane.showMessageDialog(f2,
                        "Username or Password is Invalid.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        } 
        else if(button == b3) {
            getSignupFrame();
        }
        else if(button == b){ 
            String name = tf1.getText();
            boolean bool1 = validateName(name);
            
            String email = tf2.getText();
            boolean bool2 = validateEmail(email);
            
            String pass = String.valueOf(pf1.getPassword());
            boolean bool3 = validatePassword(pass);
            
            String conf = String.valueOf(pf2.getPassword());
            boolean bool4 = validateConfirmPassword(pass,conf);

            if(bool1 && bool2 && bool3 && bool4) {
                boolean bool = addMember(name, email, pass);
                if(bool) {
                    tf1.setText("");
                    tf2.setText("");
                    pf1.setText("");
                    pf2.setText("");
                    JOptionPane.showMessageDialog(f, "Your account has been created successfully.");
                    getLoginFrame();
                }
            }
            else {
                if(!bool1) {
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
                else if(!bool2) {
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
                else if(!bool3) {
                    if(pass.isEmpty()) {
                        JOptionPane.showMessageDialog(f,
                            "Password field cannot be left blank.","Alert",
                            JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        pf1.setText("");
                        pf2.setText("");
                        JOptionPane.showMessageDialog(f,
                            "Password must have atleast 6 characters.","Alert",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(!bool4) {
                    pf1.setText("");
                    pf2.setText("");
                    JOptionPane.showMessageDialog(f,
                        "Please provide same passwords.","Alert",
                        JOptionPane.WARNING_MESSAGE);
                }
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
            ps = con.prepareStatement("INSERT INTO users (name,email,password) VALUES (?,?,?);");
            ps.setString(1, name);
            ps.setString(2, mail);
            ps.setString(3, pass);
            int i = ps.executeUpdate();
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

    //Goto the Login Frame.
    private void getLoginFrame() {
        f2.setVisible(true);
        f.setVisible(false);
        f3.setVisible(false);
    }

    //Goto the Dashboard Frame.
    private void getDashboardFrame() {
        f3.setVisible(true);
        f2.setVisible(false);
    }

    //Goto the Signup Frame.
    private void getSignupFrame() {
        f.setVisible(true);
        f2.setVisible(false);
    }
    
    //Display the name of the user in the dashboard.
    private void renderUsername(String name) {
        l10.setText(name + "!");
    }

    //Verify the login details entered by the user.
    private boolean verifyUser(String email, String pass) {
        try {
            ps = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?;");
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if(rs.next()) {
                String[] s = rs.getString("name").split(" ");
                renderUsername(s[0]);
                return true;
            }
        }
        catch(SQLException se) {
            System.out.println(se);
        }
        return false;
    }

    //Main method.
    public static void main(String args[]) {
        new LoginApp();
    }
}