import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
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
            	// Set our loggedIn loop to true
            	loggedIn = true;
            }
            
            while(loggedIn) {
            	// Apply User interface, whether it is an IT interface or a User
            	// Tell Server to set status to active
            	System.out.println("Check worked, in command loop now :)");
            }
        }
        
        // Close any open Sockets or readers
        System.out.println("Logout Successful, closing connection Socket");
        socket.close();
        sc.close();
        
	}
}