package communications;

public abstract class AppMemo extends Memo {
	
	private static final long serialVersionUID = -3140877005845594930L;
	
	// Time stamp?
	// User ID?

	private User[] users; // Users who care about this message
	
	private int chatID;
	private int appTarget;
					// App Target:
					//		0 reserved for chat app
					//		Say, -1, reserved for program communications?
					//			Or even all negative numbers?
	
	public AppMemo(User[] users, int chatID, int appTarget) {
		super();
		
		this.users = users;
		this.chatID = chatID;
		this.appTarget = appTarget;
		
		// Time stamp?
		// USer ID?
	}
	
	public User[] getUsers() {
		return users;
	}
	
	public int[] getUserIDs() {
		return MemoUtilities.getUserIDs(users);
	}
	
	public String[] getUsernames() {
		return MemoUtilities.getUsernames(users);
	}
	
	public int getChatID() {
		return chatID;
	}

	public int getAppTarget() {
		return appTarget;
	}

}
