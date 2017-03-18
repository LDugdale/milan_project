package communications;

public class HistoryRequest extends ServerMemo {

	private static final long serialVersionUID = -5338849148510784315L;
	
	private int chatID;
	private int appTarget;
	
	private int size;
	
	public HistoryRequest(int userID, int chatID, int appTarget) {
		super(userID);
		
		this.chatID = chatID;
		this.appTarget = appTarget;
		
		this.size = Integer.MAX_VALUE;
	}
	
	public HistoryRequest(int userID, int chatID, int appTarget, int size) {
		this(userID, chatID, appTarget);
		
		this.size = size;
	}

	public int getChatID() {
		return chatID;
	}

	public int getAppTarget() {
		return appTarget;
	}
	
	public int getSize() {
		return size;
	}
	
}
