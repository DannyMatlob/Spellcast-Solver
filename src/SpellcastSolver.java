package solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;


public class SpellcastSolver{
	
	public final int minimum = 5;
	public final int maximum = 10;
	public SpellcastGrid grid;
	public SpellcastSolver(SpellcastGrid grid) {
		this.grid = grid;
	}
	
	//
	//Recursive Word Search
	//
	public HashSet<String> combos = new HashSet<String>();
	public int recurses = 0;
	
	public void findWords(Node n, HashSet<Node> history, String current, int min, int max) {
		findWordsRecurse(n, history, current, min, max);
	}
	public void findWordsRecurse(Node n, HashSet<Node> history, String current, int min, int max) {
		recurses++;
		history.add(n);
		current+=n.data;
		
		if (current.length()>max) return;
		if (current.length()>=min) combos.add(current);
		
		for (Node i : n.connections) {
			if (i!=null && !history.contains(i)) findWordsRecurse(i,new HashSet<Node>(history),current,min,max);
		}
	}
	
	//
	//Word Verification
	//
	public HashSet<String> createDictionary () {
		HashSet<String> words = new HashSet<>();
		
		File dictionary = new File("src/data", "dictionary.txt");
		
		FileReader fr = null;  //Individual Characters
		try {
			fr = new FileReader(dictionary);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		BufferedReader br = new BufferedReader(fr); //Individual Words
		
		String currentWord = "";
		while (currentWord!=null) {
			try {	
				currentWord=br.readLine();
				words.add(currentWord);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return words;
	}
	
	public TreeSet<String> matchWords (HashSet<String> dictionary) {
		TreeSet<String> correctWords = new TreeSet<>(new lengthThenLexicoComparator());
		for (String s : combos) {
			if (dictionary.contains(s)) correctWords.add(s);
		}
		return correctWords;
	}
	
	/* word verify method
	public boolean verifyWord (String word) {
		boolean isWord = false;
		TreeSet<Node> startLetters = grid.findNodes(word.charAt(0));
		for (Node n : startLetters) {
			isWord = isWord || isWordRecurse(word,"",n,0, new HashSet<Node>());
		}
		return isWord;
	}
	
	public boolean isWordRecurse(String word, String current, Node n, int index, HashSet<Node> history) {
		if (current.equals(word)) return true;
		
		for (Node connection : n.connections) {
			if (connection!=null && connection.data==word.charAt(index)) {
				history.add(connection);
				current+=connection.data;
				return isWordRecurse(word, current, connection, index+1, history);
			}
		}
		return false;
	}
	*/
	//
	//
	//
	//              MAIN
	//
	//
	//
	public static void main(String[] args) {
		/////////////////////////
		//ENTER THE GRID HERE VV
		/////////////////////////
		String gridString = "gidaunsxwieilipsueeertara";
		/////////////////////////
		//ENTER THE GRID HERE ^^
		/////////////////////////
		
		char[][] charGrid = new char[5][5];
		int j = -1;
		for (int i = 0; i<25; i++) {
			if (i%5==0) j++;
			charGrid[j][i%5]=gridString.charAt(i);
		}
		System.out.println(gridString.length());
		SpellcastGrid grid = new SpellcastGrid(charGrid);
		/*SpellcastGrid grid = new SpellcastGrid(new char[][] 
				{
					{'l','l','e','e','o'},
					{'v','i','a','z','i'},
					{'e','t','v','o','a'},
					{'s','u','u','f','n'},
					{'g','o','o','i','n'}
				});
		*/
		System.out.println(grid.toString());
		SpellcastSolver solver = new SpellcastSolver(grid);
		//Slow brute force solution
		for (Node[] n1 : grid.getNodeGrid()) {
			for (Node n : n1) {				
				//ALTER PARAMETERS TO FIND WORDS HERE
				//								  		    min max  word length
				solver.findWords(n, new HashSet<Node>(), "", 3, 6);
			}
		}
		
		System.out.println("Recurses: " + solver.recurses);
		HashSet<String> dictionary = solver.createDictionary();
		TreeSet<String> finalWords = solver.matchWords(dictionary);
		System.out.println("Total words found: " + finalWords.size());
		System.out.println(finalWords);
	}
}
