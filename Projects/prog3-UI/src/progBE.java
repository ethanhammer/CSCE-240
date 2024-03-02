import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class progBE {

	//current 10k file
    private  File currentFile;

    //current filtered request
    private  String request;
    
    //Current whole line request
    private  String userInput;

    //Overall logic to check a new input. Will return false if the input is not valid and the program will stop it's process.
    public boolean checkInput(String newInput) {
    	
    	//assigns class variable to new verification request
    	this.userInput = newInput;
    	
    	//ensures the new input is not just empty
    	if (userInput.trim().equals("")) {
        	return false;
        }
        
        //checks if user wants to reset screen
        if (checkClear()) {
        	return false;
        }
        
        //checks for trivial input
        if (checkForChitChat()) {
        	progFE.print(true, "Enter either wendys or food brand international and a question to proceed");
        	return false;
        } 
        
        //attempts to create the file based on given user input. 
        if (createFile()) {
            progFE.print(true, "File found!");
        } else {
            return false;
        }

        //attempts to create the request from the give user input by comparing different keys
        if (getCommand()) {
        	progFE.print(true, "I understand, here is info on " + request + ": \n");
        } else {
        	progFE.print(true, "Please try again");
            return false;
        }
        
        return true;
        
    }
    
    // Will search for desired section of the text via the creation of a data object and print it. This is called by the front end.
    public void searchAndPrint() {
    	//creates the data object that will search for the filtered request from the desired 10k file
        data searchedData = new data(currentFile, request);
        //Separate print method.
        printFoundOutput(searchedData.getOutput());
    }
    
    //prints data found from data object to the GUI and file
    private void printFoundOutput(String output) {
    	
    	//output file writer object
        FileWriter myWriter;
        
    	  //splits the data on lines based on sentences for readability and prints
        String splitOutput[] = output.split("\n");

        //attempts to print the output to the console and the output file. It splits the lines for filewriter to print properly.
        try {
            myWriter = new FileWriter("data/output.txt");
            myWriter.write("File found!" + "\n");
            myWriter.write("I understand, here is info on " + request + ": \n");
            for (String part: splitOutput) {
                myWriter.write(part + "\n");
                progFE.print(true, part);
            }
            progFE.print(true, "What do you wish to search?");
            //closes writer object
            myWriter.close();
        } catch (IOException e1) {
            System.out.println("Could not open output file");
        }
    }
    
    //checks if the input requests to clear the screen
    private  boolean checkClear() {
    	if (userInput.equals("-clear")) {
    		//resets to default message
    		progFE.output.setText("bot->What do you wish to search? Do -clear to clear the screen\n");
    		return true;
    	}
    	return false;
    }
    
    //method checks for non-query related input. returns true if it is found. Returns true if the input does contain chit-chat.
    private  boolean checkForChitChat() {
    	//regex patterns for some hello statements
    	 Pattern pattern = Pattern.compile("(?:hel*o|hol*a)", Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(userInput);
         Pattern pattern2 = Pattern.compile("(?:how are you|are you go*d)", Pattern.CASE_INSENSITIVE);
         Matcher matcher2 = pattern2.matcher(userInput);
         
         //checks if input contains welcome statements
         if (matcher.find()) {
        	 progFE.print(true, "Hello!");
        	 return true;
         }
         if (matcher2.find()) {
        	 progFE.print(true, "I'm good, thanks!");
        	 return true;
         }
    	return false;
    }
    
    //returns true if the file can be found based on the given input
    private  boolean createFile() {

        //checks for possible, but obvious spellings and misspellings of the company names
        String regex = "w...y";
        String regex2 = "(?:restaurant|brand|international)";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(userInput);
        Matcher matcher2 = pattern2.matcher(userInput);

        try {
            //Checks to see which file name the input is close to and creates the file based on the 10k file location
            if (matcher.find()) {
                currentFile = new File("data/Wendys.txt");
                return true;
            } else if (matcher2.find()) {
                currentFile = new File("data/restBrands.txt");
                return true;
            } else {
                //in case no name is recognized
                progFE.print(true, "Please enter a company name atleast");
                return false;
            }

        } catch (Exception e) {
            progFE.print(true, "There was a problem loading the file ");
            return false;
        }
    }

    //returns true if the command can be found based on a valid set of keys. Has the scanner object for reading input.
    private boolean getCommand() {

        //list of all recognized keys from the user input
        ArrayList <String> recognizedKeys = new ArrayList < > ();

        //iterates through each possible key
        for (int i = 0; i < validKeys.length; i++) {

            Pattern pattern = Pattern.compile(validKeys[i][0], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(userInput);

            if (matcher.find()) {
                //if a match is found, it is added to the list of found keys
                recognizedKeys.add(validKeys[i][1]);
            }
        }

        //asks the user what they meant if there is more than one recognized key
        if (recognizedKeys.size() > 1) {
                //asks user for a more specific question and prints found keys        	
                progFE.print(true, "what were you asking about more specifically?");
                for (int i = 0; i < recognizedKeys.size(); i++) {
                	progFE.print(true, recognizedKeys.get(i));
                }
                return false;
            //if there is just one recognized key, then it automatically sets the request to the one found.
        } else if (recognizedKeys.size() == 1) {
            request = recognizedKeys.get(0);
            return true;
        }
        //returns false if no keys are found.
        progFE.print(true, "I do not understand");
        return false;
    }
    
    
    //Possible commands and their misspellings, associates the possible commands to items and parts in the text
    private static final String[][] validKeys = {
            {"part (?:1|i)(?: |$)", "Part I"},
            {"(?:risk fac*ors|risk|item (?:1|i)a(?: |$))", "Item 1A. Risk Factors"},
            {"(?:staff com*ent|item (?:1|i)b(?: |$))", "Item 1B. Unresolved Staff Comments"},
            {"(?:busin|item (?:1|i)(?: |$))", "Item 1. Business"},
            {"(?:Propert(?:y|ies)|item (?:2|II)(?: |$))", "Item 2. Properties"},
            {"(?:legal|court|item (?:3|III)(?: |$))", "Item 3. Legal Proceedings"},
            {"(?:safety|mine|item (?:4|IV)(?: |$))", "Item 4. Mine Safety Disclosures"},
            {"part (?:2|ii)(?: |$)", "Part II"},
            {"(?:registrant|equity|item (?:5|V)(?: |$))", "Item 5. Market for Registrant’s Common Equity, Related Stockholder Matters and Issuer Purchases of Equity Securities"},
            {"(?:reserved|item (?:6|VI)(?: |$))", "Item 6. Reserved"},
            {"(?:Discussion|Financial Conditions|Results of Operations|item (?:7|VII)(?: |$))", "Item 7. Management’s Discussion and Analysis of Financial Condition and Results of Operations"},
            {"(?:Market Risk|quantitative|qualitative|item (?:7|VII)A(?: |$))", "Item 7A. Quantitative and Qualitative Disclosures About Market Risk"},
            {"(?:Financial statements|Supplementary|Data|item (?:8|VIII)(?: |$))", "Item 8. Financial Statements and Supplementary Data"},
            {"(?:Changes|Disagre*ments with ac*ountants|Financial Disclosure|item (?:9|IX)(?: |$))", "Item 9. Changes in and Disagreements with Accountants on Accounting and Financial Disclosure"},
            {"(?:controls|Procedures|item (?:9|IX)A(?: |$))", "Item 9A. Controls and Procedures"},
            {"(?:Other|item (?:9|IX)B(?: |$))", "Item 9B. Other Information"},
            {"(?:foreign jurisdictions|prevent inspections|item (?:9|IX)C(?: |$))", "Item 9C. Disclosure Regarding Foreign Jurisdictions that Prevent Inspections"},
            {"part (?:3|iii)(?: |$)", "Part III"},
            {"(?:Directors|Executive officers|Corporate|Governance|item (?:10|X)(?: |$))", "Item 10. Directors, Executive Officers and Corporate Governance"},
            {"(?:compensation|earn|item (?:11|XI)(?: |$))", "Item 11. Executive Compensation"},
            {"(?:Security Ownership|beneficial owners|item (?:12|XII)(?: |$))", "Item 12. Security Ownership of Certain Beneficial Owners and Management and Related Stockholder Matters"},
            {"(?:relationsips|transactions|director independence|item (?:13|XIII)(?: |$))", "Item 13. Certain Relationships and Related Transactions, and Director Independence"},
            {"(?:principal Accounting|Services|item (?:14|XIV)(?: |$))", "Item 14. Principal Accounting Fees and Services"},
            {"part (?:4|IV)(?: |$)", "Part IV"},
            {"(?:Exhibits|financial statement schedules|item (?:15|XV)(?: |$))", "Item 15. Exhibits and Financial Statement Schedules"},
            {"(?:Summary|item (?:16|XVI)(?: |$))", "Item 16. Form 10-K Summary"},
            {"(?:all information|everything)(?: |$)", "all information"}
        };
}