package communications;

import java.util.List;

public final class MemoUtilities {
	
	public static int[] getUserIDs(User[] users) {
		int[] ids = new int[users.length];
		for(int i=0; i<users.length; i++) {
			ids[i] = users[i].getUserID();
		}
		return ids;
	}
	
	public static String[] getUsernames(User[] users) {
		String[] names = new String[users.length];
		for(int i=0; i<users.length; i++) {
			//System.out.println("Username: " + users[i].getUsername() + " " + (i+1) + "/" + users.length);
			names[i] = users[i].getUsername();
		}
		return names;
	}
	
	public static String getUsername(int userID, User[] users) {
		for(int i=0; i<users.length; i++) {
			if(users[i].getUserID() == userID) return users[i].getUsername();
		}
		return null;
	}
	
	public static int getUserID(String username, User[] users) {
		for(int i=0; i<users.length; i++) {
			if(users[i].getUsername().equals(username)) return users[i].getUserID();
		}
		return -1;
	}
	
	public static User getUser(int userID, User[] users) {
		for(int i=0; i<users.length; i++) {
			if(users[i].getUserID() == userID) return users[i];
		}
		return null;
	}
	
	public static User getUser(String username, User[] users) {
		for(int i=0; i<users.length; i++) {
			if(users[i].getUsername().equals(username)) return users[i];
		}
		return null;
	}
	
	public static int[] getUserIDs(List<User> users) {
		int[] ids = new int[users.size()];
		for(int i=0; i<users.size(); i++) {
			ids[i] = users.get(i).getUserID();
		}
		return ids;
	}
	
	public static String[] getUsernames(List<User> users) {
		String[] names = new String[users.size()];
		for(int i=0; i<users.size(); i++) {
			//System.out.println("Username: " + users[i].getUsername() + " " + (i+1) + "/" + users.length);
			names[i] = users.get(i).getUsername();
		}
		return names;
	}
	
	public static String getUsername(int userID, List<User> users) {
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getUserID() == userID) return users.get(i).getUsername();
		}
		return null;
	}
	
	public static int getUserID(String username, List<User> users) {
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getUsername().equals(username)) return users.get(i).getUserID();
		}
		return -1;
	}
	
	public static User getUser(int userID, List<User> users) {
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getUserID() == userID) return users.get(i);
		}
		return null;
	}
	
	public static User getUser(String username, List<User> users) {
		for(int i=0; i<users.size(); i++) {
			if(users.get(i).getUsername().equals(username)) return users.get(i);
		}
		return null;
	}
	
}
