package applications.morris;

class Node {
	
	int row;
	int col;
	int square;
	
	public Node(int row, int col, int square) {
		this.row = row;
		this.col = col;
		this.square = square;
	}
	
	public static int manhattenDistance(Node startNode, Node endNode) {
		int sum = 0;
		sum += Math.abs(endNode.row - startNode.row);
		sum += Math.abs(endNode.col - startNode.col);
		sum += Math.abs(endNode.square - startNode.square);
		return sum;
	}
	
	@Override
	public boolean equals(Object o) {
		Node n = (Node) o;
		//System.out.println("Node.equals(): " + (this.row == n.row) + " && " + (this.col == n.col) + " && " + (this.square == n.square));
		return this.row == n.row && this.col == n.col && this.square == n.square;
	}

	public String toString() {
		return row + " " + col + " " + square;
	}
	
}
