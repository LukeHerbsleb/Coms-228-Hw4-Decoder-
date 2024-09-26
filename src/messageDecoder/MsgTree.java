package messageDecoder;

public class MsgTree {


	public char payloadChar;
	public MsgTree left;
	public MsgTree right;
	private MsgTree parent;
	private static double numberOfBytes;
	private static int numberOfCharacters;
	private int numCharactersUncompressed;

	// Constructor building the tree from a string
	public MsgTree(String encodingString) {
		MsgTree current = this;
		int index = 0;
		
		while (index < encodingString.length()) {
			
			//used for debugging
//			if (index == newLineIndex) {
//				System.out.println("here");
//			}
			
			
			if (encodingString.charAt(index) == '^') {
				//make a new node (branch)
				// first part will always be ^ or the message could only decode one character
				if (index == 0) {
					//root node is already made return to the top of the loop
					index++;
				//	continue;
				} else if (current.left == null) {	//then create a left node
					current.left = new MsgTree(current);
					current = current.left;
					index++;
				} else if (current.right == null) {	//create a right branch
					current.right = new MsgTree(current);
					current = current.right;
					index++;
				} else {
					//go up until a node without a right is found
					while (current.right != null) {
						current = current.parent;
					}
				}
			} else {
				//make a new node (leaf)
				if (current.left == null) {
					current.left = new MsgTree(encodingString.charAt(index));
					current.left.parent = current;
					index++;
				} else if (current.right == null){
					current.right = new MsgTree(encodingString.charAt(index));
					current.right.parent = current;
					index++;
				} else {
					while (current.right != null) {
						current = current.parent;
					}
				}
			}
		}
	}

	// Constructor for a single node with null children
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
	}
	
	private MsgTree(MsgTree parent) {	//for creating a leaf
		this.parent = parent;
	}

	// method to print characters and their binary codes
	public static void printCodes(MsgTree root, String code) {
		
		//recursive call to left side of root
		if (root.parent == null) {	//runs for the very first call and prints out the setup
			System.out.println("character code\r\n"
					+ "-------------------------");
		}
		if (root.left != null) {
			printCodes(root.left, code + '0'); //print the left side of the array and add 0 to the current path
		} else {
			if (root.payloadChar == '\n') {
				System.out.println("(newline)" + " " + code);
				numberOfBytes += code.length();
				numberOfCharacters++;
			} else {
				System.out.println("   " + root.payloadChar + "      " + code);
				numberOfBytes += code.length();
				numberOfCharacters++;
			}
		}
		if (root.right != null) {
			printCodes(root.right, code + '1'); //print the right side of the array and add 0 to the current path
		}
		
		
	}

	
	public void decode(MsgTree codes, String msg) {
		String returnString = "";	
		MsgTree current = codes;
		int depth = 0;
		while (!msg.isEmpty()) {
			if ((int) current.payloadChar == 0) {//then the current char is null
				if (msg.charAt(depth) == '0') {
					//go left
					current = current.left;
					depth++;
				} else {
					//go right
					current = current.right;
					depth++;
				}
			} else {	//at a leaf node
				returnString += current.payloadChar;
				msg = msg.substring(depth);
				depth = 0;
				current = codes;
				numCharactersUncompressed++;
			}
		}
		
		System.out.println(returnString);
	}
	
	public void printStatistics(String msg, String Codes) {
		double bitsPerChar = 0;
		double percentSavings = 0;
		
		//getting bits per char
		//number of bits + chars was saved during printCodes
		bitsPerChar = numberOfBytes / numberOfCharacters;
		
		//calculating space savings
		double compressedSize = bitsPerChar * numberOfCharacters;
		double uncompressedSize = 16 * numberOfCharacters;
		percentSavings = (1 - (compressedSize / uncompressedSize)) * 100;
		
		//printing out information
		System.out.println("STATISTICS:");
		System.out.print("Avg bits/char:            ");
		System.out.println(bitsPerChar);
		System.out.print("Total Characters:         ");
		System.out.println(numCharactersUncompressed);
		System.out.print("Space savings:            ");
		System.out.println(percentSavings + "%");
		
		
		
	}

}
