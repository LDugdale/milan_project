package applications.morris;

class Piece {
	
	protected char token;
	
	protected Node node;
	
	protected boolean inMill;
	
	private Piece(char token) {
		this.token = token;
		this.inMill = false;
	}
	
	// Location with no assigned token
	protected Piece(int row, int col, int square) {
		this('n');
		
		this.node = new Node(row, col, square);
	}

	protected Piece(char token, int row, int col, int square) {
		this(token);
		
		this.node = new Node(row, col, square);
	}
	
	protected Piece(char token, Node node) {
		this(token);
		
		this.node = node;
	}

	@Override
	public boolean equals(Object o) {
		Piece p = (Piece) o;
		
		//System.out.println("Piece.equals: " + (this.node.equals(p.node)));
		
		return this.node.equals(p.node);
	}

	public String toString() {
		return token + " " + node;
	}
}