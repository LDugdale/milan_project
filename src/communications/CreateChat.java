package communications;

public class CreateChat extends ServerMemo {

	private static final long serialVersionUID = 7352641389481082212L;
	
	private int[] userIDs;

	public CreateChat(int userID, int[] userIDs) {
		super(userID);
		
		this.userIDs = userIDs;
	}
	
	public int[] getUserIDs() {
		return userIDs;
	}

}
