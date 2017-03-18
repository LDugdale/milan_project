package applications.morris;

import java.util.Arrays;

public class TestMorris {

	public static void main(String[] args) {
		
		Move move = new Move('w', Move.PLACE, new Piece('w', 0, 1, 1));
		
		System.out.println("Move: " + move);
		System.out.println("\t" + move.token);
		System.out.println("\t" + move.type);
		for(int i=0; i<move.pieces.length; i++) {
			System.out.println("\t" + move.pieces[i]);
		}
		
		String moveString = move.toString();
		
		System.out.println("My move: " + moveString);
		
		String[] moveInfo = moveString.split(" ");
		
		System.out.println("Array: " + Arrays.toString(moveInfo));
		
		Move reconstructed = new Move(moveString);
		
		System.out.println("Reconstructed: " + reconstructed);
		System.out.println("\t" + reconstructed.token);
		System.out.println("\t" + reconstructed.type);
		for(int i=0; i<reconstructed.pieces.length; i++) {
			System.out.println("\t" + reconstructed.pieces[i]);
		}
		
		/*
		MorrisApp app = new MorrisApp(null, 1);
		
		JFrame frame = new JFrame();
		
		frame.add(app.getPanel());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		User[] users = new User[] {new User(1, "Tom"), new User(2, "Laurie")};
		int chatID = 1;
		int appTarget = 1;
		
		List<Message> history = new ArrayList<Message>();
		history.add(new Message(users, chatID, appTarget, 1, 1, "new", "1 w 0 0"));
		app.receiveHistory(new History(users, chatID, appTarget, history));
		*/
		
		Node n1 = new Node(0,0,0);
		Piece p1 = new Piece('w', 0, 0, 0);
		
		System.out.println("n1.equals(p1) = " + (p1.equals(new Piece('n', n1))));
	}

}
