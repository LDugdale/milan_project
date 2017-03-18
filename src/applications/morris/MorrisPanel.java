package applications.morris;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import applications.App;
import applications.AppPanel;

public class MorrisPanel extends AppPanel {
	
	private static final long serialVersionUID = 3760507708847192708L;
	
	MorrisBoardPanel board;
	
	JLabel controlsLabel;
	JLabel bannerLabel;
	
	PlayerBadge player1;
	PlayerBadge player2;

	public MorrisPanel(App model) {
		super(model);
		
		board = new MorrisBoardPanel(model);
	}

	@Override
	public void configure() {
		
		board = new MorrisBoardPanel(model);
		
		removeAll();
		
		setLayout(new GridLayout(1, 3));
		
		//JLabel player1 = new JLabel("<html><center>Player 1<br>" + ((TictactoeApp)model).getPlayer(0) + "</center>");
		//JLabel player2 = new JLabel("<html><center>Player 2<br>" + ((TictactoeApp)model).getPlayer(1) + "</center>");
		
		player1 = new PlayerBadge((MorrisApp)model, board, 0);
		player2 = new PlayerBadge((MorrisApp)model, board, 1);
		
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
		        ((MorrisApp)model).startNewGame();
		        repaint();
		    }
		}));
		center.add(controls, BorderLayout.PAGE_END);
		
		JPanel banner = new JPanel();
		banner.setLayout(new GridLayout(1,1));
		bannerLabel = new JLabel();
		banner.add(bannerLabel);
		
		center.add(banner, BorderLayout.PAGE_START);
		
		add(center);
		
		add(player2);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void updatePanel(Object arg) {
		updateLabels();
		this.repaint();
		if(player1 != null) player1.repaint();
		if(player2 != null) player2.repaint();
	}
	
	public void updateLabels() {
		//System.out.println("Update controls panel label: " + ((MorrisApp)model).getControlsMessage());
		if(controlsLabel != null) controlsLabel.setText(((MorrisApp)model).getControlsMessage());
		if(bannerLabel != null) bannerLabel.setText(((MorrisApp)model).getBannerMessage());
	}

}
