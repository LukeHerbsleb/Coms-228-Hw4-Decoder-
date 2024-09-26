package messageDecoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Decoder {
	
	
	

	public static void main(String[] args) throws FileNotFoundException {
		//file 1: C:\\Users\\luke\\eclipse-workspace\\coms228hw4\\src\\cadbard.arch
		//File 2: C:\Users\luke\eclipse-workspace\coms228hw4\src\twocities.arch	
		//File 3: C:\Users\luke\eclipse-workspace\coms228hw4\src\monalisa.arch
		//File 4: C:\Users\luke\eclipse-workspace\coms228hw4\src\constitution.arch
		
		System.out.print("Please enter filename to decode: ");
		
		Scanner scnr = new Scanner(System.in);
		String filename = scnr.next();
		scnr.close();
		//create a tree from the file
		//first get the encoding staring from the file
		File file = new File(filename);
		Scanner scnr1 = new Scanner(file);
		
		//check to see if the file has the newline character
		scnr1.nextLine();
		scnr1.nextLine();
		
		String encodingString = "";
		String code = "";
		Scanner scnr2 = new Scanner(file);
		
		if (!scnr1.hasNextLine()) {	//checks the 3rd line to see if it exists
			//then the file does not have a new line character
			//only needs to be done for the first line
			encodingString += scnr2.nextLine();
			
			//now getting code
			code = scnr2.next();
			
		} else {
			//assume file has new line character
			
			
			encodingString += scnr2.nextLine();
			//add the newline character in here
			encodingString += '\n';
			encodingString += scnr2.nextLine();
			
			code = scnr2.next();
			//System.out.println(encodingString);		(useful for debugging)
		}
		scnr1.close();
		scnr2.close();
		
		//now both encodingString and code are set from the file
		MsgTree tree = new MsgTree(encodingString);
		String path = "";
		MsgTree.printCodes(tree, path);
		
		System.out.println("");
		System.out.println("MESSAGE:");
		tree.decode(tree, code);
		System.out.println("");
		tree.printStatistics(code, encodingString);
	}

}
