package applications.morris;

class Mill {
	protected Piece[] mill;
	protected int type;

	protected Mill(Piece[] mill) {
		if (mill.length != 3) throw new IllegalArgumentException();
		this.mill = mill;
	}

	protected Mill(Piece p1, Piece p2, Piece p3) {
		this.mill = new Piece[] { p1, p2, p3 };
		this.type = findType(this);
	}

	public boolean equals(Object o) {
		Mill m = null;
		try {
			m = (Mill) o;
		} catch (ClassCastException e) {
			return false;
		}
		return this.contains(m.mill[0]) && this.contains(m.mill[1]) && this.contains(m.mill[2]);
	}

	static int findType(Mill m) {
		if (m.mill[0].node.row == m.mill[1].node.row && m.mill[0].node.row == m.mill[2].node.row) return 0;
		if (m.mill[0].node.col == m.mill[1].node.col && m.mill[0].node.col == m.mill[2].node.col) return 1;
		if (m.mill[0].node.square == m.mill[1].node.square && m.mill[0].node.square == m.mill[2].node.square) return 2;
		else return -1;
	}

	boolean contains(Piece piece) {
		for (Piece p : mill) {
			if (p.equals(piece)) return true;
		}
		return false;
	}
	
	static boolean possibleMill(Piece p1, Piece p2) {
		return (p1.node.row==p2.node.row) || (p1.node.col==p2.node.col) || (p1.node.square==p2.node.square);
	}
	
	static boolean isMill(Piece p1, Piece p2, Piece p3) {
		int sameRow = 0, sameCol = 0, sameSquare = 0;
		
		if(p1.node.row==p2.node.row && p1.node.row==p3.node.row) sameRow = 1;
		if(p1.node.col==p2.node.col && p1.node.col==p3.node.col) sameCol = 1;
		if(p1.node.square==p2.node.square && p1.node.square==p3.node.square) sameSquare = 1;
		
		int sum = sameRow + sameCol + sameSquare;
		
		return sum==2;
	}
}