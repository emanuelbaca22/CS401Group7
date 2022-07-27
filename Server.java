import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	// Let's us keep track of logged in users
	private static EmpDataBase localAccess = new EmpDataBase();
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
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
			Message msg = null;
			
			User clientUser = null;
			User newUser = null;
			
			// local Variable to access our Employee Data Base
			// EmpDataBase localAccess = new EmpDataBase();
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
					System.out.println("\nUser ID: " + login.getMsgSize() + "\n  Password Entered:" + login.getData());
					
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
						
						// return the Clients information by sending them back a User object
						clientUser = localAccess.returnEmployee(login.getMsgSize(), login.getData());
						objectOutputStream.writeObject(clientUser);
						objectOutputStream.flush();
						System.out.println("Returned User Account Information to Client");
						
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
						// System.out.println("Check worked, in command loop now :)");
						
						System.out.println("\nAwating Client Commands...");
						// Process user choice then go into a switch 
						// This way the server will prepare to receive appropriate methods
						// The Message object being read in will have the User choice stored in msgData
						// Use .getMsgSize to access the User's choice
						Message userChoice = (Message) objectInputStream.readObject();
						
						// Depending if User Interface or IT Interface, adjust to incoming Client Data
						// User Interface is below
						if(clientUser.getRole().equals("User"))
						{
							switch(userChoice.getMsgSize())
							{
							case 1:
								// This will be the sendMessage() method handler
								// Get msg object from client
								msg = (Message) objectInputStream.readObject();
								System.out.println("Message Received, sending to Recipient...");
							
								// First verify that the User Exists in our empDataBase
								if(localAccess.checkUser(msg.getToFirstName(), msg.getToLastName()))
								{
									localAccess.sendMessage(msg);
								}
								System.out.println("Message has been sent");
								// After Sending message, log it in our chatHistoryLog.txt
								// implement writing to log file, may not be needed as all chats are stored
								// in msgDataBase list so we can just write that to a text
							
								break;
							case 2:
								// This will be the chatHistory() method handler
								// Send a Message Object Back to the Client holder their Chat History
								clientUser = (User) objectInputStream.readObject();
							
								// Create a Message Object with the Client's Chat History
								msgServer = new Message(localAccess.chatHistory(clientUser));
							
								// Send Chat Data to Client
								objectOutputStream.writeObject(msgServer);
								objectOutputStream.flush();
								System.out.println("Chat History Sent to Client\n");
								break;
							case 3:
								// This will be the createGroup() method handler
								break;
							case 4:
								// This will be the logOut() method handler
								clientUser = (User) objectInputStream.readObject();
							
								// Log User off the System
								localAccess.logOff(clientUser);
							
								System.out.println("\n Client has been successfully logged out");
								// break out of loops
								loggedIn = false;
								loop = false;
							
								break;
							default:
								// Should not be needed as it will be checked previously in Client.java but just in case
								// error checking message here
								break;										
							}
						}
						else
						{
							// Implement IT Interaction Interface
							switch(userChoice.getMsgSize())
							{
							case 1:
								// This will be the sendMessage() method handler
								// Get msg object from client
								msg = (Message) objectInputStream.readObject();
								System.out.println("Message Received, sending to Recipient...");
							
								// First verify that the User Exists in our empDataBase
								if(localAccess.checkUser(msg.getToFirstName(), msg.getToLastName()))
								{
									localAccess.sendMessage(msg);
								}
								System.out.println("Message has been sent");
								// After Sending message, log it in our chatHistoryLog.txt
								// implement writing to log file, may not be needed as all chats are stored
								// in msgDataBase list so we can just write that to a text
							
								break;
							case 2:
								// This will be the chatHistory() method handler
								// Send a Message Object Back to the Client holder their Chat History
								clientUser = (User) objectInputStream.readObject();
							
								// Create a Message Object with the Client's Chat History
								msgServer = new Message(localAccess.chatHistory(clientUser));
							
								// Send Chat Data to Client
								objectOutputStream.writeObject(msgServer);
								objectOutputStream.flush();
								System.out.println("Chat History Sent to Client\n");
								break;
							case 3:
								// This will be the createGroup() method handler
								break;
							case 4:
								// This will be createNewUser()
								// get new User Info from Client
								newUser = (User) objectInputStream.readObject();
								
								// Create a new User and it them to our empDataBase
								localAccess.createEmployee(newUser);
								
								System.out.println("\nNew User Created and added into EmpDataBase");
							
								break;
							case 5:
								// View ENTIRE CHAT LOG
								// Retrieving Chat Log and putting it in msg.data
								// Send string in a message object
								msg = new Message(localAccess.chatLog());
								
								// Send off the Client
								objectOutputStream.writeObject(msg);
								objectOutputStream.flush();
								
								break;
							case 6:
								// logOff()
								clientUser = (User) objectInputStream.readObject();
								
								// Log User off the System
								localAccess.logOff(clientUser);
							
								System.out.println("\n Client has been successfully logged out");
								// break out of loops
								loggedIn = false;
								loop = false;
								break;
							default:
								// Should not be needed as it will be checked previously in Client.java but just in case
								// error checking message here
								break;										
							}
						}
						
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
}

