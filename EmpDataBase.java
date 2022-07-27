import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// EmpDataBase will hold all employees and be accessed through this class
public class EmpDataBase {
	// Create a List, super easy to work with unlike a Linked List or Queue which my team disagreed on
	private static List<User> empDataBase = new ArrayList<User>();
	
	// Create a Message List to hold all of the messages being sent and let's us log them as well
	private static List<Message> msgDataBase = new ArrayList<Message>();
	
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
	
	public User returnEmployee(int userID, String password)
	{
		String searchPassword = "";
		User employee = new  User();
		for(int i = 0; i < empDataBase.size(); i++)
		{
			// set our string values in order to use equals method
			searchPassword = empDataBase.get(i).getPassword();
			
			if(userID == empDataBase.get(i).getEmpNum() && searchPassword.equals(password))
			{
				// User employee ID and password is found!
				// We can choose to either return true to that the server code is easier
				// Set employee Object to Server Record of Employee
				employee = empDataBase.get(i);
				
				// Set the logged in employee as Active 
				// Logic behind doing it here is because of the following
				// We are already returning the User Object back to the incoming Client
				// Which means that they have already been verified via empNum and password
				// Since they are now logged in, we can set their status as Active 
				// to enable Synchronous Messaging
				empDataBase.get(i).setStatus("Active");
			}
		}
		
		return employee;
	} 
	public void sendMessage(Message msg) throws IOException
	{
		// Steps to implement:
		// 1) Check if Recipient is Online or Offline, this determines
		// whether the message is sent Synchronously or Asynchronously 
		// Remember Message object passed in has the following data:
		// from first and last name, to first and last name and the message data/contents
		
		// Check if Recipient is Active
		// Pass in Recipient First and Last Name
		if(checkStatus(msg.getToFirstName(), msg.getToLastName()))
		{
			// Recipient is Active, so set new message and package it up to add to our msgDataBase
			Message msgToAdd = new Message(msg.getFromFirstName(), msg.getFromLastName(), msg.getToFirstName(), msg.getToLastName(), msg.getData(), "Synchronous");
			 // Now that our message is packaged, add it to our msgDataBase
			msgDataBase.add(msgToAdd);
		}
		else
		{
			// Recipient is Offline, so set new message and package it up to add to our msgDataBase
			Message msgToAdd = new Message(msg.getFromFirstName(), msg.getFromLastName(), msg.getToFirstName(), msg.getToLastName(), msg.getData(), "Asynchronous");
			// Now that our message is packaged, add it to our msgDataBase
			msgDataBase.add(msgToAdd);
			// write msg to our log file
			writeToLogFile(msgToAdd);
		}
	}
	
	public void writeToLogFile(Message msg) throws IOException 
	{
		// Format of Messages in text file will be the following:
		// From First/Last Name -> To First/Last Name -> Data -> Type
		String data = msg.getFromFirstName() + " " + msg.getFromLastName() + " " + msg.getToFirstName() + " " + msg.getToLastName() + " " + msg.getData() + " " + msg.getType() + "\n";
		FileWriter fileWritter = new FileWriter("chatLog.txt",true);
        BufferedWriter bw = new BufferedWriter(fileWritter);
        bw.write(data);
        bw.close();
	}
	
	// Method checkStatus is to see if the Recipient is Active or In-Active
	public Boolean checkStatus(String first, String last)
	{
		// Declaration of used variables
		// when check is false, message is sent Asynchronously
		// when true, message is sent Synchronously
		Boolean check = false;
		String tempFirst = "";
		String tempLast = "";
		String status = "";
		
		for(int i = 0; i < empDataBase.size(); i++)
		{
			// set our string values in order to use equals method
			tempFirst = empDataBase.get(i).getFirstName();
			tempLast = empDataBase.get(i).getLastName();
			status = empDataBase.get(i).getStatus();
			
			if(tempFirst.equals(first) && tempLast.equals(last))
			{
				status = empDataBase.get(i).getStatus();
				// Check the User Status
				if(status.equals("Active"))
				{
					// set check to true
					check = true;
				}
			}
		}
		
		// return check, will be false if the Recipient isn't Active
		return check;
	}
	
	// Method checkUser either returns true or false 
	// Purpose is to first confirm that the user exists before attempting to send a message
	public Boolean checkUser(String first, String last)
	{
		// Declaration of used variables
		Boolean exist = false;
		String tempFirst = "";
		String tempLast = "";
				
		for(int i = 0; i < empDataBase.size(); i++)
		{
			// set our string values in order to use equals method
			tempFirst = empDataBase.get(i).getFirstName();
			tempLast = empDataBase.get(i).getLastName();
				
			if(tempFirst.equals(first) && tempLast.equals(last))
			{
				// User does exist in the Data Base, return true
				exist = true;
			}
		}	
				// return exist, will be false if the User does not exist in empDataBase
				return exist;
	}
	
	public String chatHistory(User user)
	{
		// Search through our msgDataBase and format messages
		String chatLog = "";
		
		for(int i = 0; i < msgDataBase.size(); i++)
		{
			
		}
		return chatLog;
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
}