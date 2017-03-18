package applications.morris;

class Move {
	
	char token;
	
	MoveType type;
	
	Piece[] pieces;
	
	public Move(char token, MoveType type, Piece... pieces) {
		this.token = token;
		this.type = type;
		this.pieces = pieces;
		
		switch(type) {
			case PLACE:
				if(pieces.length != 1) this.type = MoveType.INVALID;
				break;
				
			case MOVE:
				if(pieces.length != 2) this.type = MoveType.INVALID;
				break;
				
			case PLACE_TAKE:
				if(pieces.length != 2) this.type = MoveType.INVALID;
				break;
				
			case MOVE_TAKE:
				if(pieces.length != 3) this.type = MoveType.INVALID;
				break;
				
			default:
				this.type = MoveType.INVALID;
				break;
		}
		
		if(this.type == MoveType.INVALID) this.pieces = new Piece[0];
	
	}
	
	public Move(String move) {
		String[] moveInfo = move.split(" ");
		
		this.token = moveInfo[0].charAt(0);
		this.type = MoveType.valueOf(moveInfo[1]);
		
		int pieceCount = (moveInfo.length-1)/4;
		
		this.pieces = new Piece[pieceCount];
		
		for(int i=0; i<pieceCount; i++) {
			int start = 2 + 4*i;
			char token = moveInfo[start].charAt(0);
			int row = Integer.parseInt(moveInfo[start+1]);
			int col = Integer.parseInt(moveInfo[start+2]);
			int square = Integer.parseInt(moveInfo[start+3]);
			this.pieces[i] = new Piece(token, row, col, square);
		}
	}
	
	public String toString() {
		String str = "";
		str += token + " " + this.type;
		for(int i=0; i<pieces.length; i++) {
			str += " " + pieces[i];
		}
		return str;
	}
	
	public static MoveType PLACE = MoveType.PLACE;
	public static MoveType MOVE = MoveType.MOVE;
	public static MoveType PLACE_TAKE = MoveType.PLACE_TAKE;
	public static MoveType MOVE_TAKE = MoveType.MOVE_TAKE;
	
}
