package applications.tictactoe;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

class PlayerBadge extends JPanel {

	private static final long serialVersionUID = -6012572855134270039L;
	
	private TictactoeBoardPanel board;
	
	private Player player;
	
	private JLabel label;
	private JPanel badge;
	
	protected PlayerBadge(TictactoeBoardPanel board, Player player) {
		super();
		this.board = board;
		this.player = player;
		
		label = new JLabel();
		updateLabel();
		
		badge = new Badge();
		
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
		label.setText("<html><center>" + this.player.user.getUsername() + "<br>" + this.player.score + "</center>");
	}
	
	class Badge extends JPanel {
		private static final long serialVersionUID = 5781470035673776453L;
		
		protected Badge() {
			super();
			//setPreferredSize(new Dimension(20,30));
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(board.pieceStrokeSize()));
			board.drawToken(g2, player.token, this.getWidth()/2, this.getHeight()/2);
			
			updateLabel();
		}
	}

}
