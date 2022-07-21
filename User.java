// Implementation of User Parent Class
public class User {
	// list properties based on Design Document
	protected String firstName;
	protected String lastName;
	static private int empNum = 0;
	protected String password;
	protected String role;
	protected String status;
	
	// list all methods
	// Constructor to set all attributes 
	public User () {
		this.firstName = "N/A";
		this.lastName = "N/A";
		this.password = "N/A";
		this.role = "User";
		this.status = "N/A";
		
	}
	
	public User(String first, String last, String password, String role) {
		this.firstName = first;
		this.lastName = last;
		this.password = password;
		this.role = role;	
	}
	
	// setters
	public void setStatus(String stat) {
		this.status = stat;
	}
	
	// getters
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public int getEmpNum() {
		return this.empNum;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	// methods
	public void sendMessage(Message msg) {
		// Create a new Message object and send it to the server
	}
	
	public void viewChats() {
		// Be able to retrieve all previous chat history
	}
	
	public void createGroup() {
		// Be able to create a group with Employees
		
	}
	
	public void leaveGroup() {
		// Be able to leave a group chat
		
	}
	
	public void logOff() {
		// Be able to log off
	}
}