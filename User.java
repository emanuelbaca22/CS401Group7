import java.io.Serializable;
import java.net.Socket;
// Implementation of User Parent Class
public class User implements Serializable {
	// list properties based on Design Document
	protected String firstName;
	protected String lastName;
	protected int empNum;
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
	
	// Used to create a new employee by Server
	public User(String first, String last, int id, String password, String role) {
		this.firstName = first;
		this.lastName = last;
		this.empNum = id;
		this.password = password;
		this.role = role;	
		this.status = "In-Active";
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
}