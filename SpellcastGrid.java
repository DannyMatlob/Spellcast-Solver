package solver;

import java.util.TreeSet;

public class SpellcastGrid {
	Node[][] nodeGrid = new Node[5][5];
	Node topLeft;
	public SpellcastGrid(char[][] letters) {
		for (int i = 0; i<letters.length; i++) {
			for (int j = 0; j<letters[i].length; j++) {
				nodeGrid[i][j] = new Node(letters[i][j]);
			}
		}
		
		for (int i = 0; i<nodeGrid.length; i++) {
			for (int j = 0; j<nodeGrid.length; j++) {
				//0  1  2
				//3     4
				//5  6  7
				if (i-1>=0 && j-1>=0) nodeGrid[i][j].connect(0, nodeGrid[i-1][j-1]);
				if (i-1>=0)           nodeGrid[i][j].connect(1, nodeGrid[i-1][j]);
				if (i-1>=0 && j+1<5)  nodeGrid[i][j].connect(2, nodeGrid[i-1][j+1]);
				
				if (j-1>=0)           nodeGrid[i][j].connect(3, nodeGrid[i][j-1]);
				if (j+1<5)            nodeGrid[i][j].connect(4, nodeGrid[i][j+1]);
				
				if (i+1<5 && j-1>=0)  nodeGrid[i][j].connect(5, nodeGrid[i+1][j-1]);
				if (i+1<5)            nodeGrid[i][j].connect(6, nodeGrid[i+1][j]);
				if (i+1<5 && j+1<5)   nodeGrid[i][j].connect(7, nodeGrid[i+1][j+1]);
			}
		}
		topLeft = nodeGrid[0][0];
	}
	
	public Node[][] getNodeGrid() {
		return nodeGrid;
	}
	
	public TreeSet<Node> findNodes(char data) {
		TreeSet<Node> result = new TreeSet<Node>();
		for (Node[] nodeArray : nodeGrid) {
			for (Node n : nodeArray) {
				if (n.data == data) result.add(n);
			}
		}
		return result;
	}
	
	public Node getTopLeft () {
		return topLeft;
	}
	
	public String toString() {
		String result = "";
		for (Node[] i : nodeGrid) {
			for (Node n : i) {
				result += n.data + ",";
			}
			result += "\n";
		}
		return result;
	}
}
