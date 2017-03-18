package applications.chat;

import java.util.ArrayList;
import java.util.List;

import applications.App;
import communications.*;
import messenger.AppModel;

public final class ChatApp extends App {
	
	private List<String> chatHistory;
	
	public ChatApp() {
		super();
	}

	public ChatApp(AppModel model, Integer myChatID) {
		super(model, myChatID);
	}
	
	public ChatApp(AppModel model, Integer myChatID, History history) {
		super(model, myChatID, history);
	}
	
	@Override
	public boolean receiveMessage(Message message) {
		
		try {
			chatHistory.add(message.getSenderUsername() + ": " + message.getMessage());
			
			setChanged();
			notifyObservers();
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not process message.");
			
			return false;
		}
		
	}
	
	@Override
	public boolean receiveHistory(History history) {
		
		// PLACEHOLDER
		if(chatHistory != null) {
			System.out.println("Already have a chat history.");
			return true;
		}
		
		try {
			chatHistory = new ArrayList<String>();
			
			myUsers = history.getUsers();
			myChatID = history.getChatID();
			
			//System.out.println("ChatApp.recieveHistory: history.getHistory() = " + history.getHistory());
			for(int i=0; i<history.size(); i++) {
				chatHistory.add(history.get(i).getSenderUsername() + ": " + history.get(i).getMessage());
			}
			
			resetMessageSettings();
			
			//view.configure();
			//((ChatPanel)view).updateTitle();
			configured();
			setChanged();
			notifyObservers(); // Send String to update title?
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not process history.");
			e.printStackTrace();
			
			return false;
		}
		
	}
	
	public void openApp(int targetID) {
		model.openApp(myChatID, targetID);
	}
	
	public List<String> getChatHistory() {
		List<String> response = new ArrayList<String>();
		response.addAll(chatHistory);
		
		//synchronized (this.getUnvalidated()) {
			for(int i=0; i<this.getUnvalidated().size(); i++) {
				response.add("(Pending) " + getUnvalidated().get(i).getSenderUsername() + ": " + getUnvalidated().get(i).getMessage());
			}
		//}
		return response;
	}
	
	@Override
	public String getTitle() {
		if(myUsers == null || myUsers.length == 0) {
			//System.out.println("No users.");
			if(model.getUserChats().contains(new ChatInfo(myChatID, null))) {
				//System.out.println("Users available from model.");
				User[] tempUsers = model.getChatInfo(myChatID).getUsers();
				
				return titleFromUsers(model.getUsername(), tempUsers);
			}
			else {
				return getName() + " " + myChatID;
			}
		}
		else {
			return titleFromUsers(model.getUsername(), myUsers);
		}
	}
	
	public static String generateTitle(String username, ChatInfo info) {
		return titleFromUsers(username, info.getUsers());
	}
	
	private static String titleFromUsers(String username, User[] tempUsers) {
		String title = "";
		for(int i=0; i<tempUsers.length; i++) {
			if(!tempUsers[i].getUsername().equals(username)) {
				title += tempUsers[i].getUsername() + ", ";
			}
		}
		if(title.length() > 2) title = title.substring(0, title.length()-2);
		return title;
	}
	
	@Override
	public void updateHistory() {
		// Does nothing for chat app, as it never updates its own history.
	}
}
