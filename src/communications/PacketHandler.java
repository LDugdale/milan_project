package communications;

public interface PacketHandler {
	
	public boolean handleMessage(Message message);
	
	public boolean handleHistory(History history);
	
	public boolean handleHistoryRequest(HistoryRequest historyRequest);
	
	public boolean handleServerMessage(ServerMessage serverMessage);
	
	public boolean handleSignUp(SignUp signUp);
	
	public boolean handleLogout(Logout logout);
	
	public boolean handleCreateChat(CreateChat createChat);
	
	public boolean handleFriendRequest(FriendRequest friendRequest);
	
	public boolean handleAddFriend(AddFriend addFriend);
	
	public boolean handleSetup(Setup setup);
	
	public boolean handleLogin(Login login);
	
	public boolean handleEditUser(EditUser editUser);
	
}
