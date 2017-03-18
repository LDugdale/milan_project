package communications;

import java.util.List;

public class History extends AppMemo {
	
	private static final long serialVersionUID = -5534187414689359728L;
	
	private List<Message> history;

	public History(User[] users, int chatID, int appTarget, List<Message> history) {
		super(users, chatID, appTarget);
		
		this.history = history;
	}
	
	public List<Message> getHistory() {
		return history;
	}
	
	public int size() {
		return history.size();
	}
	
	public Message get(int i) {
		return history.get(i);
	}
	
	public ChatInfo getChatInfo() {
		
		return new ChatInfo(getChatID(), getUsers());
		
	}
	
}
