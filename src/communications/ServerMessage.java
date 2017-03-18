package communications;

public class ServerMessage extends ServerMemo {
	
	private static final long serialVersionUID = -1622895595050807200L;
	
	private String message;

	public ServerMessage(int userID, String message) {
		super(userID);
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
