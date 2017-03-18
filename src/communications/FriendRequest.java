package communications;

public class FriendRequest extends ServerMemo {
	
	private static final long serialVersionUID = 5143654357122319150L;
	
	private String username;

	public FriendRequest(int userID, String username) {
		super(userID);
		
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
