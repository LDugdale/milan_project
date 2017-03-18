package applications.morris;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

class PlayerBadge extends JPanel {

	private static final long serialVersionUID = -6012572855134270039L;
	
	private MorrisApp model;
	private MorrisBoardPanel board;
	
	private int player;
	
	private JLabel label;
	
	PiecesList leftToPlace;
	PiecesList takenPieces;
	
	protected PlayerBadge(MorrisApp model, MorrisBoardPanel board, int player) {
		super();
		this.model = model;
		this.board = board;
		this.player = player;
		
		label = new JLabel();
		updateLabel();
		
		JPanel badge = new JPanel();
		
		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		
		leftToPlace = new PiecesList("To Place");
		takenPieces = new PiecesList("Taken");
		
		JLabel leftLabel = new JLabel(leftToPlace.title);
		JLabel rightLabel = new JLabel(takenPieces.title);
		
		left.add(leftToPlace, BorderLayout.CENTER);
		right.add(takenPieces, BorderLayout.CENTER);
		
		left.add(leftLabel, BorderLayout.PAGE_START);
		right.add(rightLabel, BorderLayout.PAGE_START);
		
		badge.setLayout(new GridLayout(1,2));
		
		badge.add(left);
		badge.add(right);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(label);
		add(badge);
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		badge.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(label.getFont().deriveFont(32.0f));
		
		revalidate();
		repaint();
	}
	
	protected void updateLabel() {
		label.setText("<html><center>" + model.getPlayer(player).user.getUsername() + "<br>" + model.getPlayer(player).score + "</center>");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		leftToPlace.token = model.getPlayer(player).token;
		takenPieces.token = model.swapToken(model.getPlayer(player).token);
		leftToPlace.count = model.piecesLeftToTake(model.getPlayer(player).token);
		takenPieces.count = model.piecesTaken(model.swapToken(model.getPlayer(player).token));
		
		leftToPlace.repaint();
		takenPieces.repaint();
		
		updateLabel();
	}
	
	class PiecesList extends JPanel {

		private static final long serialVersionUID = 173651312858972173L;
		
		String title;
		char token;
		int count;
		
		protected PiecesList(String title) {
			super();
			
			this.title = title;
			this.token = 'n';
			this.count = 0;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(board.pieceStrokeSize()));
			
			int height = this.getHeight();
			int x = this.getWidth()/2;
			for(int i=0; i<count; i++) {
				int y = (int)(height*0.1 + i*(height*0.8)/12);
				board.drawPiece(g2, x, y, token);
			}
			
			updateLabel();
		}
	}

}
