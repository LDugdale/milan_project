package communications;

public final class PacketReader {
	
	public static boolean readRawPacket(Object rawPacket, PacketHandler handler) throws ClassCastException {
		return readPacket((Packet)rawPacket, handler);
	}
	
	public static boolean readPacket(Packet packet, PacketHandler handler) throws ClassCastException {
		
		boolean success = false;
		
		if(packet.getType() == 0) {
			success = handler.handleMessage((Message)packet.getMemo());
		}
		else if(packet.getType() == 1) {
			success = handler.handleHistory((History)packet.getMemo());
		}
		else if(packet.getType() == 2) {
			success = handler.handleHistoryRequest((HistoryRequest)packet.getMemo());
		}
		else if(packet.getType() == 3) {
			success = handler.handleServerMessage((ServerMessage)packet.getMemo());
		}
		else if(packet.getType() == 4) {
			success = handler.handleSignUp((SignUp)packet.getMemo());
		}
		else if(packet.getType() == 5) {
			success = handler.handleLogout((Logout)packet.getMemo());
		}
		else if(packet.getType() == 6) {
			success = handler.handleCreateChat((CreateChat)packet.getMemo());
		}
		else if(packet.getType() == 7) {
			success = handler.handleFriendRequest((FriendRequest)packet.getMemo());
		}
		else if(packet.getType() == 8) {
			success = handler.handleAddFriend((AddFriend)packet.getMemo());
		}
		else if(packet.getType() == 9) {
			success = handler.handleSetup((Setup)packet.getMemo());
		}
		else if(packet.getType() == 10) {
			success = handler.handleLogin((Login)packet.getMemo());
		}
		else if(packet.getType() == 11) {
			success = handler.handleEditUser((EditUser)packet.getMemo());
		}
		else {
			System.out.println("Could not read packet.");
			success = false;
			// throw new UnknownPacketException(); ?????????????
		}
		
		return success;
	}
	
}
