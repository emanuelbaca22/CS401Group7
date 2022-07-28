import java.io.Serializable;
// Message Object
public class Message implements Serializable{
	// list properties based on Design Document
	private String fromFirstName;
	private String fromLastName;
	private String toFirstName;
	private String toLastName;
	private int msgSize;
	private String data;
	private String type;
	
	// Constructor used to send a new chat message
	public Message(String clientFromFirst, String clientFromLast, String toFirst, String toLast, String clientData) {
	this.fromFirstName = clientFromFirst;
	this.fromLastName = clientFromLast;
	this.toFirstName = toFirst;
	this.toLastName = toLast;
	this.data = clientData;
	}
	// Constructor used to add a Message Object to our msgDataBase
	public Message(String clientFromFirst, String clientFromLast, String toFirst, String toLast, String clientData, String clientType) {
	this.fromFirstName = clientFromFirst;
	this.fromLastName = clientFromLast;
	this.toFirstName = toFirst;
	this.toLastName = toLast;
	this.data = clientData;
	this.type = clientType;
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
	
	// Constructor used to as a choice selector for our switch statements
	public Message(int size)
	{
		this.msgSize = size;
	}
	
	// list all getter methods
	public String getFromFirstName() {
		return this.fromFirstName;
	}
	
	public String getFromLastName() {
		return this.fromLastName;
	}
	public String getToFirstName() {
		return this.toFirstName;
	}
	
	public String getToLastName() {
		return this.toLastName;
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