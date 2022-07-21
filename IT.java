// IT is just an extension of User
public class IT extends User{
	// no other properties to list as they are the same as a User
	
	// Constructor
	public IT() {
		this.role = "IT";
	}
	// methods strictly for IT
	public User createEmployee(String first, String last, String password, String role) {
		// create new Employee, either a User or IT role
		User newUser = new User(first, last, password, role);
		// pass newly created employee to server
		return newUser;
	}
	
	// Will not implement deleteEmployee as it is not required and is extra work
	
	public void viewChatLog()
	{
		// think of a way to open the file and search for specific fields to filter log file
	}
}