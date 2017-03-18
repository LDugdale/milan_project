package communications;

public class Message extends AppMemo {

	private static final long serialVersionUID = -2141469399085312674L;
	
	private int messageID;
	
	private int senderID; // The user who sent this message
	private String meta;
	private String message;
	
	public Message(User[] users, int chatID, int appTarget, int messageID, int senderID, String meta, String message) {

		super(users, chatID, appTarget);
		this.messageID = messageID;
		this.senderID = senderID;
		this.meta = meta;
		this.message = message;
	}
	
	public int getMessageID() {
		return messageID;
	}

	public int getSenderID() {
		return senderID;
	}
	
	public String getSenderUsername() {
		return MemoUtilities.getUsername(this.getSenderID(), this.getUsers());
	}
	
	public String getMeta() {
		return meta;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Message [messageID=" + messageID + ", senderID=" + senderID + ", meta=" + meta + ", message=" + message
				+ "]";
	}
	
	public boolean equals(Object o) {
		Message m = null;
		
		try {
			m = (Message)o;
		} catch (ClassCastException e) {
			return false;
		}
		
		return this.senderID == m.senderID && this.meta.equals(m.meta) && this.message.equals(m.message);
	}
	
}
