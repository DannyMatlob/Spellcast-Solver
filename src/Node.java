package solver;

public class Node {
	char data = 0;
	
	//connections
	/*
	 *0   1   2
	 *3       4
	 *5   6   7
	 */
	Node[] connections = new Node[8];
	
	//Constructors
	public Node (char d, Node[] connections) {
		data = d;
		this.connections = connections;
	}
	
	public Node (char d) {
		data = d;
	}
	
	
	//Methods
	public void connect (int position, Node node) {
		connections[position] = node;
	}
	
	public void printConnections() {
		for (Node i : connections) {
			
			if (i!=null) System.out.print(i.toString() + ", ");
		}
		System.out.println();
	}
	
	public String toString() {
		String result = "";
		result+=data;
		return result;
	}
	
	public void setData (char d) {
		data = d;
	}
	
}
