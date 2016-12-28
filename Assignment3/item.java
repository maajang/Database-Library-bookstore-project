
package ca.ubc.cpsc304.library.models;

// We need to import the java.sql package to use JDBC
import java.sql.*;
// for reading from the command line
import java.io.*;

// for the login window
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/*
 * This class implements a graphical login window and a simple text
 * interface for interacting with the item table
 */
public class item implements ActionListener
{
    // command line reader
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    private Connection con;
    
    // user is allowed 3 login attempts
    private int loginAttempts = 0;
    
    // components of the login window
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame mainFrame;
    
    
    
    /*
     * constructs login window and loads JDBC driver
     */
    public item()
    {
        mainFrame = new JFrame("User Login");
        
        JLabel usernameLabel = new JLabel("Enter username: ");
        JLabel passwordLabel = new JLabel("Enter password: ");
        
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        
        JButton loginButton = new JButton("Log In");
        
        JPanel contentPane = new JPanel();
        mainFrame.setContentPane(contentPane);
        
        
        // layout components using the GridBag layout manager
        
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(usernameLabel, c);
        contentPane.add(usernameLabel);
        
        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        contentPane.add(usernameField);
        
        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        contentPane.add(passwordLabel);
        
        // place the password field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        contentPane.add(passwordField);
        
        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        contentPane.add(loginButton);
        
        // register password field and OK button with action event handler
        passwordField.addActionListener(this);
        loginButton.addActionListener(this);
        
        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter()
                                    {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        // size the window to obtain a best fit for the components
        mainFrame.pack();
        
        // center the frame
        Dimension d = mainFrame.getToolkit().getScreenSize();
        Rectangle r = mainFrame.getBounds();
        mainFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        
        // make the window visible
        mainFrame.setVisible(true);
        
        // place the cursor in the text field for the username
        usernameField.requestFocus();
        
        try
        {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }
    }
    
    /*
     * connects to Oracle database named ug using user supplied username and password
     */
    private boolean connect(String username, String password)
    {
        String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
        
        try
        {
            con = DriverManager.getConnection(connectURL,username,password);
            
            System.out.println("\nConnected to Oracle!");
            return true;
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }
    
    
    /*
     * event handler for login window
     */
    public void actionPerformed(ActionEvent e)
    {
        if ( connect(usernameField.getText(), String.valueOf(passwordField.getPassword())) )
        {
            // if the username and password are valid,
            // remove the login window and display a text menu
            mainFrame.dispose();
            showMenu();
        }
        else
        {
            loginAttempts++;
            
            if (loginAttempts >= 3)
            {
                mainFrame.dispose();
                System.exit(-1);
            }
            else
            {
                // clear the password
                passwordField.setText("");
            }
        }
        
    }
    
    
    /*
     * displays simple text interface
     */
    private void showMenu()
    {
        int choice;
        boolean quit;
        
        quit = false;
        
        try
        {
            // disable auto commit mode
            con.setAutoCommit(false);
            
            while (!quit)
            {
                System.out.print("\n\nWelcome! Mr./Mrs. Manager/Clerk: Please choose one of the following: \n");
                System.out.print("1.  To insert item\n");
                System.out.print("2.  To remove item\n");
                System.out.print("3.  To update item\n");
                System.out.print("4.  To Show item\n");
                System.out.print("5.  To get textbooks satisfying the two conditions given\n");
                System.out.print("6.  To find top three items\n");
                System.out.print("7.  Quit\n>> ");
                
                choice = Integer.parseInt(in.readLine());
                
                System.out.println(" ");
                
                switch(choice)
                {
                    case 1:  insertItem();          break;    // represent addItem
                    case 2:  removeItem();          break;
                    case 3:  updateItem();          break;
                    case 4:  showItem();            break;
                    case 5:  courseTexts();         break;
                    case 6:  topThreeItems();       break;
                    case 7:  quit = true;
                }
            }
            
            con.close();
            in.close();
            System.out.println("\nGood Bye!\n\n");
            System.exit(0);
        }
        catch (IOException e)
        {
            System.out.println("IOException!");
            
            try
            {
                con.close();
                System.exit(-1);
            }
            catch (SQLException ex)
            {
                System.out.println("Message: " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }
    }
    
    
    /*
     * inserts an item
     */
    private void insertItem()
    {
        String             upc;
        float              sellingPrice;
        int                stock;
        String             taxable;
        PreparedStatement  ps;
        
        try
        {
            
            ps = con.prepareStatement("INSERT INTO item VALUES (?,?,?,?)");
            
            System.out.print("\nItem UPC: ");
            upc = in.readLine();
            ps.setString(1, upc);
            
            System.out.print("\nItem Price: ");
            sellingPrice = Float.parseFloat(in.readLine());
            ps.setFloat(2, sellingPrice);
            
            System.out.print("\nItem Stock: ");
            stock = Integer.parseInt(in.readLine());
            ps.setInt(3, stock);
            
            System.out.print("\nItem Taxable: ");
            taxable = in.readLine();
            if (taxable.length() == 0)
            {
                ps.setString(4, null);
            }
            else
            {
                ps.setString(4, taxable);
            }
            
            
            ps.executeUpdate();
            
            // commit work
            con.commit();
            
            ps.close();
        }
        catch (IOException e)
        {
            System.out.println("IOException!");
        }
        catch (SQLException ex)
        {
            System.out.println("Message:  " + ex.getMessage());
            try
            {
                // undo the insert
                con.rollback();
            }
            catch (SQLException ex2)
            {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
        
    }
    
    
    /*
     * deletes or removes an item
     */
    public void removeItem()
    {
        String             upc;
        PreparedStatement  ps, ps1, ps2;
        
        try
        {
            // deleting from the children (itemPurchase, book) first before deleting from the parent (item)
            ps = con.prepareStatement("DELETE FROM itemPurchase WHERE upc = ?");
            
            ps1 = con.prepareStatement("DELETE FROM book WHERE upc = ?");
            
            ps2 = con.prepareStatement("DELETE FROM item WHERE upc = ?");
            
            
            
            System.out.print("\nitemPurchase upc: ");
            upc = in.readLine();
            ps.setString(1, upc);
            
            System.out.print("\nbook upc: ");
            upc = in.readLine();
            ps1.setString(1, upc);
            
            System.out.print("\nitem upc: ");
            upc = in.readLine();
            ps2.setString(1, upc);
            
            
            
            ps.execute();
            ps1.execute();
            ps2.execute();
            
            con.commit();
            
            ps.close();
        }
        catch (IOException e)
        {
            System.out.println("IOException!");
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            
            try
            {
                con.rollback();
            }
            catch (SQLException ex2)
            {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
            
        }
    }
    private void updateItem()
    {
        String               upc;
        Float              sellingPrice;
        PreparedStatement    ps;
        
        try
        {
            ps = con.prepareStatement("UPDATE item SET sellingPrice = ? WHERE upc = ?");
            
            System.out.print("\nupc: ");
            upc = in.readLine();
            ps.setString(2, upc);
            
            System.out.print("\nsellingPrice: ");
            sellingPrice = Float.parseFloat(in.readLine());
            ps.setFloat(1, sellingPrice);
            
            int rowCount = ps.executeUpdate();
            if (rowCount == 0)
            {
                System.out.println("\nItem " + upc + " does not exist!");
            }
            
            con.commit();
            
            ps.close();
        }
        catch (IOException e)
        {
            System.out.println("IOException!");
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
            
            try
            {
                con.rollback();
            }
            catch (SQLException ex2)
            {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }
    // display information about branches
    
    private void showItem()
    {
        String             upc;
        String             sellingPrice;
        String             stock;
        String             taxable;
        Statement  stmt;
        ResultSet  rs;
        
        try
        {
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("SELECT * FROM item");
            
            // get info on ResultSet
            ResultSetMetaData rsmd = rs.getMetaData();
            
            // get number of columns
            int numCols = rsmd.getColumnCount();
            
            System.out.println(" ");
            
            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it
                
                System.out.printf("%-15s", rsmd.getColumnName(i+1));
            }
            
            System.out.println(" ");
            
            while(rs.next())
            {
                // for display purposes get everything from Oracle
                // as a string
                
                // simplified output formatting; truncation may occur
                
                upc = rs.getString("upc");
                System.out.printf("%-10.10s", upc);
                
                sellingPrice = rs.getString("sellingPrice");
                System.out.printf("%-20.20s", sellingPrice);
                
                stock = rs.getString("stock");
                System.out.printf("%-20.20s",stock);
                
                taxable = rs.getString("taxable");
                if (rs.wasNull())
                {
                    System.out.printf("%-10.10s", " ");
                }
                else
                {
                    System.out.printf("%-10.10s\n", taxable);
                }
                
            }
            
            
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }
    }
    
    // part 3 (i) and (ii)
    public void courseTexts(){
        
        //String t_id;
        String upc;
        String title;
        String quantity;
        // String stock;
        Statement stmt;
        ResultSet rs;
        try{
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("select upc, title, quantity from "
                                   + "(select b.upc, b.title, SUM(ip.quantity) AS quantity"
                                   + " from itemPurchase ip,item i, book b, purchase p "
                                   + " where ip.t_id = p.t_id"
                                   + " and i.upc=b.upc and b.upc=ip.upc and ip.t_id = p.t_id "
                                   + " and 'purchaseDate' > '15-OCT-25' and 'quantity'>'50' "
                                   + " group by b.upc, b.title order by quantity DESC) ");
            
            
            ResultSetMetaData rsmd = rs.getMetaData();
            
            
            int numCols = rsmd.getColumnCount();
            
            System.out.println(" ");
            
            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it
                
                System.out.printf("%-15s", rsmd.getColumnName(i+1));    
            }
            
            System.out.println(" ");
            
            while(rs.next())
            {
                
                upc = rs.getString(1);
                System.out.printf("%-15.15s", upc);
                
                title = rs.getString(2);
                System.out.printf("%-50.50s", title);
                
                quantity = rs.getString(3);
                System.out.printf("%-15.15s\n",quantity);
                
            }
            
            // close the statement; 
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }	
        
        
    }
    
    
    
    // part 4 of the assignment; top three items with respect to total amount
    
    public void topThreeItems(){
        String upc;
        String title;
        Float totalamt;
        
        
        Statement stmt;
        ResultSet rs;
        try{
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("select upc, title, totalamt from "
                                   + "(select b.upc, b.title, SUM(i.sellingprice) AS totalamt"
                                   + " from itemPurchase ip,item i, book b, purchase p "
                                   + " where ip.t_id = p.t_id"
                                   + " and i.upc=b.upc and b.upc=ip.upc and ip.t_id = p.t_id "
                                   + " and 'purchaseDate' > '15-OCT-25'"
                                   + " group by b.upc, b.title order by totalamt DESC)"
                                   
                                   + " where rownum <= 3");
            ResultSetMetaData rsmd = rs.getMetaData();
            
            
            int numCols = rsmd.getColumnCount();
            
            System.out.println(" ");
            
            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it
                
                System.out.printf("%-15s", rsmd.getColumnName(i+1));    
            }
            
            System.out.println(" ");
            
            while(rs.next())
            {
                
                upc = rs.getString(1);
                System.out.printf("%-15.15s", upc);
                
                title = rs.getString(2);
                System.out.printf("%-50.50s", title);
                
                totalamt = rs.getFloat(3);
                System.out.printf("%-15.15s\n",totalamt);
                
            }
            
            // close the statement; 
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }	
        
        
    }
    
    
    public static void main(String args[])
    {
        item i = new item();
        
        
        
        
        
        
    }
}
