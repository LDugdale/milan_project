package applications.tictactoe;

import java.util.ArrayList;
import java.util.List;

import applications.App;
import communications.History;
import communications.Message;
import communications.User;
import messenger.AppModel;

public class TictactoeApp extends App {
	
	private Player[] players;
	
	private List<Piece> moves;
	private char[][] board;
	private boolean[][] win;
	
	private boolean gameWon;
	
	private char myToken;
	
	private char nextPiece;
	
	private String controlsMessage;
	
	public TictactoeApp() {
		super();
	}

	public TictactoeApp(AppModel model, Integer myChatID) {
		super(model, myChatID, false);
		
		//System.out.println("TictactoeApp constructor...");
		
		myUsers = model.getOpenChatUsers(myChatID);
		
		if(myUsers.length!=2) {
			error = 1;
			return;
		}
		players = new Player[2];
		players[0] = new Player(model.getUser(), 'n');
		players[1] = new Player(new User((myUsers[0].getUserID()==players[0].user.getUserID())?myUsers[1].getUserID():myUsers[0].getUserID(), (myUsers[0].getUsername().equals(players[0].user.getUsername()))?myUsers[1].getUsername():myUsers[0].getUsername()), 'n');
		
		myToken = 'n';
		
		moves = new ArrayList<Piece>(); // Initialise to prevent NullPointerException errors
		
		controlsMessage = new String();
		
		//System.out.println("Tic tac toe user IDS: " + Arrays.toString(myUserIDs));
		//view.configure();
		configured();
		setChanged();
		notifyObservers(); // What kind of object to send to configure view?
		
		startNewGame();
	}

	@Override
	public void updateHistory() {
		List<Message> messages = new ArrayList<Message>();
		messages.add(generateMessage("new", model.getUsername() + " " + swapToken(myToken))); // Pass score?
		History history = new History(myUsers, myChatID, getTargetID(), messages);
		
		model.passMemoToServer(history);
	}

	@Override
	public boolean receiveMessage(Message message) {
		String[] meta = message.getMeta().split(" ");
		//System.out.println("TTT app recieved message: " + Arrays.toString(meta) + " " + message.getMessage());
		
		if(meta[0].equals("move")) {
			//System.out.println("Move: " + message.getMessage());
			String[] moveInfo = message.getMessage().split(" ");
			moveFromMemo(moveInfo[0].charAt(0), Integer.parseInt(moveInfo[1]), Integer.parseInt(moveInfo[2]));
		}
		//else if(meta[0].equals("win")) {
			//System.out.println("Message says win for " + (new Piece(message.getMessage())).token);
		//}
		else if(meta[0].equals("new")) {
			//System.out.println("New game.");
			//((TictactoePanel)view).updateControlsLabel(message.getSenderUsername() + " has started a new game.");
			controlsMessage = message.getSenderUsername() + " has started a new game.";
			setChanged();
			notifyObservers();
			
			String[] tokenInfo = message.getMessage().split(" ");
			if(tokenInfo[0].equals(model.getUsername())) {
				myToken = tokenInfo[1].charAt(0);
			}
			else myToken = swapToken(tokenInfo[1].charAt(0));
			players[0].token = myToken;
			players[1].token = swapToken(myToken);
			newGame();
		}
		
		return true;
	}

	@Override
	public boolean receiveHistory(History history) {
		for(int i=0; i<history.getHistory().size(); i++) {
			receiveMessage(history.getHistory().get(i));
		}
		resetMessageSettings();
		return true;
	}
	
	@Override
	public String getDisplayName() {
		return "Tic Tac Toe";
	}
	
	@Override
	public String getTitle() {
		return "Game of Tic Tac Toe";
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
	
	boolean getWin(Piece p) {
		return win[p.row][p.col];
	}
	
	void moveFromGUI(int row, int col) {
		if(pollNextPiece() == myToken) {
			
			if(!gameWon && isValidMove(myToken, row, col)) {
				
				Piece p = new Piece(myToken, row, col);
				
				move(p);
				
				sendMessage("move", p.toString());
				
			}
			else {
				// Deal with invalid move from GUI
				System.out.println("Invalid move from GUI.");
			}
			
		}
	}
	
	void moveFromMemo(char token, int row, int col) {
		if(pollNextPiece() == token) {
			
			if(!gameWon && isValidMove(token, row, col)) {
				move(new Piece(token, row, col));
			}
			else {
				// Deal with invalid move from server
				System.out.println("Invalid move from Memo.");
			}
			
		}
	}
	
	void move(Piece p) {
		
		moves.add(p);
		board[p.row][p.col] = p.token;
		nextPiece();
		//System.out.println("Piece added: " + moves.get(moves.size()-1).token);
		
		//((TictactoePanel)view).updateControlsLabel(""); // Update message to user to be empty
														// Will be updated later (by checkEnd() -> setWin() is needed
		controlsMessage = "";
		
		checkEnd();
		
		setChanged();
		notifyObservers(); // Update GUI
	}
	
	boolean isValidMove(char token, int row, int col) {
		return row >= 0 && row <=2 && col >= 0 && col <=2 && (board[row][col] != 'x' && board[row][col] != 'o');
	}
	
	boolean checkEnd() {
		//System.out.println("checkEnd() start.");
		// Check rows
		for(int i=0; i<3; i++) {
			if((board[i][0] == 'x' || board[i][0] == 'o') && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
				for(int j=0; j<3; j++) {
					win[i][j] = true;
				}
				//System.out.println("checkEnd(): row win.");
				//Thread.dumpStack();
				setWin(board[i][0]);
				return true;
			}
		}
		// Check columns
		for(int i=0; i<3; i++) {
			if((board[0][i] == 'x' || board[0][i] == 'o') && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
				for(int j=0; j<3; j++) {
					win[j][i] = true;
				}
				//System.out.println("checkEnd(): column win.");
				setWin(board[0][i]);
				return true;
			}
		}
		// Check diagonals
		if((board[0][0] == 'x' || board[0][0] == 'o') && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
			for(int j=0; j<3; j++) {
				win[j][j] = true;
			}
			//System.out.println("checkEnd(): diagonal 1 win.");
			setWin(board[0][0]);
			return true;
		}
		if((board[0][2] == 'x' || board[0][2] == 'o') && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
			win[0][2] = true;
			win[1][1] = true;
			win[2][0] = true;
			//System.out.println("checkEnd(): diagonal 2 win.");
			setWin(board[0][2]);
			return true;
		}
		
		// Check draw
		if(!gameWon && moves.size()==9) {
			// Draw
			//System.out.println("Draw.");
			//((TictactoePanel)view).updateControlsLabel("Draw");
			controlsMessage = "Draw";
			notifyObservers();
			return true;
		}
		
		//System.out.println("checkWin() end.");
		return false;
	}
	
	void setWin(char winningToken) {
		if(!gameWon) {
			gameWon = true;
			System.out.println(winningToken + " winner!");
			
			if(players[0].token == winningToken) players[0].incrementScore();
			else players[1].incrementScore();
			
			//((TictactoePanel)view).updateControlsLabel(Character.toUpperCase(winningToken) + " winner!");
			controlsMessage = Character.toUpperCase(winningToken) + " winner!";
			setChanged();
			notifyObservers();
		}
	}
	
	void startNewGame() {
		if(Character.compare(myToken, 'n') == 0) myToken = 'o'; // Initialise token if this is the first game
		
		//((TictactoePanel)view).updateControlsLabel("Pleas wait...");
		controlsMessage = "Please wait...";
		setChanged();
		notifyObservers();
		
		updateHistory();
	}
	
	void newGame() {
		gameWon = false;
		moves = new ArrayList<Piece>();
		board = new char[3][3];
		win = new boolean[3][3];
		nextPiece = 'x';
		//((TictactoePanel)view).updateControlsLabel("");
		
		//System.out.println(players[0]);
		//System.out.println(players[1]);
		
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
	
	char swapToken(char token) {
		if(token == 'x') return 'o';
		else return 'x';
	}
	
	public List<Piece> getMoves() {
		return moves;
	}
	
	public boolean isGameWon() {
		return gameWon;
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	public String getControlsMessage() {
		return controlsMessage;
	}

}
