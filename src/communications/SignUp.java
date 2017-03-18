package communications;

public class SignUp extends ServerMemo {

	private static final long serialVersionUID = -1286501796460145708L;
	
	private String username;
	private String password;
	
	public SignUp(String username, String password) {
		super(-1);
		
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
