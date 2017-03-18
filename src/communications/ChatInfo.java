package communications;

import java.io.Serializable;

public class ChatInfo implements Serializable, Comparable<ChatInfo> {
	
	private static final long serialVersionUID = 1179839870237126792L;
	
	private int chatID;
	private User[] users;
	
	public ChatInfo(int chatID, User[] users) {
		this.chatID = chatID;
		this.users = users;
	}

	public int getChatID() {
		return chatID;
	}

	public User[] getUsers() {
		return users;
	}
	
	public void updateUser(User user) {
		for(int i=0; i<users.length; i++) {
			if(users[i].equals(user)) {
				System.out.println("(ChatInfo) Change " + users[i] + " to " + user);
				users[i] = user;
				return;
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		ChatInfo info = null;
		try {
			info = (ChatInfo)o;
		}
		catch (ClassCastException e) {
			return false;
		}
		
		return this.chatID == info.chatID;
	}

	@Override
	public int compareTo(ChatInfo o) {
		int a = this.chatID;
		int b = o.chatID;
		
		if(a > b) return 1;
		else if (a < b) return -1;
		else return 0;
	}
	
	@Override
	public String toString() {
		String text =  "(" + chatID + ")";
		
		for(User u : users) {
			text += " " + u.getUsername();
		}
		
		return text;
	}
	
}
