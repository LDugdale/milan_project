package communications;

public class Login extends ServerMemo {
	
	private static final long serialVersionUID = -656203520948072909L;
	
	private String username;
	private String password;
	
	public Login(String username, String password) {
		super(-1); // Client doesn't know its own userID at login
		
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
