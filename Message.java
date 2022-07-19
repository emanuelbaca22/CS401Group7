// Message Object
public class Message {
	// list properties based 
	String from;
	String to;
	int msgSize;
	String data;
	String type;
	
	// methods of message class
	public Message() {
	// set Message object attributes here
		
	}
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