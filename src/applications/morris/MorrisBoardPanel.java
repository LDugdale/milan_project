package applications.morris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

import applications.App;

public class MorrisBoardPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = -8901130947534299554L;
	
	MorrisApp model;
	
	int boardX0, boardY0, boardSize;
	int x0, y0, edgeLength, gridSize;
	int pieceSize, markerSize, highlightSize;

	public MorrisBoardPanel(App model) {
		super();
		this.model = (MorrisApp) model;
		
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
		
		g2.setStroke(new BasicStroke(pieceStrokeSize()));
        
        for(Node n : model.possibleMoveNodes()) {
        	int[] node = moveToBoard(n);
			int x = x0+node[1]*gridSize;
			int y = y0+node[0]*gridSize;
			
			drawHighlight(g2, x, y, Color.ORANGE);
        }
		
        g2.setStroke(new BasicStroke(boardStrokeSize()));
		
		g2.setColor(Color.BLACK);
		boardSize = Math.max(0, Math.min(getWidth(), getHeight()) - 20);
		boardX0 = Math.max(0, (getWidth() - boardSize)/2);
		boardY0 = Math.max(0, (getHeight() - boardSize)/2);
		
		edgeLength = 7*boardSize/8;
		x0 = Math.max(0, (getWidth() - edgeLength)/2);
		y0 = Math.max(0, (getHeight() - edgeLength)/2);
		gridSize = edgeLength/6;
		
		markerSize = gridSize/4;
		pieceSize = gridSize/2;
		highlightSize = (int)(pieceSize*1.5);
		
		//System.out.println("Square: " + edgeLength + "x" + edgeLength + ", at (" + x0 + "," + y0 + ")");
		
		int[] cornerX = new int[] {x0, x0+gridSize, x0+2*gridSize};
		int[] cornerY = new int[] {y0, y0+gridSize, y0+2*gridSize};
		int[] side = new int[] {6*gridSize, 4*gridSize, 2*gridSize};
		
		g2.drawRect(x0, y0, 6*gridSize, 6*gridSize);
		g2.drawRect(x0+gridSize, y0+gridSize, 4*gridSize, 4*gridSize);
		g2.drawRect(x0+2*gridSize, y0+2*gridSize, 2*gridSize, 2*gridSize);
		
		g2.drawLine(x0, y0, x0+2*gridSize, y0+2*gridSize);;
		g2.drawLine(x0+6*gridSize, y0+6*gridSize, x0+4*gridSize, y0+4*gridSize);;
		g2.drawLine(x0, y0+6*gridSize, x0+2*gridSize, y0+4*gridSize);;
		g2.drawLine(x0+6*gridSize, y0, x0+4*gridSize, y0+2*gridSize);;
		
		g2.drawLine(x0+3*gridSize, y0, x0+3*gridSize, y0+2*gridSize);;
		g2.drawLine(x0, y0+3*gridSize, x0+2*gridSize, y0+3*gridSize);;
		g2.drawLine(x0+3*gridSize, y0+6*gridSize, x0+3*gridSize, y0+4*gridSize);;
		g2.drawLine(x0+6*gridSize, y0+3*gridSize, x0+4*gridSize, y0+3*gridSize);;
		
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				for(int k=0; k<3; k++) {
					if(!(j==1 && k==1)) g2.fillOval(cornerX[i]+j*side[i]/2-markerSize/2, cornerY[i]+k*side[i]/2-markerSize/2, markerSize, markerSize);
				}
			}
		}

        g2.setStroke(new BasicStroke(pieceStrokeSize()));
		for(Piece p : model.getMoves()) {
			int[] move = moveToBoard(p);
			int x = x0+move[1]*gridSize;
			int y = y0+move[0]*gridSize;
			
			g2.setColor(Color.BLACK);
			
			if(p.token==model.highlighted()) {
				if(!p.inMill || model.allInMills(p.token)) {
					drawHighlight(g2, x, y, Color.RED);
				}
			}
			drawPiece(g2, x, y, p.token);
		}
	}
	
	void drawHighlight(Graphics2D g2, int x, int y, Color color) {
		g2.setColor(color);
		g2.fillOval(x-(highlightSize/2), y-(highlightSize/2), highlightSize, highlightSize);
	}
	void drawWhite(Graphics2D g2, int x, int y) {
		drawPiece(g2, x, y, Color.WHITE);
	}
	void drawBlack(Graphics2D g2, int x, int y) {
		drawPiece(g2, x, y, Color.DARK_GRAY);
	}
	void drawPiece(Graphics2D g2, int x, int y, Color color) {
		g2.setColor(color);
		g2.fillOval(x-(pieceSize/2), y-(pieceSize/2), pieceSize, pieceSize);
		g2.setColor(Color.BLACK);
		g2.drawOval(x-(pieceSize/2), y-(pieceSize/2), pieceSize, pieceSize);
	}
	public void drawPiece(Graphics2D g2, int x, int y, char token) {
		if(token == 'w') drawPiece(g2, x, y, Color.WHITE);
		else if(token == 'b') drawPiece(g2, x, y, Color.DARK_GRAY);
	}
	
	protected int pieceStrokeSize() {
		return pieceSize/7;
	}
	protected int boardStrokeSize() {
		return Math.min(8, pieceSize/10);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			
			int gridRow = 7*(e.getY()-boardY0)/boardSize;
			int gridCol = 7*(e.getX()-boardX0)/boardSize;
			
			int[] move = boardToMove(gridRow, gridCol);
			
			int row = move[0];
			int col = move[1];
			int square = move[2];
			
			int[] recovered = moveToBoard(new Node(row, col, square));

			System.out.println("Click: " + gridRow + ", " + gridCol + ". Recovered: " + recovered[0] + ", " + recovered[1] + ". Move: " + row + ", " + col + ", " + square);
			
			model.click(new Node(row, col, square));
			
			this.repaint();
		}
	}
	
	public int[] boardToMove(int gridRow, int gridCol) {
		int row=0, col=0, square=0;
		
		row = col = ((int)Math.signum(gridRow-3))+1;
		col = ((int)Math.signum(gridCol-3))+1;
		
		if(gridRow==0 || gridRow==6 || gridCol==0 || gridCol==6) square = 2;
		else if(gridRow==1 || gridRow==5 || gridCol==1 || gridCol==5) square = 1;
		else if(gridRow==2 || gridRow==4 || gridCol==2 || gridCol==4) square = 0;
		
		if(row==0 && gridRow != 2-square) row = -1;
		else if(row==1 && gridRow != 3) row = -1;
		else if(row==2 && gridRow != 6-(2-square)) row = -1;
		
		if(col==0 && gridCol != 2-square) col = -1;
		else if(col==1 && gridCol != 3) col = -1;
		else if(col==2 && gridCol != 6-(2-square)) col = -1;
		
		return new int[] {row, col, square};
	}
	
	public int[] moveToBoard(Piece p) {
		return moveToBoard(p.node);
	}
	
	public int[] moveToBoard(Node n) {
		int gridRow = 0, gridCol = 0;
		
		if(n.row==-1 || n.col==-1) return new int[] {-1, -1};
		
		if(n.row==0) gridRow = 2-n.square;
		else if(n.row==1) gridRow = 3;
		else gridRow = 6-(2-n.square);
		
		if(n.col==0) gridCol = 2-n.square;
		else if(n.col==1) gridCol = 3;
		else gridCol = 6-(2-n.square);
		
		return new int[] {gridRow, gridCol};
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
