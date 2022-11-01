package solver;

import java.io.*;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Scanner;
import java.awt.GraphicsEnvironment;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class SpellcastSolver{
	
	//Initialization
	public final int minimum = 5;
	public final int maximum = 10;
	public SpellcastGrid grid;
	public SpellcastSolver(SpellcastGrid grid) {
		this.grid = grid;
	}
	
	//Recursive Word Search
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
	
	//Word Verification
	public HashSet<String> createDictionary () {
		HashSet<String> words = new HashSet<>();
		BufferedReader br = null;
		try {//Open file from eclipse environment
			br = new BufferedReader(new FileReader(new File("data", "dictionary.txt")));
		} catch (FileNotFoundException e) {
			try {//Open file from within jar file
				InputStream in = SpellcastSolver.class.getResourceAsStream("/data/dictionary.txt");
				br = new BufferedReader(new InputStreamReader(in));
			} catch (Exception g) {
				g.printStackTrace();
			}
		}
		//Read from buffered reader
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

	//              MAIN
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		//Open terminal
		Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){ //If a console is not open, open one
            String filename = SpellcastSolver.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            filename = URLDecoder.decode(filename, "UTF-8");
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
        }else{//Run Main
        	//Handle input of spellcast grid
    		Scanner userInput = new Scanner(System.in);  // Create a Scanner object to listen for input
    		System.out.println("Enter the Spellcast Grid as a string of 25 letters, left to right, top to bottom");
    		String gridString = userInput.nextLine();
    		while (gridString.length()!=25) {
    			System.out.println("Incorrect format");
    			System.out.println("Enter the Spellcast Grid as a string of 25 letters, left to right, top to bottom");
    			gridString = userInput.nextLine();
    		}
    		
    		//Prepare to handle min and max length
    		String input = "";
    		int minLength = 5;
    		int maxLength = 9;
    		
    		int length = minLength;
    		//Handle input of minimum word length
    		System.out.println("Enter the minimum word length to search, between 1 and 10");
    		while (true) {
    			input = userInput.nextLine();
    			if (input.equals("")) break;
    			if (!input.matches("[0-9.]+")) {
    				System.out.println("Invalid input, please only input numbers between 1 and 10");
    			} else {
    				length = Integer.parseInt(input);
    				if (length < 1 || length > 10) {
    					System.out.println("Invalid input, please only input numbers between 1 and 10");
    				} else {
    					break;
    				}
    			}
    		}
    		minLength = length;
    		
    		//Handle input of maximum word length
    		length = maxLength;
    		System.out.println("Enter the maximum word length to search, between 1 and 10");
    		while (true) {
    			input = userInput.nextLine();
    			if (input.equals("")) break;
    			if (!input.matches("[0-9.]+")) {
    				System.out.println("Invalid input, please only input numbers between 1 and 10");
    			} else {
    				length = Integer.parseInt(input);
    				if (length < 1 || length > 10) {
    					System.out.println("Invalid input, please only input numbers between 1 and 10");
    				} else {
    					break;
    				}
    			}
    		}
    		maxLength = length;
    		userInput.close();
    		
    		//Print summary of inputs
    		System.out.println(gridString.length());
    		System.out.println("Minlength is: "+minLength);
    		System.out.println("Maxlength is: "+maxLength);
    		
    		//Convert string to 5x5 2D char array
    		char[][] charGrid = new char[5][5];
    		int j = -1;
    		for (int i = 0; i<25; i++) {
    			if (i%5==0) j++;
    			charGrid[j][i%5]=gridString.charAt(i);
    		}
    		
    		//Create a new SpellcastGrid object and print it
    		SpellcastGrid grid = new SpellcastGrid(charGrid);
    		System.out.println(grid.toString());
    		
    		//Create a new SpellcastSolver object with this grid
    		SpellcastSolver solver = new SpellcastSolver(grid);
    		
    		//Slow brute force solution
    		for (Node[] n1 : grid.getNodeGrid()) {//Loop through each row of nodes
    			for (Node n : n1) {//Loop through each node
    				//Starting at this node, find all possible combinations of of letters between minimum and maximum word length
    				solver.findWords(n, new HashSet<Node>(), "", minLength, maxLength);
    			}
    		}
    		
    		//Finished finding all word combinations, print recurse number
    		System.out.println("Recurses: " + solver.recurses);
    		
    		//Separate gibberish combinations from real words using dictionary
    		HashSet<String> dictionary = solver.createDictionary();
    		TreeSet<String> finalWords = solver.matchWords(dictionary);
    		
    		//Print results
    		System.out.println("Total words found: " + finalWords.size());
    		System.out.println(finalWords);
        	///////////////////////////////////////
        	
            System.out.println("Program has ended, please type 'exit' to close the console");
        }
		
	}
}
