package communications;

public abstract class ServerMemo extends Memo {
	
	private static final long serialVersionUID = 4765465923313213907L;
	
	private int userID;

	public ServerMemo(int userID) {
		super();
		
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

}
