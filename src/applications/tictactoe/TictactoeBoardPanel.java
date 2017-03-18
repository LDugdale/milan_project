package applications.tictactoe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import applications.App;

public class TictactoeBoardPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = -8901130947534299554L;
	
	TictactoeApp model;
	
	int x0, y0, edgeLength;
	int pieceSize;

	public TictactoeBoardPanel(App model) {
		super();
		this.model = (TictactoeApp) model;
		
		//setPreferredSize(new Dimension(200,200));
		
		addMouseListener(this);
	}
	
	int toPixelsX(float x) {
		return (int) (x*this.getWidth());
	}
	int toPixelsY(float y) {
		return (int) (y*this.getHeight());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(boardStrokeSize()));
		
		g2.setColor(Color.BLACK);
		edgeLength = Math.max(0, Math.min(getWidth(), getHeight()) - 20);
		x0 = Math.max(0, (getWidth() - edgeLength)/2);
		y0 = Math.max(0, (getHeight() - edgeLength)/2);
		//System.out.println("Square: " + edgeLength + "x" + edgeLength + ", at (" + x0 + "," + y0 + ")");
		
		g2.drawRect(x0, y0, edgeLength, edgeLength);
		g2.drawLine(x0, y0+edgeLength/3, x0+edgeLength, y0+edgeLength/3);
		g2.drawLine(x0, y0+2*edgeLength/3, x0+edgeLength, y0+2*edgeLength/3);
		g2.drawLine(x0+edgeLength/3, y0, x0+edgeLength/3, y0+edgeLength);
		g2.drawLine(x0+2*edgeLength/3, y0, x0+2*edgeLength/3, y0+edgeLength);
		
		pieceSize = edgeLength/6;

        g2.setStroke(new BasicStroke(pieceStrokeSize()));
        
        synchronized (model.getMoves()) { // Is this overkill?
			for(Piece p : model.getMoves().toArray(new Piece[0])) { // toArray prevents Concurrency exception
				int x = x0+((2*p.col+1)*edgeLength)/6;
				int y = y0+((2*p.row+1)*edgeLength)/6;
				
				if(model.isGameWon()) {
					if(model.getWin(p)) g2.setColor(Color.RED);
					else g2.setColor(Color.BLACK);
				}
				
				drawToken(g2, p.token, x, y);
			}
        }
	}
	
	protected int pieceStrokeSize() {
		return pieceSize/5;
	}
	protected int boardStrokeSize() {
		return Math.min(8, pieceSize/10);
	}
	
	protected void drawToken(Graphics2D g2, char token, int x, int y) {
		if(token == 'o') drawNought(g2, x, y);
		else if(token == 'x') drawCross(g2, x, y);
	}
	
	protected void drawNought(Graphics2D g2, int x, int y) {
		g2.drawOval(x-(pieceSize/2), y-(pieceSize/2), pieceSize, pieceSize);
	}
	protected void drawCross(Graphics2D g2, int x, int y) {
		int l1x1 = x - pieceSize/2;
		int l1y1 = y - pieceSize/2;
		int l1x2 = x + pieceSize/2;
		int l1y2 = y + pieceSize/2;
		int l2x1 = x - pieceSize/2;
		int l2y1 = y + pieceSize/2;
		int l2x2 = x + pieceSize/2;
		int l2y2 = y - pieceSize/2;
		
		//g2.drawRect(x-(pieceSize/2), y-(pieceSize/2), pieceSize, pieceSize);
		g2.drawLine(l1x1, l1y1, l1x2, l1y2);
		g2.drawLine(l2x1, l2y1, l2x2, l2y2);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			//System.out.println("Column: " + 3*(e.getX()-x0)/edgeLength);
			//System.out.println("Row: " + 3*(e.getY()-y0)/edgeLength);
			
			int row = 3*(e.getY()-y0)/edgeLength;
			int col = 3*(e.getX()-x0)/edgeLength;
			
			model.moveFromGUI(row, col);
			
			this.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
	}

}
