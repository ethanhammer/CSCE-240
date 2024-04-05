import java.util.Scanner;

public class statFE {

    static Scanner scan = new Scanner(System.in);

    public static void main(String args[]) {
    	
    	//start the back end obect
         statBE backEnd = new statBE();
        
        //stores user requests
        String input;
        int num = 0;
        
        //runs until closed
        while (true) {
        	
            System.out.println("type command");
            input = scan.nextLine();
            
            //split input based on spaces
            String[] splitInput = input.toLowerCase().split(" ");
            
            //cases to decide what to output based on first index of splitInput
            if (splitInput[0].equals("-summary")) {
                statBE.doSummary();
                continue;
            }
            
            //end program
            if (splitInput[0].equals("-quit")) {
                break;
            }
            
            //past this point, the array needs two values to proceed
            if (splitInput.length < 2) {
                System.out.println("Invalid input");
                continue;
            }
            
            //Past this point, it also needs a valid numberin the second spot, which this checks for
            if (!getNumber(splitInput[1], num)) {
                System.out.println("Invalid input");
                continue;
            } else {
            	//assign digit
                num = Integer.parseInt(splitInput[1]);
            }
            
            if (splitInput[0].equals("-showchat-summary")) {
                statBE.doShowChatSummary(num);
                continue;
            }
            if (splitInput[0].equals("-showchat")) {
                statBE.doShowChat(num);
                continue;
            } else {
            	//final end case if nothing is recognized
                System.out.println("I do not understand");
            }


        }
    }
    
    //Makes sure the second user input is a valid number
    public static boolean getNumber(String input, int num) {
        try {
            num = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}