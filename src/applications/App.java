package applications;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import communications.*;
import messenger.AppModel;

public abstract class App extends Observable {
	
	protected AppModel model;
	
	protected User[] myUsers;
	protected int myChatID;
	
	private int targetID;
	private String name;
	
	private List<Message> unvalidatedMessages;
	
	private boolean configured;
	
	protected int error = -1;
	
	private final void createBadge() {
		name = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-3);
		Integer myTargetID = AppUtilities.getTargetID(this.getClass());
		targetID = (myTargetID==null)?AppUtilities.chatTargetID:myTargetID.intValue();
		
		//System.out.println("createBadge class: " + this.getClass().getName() + " -> " + targetID);
	}
	
	public App() {
		createBadge();
		
		unvalidatedMessages = new ArrayList<Message>();
		
		configured = false;
	}
	
	public App(AppModel model, Integer myChatID) {
		this(model, myChatID, true);
	}
	
	public App(AppModel model, Integer myChatID, boolean requestHistory) {
		this();
		this.model = model;
		this.myChatID = myChatID;
		
		// Request history.
		if(requestHistory && model != null) model.passMemoToServer(new HistoryRequest(model.getUserID(), myChatID, getTargetID()));
	}
	
	public App(AppModel model, Integer myChatID, History history) {
		this();
		this.model = model;
		this.myChatID = myChatID;
		
		receiveHistory(history);
	}
	
	public final boolean sendMessage(String meta, String message) {
		
		try {
			//System.out.println("Send message.");
			
			Message myMessage = generateMessage(meta, message);
			unvalidatedMessages.add(myMessage);
			//System.out.println("Add message: " + Arrays.toString(unvalidatedMessages.toArray()));
			
			if(model != null) model.passMemoToServer(myMessage);
			
			setChanged();
			notifyObservers();
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not send message.");
			e.printStackTrace();
			
			return false;
		}
		
	}
	
	public final Message generateMessage(String meta, String message) {
		if(model == null) return null;
		return new Message(myUsers, myChatID, getTargetID(), -1, model.getUserID(), meta, message);
	}
	
	public abstract void updateHistory();
	
	public boolean preprocessMessage(Message message) {
		
		if(model == null) return false;
		
		if(message.getSenderID() == model.getUserID()) {
			
			if(unvalidatedMessages.contains(message)) {
				
				//System.out.println("Remove: " + message.getMessage());
				unvalidatedMessages.remove(message);
				//System.out.println("Now: " + Arrays.toString(unvalidatedMessages.toArray()));
				
				return this.receiveMessage(message);
			}
			else {
				return false;
			}
		}
		else return this.receiveMessage(message);
	}
	
	public abstract boolean receiveMessage(Message message);
	
	public abstract boolean receiveHistory(History history);
	
	public final void resetMessageSettings() {
		unvalidatedMessages = new ArrayList<Message>();
	}
	
	public final int getChatID() {
		return myChatID;
	}
	
	public final int getTargetID() {
		return targetID;
	}
	
	public final String getName() {
		return name;
	}
	
	/**
	 * Returns the type of the app. Type here meaning the kind
	 * of app this is. I.e. a game, organiser, etc.
	 * @return App type as a String.
	 */
	public String getType() { // This has the possibility of being overridden in subtypes
		return "";				// If the programmer chooses not to give a type
	}							// then the app is considered to be generic, and is not
								// put into any category.
	/**
	 * Returns the app title. This is intended to be the title
	 * of the app tab when displayed in AppsPane.
	 * @return App title as a String.
	 */
	public String getTitle() {
		return getName();		// Same idea for getTitle as getType
	}
	
	/**
	 * Returns the app display name. This is intended to be the name 
	 * which will be displayed in the App menu of the MessengerPane.
	 * @return App display name as a String.
	 */
	public String getDisplayName() { // Same idea as getTitle and getType
		return getName();
	}
	
	public final AppPanel getPanel() {
		//System.out.println("View created: CID:" + this.getChatID() + " target:" + this.getTargetID());
		AppPanel view = null;
		try {
			//System.out.println("Panel name: " + AppUtilities.applicationsLocation + "." + this.getName().toLowerCase() + "." + this.getName() + "Panel");
			view = (AppPanel)Class.forName(AppUtilities.applicationsLocation + "." + this.getName().toLowerCase() + "." + this.getName() + "Panel").getConstructor(App.class).newInstance(this);
		} catch (Exception e) {
			System.out.println("Could not create view.");
			e.printStackTrace();
			view = null;
		}
		return view;
	}
/*
	public final AppPanel getPanel() {	// This produces a panel (which will actually be of a type which extends AppPanel)
										// which is of the correct type to represent this app. The given app model will be
										// passed to the panel inside this method.
		return view;
	}
*/
	public User[] getUsers() {
		return myUsers;
	}
	
	public void updateUser(User user) {
		for(int i=0; i<myUsers.length; i++) {
			if(myUsers[i].equals(user)) {
				System.out.println("(App) Change " + myUsers[i] + " to " + user);
				myUsers[i] = user;
				return;
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	public int getError() {
		return error;
	}
	
	public String getErrorMessage(int errorCode) {
		return "";
	}
	
	public void configured() {
		configured = true;
	}
	
	public boolean isConfigured() {
		return configured;
	}
	
	public final List<Message> getUnvalidated() {
		return unvalidatedMessages;
	}
	
}