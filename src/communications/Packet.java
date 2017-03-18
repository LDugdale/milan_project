package communications;

import java.io.Serializable;

public class Packet implements Serializable {
	
	private static final long serialVersionUID = -558662778117028392L;
	
	private int type;
	private Memo memo;
	
	public Packet(Memo memo) {
		this.memo = memo;
		
		if(memo.getClass() == Message.class) {
			this.type = 0;
		}
		else if(memo.getClass() == History.class) {
			this.type = 1;
		}
		else if(memo.getClass() == HistoryRequest.class) {
			this.type = 2;
		}
		else if(memo.getClass() == ServerMessage.class) {
			this.type = 3;
		}
		else if(memo.getClass() == SignUp.class) {
			this.type = 4;
		}
		else if(memo.getClass() == Logout.class) {
			this.type = 5;
		}
		else if(memo.getClass() == CreateChat.class) {
			this.type = 6;
		}
		else if(memo.getClass() == FriendRequest.class) {
			this.type = 7;
		}
		else if(memo.getClass() == AddFriend.class) {
			this.type = 8;
		}
		else if(memo.getClass() == Setup.class) {
			this.type = 9;
		}
		else if(memo.getClass() == Login.class) {
			this.type = 10;
		}
		else if(memo.getClass() == EditUser.class) {
			this.type = 11;
		}
		else {
			System.out.println("Could not write packet.");
			// throw new UnknownPacketException(); ?????????????
			this.type = -1;
		}
	}

	public int getType() {
		return type;
	}

	public Memo getMemo() {
		return memo;
	}
	
}
