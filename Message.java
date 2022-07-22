import java.io.Serializable;
// Message Object
public class Message implements Serializable{
	// list properties based on Design Document
	private String from;
	private String to;
	private int msgSize;
	private String data;
	private String type;
	
	// Constructor used to send a new chat message
	public Message(String clientFrom, String clientTo, String clientData) {
	this.from = clientFrom;
	this.to = clientTo;
	this.data = clientData;
	
	// Use a function to check whether the recipient is offline or online in order to set the message type
	//this.type = checkType();
	}
	
	// initial login Message constructor
	// from will hold the empNum of Client
	// to will hold the password of Client
	public Message(int empNum, String password)
	{
		this.msgSize = empNum;
		this.data = password;
		
	}
	
	// Constructor used to communicate back n forth to client n server
	public Message(String data)
	{
		this.data = data;
	}
	// implement checkEmployeeStatus()
	
	// list all getter methods
	public String getFrom() {
		return this.from;
	}
	
	public String getTo() {
		return this.to;
	}
	
	public int getMsgSize() {
		return this.msgSize;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getType() {
		return this.type;
	}
	
	// setters are not needed as we want the Message Object Data to be IMMUTABLE 
	// (not be changed at all, only be able to view)
}