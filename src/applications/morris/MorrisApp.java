package applications.morris;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import applications.App;
import communications.History;
import communications.Message;
import communications.User;
import messenger.AppModel;

public class MorrisApp extends App {
	
	private Player[] players;
	
	private int whitePiecesPlaced;
	private int blackPiecesPlaced;
	
	private int whitePiecesTaken;
	private int blackPiecesTaken;
	
	private List<Piece> moves;
	private char[][][] board;
	
	private boolean gameWon;
	
	private char myToken;
	
	private char nextPiece;
	
	private String controlsMessage;
	private String bannerMessage;
	
	GameState gameState;
	
	private char highlighted;
	
	public MorrisApp() {
		super();
	}
	
	public MorrisApp(AppModel model, Integer myChatID) {
		super(model, myChatID);
		
		if(model != null) {
			if(model.getOpenChatUsers(myChatID).length!=2) {
				error = 1;
				return;
			}
		}
		
		controlsMessage = new String();
		bannerMessage = new String();
		nodes = new ArrayList<Node>();
		
		highlighted = 'n';
	}
	
	Map<Character, List<Mill>> millHistory;
	
	List<Node> nodes;
	Awaiting awaiting;
	
	enum GameState {
		PLACING, MOVING
	}
	
	enum Awaiting {
		START, MOVE_END, TAKE, MEMO
	}
	
	public void click(Node node) {
		if(!isValidNode(node)) return;
		
		if(myMove() && !gameWon) {
			if(gameState == GameState.PLACING) {
				if(awaiting == Awaiting.START) {
					if(isOccupied(node)) return;
					else {
						if(needTake(node)) {
							awaiting = Awaiting.TAKE;
							nodes.add(node);
							highlighted = swapToken(myToken);
							return;
						}
						else {
							move(true, new Move(myToken, Move.PLACE, new Piece(myToken, node)));
							return;
						}
					}
				}
				else if(awaiting == Awaiting.TAKE) {
					if(isValidTake(node, myToken)) {
						move(true, new Move(myToken, Move.PLACE_TAKE, new Piece(myToken, nodes.get(0)), new Piece(swapToken(myToken), node)));
						highlighted = 'n';
						return;
					}
					else { // Invalid take
						return;
					}
				}
			}
			else if(gameState == GameState.MOVING) {
				if(awaiting == Awaiting.START) {
					if(isToken(node, myToken)) {
						awaiting = Awaiting.MOVE_END;
						nodes.add(node);
						return;
					}
					else { // Not my token to move
						return;
					}
				}
				else if(awaiting == Awaiting.MOVE_END) {
					if(isToken(node, myToken)) {
						nodes.remove(0);
						nodes.add(node);
						return;
					}
					else if(isValidMove(myToken, nodes.get(0), node)) {
						if(needTake(nodes.get(0), node)) {
							awaiting = Awaiting.TAKE;
							nodes.add(node);
							highlighted = swapToken(myToken);
							return;
						}
						else {
							move(true, new Move(myToken, Move.MOVE, new Piece(myToken, nodes.get(0)), new Piece(myToken, node)));
							return;
						}
					}
					else { // Not valid move
						return;
					}
				}
				else if(awaiting == Awaiting.TAKE) {
					if(isValidTake(node, myToken)) {
						move(true, new Move(myToken, Move.MOVE_TAKE, new Piece(myToken, nodes.get(0)), new Piece(myToken, nodes.get(1)), new Piece(swapToken(myToken), node)));
						highlighted = 'n';
						return;
					}
					else { // Invalid take
						return;
					}
				}
			}
		}
		else { // Not my move
			System.out.println("Not my move.");
			return;
		}
	}
	
	Object moveLock = new Object();
	
	void move(boolean userInput, Move move) {
		
		synchronized (moveLock) {
			
			if (gameWon) return; // Game is already over
			
			if (move.token == pollNextPiece()) {
				
				System.out.println("Move: " + move);
				
				controlsMessage = "";

				if (gameState == GameState.PLACING) {
					setPiece(move.pieces[0]);
					incrementPieceCounter(move.pieces[0]);
					moves.add(move.pieces[0]);

					if (move.type == Move.PLACE_TAKE) {
						setNode(move.pieces[1].node, 'n');
						moves.remove(move.pieces[1]);
						incrementTakenCounter(move.pieces[1]);
					}

					if (allPiecesPlaced()) {
						gameState = GameState.MOVING;
					}
				} else if (gameState == GameState.MOVING) {
					setNode(move.pieces[0].node, 'n');
					setPiece(move.pieces[1]);
					moves.remove(move.pieces[0]);
					moves.add(move.pieces[1]);

					if (move.type == Move.MOVE_TAKE) {
						setNode(move.pieces[2].node, 'n');
						moves.remove(move.pieces[2]);
						incrementTakenCounter(move.pieces[2]);
					}
				}

				nodes = new ArrayList<Node>();

				setMillHistory(move.token);
				//for (int i = 0; i < millHistory.get(move.token).size(); i++) {
				//	System.out.println("Mill for " + move.token + ": " + millHistory.get(move.token).get(i));
				//}

				if (userInput) {
					//System.out.println("Send message from Morris.");
					sendMessage("move", move.toString());
				}
				
				nextPiece();
				if (pollNextPiece() == myToken) {
					awaiting = Awaiting.START;
				} else {
					awaiting = Awaiting.MEMO;
				}

				if (isGameEnd()) {
					gameWon = true;

					Player winner = getPlayer(swapToken(pollNextPiece()));
					winner.incrementScore();
					controlsMessage = winner.user.getUsername() + " has won!";
					bannerMessage = "";
				}
				else {
					bannerMessage = getPlayer(pollNextPiece()).user.getUsername() + " to " + ((gameState == GameState.MOVING)?"move":"place") + ".";
				}

				setChanged();
				notifyObservers();
			} else { // Invalid move - wrong token
				System.out.println("Move supplied with wrong token: " + move.token);
				return;
			}
			
		}
	}
	
	boolean isGameEnd() {
		if(gameState == GameState.MOVING && (pieces('w').size() < 3 || pieces('b').size() < 3)) {
			System.out.println("Game over: only two pieces left.");
			return true;
		}
		else {
			//System.out.println("w: " + pieces('w') + ", b: " + pieces('b'));
			//System.out.println("pollNextPiece: " + pollNextPiece());
			
			if(pieces(pollNextPiece()).size() == 0) {
				return false;
			}
			else {
				for(Piece p : pieces(pollNextPiece())) {
					//System.out.println(p);
					if(canMove(p)) return false;
				}
				System.out.println("Game over: cannot move.");
				return true;
			}
		}
	}
	
	public char highlighted() {
		return highlighted;
	}
	
	public List<Node> possibleMoveNodes() {
		
		if(awaiting == Awaiting.MOVE_END) {
			List<Node> nodes = new ArrayList<Node>();
			
			if(pieces(pollNextPiece()).size() <= 3) {
				for(int i=0; i<=2; i++) {
					for(int j=0; j<=2; j++) {
						for(int k=0; k<=2; k++) {
							Node temp = new Node(i, j, k);
							if(isValidNode(temp)) {
								if(!isOccupied(temp)) {
									nodes.add(temp);
								}
							}
						}
					}
				}
				return nodes;
			}
			
			if(this.nodes.size() > 0) {
				Node n = this.nodes.get(0);
				
				for(int i=-1; i<=1; i++) {
					for(int j=-1; j<=1; j++) {
						for(int k=-1; k<=1; k++) {
							Node temp = new Node(n.row+i, n.col+j, n.square+k);
							if(isValidNode(temp)) {
								if(!isOccupied(temp) && Node.manhattenDistance(n, temp) == 1) {
									nodes.add(temp);
								}
							}
						}
					}
				}
				
				return nodes;
			}
			else return new ArrayList<Node>();
		}
		else {
			return new ArrayList<Node>();
		}
	}
	
	
	boolean canMove(Piece piece) {
		Node n = piece.node;
		
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				for(int k=-1; k<=1; k++) {
					Node temp = new Node(n.row+i, n.col+j, n.square+k);
					if(isValidNode(temp)) {
						if(!isOccupied(temp) && Node.manhattenDistance(n, temp) == 1) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	boolean needTake(Node node) {
		List<Mill> newMills = findNewMillsWith(new Piece(myToken, node));
		return newMills.size() > 0;
	}
	
	boolean needTake(Node without, Node with) {
		List<Mill> mills = findMillsWithWithout(new Piece(myToken, with), new Piece(myToken, without));
		
		List<Mill> newMills = mills.stream().filter(m -> !millHistory.get(myToken).contains(m)).collect(Collectors.toList());
		
		return newMills.size() > 0;
	}
	
	boolean isValidTake(Node node, char token) {
		if(isToken(node, swapToken(myToken))) { // Correct token...
			// Check it is not in a mill
			if(allInMills(swapToken(token))) return true;
			else { // Some of the pieces are in mills, some are not
				return !getPiece(node).inMill;
			}
		}
		else { // Incorrect token...
			return false;
		}
	}
	
	boolean isOccupied(Node node) {
		char location = board[node.row][node.col][node.square];
		
		return location=='w' || location =='b';
	}
	
	boolean isToken(Node node, char token) {
		return board[node.row][node.col][node.square] == token;
	}
	
	void setNode(Node node, char token) {
		board[node.row][node.col][node.square] = token;
	}
	
	void setPiece(Piece piece) {
		board[piece.node.row][piece.node.col][piece.node.square] = piece.token;
	}
	
	void incrementPieceCounter(Piece piece) {
		if(piece.token == 'w') {
			whitePiecesPlaced++;
		}
		else if(piece.token == 'b') {
			blackPiecesPlaced++;
		}
	}
	
	void incrementTakenCounter(Piece piece) {
		if(piece.token == 'w') {
			whitePiecesTaken++;
		}
		else if(piece.token == 'b') {
			blackPiecesTaken++;
		}
	}
	
	public int piecesLeftToTake(char token) {
		if(token == 'w') {
			return 12 - whitePiecesPlaced;
		}
		else if(token == 'b') {
			return 12 - blackPiecesPlaced;
		}
		else return -1;
	}
	
	public int piecesTaken(char token) {
		if(token == 'w') {
			return whitePiecesTaken;
		}
		else if(token == 'b') {
			return blackPiecesTaken;
		}
		else return -1;
	}
	
	boolean allPiecesPlaced() {
		return whitePiecesPlaced>=12 && blackPiecesPlaced >= 12;
	}
	
	boolean isValidMove(char token, Node startNode, Node endNode) {
		if(isOccupied(endNode)) return false;
		
		if(pieces(token).size() <= 3) {
			return true;
		}
		else {
			int dist = Node.manhattenDistance(startNode, endNode);
			return dist==1;
		}
	}
	
	public boolean myMove() {
		return pollNextPiece() == myToken;
	}
	
	boolean isValidNode(Node node) {
		return isValidNode(node.row, node.col, node.square);
	}
	
	boolean isValidNode(int row, int col, int square) {
		return (row >= 0 && row <=2 && col >= 0 && col <=2 && !(row==1 && col==1) && square >= 0 && square <=2);
	}
	
	List<Piece> pieces(char token) {
		return moves.stream().filter(e -> e.token==token).collect(Collectors.toList());
	}
	
	List<Mill> findNewMillsWith(Piece suggestedPiece) {
		List<Mill> mills = findMillsWith(suggestedPiece);
		
		return mills.stream().filter(m -> !millHistory.get(suggestedPiece.token).contains(m)).collect(Collectors.toList());
	}
	
	void resetMills(char token) {
		List<Piece> pieces = pieces(token);
		for(Piece p : pieces) {
			p.inMill = false;
		}
	}
	
	void setMillHistory(char token) {
		resetMills(token);
		millHistory.put(token, millsInPieces(pieces(token)));
	}
	
	List<Mill> findMillsWith(Piece suggestedPiece) {
		List<Piece> pieces = pieces(suggestedPiece.token);
		pieces.add(suggestedPiece);
		
		List<Mill> tokenMills = millsInPieces(pieces);
		
		List<Mill> mills = tokenMills.stream().filter(m -> m.contains(suggestedPiece)).collect(Collectors.toList());

		return mills;
	}
	
	List<Mill> findMillsWithWithout(Piece with, Piece without) {
		if(with.token != without.token) throw new IllegalArgumentException();
		
		List<Piece> pieces = pieces(with.token);
		pieces.add(with);
		pieces.remove(without);
		
		List<Mill> tokenMills = millsInPieces(pieces);
		
		List<Mill> mills = tokenMills.stream().filter(m -> m.contains(with)).collect(Collectors.toList());

		return mills;
	}
		
	List<Mill> millsInPieces(List<Piece> pieces) {
		List<Mill> mills = new ArrayList<Mill>();
		
		for(Piece p1 : pieces) {
			for(Piece p2 : pieces.stream().filter(e -> !e.equals(p1)).collect(Collectors.toList())) {
				if(Mill.possibleMill(p1, p2)) {
					for(Piece p3 : pieces.stream().filter(e -> !e.equals(p1) && !e.equals(p2)).collect(Collectors.toList())) {
						if(Mill.isMill(p1,p2,p3)) {
							p1.inMill = true;
							p2.inMill = true;
							p3.inMill = true;
							Mill mill = new Mill(p1,p2,p3);
							if(!mills.contains(mill)) mills.add(mill);
						}
					}
				}
			}
		}
		
		return mills;
	}
	
	boolean allInMills(char token) {
		List<Piece> pieces = pieces(token);
	
		for(Piece p : pieces) {
			if(!p.inMill) {
				return false;
			}
		}
		return true;
	}
	
	void startNewGame() {
		
		controlsMessage = "Please wait...";
		setChanged();
		notifyObservers();
		
		updateHistory();
	}
	
	void newGame() {
		System.out.println("New Game.");
		
		gameWon = false;
		moves = new ArrayList<Piece>();
		board = new char[3][3][3];
		nextPiece = 'w';
		whitePiecesPlaced = 0;
		blackPiecesPlaced = 0;
		whitePiecesTaken = 0;
		blackPiecesTaken = 0;
		millHistory = new TreeMap<Character, List<Mill>>();
		millHistory.put('w', new ArrayList<Mill>());
		millHistory.put('b', new ArrayList<Mill>());
		
		gameState = GameState.PLACING;
		
		if(myToken == nextPiece) awaiting = Awaiting.START;
		else awaiting = Awaiting.MEMO;
		
		bannerMessage = getPlayer(nextPiece).user.getUsername() + " to move.";
		
		setChanged();
		notifyObservers();
	}
	
	char nextPiece() {
		char t = nextPiece;
		nextPiece = swapToken(nextPiece);
		return t;
	}
	char pollNextPiece() {
		return nextPiece;
	}
	
	public char swapToken(char token) {
		if(token == 'w') return 'b';
		else return 'w';
	}
	
	public List<Piece> getMoves() {
		List<Piece> myMoves = new ArrayList<Piece>();
		myMoves.addAll(moves);
		
		if(awaiting == Awaiting.TAKE) {
			if(gameState == GameState.PLACING) {
				myMoves.add(new Piece(pollNextPiece(), nodes.get(0)));
			}
			else if(gameState == GameState.MOVING) {
				myMoves.add(new Piece(pollNextPiece(), nodes.get(1)));
				myMoves.remove(new Piece(pollNextPiece(), nodes.get(0)));
			}
		}
		
		return myMoves;
	}
	
	Piece getPiece(Node node) {
		/*
		System.out.println("Begin getPiece().");
		int index = moves.indexOf(new Piece('n', node));
		
		System.out.println("Node: " + node);
		System.out.println("Moves: ");
		for(int i=0; i<moves.size(); i++) {
			System.out.println("\t" + moves.get(i));
		}
		System.out.println("Index: " + index);
		
		System.out.println("End getPiece().");
		if(index == -1) return null;
		else return moves.get(index);
		*/
		Piece temp = new Piece('n', node);
		for(int i=0; i<moves.size(); i++) {
			if(moves.get(i).equals(temp)) return moves.get(i);
		}
		return null;
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	public Player getPlayer(char token) {
		if(players[0].token == token) return players[0];
		else return players[1];
	}
	
	

	@Override
	public void updateHistory() {
		List<Message> messages = new ArrayList<Message>();
		
		// Add "new game" message, with tokens and scores
		if(myToken!='w' && myToken!='b') myToken = 'b';
		messages.add(generateMessage("new", model.getUserID() + " " + swapToken(myToken) + " " + players[0].score + " " + players[1].score));
		
		History history = new History(myUsers, myChatID, getTargetID(), messages);
		
		model.passMemoToServer(history);
	}

	@Override
	public boolean receiveMessage(Message message) {
		//System.out.println("Morris message: " + message.getMeta() + " " + message.getMessage());
		
		String[] meta = message.getMeta().split(" ");
		//System.out.println("TTT app recieved message: " + Arrays.toString(meta) + " " + message.getMessage());
		
		if(meta[0].equals("move")) {
			//System.out.println("Move: " + message.getMessage());
			move(false, new Move(message.getMessage()));
		}
		else if(meta[0].equals("new")) {
			//System.out.println("New game.");
			//((TictactoePanel)view).updateControlsLabel(message.getSenderUsername() + " has started a new game.");
			controlsMessage = message.getSenderUsername() + " has started a new game.";
			
			String[] messageData = message.getMessage().split(" ");
			int id = Integer.parseInt(messageData[0]);
			char token = messageData[1].charAt(0);
			int score1 = Integer.parseInt(messageData[2]);
			int score2 = Integer.parseInt(messageData[3]);
			
			System.out.println("ID: " + id + ", token: " + token + ", score1: " + score1 + ", score2: " + score2);
			
			if(id == model.getUserID()) {
				players[0].token = token;
				players[0].score = score1;
				players[1].token = swapToken(token);
				players[1].score = score2;
			}
			else {
				players[0].token = swapToken(token);
				players[0].score = score2;
				players[1].token = token;
				players[1].score = score1;
			}
			
			System.out.println("Players[0]: " + players[0].user + " " + players[0].token);
			System.out.println("Players[1]: " + players[1].user + " " + players[1].token);
			
			myToken = players[0].token;
			
			newGame();
			
			setChanged();
			notifyObservers();
		}
		
		return true;
	}

	@Override
	public boolean receiveHistory(History history) {
		
		try {
			myUsers = history.getUsers();
			
			players = new Player[2];
			players[0] = new Player(model.getUser(), 'n');
			players[1] = new Player(new User((myUsers[0].getUserID()==players[0].user.getUserID())?myUsers[1].getUserID():myUsers[0].getUserID(), (myUsers[0].getUsername().equals(players[0].user.getUsername()))?myUsers[1].getUsername():myUsers[0].getUsername()), 'n');
			
			myToken = 'n';
			
			moves = new ArrayList<Piece>();
			
			myUsers = history.getUsers();
			myChatID = history.getChatID();
			
			if(history.size()==0) {
				// No history given
				// Respond by creating a new game.
				updateHistory();
			}
			else {
				for(int i=0; i<history.size(); i++) {
					receiveMessage(history.get(i));
				}
				
				configured();
			}
			
			setChanged();
			notifyObservers();
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not process history.");
			e.printStackTrace();
			
			return false;
		}
	}
	
	@Override
	public String getDisplayName() {
		return "12 Men's Morris";
	}
	
	@Override
	public String getTitle() {
		return "Game of 12 Men's Morris";
	}
	
	@Override
	public String getType() {
		return "Game";
	}
	
	@Override
	public String getErrorMessage(int errorCode) {
		if(errorCode == 1) return getDisplayName() + " can only be played with 2 players.";
		else return "";
	}
	
	public String getControlsMessage() {
		return controlsMessage;
	}
	
	public String getBannerMessage() {
		return bannerMessage;
	}

}
