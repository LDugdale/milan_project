package communications;

public class AddFriend extends ServerMemo {
	
	private static final long serialVersionUID = 8181478825861872207L;
	
	private User friend;

	public AddFriend(int userID, User friend) {
		super(userID);
		
		this.friend = friend;
	}
	
	public User getFriend() {
		return friend;
	}

}
