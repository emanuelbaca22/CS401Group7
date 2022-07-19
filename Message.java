//ayumi
import java.io.Serializable;
// Message Object
public class Message implements Serializable{
	// list properties based 
	private String from;
	private String to;
	private int msgSize;
	private String data;
	private String type;
	
	// methods of message class
	public Message(String clientFrom, String clientTo, String clientData) {
	// set Message object attributes here
	this.from = clientFrom;
	this.to = clientTo;
	this.data = clientData;
	// implement msg size here
	
	// Use a function to check whether the recipient is offline or online in order to set the message type
	//this.type = checkType();
		
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
	
	// setters are not needed as we want the Message Object Data to be IMMUTABLE (not be changed at all, only be able to view)
}
