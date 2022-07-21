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
		User user1 = new User("Manny", "Baca", "password", "User");
		User user2 = new User("John", "Smith", "password", "User");
		userList.add(user1);
		userList.add(user2);
		
		///checkRecords(userList);
		
		// Create our list of Messages that we will later write to a text file
		List<Message> msgList = new ArrayList<Message>();
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
			// Input Stream to receive messages object from Clients
			InputStream inputStream = null;
			ObjectInputStream objectInputStream = null;
			
			// String input stream to check for username and password in order to login to the server
			// DataInputStream dataInputStream;
			// DataOutputStream dataOutputStream;
			// BufferedReader bufferedReader;
			
			// try and catch block
			try {
				// set an infinite loop till User logs out
				Boolean loop = true;
				while(loop) {
					// Set up our Object Data input
					inputStream = clientSocket.getInputStream();
					// Then create it in an object
					objectInputStream = new ObjectInputStream(inputStream);
					
					// Think this part through before you code in, remember there are multiple steps here
					// First, you need to authorize the Client's credentials before moving onto next phase
					// I went ahead and implemented a userList that we can look at to see if the Client
					// that wants to login exists in our "database"
					System.out.println("Awaiting to authorize incoming Client...");
					User login = (User) objectInputStream.readObject();
					// Just to check if the Message was received, display it!
					System.out.println("User ID: " + login.getEmpNum() + "Password Entered:" + login.getPassword());
					// Search through our user data base to see if the employee number and password matches
					// if a yes continue to login to the server
					// if a no, then send a message back to the client informing them that either the 
					// employee id or password is incorrect and to try again
					
					
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