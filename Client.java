import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Attempt to Implement GUI
public class Client extends JFrame implements ActionListener{
	
	JLabel labelEmpNum;
	JLabel labelPassword;
	JTextField txtEmpNum;
	JTextField txtPassword;
	JButton btnLogin;
	JTextArea txtS;
	 
	// Frame Layout
	public Client() {
        this.setTitle("Client Login");
        this.setSize(420, 420);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        labelEmpNum = new JLabel("Employee Number: ");
        labelEmpNum.setBounds(10, 10, 90, 21);
        add(labelEmpNum);

        txtEmpNum = new JTextField();
        txtEmpNum.setBounds(105, 10, 90, 21);
        add(txtEmpNum);

        labelPassword = new JLabel("Password: ");
        labelPassword.setBounds(10, 35, 90, 21);
        add(labelPassword);
        
        txtPassword = new JTextField();
        txtPassword.setBounds(105, 35, 90, 21);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(200, 40, 90, 21);
        btnLogin.addActionListener(this);
        add(btnLogin);

        txtS = new JTextArea();
        txtS.setBounds(10, 85, 290, 120);
        add(txtS);

        this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Client();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		 try {
             processInformation();
         } catch (UnknownHostException e1) {
             e1.printStackTrace();
         } catch (IOException e1) {
             e1.printStackTrace();
         } catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
     }
	
	public void processInformation() throws UnknownHostException, IOException, ClassNotFoundException {
		// Create a scanner object in order to get input stream
		// Use System.in as it is a standard input stream
		Scanner sc= new Scanner(System.in);
		User clientUser = null;
        User newUser = null;
        Message chat = null;
        Boolean loop = true;
        Boolean loggedIn = false;
		
		Socket socket = new Socket("localhost", 2022);
        // Output stream socket to get our new msg ready
        OutputStream outputStream = socket.getOutputStream();

        // Create object output stream from the output stream to send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        
        // Create input Stream to receive messages from the server
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;

        int clientEmpNum = Integer.parseInt(txtEmpNum.getText());
        String clientPassword = txtPassword.getText();

     // Load into a Message object
        // Passing in as msgSize and data of Message Class
        Message login = new Message(clientEmpNum, clientPassword);
        
        // Pass object to Server and await if login is successful
        System.out.println("Sending Login Credentials to Server");
        objectOutputStream.writeObject(login);
        objectOutputStream.flush();
        
        inputStream = socket.getInputStream();
        // Await Server response
        objectInputStream = new ObjectInputStream(inputStream);
        Message msgServer = (Message) objectInputStream.readObject();
        
        // Server responded, so print out what it said back
        System.out.println("Server Response: " + msgServer.getData());
        
        // After login is successful, continue to interface
        if(msgServer.getData().equals("Login Successful, continue to Interface"))
        {
        	// Print a message saying Loading Application
        	System.out.println("Loading Application...");
        	// Get back out Client User Account from the server so we can use it
        	clientUser = (User) objectInputStream.readObject();
        	// Print out the Client info just to make sure the object was received
        	System.out.println("Displaying Logged in User Info: "
        			+ "\n First Name: " + clientUser.getFirstName()
        			+ "\n Last Name: " + clientUser.getLastName()
        			+ "\n Employee Number: " + clientUser.getEmpNum()
        			+ "\n Role: " + clientUser.getRole());
        	// Set our loggedIn loop to true
        	loggedIn = true;
        }  
        while(loggedIn) {
        	//UserInterfaceGUI userGUI = new UserInterfaceGUI();
        	// Apply User interface, whether it is an IT interface or a User
        	// Tell Server to set status to active
        	// System.out.println("Check worked, in command loop now :)");
        	// Start by checking what user interface to display, either a Standard
        	// User login or an IT User Interface
        	
        	// Implement later, make sure to test and get up User Interface 1st!
        	// Implementation of User Interface
        	if(clientUser.getRole().equals("User"))
        	{
        		// Implement User interface
        		System.out.println("\n\nEnter a option number: ");
        		System.out.println("1) Send a Message"
        				+ "\n2) View Chat History"
        				+ "\n3) Create a New Group "
        				+ "\n4) Log Out");
        		// load in User choice into a variable
        		int choice = sc.nextInt();
        		// Put it into our Message Object then send it off to the Server to manage next steps
        		Message userChoice = new Message(choice);
        	
        		// Send to Server
        		objectOutputStream.writeObject(userChoice);
        		objectOutputStream.flush();
        		System.out.println("Sending option to Server");
        	
        		// load switch interface
        		switch(choice)
        		{
        		case 1:
        			// This will be the sendMessage() method handler
        			// sendMessage() will return a Message Object that we can send to the Server
        			System.out.println("Enter First Name of Recipient: ");
        			String toFirst = sc.next();
        			System.out.println("Enter Last Name of Recipient: ");
        			String toLast = sc.next();
        			// use sc.nextLine to clear the skipping line
        			sc.nextLine();
        			System.out.println("Enter the Message: ");
        			String data = sc.nextLine();
        		
        			// Create a Message object to be send to the Server 
        			Message msg = new Message(clientUser.getFirstName(), clientUser.getLastName(), toFirst, toLast, data);
        		
        			// Send Message object to the Server
        			objectOutputStream.writeObject(msg);
        			objectOutputStream.flush();
        		
        			System.out.println("Message has been sent");
        		
        			break;
        		case 2:
        			// This will be the chatHistory() method handler
        			System.out.println("\nChat History is Below: ");
				
        			// Sending Server Client Profile
        			objectOutputStream.writeObject(clientUser);
        			objectOutputStream.flush();
				
        			// Read in what Server has sent back
        			chat = (Message) objectInputStream.readObject();
				
        			// Print out chat history
        			System.out.println(chat.getData());
				
        			break;
        		case 3:
        			// This will be the createGroup() method handler
        			// Instead of creating a Group, why not just send Messages to multiple
        			
        			break;
        		case 4:
        			// This will be the logOut() method handler
        			System.out.println("\nLogging out of System");
				
        			// Sending Server Client Profile
        			objectOutputStream.writeObject(clientUser);
        			objectOutputStream.flush();
				
        			// break out of loops
        			loggedIn = false;
        			loop = false;
				
        			break;
        		default:
        			break;								
        		}
        	}
        	else
        	{
        		// Implement IT Interface
        		System.out.println("\n\nEnter a option number: ");
        		System.out.println("1) Send a Message"
        				+ "\n2) View Chat History"
        				+ "\n3) Create a New Group "
        				+ "\n4) Create a New User"
        				+ "\n5) View ALL CHAT LOG FILE"
        				+ "\n6) Log Out");
        		// load in User choice into a variable
        		int choice = sc.nextInt();
        		// Put it into our Message Object then send it off to the Server to manage next steps
        		Message userChoice = new Message(choice);
        	
        		// Send to Server
        		objectOutputStream.writeObject(userChoice);
        		objectOutputStream.flush();
        		System.out.println("Sending option to Server");
        	
        		// load switch interface
        		switch(choice)
        		{
        		case 1:
        			// This will be the sendMessage() method handler
        			// sendMessage() will return a Message Object that we can send to the Server
        			System.out.println("Enter First Name of Recipient: ");
        			String toFirst = sc.next();
        			System.out.println("Enter Last Name of Recipient: ");
        			String toLast = sc.next();
        			// use sc.nextLine to clear the skipping line
        			sc.nextLine();
        			System.out.println("Enter the Message: ");
        			String data = sc.nextLine();
        		
        			// Create a Message object to be send to the Server 
        			Message msg = new Message(clientUser.getFirstName(), clientUser.getLastName(), toFirst, toLast, data);
        		
        			// Send Message object to the Server
        			objectOutputStream.writeObject(msg);
        			objectOutputStream.flush();
        		
        			System.out.println("Message has been sent");
        		
        			break;
        		case 2:
        			// This will be the chatHistory() method handler
        			System.out.println("\nChat History is Below: ");
				
        			// Sending Server Client Profile
        			objectOutputStream.writeObject(clientUser);
        			objectOutputStream.flush();
				
        			// Read in what Server has sent back
        			chat = (Message) objectInputStream.readObject();
				
        			// Print out chat history
        			System.out.println(chat.getData());
				
        			break;
        		case 3:
        			// This will be the createGroup() method handler
        			break;
        		case 4:
        			// This will be the createNewUser() method handler
        			System.out.println("\nEnter New Employee First Name: ");
        			String newFirst = sc.next();
        			System.out.println("\nEnter New Employee Last Name: ");
        			String newLast = sc.next();
        			System.out.println("\nEnter New Employee Number: ");
        			int newEmpNum = sc.nextInt();
        			// Clear nextLine error
        			sc.nextLine();
        			System.out.println("\nEnter New Employee Password: ");
        			String newPassword = sc.nextLine();
        			System.out.println("\nEnter New Employee Role: ");
        			String newRole = sc.next();
        			
        			// Package up newly created user and pass into Server
        			newUser = new User(newFirst, newLast, newEmpNum, newPassword, newRole);
        			
        			// Send off to server
        			objectOutputStream.writeObject(newUser);
        			objectOutputStream.flush();
        			break;
        		case 5:
        			// This will be viewLogFile() method handler
        			System.out.println("\nEntire Log File Displayed Below:");
        			
        			// get Server Chat Log
        			chat = (Message) objectInputStream.readObject();
        			
        			// Display Chat Log Contents
        			System.out.println(chat.getData());
        			break;
        		case 6:
        			// This will be the logOut() method handler
        			System.out.println("\nLogging out of System");
				
        			// Sending Server Client Profile
        			objectOutputStream.writeObject(clientUser);
        			objectOutputStream.flush();
				
        			// break out of loops
        			loggedIn = false;
        			loop = false;
				
        			break;
        		default:
        			break;								
        		}
        	}
        }
     // Close any open Sockets or readers
        System.out.println("Logout Successful, closing connection Socket");
        socket.close();
        sc.close();
    }
}
    
    



/*public class Client {
	// Implement multi-threading
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Create a scanner object in order to get input stream
		// Use System.in as it is a standard input stream
        Scanner sc= new Scanner(System.in);
        
        // Connect to the server via TCP/IP
        System.out.print("Enter the port number to connect to: <2022>");
        int port = sc.nextInt();
        // Display local IP address using java networking 
        InetAddress localhost = InetAddress.getLocalHost(); 
        System.out.println("IPV4 Address : " + 
                    (localhost.getHostAddress()).trim());
        
        // Take in user IP address to connect to the server
        System.out.print("Enter the host address to connect to: <localhost> ");
        // load it into string variable host by using scanner object
        String host = sc.next();

        // Create a socket object using the input of host and port: format is host:port
        Socket socket = new Socket(host, port);
        // Just to confirm that the connection was successful 
        System.out.println("Connected to " + host + ":" + port);
        
        // Output stream socket to get our new msg ready
        OutputStream outputStream = socket.getOutputStream();

        // Create object output stream from the output stream to send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        
        // Create input Stream to receive messages from the server
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        
        User clientUser = null;
        User newUser = null;
        Message chat = null;
        Boolean loop = true;
        Boolean loggedIn = false;
        while(loop)
        {
        	// Next Attempt to Login to the server
            // Load in data into a User object to send to the server in order to verify employee ID and password
            System.out.println("Enter Employee ID# ");
            int empID = sc.nextInt();
            // skip newline error by using another sc.nextLine()
            sc.nextLine();
            System.out.println("Enter Password: ");
            String password = sc.nextLine();
            
            // Load into a Message object
            // Passing in as msgSize and data of Message Class
            Message login = new Message(empID, password);
            
            // Pass object to Server and await if login is successful
            System.out.println("Sending Login Credentials to Server");
            objectOutputStream.writeObject(login);
            objectOutputStream.flush();
            
            inputStream = socket.getInputStream();
            // Await Server response
            objectInputStream = new ObjectInputStream(inputStream);
            Message msgServer = (Message) objectInputStream.readObject();
            
            // Server responded, so print out what it said back
            System.out.println("Server Response: " + msgServer.getData());
            
            // After login is successful, continue to interface
            if(msgServer.getData().equals("Login Successful, continue to Interface"))
            {
            	// Print a message saying Loading Application
            	System.out.println("Loading Application...");
            	// Get back out Client User Account from the server so we can use it
            	clientUser = (User) objectInputStream.readObject();
            	// Print out the Client info just to make sure the object was received
            	System.out.println("Displaying Logged in User Info: "
            			+ "\n First Name: " + clientUser.getFirstName()
            			+ "\n Last Name: " + clientUser.getLastName()
            			+ "\n Employee Number: " + clientUser.getEmpNum()
            			+ "\n Role: " + clientUser.getRole());
            	// Set our loggedIn loop to true
            	loggedIn = true;
            }
            
            while(loggedIn) {
            	// Apply User interface, whether it is an IT interface or a User
            	// Tell Server to set status to active
            	// System.out.println("Check worked, in command loop now :)");
            	// Start by checking what user interface to display, either a Standard
            	// User login or an IT User Interface
            	
            	// Implement later, make sure to test and get up User Interface 1st!
            	// Implementation of User Interface
            	if(clientUser.getRole().equals("User"))
            	{
            		// Implement User interface
            		System.out.println("Enter a option number: ");
            		System.out.println("1) Send a Message"
            				+ "\n2) View Chat History"
            				+ "\n3) Create a New Group "
            				+ "\n4) Log Out");
            		// load in User choice into a variable
            		int choice = sc.nextInt();
            		// Put it into our Message Object then send it off to the Server to manage next steps
            		Message userChoice = new Message(choice);
            	
            		// Send to Server
            		objectOutputStream.writeObject(userChoice);
            		objectOutputStream.flush();
            		System.out.println("Sending option to Server");
            	
            		// load switch interface
            		switch(choice)
            		{
            		case 1:
            			// This will be the sendMessage() method handler
            			// sendMessage() will return a Message Object that we can send to the Server
            			System.out.println("Enter First Name of Recipient: ");
            			String toFirst = sc.next();
            			System.out.println("Enter Last Name of Recipient: ");
            			String toLast = sc.next();
            			// use sc.nextLine to clear the skipping line
            			sc.nextLine();
            			System.out.println("Enter the Message: ");
            			String data = sc.nextLine();
            		
            			// Create a Message object to be send to the Server 
            			Message msg = new Message(clientUser.getFirstName(), clientUser.getLastName(), toFirst, toLast, data);
            		
            			// Send Message object to the Server
            			objectOutputStream.writeObject(msg);
            			objectOutputStream.flush();
            		
            			System.out.println("Message has been sent");
            		
            			break;
            		case 2:
            			// This will be the chatHistory() method handler
            			System.out.println("\nChat History is Below: ");
					
            			// Sending Server Client Profile
            			objectOutputStream.writeObject(clientUser);
            			objectOutputStream.flush();
					
            			// Read in what Server has sent back
            			chat = (Message) objectInputStream.readObject();
					
            			// Print out chat history
            			System.out.println(chat.getData());
					
            			break;
            		case 3:
            			// This will be the createGroup() method handler
            			// Instead of creating a Group, why not just send Messages to multiple
            			
            			break;
            		case 4:
            			// This will be the logOut() method handler
            			System.out.println("\nLogging out of System");
					
            			// Sending Server Client Profile
            			objectOutputStream.writeObject(clientUser);
            			objectOutputStream.flush();
					
            			// break out of loops
            			loggedIn = false;
            			loop = false;
					
            			break;
            		default:
            			break;								
            		}
            	}
            	else
            	{
            		// Implement IT Interface
            		System.out.println("Enter a option number: ");
            		System.out.println("1) Send a Message"
            				+ "\n2) View Chat History"
            				+ "\n3) Create a New Group "
            				+ "\n4) Create a New User"
            				+ "\n5) View ALL CHAT LOG FILE"
            				+ "\n6) Log Out");
            		// load in User choice into a variable
            		int choice = sc.nextInt();
            		// Put it into our Message Object then send it off to the Server to manage next steps
            		Message userChoice = new Message(choice);
            	
            		// Send to Server
            		objectOutputStream.writeObject(userChoice);
            		objectOutputStream.flush();
            		System.out.println("Sending option to Server");
            	
            		// load switch interface
            		switch(choice)
            		{
            		case 1:
            			// This will be the sendMessage() method handler
            			// sendMessage() will return a Message Object that we can send to the Server
            			System.out.println("Enter First Name of Recipient: ");
            			String toFirst = sc.next();
            			System.out.println("Enter Last Name of Recipient: ");
            			String toLast = sc.next();
            			// use sc.nextLine to clear the skipping line
            			sc.nextLine();
            			System.out.println("Enter the Message: ");
            			String data = sc.nextLine();
            		
            			// Create a Message object to be send to the Server 
            			Message msg = new Message(clientUser.getFirstName(), clientUser.getLastName(), toFirst, toLast, data);
            		
            			// Send Message object to the Server
            			objectOutputStream.writeObject(msg);
            			objectOutputStream.flush();
            		
            			System.out.println("Message has been sent");
            		
            			break;
            		case 2:
            			// This will be the chatHistory() method handler
            			System.out.println("\nChat History is Below: ");
					
            			// Sending Server Client Profile
            			objectOutputStream.writeObject(clientUser);
            			objectOutputStream.flush();
					
            			// Read in what Server has sent back
            			chat = (Message) objectInputStream.readObject();
					
            			// Print out chat history
            			System.out.println(chat.getData());
					
            			break;
            		case 3:
            			// This will be the createGroup() method handler
            			break;
            		case 4:
            			// This will be the createNewUser() method handler
            			System.out.println("\nEnter New Employee First Name: ");
            			String newFirst = sc.next();
            			System.out.println("\nEnter New Employee Last Name: ");
            			String newLast = sc.next();
            			System.out.println("\nEnter New Employee Number: ");
            			int newEmpNum = sc.nextInt();
            			// Clear nextLine error
            			sc.nextLine();
            			System.out.println("\nEnter New Employee Password: ");
            			String newPassword = sc.nextLine();
            			System.out.println("\nEnter New Employee Role: ");
            			String newRole = sc.next();
            			
            			// Package up newly created user and pass into Server
            			newUser = new User(newFirst, newLast, newEmpNum, newPassword, newRole);
            			
            			// Send off to server
            			objectOutputStream.writeObject(newUser);
            			objectOutputStream.flush();
            			break;
            		case 5:
            			// This will be viewLogFile() method handler
            			System.out.println("\nEntire Log File Displayed Below:");
            			
            			// get Server Chat Log
            			chat = (Message) objectInputStream.readObject();
            			
            			// Display Chat Log Contents
            			System.out.println(chat.getData());
            			break;
            		case 6:
            			// This will be the logOut() method handler
            			System.out.println("\nLogging out of System");
					
            			// Sending Server Client Profile
            			objectOutputStream.writeObject(clientUser);
            			objectOutputStream.flush();
					
            			// break out of loops
            			loggedIn = false;
            			loop = false;
					
            			break;
            		default:
            			break;								
            		}
            	}
            }
        }
        
        // Close any open Sockets or readers
        System.out.println("Logout Successful, closing connection Socket");
        socket.close();
        sc.close();
        
	}
}*/