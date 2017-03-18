package applications.tictactoe;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import applications.App;
import applications.AppPanel;

public class TictactoePanel extends AppPanel {
	
	private static final long serialVersionUID = -8901130947534299554L;

	TictactoeBoardPanel board;
	
	JLabel controlsLabel;
	
	public TictactoePanel(App model) {
		super(model);
		
		board = new TictactoeBoardPanel(model);
		//System.out.println("Constructor: TictactoePanel.board = " + board);
	}

	@Override
	public void configure() {
		
		board = new TictactoeBoardPanel(model);
		
		removeAll();
		
		setLayout(new GridLayout(1, 3));
		
		//JLabel player1 = new JLabel("<html><center>Player 1<br>" + ((TictactoeApp)model).getPlayer(0) + "</center>");
		//JLabel player2 = new JLabel("<html><center>Player 2<br>" + ((TictactoeApp)model).getPlayer(1) + "</center>");
		
		PlayerBadge player1 = new PlayerBadge(board, ((TictactoeApp)model).getPlayer(0));
		PlayerBadge player2 = new PlayerBadge(board, ((TictactoeApp)model).getPlayer(1));
		
		add(player1);
		
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		//System.out.println("configure(): TictactoePanel.board = " + board);
		center.add(board, BorderLayout.CENTER);
		
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(1, 2));
		controlsLabel = new JLabel("");
		controls.add(controlsLabel);
		controls.add(new JButton(new AbstractAction("New Game") {
			private static final long serialVersionUID = 3332321548296923111L;

			public void actionPerformed(ActionEvent e) {
		        //System.out.println("Button pressed.");
		        ((TictactoeApp)model).startNewGame();
		        repaint();
		    }
		}));
		center.add(controls, BorderLayout.PAGE_END);
		
		add(center);
		
		add(player2);
		
		revalidate();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(int i=0; i<this.getComponentCount(); i++) {
			this.getComponent(i).repaint();
		}
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(board.pieceStrokeSize()));
		board.drawCross(g2, 20, 20);
	}

	@Override
	public void updatePanel(Object arg) {
		updateControlsLabel();
		this.repaint();
	}
	
	public void updateControlsLabel() {
		//System.out.println("Update controls panel label: " + ((TictactoeApp)model).getControlsMessage());
		if(controlsLabel != null) controlsLabel.setText(((TictactoeApp)model).getControlsMessage());
	}

}
