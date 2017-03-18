package communications;

import java.util.ArrayList;
import java.util.List;

public class Setup extends ServerMemo {
	
	private static final long serialVersionUID = 641882294082622255L;

	private boolean valid;
	
	private User user;
	
	private List<User> friends;
	
	private List<ChatInfo> chats; // chat IDs to which the user belongs
	private History activeChatHistory; // chat with most recent message
	
	public Setup() {
		super(-1);
		
		valid = false;
	}
	
	public Setup(int userID, User user){
		super(userID);
		valid = true;
		this.user = user;
		friends = new ArrayList<>();
		chats = new ArrayList<>();
		activeChatHistory = new History(new User [0], 0, 0 , new ArrayList<>());
	}
	
	public Setup(int userID, User user, List<User> friends, List<ChatInfo> chats, History activeChatHistory) {
		super(userID);
		
		valid = true;
		
		this.user = user;
		this.friends = friends;
		
		this.chats = chats;
		this.activeChatHistory = activeChatHistory;
	}
	
	public User getUser() {
		return user;
	}
	
	public List<User> getFriends() {
		return friends;
	}

	public List<ChatInfo> getChats() {
		return chats;
	}

	public History getActiveChatHistory() {
		return activeChatHistory;
	}
	
	public boolean isValidSetup() {
		return valid;
	}
	
}
