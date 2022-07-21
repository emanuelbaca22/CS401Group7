import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	// Implement multi-threading
	public static void main(String[] args) throws IOException {
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
        
        // Next Attempt to Login to the server
        // Load in data into a User object to send to the server in order to verify employee ID and password
        System.out.println("Enter Employee ID# ");
        int empID = sc.nextInt();
        System.out.println("Enter Password: ");
        String password = sc.next();
        
        // Load into a User object
        User login = new User(empID, password);
        
        // Pass object to Server and await if login is successful
        objectOutputStream.writeObject(login);
        objectOutputStream.flush();
        
        // Await Server response
        
        // After login is successful, continue to interface
        
        // Close any open Sockets or readers
        System.out.println("Logout Successful, closing connection Socket");
        socket.close();
        sc.close();
        
	}
}