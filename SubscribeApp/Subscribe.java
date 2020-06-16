import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Subscribe implements ActionListener {

    private Connection con;
    private Statement st;
    private PreparedStatement stmt;
    private ResultSet rs;
    private int subscribersToll = 0;
    private int i = 0;

    private JFrame f;
    private JLabel l1, l2, l3, l4, l5, l6;
    private JTextField tf;
    private JButton b;
    private JDialog d;

    //Establish connection to database and initialize GUI window.
    public void startUp() throws SQLException {
        
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_db", "<username>", "<password>");
        st = con.createStatement();
        rs = st.executeQuery("SELECT COUNT(*) FROM subscribers;");
        rs.next();
        subscribersToll = rs.getInt(1);

        f = new JFrame("Subscribe");

        l1 = new JLabel("Subscribe to our newsletters");
        l1.setFont(new Font("Consolas", Font.BOLD, 30));
        l1.setForeground(Color.WHITE);
        l1.setBounds(30,40,480,30);

        l2 = new JLabel("<html>Sign up here to get the latest news, updates and special offers"
                        + "<br/>delivered directly to your inbox.<html>");
        l2.setFont(new Font("Consolas", Font.PLAIN, 18));
        l2.setForeground(Color.WHITE);
        l2.setBounds(30,100,700,40);

        l3 = new JLabel("Join " + subscribersToll + " other subscribers");
        l3.setFont(new Font("Consolas", Font.PLAIN, 18));
        l3.setForeground(Color.WHITE);
        l3.setBounds(30,180,260,20);

        l4 = new JLabel("Enter your email");
        l4.setFont(new Font("Consolas", Font.BOLD, 18));
        l4.setForeground(Color.WHITE);
        l4.setBounds(30,240,260,20);

        tf = new JTextField();
        tf.setFont(new Font("Ariel", Font.PLAIN, 18));
        tf.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        tf.setBounds(30,265,350,30);

        b = new JButton("Subscribe");
        b.setFont(new Font("Ariel", Font.PLAIN, 18));
        b.setForeground(Color.WHITE);
        b.setBackground(Color.RED);
        b.setBounds(385,265,130,30);
        b.setFocusable(false);
        b.setBorder(BorderFactory.createLineBorder(Color.RED));
        b.addActionListener(this);

        l5 = new JLabel("We will not share your information with anyone");
        l5.setFont(new Font("Consolas", Font.PLAIN, 15));
        l5.setForeground(Color.WHITE);
        l5.setBounds(30,320,400,15);

        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(l4);
        f.add(tf);
        f.add(b);
        f.add(l5);
        
        f.addWindowListener(new WindowAdapter(){
            //Define the action when window is closed.
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
        f.setSize(800,400);
        f.getContentPane().setBackground(new Color(0, 51, 102));
        f.setLayout(null);
        f.setVisible(true);
    }

    //Define the action when button is clicked.
    @Override
    public void actionPerformed(ActionEvent e) {
        String mail = tf.getText();
        String emailPattern = "^([a-z0-9]{1}[a-z0-9-_.]+)@(?:[a-zA-Z0-9-]+\\.)+([a-zA-Z]{2,6})$";
        tf.setText("");
        
        if(mail.toLowerCase().matches(emailPattern)) {
            addSubscriber(mail);
        }
        else {
            JOptionPane.showMessageDialog(f,
                "You have entered an invalid email address.","Alert",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    //Dynamic label that shows subscribers count.
    private void displaySubscribersCount() throws SQLException {
        rs = st.executeQuery("SELECT COUNT(*) FROM subscribers;");
        rs.next();
        subscribersToll = rs.getInt(1);
        l3.setText("Join " + subscribersToll + " other subscribers");
    }

    //Insert subscriber into the database.
    private void addSubscriber(String mail) {
        try {
            stmt = con.prepareStatement("INSERT INTO subscribers (email) VALUES (?);");
            stmt.setString(1, mail);
            i += stmt.executeUpdate();
            displaySubscribersCount();
        }
        catch(SQLIntegrityConstraintViolationException sie) {
            JOptionPane.showMessageDialog(f,
                "You have already subscribed.","Alert",
                JOptionPane.WARNING_MESSAGE);
        }
        catch(SQLException se) {
            System.out.println(se);
        }
    }

    //Main method.
    public static void main(String[] args) {
        try {
            Subscribe s = new Subscribe();
            s.startUp();
        }
        catch(Exception e) {
            System.out.println("Error: Failed to connect to the database.");
        }   
    }
}