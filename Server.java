import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Create our User Records list
		List<User> userList = new ArrayList<User>();
		// Add a couple of employees to test login
		// User user1 = new User("Manny", "Baca", "password", "User");
		// User user2 = new User("John", "Smith", "password", "User");
		// userList.add(user1);
		// add(user2);
		
		///checkRecords(userList);
		
		// Create our list of Messages that we will later write to a text file
		//List<Message> msgList = new ArrayList<Message>();
		// We will implement a multi-threaded server as per Requirements
		// Step 1: Create ServerSocket Object
		ServerSocket ss = null;
		try {
			// Set the listening port to 2022
			// Display that the server is awaiting a connection
			System.out.println("Server is awaiting incoming connections...");
			ss = new ServerSocket(2022);
			ss.setReuseAddress(true);
			
			// run an infinite loop to connect an infinite amount of clients
			while(true) {
				// create a socket for incoming requests
				Socket client = ss.accept();
				
				// Show that the client is now connected to the server
				System.out.println("New Client Connected: " + client.getInetAddress().getHostAddress());
				
				// Now throw the connected Client to a new thread
				// Let the Client Handler Class take over the new thread
				ClientHandler clientSock = new ClientHandler(client);
				// Pass it onto newly created thread
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ss != null) {
				try {
					ss.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Implement ClientHandler Class
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}
		
		// Implement our run method
		public void run() {
			// loop condition
			Boolean loggedIn = false;
			
			// local msg object received from Server replies
			Message msgServer = null;
			
			// local Variable to access our Employee Data Base
			EmpDataBase localAccess = new EmpDataBase();
			
			// Input Stream to receive messages object from Clients
			InputStream inputStream = null;
			ObjectInputStream objectInputStream = null;
			
			// Output Stream to sent messages to Client
			OutputStream outputStream = null;
			ObjectOutputStream objectOutputStream = null;
			// try and catch block
			try {
				// set an infinite loop till User logs out
				Boolean loop = true;
				while(loop) {
					// Temporary for Debugging Purposes
					// Display Employees in Data Base
					localAccess.displayEmployees();
					
					
					// Set up our Object Data input
					inputStream = clientSocket.getInputStream();
					// Then create it in an object
					objectInputStream = new ObjectInputStream(inputStream);
					
					// Think this part through before you code in, remember there are multiple steps here
					// First, you need to authorize the Client's credentials before moving onto next phase
					// I went ahead and implemented a userList that we can look at to see if the Client
					// that wants to login exists in our "database"
					System.out.println("Awaiting to authorize incoming Client...");
					
					// Use the Message class as a temporary object to hold the Client's empNum and password
					// empNum = msg.getFrom()
					// password = msg.getTo()
					// Reason for this is so we do not have to to implement Serializable on another class
					// other method is to pass a strings to Server then continue but this seems easier
					
					// Read in data from Client
					Message login = (Message) objectInputStream.readObject();
					// Just to check if the Message was received, display it!
					// Remember empNum is stored login.msgSize and password is stored in login.Data 
					System.out.println("User ID: \n" + login.getMsgSize() + "Password Entered:" + login.getData());
					
					// Search through our user data base to see if the employee number and password matches
					// if a true continue to login to the server
					// if a false, then send a message back to the client informing them that either the 
					// employee id or password is incorrect and to try again
					if(localAccess.loginEmployee(login.getMsgSize(), login.getData()))
					{
						// Login was successful, continue to interface
						// Send a message to the Client allowing it to continue
						// System.out.println("Able to See the Type in an if statement");
						// Send message back to client allowing Login
						outputStream = clientSocket.getOutputStream();
						objectOutputStream = new ObjectOutputStream(outputStream);
						msgServer = new Message("Login Successful, continue to Interface");
						// Send to Client
						objectOutputStream.writeObject(msgServer);
						objectOutputStream.flush();
						System.out.println("Message Sent to Client\n");
						// Set our while loop to allow Client to keep inputting data
						loggedIn = true;
					}
					else
					{
						// Send message to Client that login has failed and to try again
						System.out.println("Login Attempt Failed");
						outputStream = clientSocket.getOutputStream();
						objectOutputStream = new ObjectOutputStream(outputStream);
						msgServer = new Message("Failed to Login, please try again");
						// Send to Client
						objectOutputStream.writeObject(msgServer);
						objectOutputStream.flush();
						System.out.println("Message Sent to Client\n");
					}
					
					
					// Start thinking of all possible Client actions, follow UML Designs for User and IT
					while(loggedIn) {
						// Begin to process all commands, use a switch statement for ease of implementation
						// Process Client choice of commands till they logout
						// Set Logged in User to Active
						System.out.println("Check worked, in command loop now :)");
						
					}
					
					
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					if(objectInputStream != null) {
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// helper methods ?
}