import java.util.Scanner;
import java.util.Random;
import java.io.Console;
import java.sql.*;

public class PasswordManager {

    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private Console cons = System.console();
    private Scanner sc = new Scanner(System.in);

    //Get a command from the user to perform an operation.
    private char getCommand() {
        try {
            System.out.print("\n:> ");
            char ch = sc.nextLine().toLowerCase().charAt(0);
            
            if(ch == 'i' || ch == 'v' || ch == 'm' || ch == 'd' || ch == 'q') {
                return ch;
            }
            else {
                System.out.println("Error: Invalid command.");
                return getCommand();
            }
        }
        catch(StringIndexOutOfBoundsException e) {
            return getCommand();
        }
    }

    //Get an account name from the user to create a credential.
    private String getAccountname() {
        System.out.print("What is the Account name(Gmail, Facebook... etc)? : ");
        String acc = sc.nextLine();
        
        if(acc.isEmpty()) {
            System.out.println("Error: Account name cannot be empty.");
            return getAccountname();
        } 

        return acc;
    }

    //Get a username from the user to create a credential.
    private String getUsername() {
        System.out.print("What is the Username/Email? : ");
        String user = sc.nextLine();
        
        if(user.isEmpty()) {
            System.out.println("Error: Username/Email cannot be empty.");
            return getUsername();
        }
        return user;
    }

    //Get masked password from user.
    private String getPassword() {
        char[] pwdarr = cons.readPassword("What is the Password? : ");
        String pass = String.valueOf(pwdarr);
        return pass;
    }

    //Get the credential id from the user.
    private int getCredentialID() {
        try {
            System.out.print("What is the credential id? : ");
            int id = Integer.parseInt(sc.nextLine());
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM password_store WHERE id = " + id + ";");
            
            if(!rs.next()) {
                throw new NumberFormatException();
            }
            return id;
        }
        catch(NumberFormatException ne) {
            System.out.println("Error: Invalid ID.");
            return getCredentialID();
        }
        catch(SQLException se) {
            System.out.println(se);
            return getCredentialID();
        }
    }

    //Check whether locker is empty.
    private boolean isLockerEmpty() {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM password_store;");
            if(!rs.next()) {
                return true;
            }
            return false;
        }
        catch(SQLException se) {
            return false;
        }
    }

    //Insert a new credential into the database.
    private void insertCredential() {
        try {
            String acc = getAccountname();
            String user = getUsername();
            String pass = getPassword();
            
            ps = con.prepareStatement("INSERT INTO password_store (account_name,username,passcode) VALUES (?,?,?);");
            ps.setString(1, acc);
            ps.setString(2, user);
            ps.setString(3, pass);
            
            int i = ps.executeUpdate();
            System.out.println("Credential inserted successfully.");
        }
        catch(SQLIntegrityConstraintViolationException se) {
            System.out.println("Error: You already stored a credential for that account.");
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    //Read all credentials stored in the database.
    private void viewCredential() {
        try {
            boolean emptyLocker = isLockerEmpty();
            if(emptyLocker) {
                System.out.println("Error: Currently you don't have any passwords in the locker.");
            }
            else {
                do {
                    System.out.println("|");
                    System.out.println("|---|id      : " + rs.getInt("id"));
                    System.out.println("|   |Account : " + rs.getString("account_name"));
                    System.out.println("|   |Username: " + rs.getString("username"));
                    System.out.println("|---|Password: " + rs.getString("passcode"));
                }while(rs.next());
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    //Modify an existing credential in the database.
    private void modifyCredential() {
        try {
            boolean emptyLocker = isLockerEmpty();
            if(emptyLocker) {
                System.out.println("Error: Currently you don't have any passwords in the locker.");
            }
            else {
                int id = getCredentialID();
                String acc = getAccountname();
                String user = getUsername();
                String pass = getPassword();
                
                ps = con.prepareStatement("UPDATE password_store " + 
                                          "SET account_name = ?, username = ?, passcode = ? " +
                                          "WHERE id = ?");
                ps.setString(1, acc);
                ps.setString(2, user);
                ps.setString(3, pass);
                ps.setInt(4, id);
                
                int i = ps.executeUpdate();
                System.out.println("Credential modified successfully.");
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    //Delete a credential from the database.
    private void deleteCredential() {
        try {
            boolean emptyLocker = isLockerEmpty();
            if(emptyLocker) {
                System.out.println("Error: Currently you don't have any passwords in the locker.");
            }
            else {
                int id = getCredentialID();
                ps = con.prepareStatement("DELETE FROM password_store WHERE id = ?");
                ps.setInt(1, id);
                
                int i = ps.executeUpdate();
                System.out.println("Credential deleted successfully.");
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    //Perform various operations by reading a valid command.
    private void accessLocker() {
        System.out.println("Use these commands to perform various operations.");
        System.out.println("-".repeat(34) + "\n" +
                           "i: insert a new credential.\n" +
                           "v: view all existing credentials.\n" +
                           "m: modify a credential.\n" +
                           "d: delete a credential.\n" +
                           "q: quit the application.\n" +
                           "-".repeat(34));
        
        char ch = getCommand();
        
        while(ch != 'q') {
            if(ch == 'i') {
                insertCredential();
            }
            else if(ch == 'v') {
                viewCredential();
            }
            else if(ch == 'm') {
                modifyCredential();
            }
            else if(ch == 'd') {
                deleteCredential();
            }
            ch = getCommand();
        }
        if(ch == 'q') {
            System.out.println("\nLocker Locked and Program Terminated.");
            try {
                con.close();
            } catch(Exception e) {
                System.out.println(e);
            }
            System.exit(0);
        }
    }

    //Authenticate and connect to the database.
    private boolean openLocker() {
        if(cons == null) {
            System.out.println("\nError: Console not found.. Application cannot run.");
            System.exit(0);
        }

        System.out.println("\n" + "=".repeat(15) + " PASSWORD MANAGER " + "=".repeat(15) + "\n");
        
        char[] usrarr = cons.readPassword("Username : ");
        char[] pwdarr = cons.readPassword("Password : ");
        
        try {
            String url = "jdbc:mysql://localhost:3306/java_db";
            String username = String.valueOf(usrarr);
            String password = String.valueOf(pwdarr);
            con = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException e) {
            return false;
        }
        return true;
    }

    //Main method.
    public static void main(String[] args) {
        PasswordManager p = new PasswordManager();
        
        boolean b = p.openLocker();
        if(b) {
            System.out.println("\nLocker Unlocked.\n");
            p.accessLocker();
        }
        else {
            System.out.println("Error: Username or Password you have entered is wrong.");
        }
    }
}
