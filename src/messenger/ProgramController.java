package messenger;

import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import applications.App;
import applications.AppUtilities;
import communications.*;
import messengerUI.MessengerFrame;

//
// Some stuff in here probably needs to be synchronised, but I'm damned
// if I know where just yet...
//
public final class ProgramController implements Observer { // Should this extend Thread? Any benefits?
	
	private boolean loggedIn;
	
	private ClientController client;
	
	private AppModel model;
	
	private MessengerFrame view;

	public ProgramController(String serverName, int port) {
		this.loggedIn = false;
		
		updateServerInfo(serverName, port);
		
		//client = new ClientController(this);
	
		System.out.println("App types:");
		for( Entry<Integer, Class<? extends App>> entry : AppUtilities.getAppTypes().entrySet() )
		{
		    System.out.println(entry.getKey() + ": " + entry.getValue().getSimpleName());
		}
		System.out.println("Chat targetID: " + AppUtilities.chatTargetID);
	
		// Initialise View
		view = new MessengerFrame(this);
	}
	
	public void createUser(String username, String password) {
		System.out.println("In ProgramController: Attempt create user with username " + username + " and password " + password);
		
		client = new ClientController(this, new SignUp(username, password));
		client.start();
	}
	
	public void editUser(String password, boolean[] edited, String[] fields) {
		passMemoToServer(new EditUser(model.getUserID(), password, edited, fields));
	}
	
	public boolean updateUser(EditUser editUser) {
		return model.updateUser(editUser);
	}
	
	public void login(String username, String password) {
		System.out.println("In ProgramController: Attempt login with username " + username + " and password " + password);
		
		client = new ClientController(this, new Login(username, password));
		client.start();
	}
	
	public void receiveSetup(Setup setup) {
		
		System.out.println("Is valid setup: " + setup.isValidSetup());
		if(setup.isValidSetup()) {
			System.out.println(setup.getUserID());
			System.out.println(setup.getActiveChatHistory());
		}
		
		loggedIn = setup.isValidSetup();
		
		if(loggedIn) {
			model = new AppModel(setup);
			model.addObserver(this);
			model.addObserver(view);
			
			System.out.println("In ProgramController: Logged in with user: " + model.getUser());
			
			view.setModel(model);
			view.onLogin(true);
		}
		else {
			System.out.println("In ProgramController: Login failed.");
			
			view.onLogin(false);
			
			client.interrupt();
		}
	}
	
	public void logoff() {
		if(client != null) client.logoff();
	}
	
	public void updateServerInfo(String serverName, int port) {
		ClientController.updateServerInfo(serverName, port);
	}
	
	public boolean passMemoToServer(Memo memo) {
		return client.passMemoToServer(memo);
	}
	
	public boolean passMessageToApps(Message message) {
		return model.passMessageToApps(message);
	}
	
	public boolean passHistoryToApps(History history) {
		return model.passHistoryToApps(history);
	}
	
	public void requestFriend(String username) {
		client.passMemoToServer(new FriendRequest(model.getUserID(), username));
	}
	
	public boolean addFriend(User friend) {
		if(friend.getUserID() == -1) {
			view.displayDialogue("Could not find user '" + friend.getUsername() + "'");
			
			return false;
		}
		else {
			return model.addFriend(friend);
		}
	}
	
	public User getUser() {
		return model.getUser();
	}
	
	public int getUserID() {
		return model.getUserID();
	}
	
	public String getUsername() {
		return model.getUsername();
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedOut() {
		this.loggedIn = false;
		this.client = null;
		this.model = null;
		view.GUILogoff();
	}

	public void informUser(String message) {
		view.displayDialogue(message);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg == null) {
			// Do nothing
		}
		else if(Memo.class.isAssignableFrom(arg.getClass())) {
			passMemoToServer((Memo)arg);
		}
		else if(arg.getClass().equals(Integer.class)) {
			int chatID = ((Integer)arg).intValue();
			passMemoToServer(new ServerMessage(model.getUserID(), "delete " + chatID));
		}
		// else do nothing
	}

}
