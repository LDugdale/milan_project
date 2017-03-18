package communications;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {
	
	private static final long serialVersionUID = -4954001457861113852L;
	
	private int userID;
	private String username;
	
	private String bio;
	
	private int avatar;
	
	public User(int userID, String username) {
		super();
		this.userID = userID;
		this.username = username;
		
		setBio("");
		setAvatar(0);
	}
	
	public User(int userID, String username, String bio) {
		this(userID, username);
		
		setBio(bio); // In case we want to enforce constraints on bio length, etc?
	}
	
	public User(int userID, String username, int avatar) {
		this(userID, username);
		
		setAvatar(avatar);
	}
	
	public User(int userID, String username, String bio, int avatar) {
		this(userID, username);
		
		setBio(bio);
		setAvatar(avatar);
	}

	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}
	
	public String getBio() {
		return bio;
	}
	
	public void setBio(String bio) {
		// Enforce constraints? e.g. length
		this.bio = bio;
	}
	
	public int getAvatar() {
		return avatar;
	}
	
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	public boolean equals(Object o) {
		User u = null;
		
		try {
			u = (User)o;
		} catch (ClassCastException e) {
			return false;
		}
		
		return this.userID == u.userID;
	}
	
	public static String toString(User[] users) {
		return Arrays.toString(MemoUtilities.getUsernames(users));
	}
	
	public String toString() {
		return username + " (" + userID + ")";
	}
	
}
