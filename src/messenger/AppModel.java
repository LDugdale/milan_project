package messenger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import applications.*;
import applications.chat.ChatApp;
import communications.*;

public final class AppModel extends Observable {
	
	private User me;
	
	private List<User> friends;
	
	private int activeChatID;
	private Set<ChatInfo> chats;
	
	private Map<Integer, ChatApp> openChats;
	
	private Map<Integer, TreeMap<Integer, App>> apps; // First key is chatID, second Map is apps associated with that chatID
														// Second key is targetID of the apps in the Map
	
	public AppModel(Setup setup) {
		
		me = setup.getUser();
		friends = setup.getFriends();
		
		activeChatID = setup.getActiveChatHistory().getChatID();
		chats = new TreeSet<ChatInfo>(setup.getChats());
		openChats = new TreeMap<Integer, ChatApp>();
		apps = new TreeMap<Integer, TreeMap<Integer, App>>();
		
		for(ChatInfo c : setup.getChats()) {
			System.out.println("\t" + c);
		}
		
		if(activeChatID != 0) {
			if(!chats.contains(new ChatInfo(activeChatID, null))) {
				System.out.println("Something has gone very wrong.");
				throw new IllegalArgumentException();
			}
			
			openChats.put(activeChatID, new ChatApp(this, setup.getActiveChatHistory().getChatID(), setup.getActiveChatHistory()));
			apps.put(activeChatID, new TreeMap<Integer, App>());
		}
		else { // There is no active chat for this user
			// Do nothing?
		}
		
	}
	
	public boolean deleteChat(int chatID) {
		try {
			if(activeChatID == chatID) {
				try {
					activeChatID = openChats.values().iterator().next().getChatID();
				}
				catch (Exception e) {
					activeChatID = 0;
				}
			}
			
			chats.remove(new ChatInfo(chatID, new User[0]));
			openChats.remove(chatID);
			apps.remove(chatID);
			
			setChanged();
			notifyObservers(new Integer(chatID));
			
			return true;
		} catch (Exception e) {
			System.out.println("Could not delete chat " + chatID + ".");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addFriend(User friend) {
		try {
			friends.add(friend);
			friends.sort((p1, p2) -> p1.getUsername().toLowerCase().compareTo(p2.getUsername().toLowerCase()));
			
			System.out.println("Friend " + friend.getUsername() + " added in AppModel.");
			
			setChanged();
			notifyObservers();
			
			return true;
		} catch (Exception e) {
			System.out.println("Could not add friend " + friend.getUsername());
			e.printStackTrace();
			
			return false;
		}
	}
	
	public boolean updateUser(EditUser editUser) {
		try {
			int myID = me.getUserID();
			String username = (editUser.getEdited()[0])?editUser.getFields()[0]:me.getUsername();
			String bio = (editUser.getEdited()[2])?editUser.getFields()[2]:me.getBio();
			
			this.me = new User(myID, username, bio);
			
			for(ChatInfo c : chats) {
				c.updateUser(me);
			}
			for(ChatApp c : openChats.values()) {
				c.updateUser(me);
			}
			
			System.out.println("ChatInfos:");
			for(ChatInfo c : chats) {
				User[] us = c.getUsers();
				for(User u : us) System.out.println(u);
			}
			System.out.println("ChatApps:");
			for(ChatApp c : openChats.values()) {
				User[] us = c.getUsers();
				for(User u : us) System.out.println(u);
			}
			
			setChanged();
			notifyObservers();
			
			return true;
		} catch (Exception e) {
			System.out.println("Could not update user.");
			e.printStackTrace();
			
			return false;
		}
	}
	
	public void startChat(List<User> participants) {
		
		participants.add(me);
		
		setChanged();
		notifyObservers(new CreateChat(getUserID(), MemoUtilities.getUserIDs(participants)));
		
	}
	
	public void openChat(int chatID) {
		
		// If current chat, do nothing - i.e. chatID == activeChatID
		
		if(openChats.containsKey(new Integer(chatID))) { // Not current chat
			activeChatID = chatID;
		}
		else if(chats.contains(new ChatInfo(chatID, null))) { // In chats list but not open -> open chat
			openChats.put(chatID, new ChatApp(this, chatID));
			apps.put(chatID, new TreeMap<Integer, App>());
			activeChatID = chatID;
		}
		else {
			System.out.println("Chat not available."); // This really should never happen...
		}
		
		//view.openChat((ChatPanel)openChats.get(chatID).getPanel());
		setChanged();
		notifyObservers(openChats.get(chatID)); // tick
	}
	
	public void openApp(int chatID, int targetID) {
		
		if(chatID == activeChatID) { // We only want to act on opening apps for active chat
			if(apps.get(chatID).containsKey(targetID)) { // App is open -> display app
				//view.openApp(chatID, apps.get(chatID).get(targetID).getPanel());
				setChanged();
				notifyObservers(apps.get(chatID).get(targetID)); // tick
			}
			else { // App is not open
				try {
					App newApp = (App)AppUtilities.getAppTypes().get(targetID).getConstructor(AppModel.class, Integer.class).newInstance(this, chatID);
					if(newApp.getError() != -1) {
						displayDialogue(newApp.getErrorMessage(newApp.getError()));
						return;
					}
					apps.get(chatID).put(targetID, newApp); // HOW DO I INSTANTIATE FROM ID?
					//view.openApp(chatID, apps.get(chatID).get(targetID).getPanel());
					setChanged();
					notifyObservers(apps.get(chatID).get(targetID)); // Open app (add if needed) // tick
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	public boolean passMessageToApps(Message message) {
		if(message.getAppTarget() == AppUtilities.chatTargetID) {
			// Needs to handle case where no chat with matching ID exists.
			
			if(message.getMeta().length() > 0) {
				if(message.getMeta().equals("delete")) {
					deleteChat(message.getChatID());
					return true;
				}
			}
			
			return openChats.get(message.getChatID()).preprocessMessage(message);
		}
		else {
			// Find apps belonging to correct chat
			TreeMap<Integer, App> chatIDapps = apps.get(message.getChatID());
			
			// Find correct app type
			for(Entry<Integer, App> e : chatIDapps.entrySet()) {
				
				if(e.getValue().getTargetID() == message.getAppTarget()) {
					return e.getValue().preprocessMessage(message);
				}
			}
			
			// If app can't be found return a failure
			System.out.println("Could not find correct app for Message.");
			return false;
		}
	}
	
	
	public boolean passHistoryToApps(History history) {
		if(history.getAppTarget() == AppUtilities.chatTargetID) {
			if(!chats.contains(history.getChatInfo())) { // This is a newly created chat
				chats.add(history.getChatInfo());
				setChanged();
				notifyObservers();
				return true;
			}
			else { // This is an open chat
				return openChats.get(history.getChatID()).receiveHistory(history);
			}
		}
		else {
			// Find apps belonging to correct chat
			TreeMap<Integer, App> chatIDapps = apps.get(history.getChatID());
			
			// Find correct app type
			for(Entry<Integer, App> e : chatIDapps.entrySet()) {
				
				if(e.getValue().getTargetID() == history.getAppTarget()) {
					return e.getValue().receiveHistory(history);
				}
			}
			
			// If app can't be found return a failure
			// SHOULD MAKE A NEW ONE? - Or should history only be sent to me if requested?
			return false;
		}
	}
	
	public void passMemoToServer(Memo memo) {
		//return controller.passMemoToServer(memo);
		setChanged();
		notifyObservers(memo); // tick
	}
	
	public void displayDialogue(String text) {
		//view.displayDialogue(text);
		setChanged();
		notifyObservers(text); // tick
	}
	
	public User getUser() {
		return me;
	}
	
	public int getUserID() {
		return me.getUserID();
	}
	
	public String getUsername() {
		return me.getUsername();
	}
	
	public List<User> getFriends() {
		return friends;
	}
	
	public User getFriend(int friendID) {
		return MemoUtilities.getUser(friendID, friends);
	}
	
	public User getFriend(String friendName) {
		return MemoUtilities.getUser(friendName, friends);
	}
	
	public int getActiveChatID() {
		return activeChatID;
	}
	
	public ChatApp getActiveChat() {
		return openChats.get(activeChatID);
	}
	
	public Map<Integer, ChatApp> getOpenChatsMap() {
		return openChats;
	}
	
	public List<ChatApp> getOpenChatAppList() {
		List<ChatApp> myOpenChats = new ArrayList<ChatApp>();
		
		for(ChatApp chat : openChats.values()) { // How dangerous is it to return chats.values()...?
			myOpenChats.add(chat);
		}
		
		return myOpenChats;
	}
	
	public User[] getOpenChatUsers(int chatID) {
		return openChats.get(chatID).getUsers();
	}
	
	public ChatApp getOpenChatApp(int chatID) {
		return openChats.get(chatID);
	}

	public Map<Integer, App> getAppSet(int chatID) {
		return apps.get(chatID);
	}
	
	public Set<ChatInfo> getUserChats() {
		return chats;
	}
	
	public ChatInfo getChatInfo(int chatID) {
		for( ChatInfo info : chats ) {
			if(info.getChatID() == chatID) return info;
		}
		return null;
	}
	
}
