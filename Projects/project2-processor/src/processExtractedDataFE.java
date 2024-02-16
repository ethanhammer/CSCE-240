
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class processExtractedDataFE {

    //Desired company's 10K
    static File currentFile;

    //Current whole line request
    static String userInput;

    //current found user inquiry
    static String request;

    //Possible commands and their misspellings, associates the possible commands to items and parts in the text
    static final String[][] validKeys = {
        {"part (?:1|i)(?: |$)", "Part I"},
        {"(?:risk factors|risk|item (?:1|i)a(?: |$))", "Item 1A. Risk Factors"},
        {"(?:staff comment|item (?:1|i)b(?: |$))", "Item 1B. Unresolved Staff Comments"},
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
        {"(?:Changes|Disagreements with accountants|Financial Disclosure|item (?:9|IX)(?: |$))", "Item 9. Changes in and Disagreements with Accountants on Accounting and Financial Disclosure"},
        {"(?:controls|Procedures|item (?:9|IX)A(?: |$))", "Item 9A. Controls and Procedures"},
        {"(?:Other|item (?:9|IX)B(?: |$))", "Item 9B. Other Information"},
        {"(?:foreign jurisdictions|prevent inspections|item (?:9|IX)C(?: |$))", "Item 9C. Disclosure Regarding Foreign Jurisdictions that Prevent Inspections"},
        {"part (?:3|iii)(?: |$)", "Part III"},
        {"(?:Directors|Executive officers|Corporate|Governance|item (?:10|X)(?: |$))", "Item 10. Directors, Executive Officers and Corporate Governance"},
        {"(?:compensation|earn|item (?:11|XI)(?: |$))", "Item 11. Executive Compensation"},
        {"(?:Security Ownership|beneficial owners|item (?:12|XII)(?: |$))", "Item 12. Security Ownership of Certain Beneficial Owners and Management and Related Stockholder Matters"},
        {"(?:relationsips|transactions|director independence|item (?:13|XIII)(?: |$))", "Item 13. Certain Relationships and Related Transactions, and Director Independence"},
        {"(?:principal Acounting|Services|item (?:14|XIV)(?: |$))", "Item 14. Principal Accounting Fees and Services"},
        {"part (?:4|IV)(?: |$)", "Part IV"},
        {"(?:Exhibits|financial statement schedules|item (?:15|XV)(?: |$))", "Item 15. Exhibits and Financial Statement Schedules"},
        {"(?:Summary|item (?:16|XVI)(?: |$))", "Item 16. Form 10-K Summary"},
        {"all information(?: |$)", "all information"}
    };

    //returns true if the file can be found based on the given input
    static boolean createFile() {

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
                currentFile = new File("../data/Wendys.txt");
                return true;
            } else if (matcher2.find()) {
                currentFile = new File("../data/restBrands.txt");
                return true;
            } else {
            	//in case no name is recognized
                System.out.println("Please enter a company name atleast");
                return false;
            }

        } catch (Exception e) {
            System.out.println("There was a problem loading the file ");
            return false;
        }
    }

    //returns true if the command can be found based on a valid set of keys. Has the scanner object for reading input
    static boolean getCommand(Scanner scan) {

        //list of all recognized keys from the user input
        ArrayList < String > recognizedKeys = new ArrayList < > ();
        
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

            boolean on = true;

            while (on) {
            	
            	//asks user for a more specific question and prints found keys
                System.out.println("what were you asking about more specifically? (enter number)");
                for (int i = 0; i < recognizedKeys.size(); i++) {
                    System.out.println((i + 1) + ") " + recognizedKeys.get(i));
                }

                try {
                	//gets user input
                	String sInput = scan.nextLine();
                    int intInput = Integer.parseInt(sInput);
                    //checks if the users input is within the lengths of the recognized keys
                    if (intInput <= recognizedKeys.size() && intInput > 0) {
                        request = recognizedKeys.get(intInput - 1);
                        return true;
                    }
                } catch (Exception e) {
                	//loops if the user does not have a valid input
                    System.out.println("Not acceptable");
                }

            }
          //if there is just one recognized key, then it automatically sets the request to the one found.
        } else if (recognizedKeys.size() == 1) {
            request = recognizedKeys.get(0);
            return true;
        }
        //returns false if no keys are found.
        return false;
    }
    
    //controls bot loop and file writer/scanner
    public static void startBot() {
    	
    	//controls bot loop
        boolean on = true;
        
        FileWriter myWriter;
        Scanner scan = new Scanner(System.in);

        //loops chat bot
        while (on) {

            System.out.println("What do you wish to search? (enter 1 to close)");

            try {
                //first actual user input
                userInput = scan.nextLine();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("There was a problem with the input");
                continue;
            }
            
            //closes chat bot
            if (userInput.equals("1")) {
                on = false;
                System.out.println("Have a good day!");
                break;
            }
            
            //attempts to create the file based on given user input. 
            if (createFile()) {
                System.out.println("File found!");
            } else {
                continue;
            }

            //attempts to create the request from the give user input
            if (getCommand(scan)) {
                System.out.println("I understand, here is info on " + request + ": \n");
            } else {
                System.out.println("I do not understand");
                continue;
            }

            //creates the data object that will search for the filtered request from the desired 10k file
            data searchedData = new data(currentFile, request);
            String output = searchedData.getOutput();

            //splits the data on lines based on sentences for readability and prints
            String splitOutput[] = output.split("\n");
            
            //attempts to print the output to the console and the output file. It splits the lines for filewriter to print properly.
            try {
                myWriter = new FileWriter("../data/output.txt");
                myWriter.write("File found!" + "\n");
                myWriter.write("I understand, here is info on " + request + ": \n");
                for (String part: splitOutput) {
                    myWriter.write(part + "\n");
                    System.out.println(part);
                }
                //closes writer object
                myWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Could not open output file");
            }

        }
        //closes scanner object
        scan.close();
    }

    //main method
    public static void main(String[] args) {
    	startBot();
    }
  }