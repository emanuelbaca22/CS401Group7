import java.util.ArrayList;
import java.util.List;

// EmpDataBase will hold all employees and be accessed through this class
public class EmpDataBase {
	// Create a List, super easy to work with unlike a Linked List or Queue which my team disagreed on
	private List<User> empDataBase = new ArrayList<User>();
	
	// No constructor needed
	// Constructor of base employees 
	public EmpDataBase() {
		// Add a few Users and an IT to test application
		User emp1 = new User("Bob", "Smith", 1, "I am Bob", "User");
		User emp2 = new User("Val", "Gonzalez", 2, "Tacos", "IT");
		User emp3 = new User("Dave", "Go", 3, "password123", "User");
		empDataBase.add(emp1);
		empDataBase.add(emp2);
		empDataBase.add(emp3);
	}
	// Methods of EmpDataBase
	// Adding Employees
	public void addEmployee(User newEmp) {
		// add a new employee to list
		// This is assuming that IT is putting in all data fields of new User
		// then telling the server to add an employee
		this.empDataBase.add(newEmp);
	}
	public Boolean loginEmployee(int userID, String password)
	{
		Boolean employeeFound = false;
		// Change to an int as per req doc of User int empNum;
		//int searchID = Integer.parseInt(userID);
		String searchPassword = "";
		
		// loop through EmpDataBase to see if the User Exists
		for(int i = 0; i < empDataBase.size(); i++)
		{
			// set our string values in order to use equals method
			searchPassword = empDataBase.get(i).getPassword();
			
			// println temporary, for debugging purposes
			// System.out.println(password);
			// System.out.println(searchPassword);
			
			if(userID == empDataBase.get(i).getEmpNum() && searchPassword.equals(password))
			{
				// User employee ID and password is found!
				// We can choose to either return true to that the server code is easier
				// Setting loginEmployee function as a Boolean to make it easy :)
				// Set employeeFound to true
				employeeFound = true;
			}
		}
		
		// Return our employeeFound condition
		return employeeFound;
	}
	// Show Employee Attributes
	public void displayEmployee(User emp) {
		System.out.println("\nFirst Name: " + emp.getFirstName());
		System.out.println("\nLast Name: " + emp.getLastName());
		System.out.println("\nEmployee Number:  " + emp.getEmpNum());
		System.out.println("\nPassword: " + emp.getPassword());
		System.out.println("\nRole: " + emp.getRole());
		System.out.println("\nStatus: " + emp.getStatus());
	}
	// Temp method to display all Users
	public void displayEmployees() {
		for(int i = 0; i < empDataBase.size(); i++)
		{
			// Call our Display Employee Function
			displayEmployee(empDataBase.get(i));
		}
	}
	
	//add more methods here
}