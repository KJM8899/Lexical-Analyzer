import java.io.*;
import java.util.Scanner;

public class Lexical_analyzer {
	enum State {START, DIGIT, LETTER, SYMBOL2};
	private static State state = State.START;
	
	// Only 4 states, but muiltiple functions for each state
	
    private static String buffer = "";
	private static char ch;
    private static boolean chIsDigit = false;
//    private static String chOpDesc = "";  // string to save operator description
    private static int chOpIndex = 0;  // this is index within op1 or op2
    
    private static String[] keywords = {"if", "while", "print"};

    private static char[] op1 = {';', '(', ')', '{', '}', '/', '*', '-', '+'};
    private static String[] op1Desc = {"semicolon", "open parenthesis", "close parenthesis", "open braces", "close braces", 
    						"division operator", "muliplication operator", "subtraction operator", "addition operator"};

    private static char[] op2 = {'=', '<', '>', '!'};
    // op2Desc1 are the descriptions if characther after the op2 is NOT =
    private static String[] op2Desc1 = {"assignment", "comparison (<)", "comparison (>)", "invalid (!)"};
    // op2Desc2 are the descriptions if characther after the op2 IS =
    private static String[] op2Desc2 = {"comparison (==)", "comparison (<=)", "comparison (>=)", "comparison (!=)"};

//    private static int numInvalidChars = 0;  // if get file name from user, reset this before process file
//    private static int numInvalidIdentifiers = 0;  // reset to 0 if user can process multiple files
    
    public static boolean charInOp1 (char c) // bool method that takes in a character and sees 
    										 //if it matches any of the characters in op1.
    {
    	boolean found = false; //decalare and initialize a  bool varialbe called found as false

    	for (int i=0; (i < op1.length) && (!found); i++) //run for lenght of op1 array or until char c matches a char in array op1
    	{
			if (c == op1 [i]) //if character c == char in array op1 at index i
			{
				found = true; //change bool found to true.
				chOpIndex = i; //sets global variable chOpIndex to index value of i from array op1
//				chOpDesc = op1Desc [i];  // calling code will print this
			}
		}
    	return (found);// returns boolean value to method
    } 
    
    public static boolean charInOp2 (char c)
    {
    	boolean found = false;

    	for (int i=0; (i < op2.length) && (!found); i++) {
			if (c == op2 [i]) {
				found = true;
				chOpIndex = i;
//				chOpDesc = op2Desc1 [i];  // if next character is not =, we already have desc ready to print
			}
		}
    	return (found);
    } 

    public static void processBuffer () {
    	boolean found = false;

    	// if state is START, nothing to do
    	// otherwise, if state is LETTER, we have a keyword or identifier
    	// if state is DIGIT, we have a integer literal
    	// if state is SYMBOL2, we either have ch is =, or it is not =
//    	if( buffer != "")
//    	{
   		if (state == State.DIGIT)
   		{
			System.out.println ("literal integer (" + buffer + ")");
   		}
   		else if (state == State.LETTER) {
//   			System.out.println ("Debug buffer is " + buffer + "|");
   			for (int i=0; (i < keywords.length) && (!found); i++) {
//System.out.println ("Debug i is " + i + ", keywords [i] is " + keywords [i]);
   				if (buffer.equals(keywords [i])) // if the string in buffer equals string in keywords array at index i 
   				{
   					found = true; // breaks out of the for loop
   					System.out.println("keyword " + buffer);
   				}
   			}

   			if (!found) {
				System.out.println("identifier (" + buffer + ")");
   			}
   		}
   		else if (state == State.SYMBOL2) //if buffer equals < | > | = | ! 
   		{
   			if (ch != '=') //if next character after < | > | = | ! , is not =
   			{  // 1-character symbol, op2 char array has 4 elements, <, >, =, !
				System.out.println(op2Desc1 [chOpIndex]);  // desc from 1-character symbol array
   			}
   			else
   			{  // ch is = sign, 2-character symbol. look for buffer match in op2
				System.out.println(op2Desc2 [chOpIndex]);  // desc from 2-character symbol array
				ch = ' '; // reset ch to blank, why is this only done in the else case?
   			}
    	}
    		
    	state = State.START; // reset state to START
    	buffer = ""; // clear buffer
	}
    
   
    public static void processChar () //Method that sets what the state is among other things. 
    {
    	if (chIsDigit) // This variable is initalized to false, 
    	{
    		buffer = buffer + ch;
    		state = State.DIGIT;
    	}
    	else if (Character.isLetter(ch))
    	{
    		buffer = buffer + ch;
    		state = State.LETTER;
    	}
    	else if (charInOp1 (ch)) {  // this function returns true if ch is in op1, and sets chOpDesc
    		processBuffer ();  // resets buffer and sets state back to START
    		System.out.println(op1Desc [chOpIndex]);
    	}
    	else if (charInOp2 (ch)) {
    		processBuffer ();
    		buffer = buffer + ch;  // buffer is blank string, add ch, now buffer has char from op2
    		state = State.SYMBOL2;
    	}
    	else {
    		// in start state and rcd char that is not digit, letter, S1 or S2 (might be space or invalid)
    		processBuffer ();  
    	}
    }

	public static void main(String[] args) {
		//Scanner sc = new Scanner(System.in);
		
	    String fileName = "Lex.txt"; // declare new string called fileName and initialize it as string from file Lex.txt
	    
        // This will reference one line at a time
        String line = null;

        try {
           
            FileReader fileReader = new FileReader(fileName);//?

         
            BufferedReader bufferedReader = new BufferedReader(fileReader);//?

            while((line = bufferedReader.readLine()) != null)// loop ends when after last line read from Lex.txt
            {

            	for(int i =0; i < line.length(); i++)
            	{
            		ch = line.charAt(i);
        			chIsDigit = Character.isDigit(ch);
            		
            		if (state == State.START) {
            			processChar ();
            		}
            		
            		else if (state == State.DIGIT) {
            			if (chIsDigit) {
            				buffer = buffer + ch;
            			}
            			else if (Character.isLetter(ch)) {
            				processBuffer ();
            				buffer = buffer + ch;
            				state = State.LETTER;
            			}
            			else if (charInOp1 (ch)) {  // this function returns true if ch is in op1, and sets chOpDesc
            				processBuffer ();  // resets buffer and sets state back to START
        					System.out.println(op1Desc [chOpIndex]);
            			}
            			else if (charInOp2 (ch)) {  // charInOp2 sets chOpDesc, string to print if 1-char 
            				processBuffer ();
            				buffer = buffer + ch;  // buffer is blank string, add ch, now buffer has char from op2
            				state = State.SYMBOL2;
            			}
            			else {
            				// in start state and rcd char that is not digit, letter, S1 or S2 (might be space or invalid)
            				processBuffer ();  
            			}
            		}
            		
            		else if (state == State.LETTER) {
            			if (chIsDigit || Character.isLetter(ch)) {
            				buffer = buffer + ch;
            			}
            			else if (charInOp1 (ch)) {  // this function returns true if ch is in op1, and sets chOpDesc
            				processBuffer ();  // resets buffer and sets state back to START
        					System.out.println(op1Desc [chOpIndex]);
            			}
            			else if (charInOp2 (ch)) {
            				processBuffer ();
            				buffer = buffer + ch;  // buffer is blank string, add ch, now buffer has char from op2
            				state = State.SYMBOL2;
            			}
            			else {
            				processBuffer ();
            			}
            		}
            		else if (state == State.SYMBOL2) 
            		{
            			processBuffer ();
            			processChar ();    
            		}
            		else 
            		{
            			// in start state and rcd char that is not digit, letter, S1 or S2 (might be space or invalid)
        				processBuffer ();  
        			}
        		}  // for
            } // while			
            			
            
            processBuffer ();

            bufferedReader.close(); 
        }
        
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    }
}
