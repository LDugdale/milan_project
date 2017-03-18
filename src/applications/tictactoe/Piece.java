package applications.tictactoe;

class Piece {
	
	protected char token;
	protected int row;
	protected int col;
	
	protected Piece(char token, int row, int col) {
		this.token = token;
		this.row = row;
		this.col = col;
	}
	
	protected Piece(String message) {
		String[] move = message.split(" ");
		
		this.token = move[0].charAt(0);
		this.row = Integer.parseInt(move[1]);
		this.col = Integer.parseInt(move[2]);
	}
	
	public String toString() {
		return token + " " + row + " " + col;
	}
	
	public boolean equals(Object o) {
		Piece p = (Piece)o;
		return this.row==p.row && this.col==p.col;
	}
	
}
