package applications.morris;

import communications.User;

class Player {
	
	User user;
	
	char token;
	
	int score;

	protected Player(User user, char token) {
		this.user = user;
		this.token = token;
		
		score = 0;
	}
	
	protected Player(User user, char token, int score) {
		this(user, token);
		
		this.score = score;
	}
	
	protected void incrementScore() {
		score++;
	}
	
	public String toString() {
		return user.getUsername() + ": " + token;
	}
	
	public int getUserID() {
		return user.getUserID();
	}
	
}
