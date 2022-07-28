// IT is just an extension of User
public class IT extends User{
	// no other properties to list as they are the same as a User
	
	// Constructor
	public IT() {
		this.role = "IT";
	}
	// methods strictly for IT
	public User createEmployee(String first, String last, int id, String password, String role) {
		// create new Employee, either a User or IT role
		User newUser = new User(first, last, id, password, role);
		// pass newly created employee to server
		return newUser;
	}
}