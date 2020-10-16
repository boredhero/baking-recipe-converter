
//This may be a thing, idk.
package rc.bored;

import java.util.*;

import rc.bored.ConversionHelper;
import rc.bored.FormatVerifier;

import java.io.*;

public class RecipeConverter{
public static void main(String[] args){
  //Meta
  final String name = "Baking Recipe Converter";
  final String version = "1.0.2";
  final String credits = "Noah Martino";
  final String license = "GNU GPL v3";
  
  //CSV Stuff
  boolean csvExport = false;
  String csvName = "default.csv";
  
  boolean diagSwitch = false;
  //Make sure there is one argument
  if(args.length < 1){
	System.out.println("-------------------------");
    System.out.println("This program must be ran with a single command-line argument.");
    System.out.println("-------------------------");
    System.out.println("I.E: java RecipeConverter.java inputFile.csv");
    System.out.println("-------------------------");
    System.out.println("This program excepts CSV formatted files. For formatting details, run program with the argument 'formatting'");
    System.out.println("-------------------------");
    System.exit(0);
  } // End of if statement to check if our user imputed an argument
  String[] cmdArgs = new String[args.length];
  for(int i = 0; i < args.length; i++) {
	  cmdArgs[i] = args[i];
  }
  if(args[0].equalsIgnoreCase("formatting")){
	System.out.println("-------------------------");
	System.out.println("Formatting Man-Page");
    System.out.println("-------------------------");
    System.out.println("Accepted File Type is CSV");
    System.out.println("-------------------------");
    System.out.println("The file should have five columns.");
    System.out.println("-------------------------");
    System.out.println("Column 1: Label: 'ingredients' fill with list of ingredients in lower case letters.");
    System.out.println("  -The base ingredient (almost always flour) MUST be in the first row in order for the program to work.");
    System.out.println("-------------------------");
    System.out.println("Column 2: Label: 'in-units' fill with list of units that correspond to each ingredient.");
    System.out.println("  -Accepted input units are as follows 'lb, oz, gal, qt, gram'");
    System.out.println("-------------------------");
    System.out.println("Column 3: Label: 'values' fill with recipe numbers");
    System.out.println("-------------------------");
    System.out.println("Column 4: Label: 'out-unit'");
    System.out.println("  -Accepted output units are as follows 'lb, oz, gram'. There should be only one thing in this column at the top");
	System.out.println("-------------------------");
    System.out.println("Column 5: Label: 'out-size' One decimal or integer number that is the amount of base ingredient in oz or lbs we will scale to");
	System.out.println("-------------------------");
	System.exit(0);
    //System.out.println("Column 6: Optional: 'base-unit' must match one of the ingredients, we will default to the first row after the headers if you do not include this column.");
  }
  else if(args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("version")){
    System.out.println("-------------------------");
	System.out.println(name + " v" + version);
    System.out.println("Credits: " + credits);
    System.out.println("License: " + license);
    System.out.println("-------------------------");
    System.exit(0);
  }
    BufferedReader csvReader = null;
	try {
		csvReader = new BufferedReader(new FileReader(args[0]));
	} catch (FileNotFoundException e1) {
		System.out.println("Error: The File Doesn't Exist. Stopping now.");
		System.exit(0);
	}
	System.out.println("-------------------------");
	System.out.println("CSV Reader Successfully Opened");
  String row = null;
try {
	row = csvReader.readLine();
} catch (IOException e) {
	e.printStackTrace();
}
  ArrayList<String[]> csvData = new ArrayList<String[]>();
  while(row != null){
	  String[] temp = row.split(",");
	  csvData.add(temp);
    try {
		row = csvReader.readLine();
	} catch (IOException e) {
		System.out.println("Invalid File Format. See the manpage by running with arg 'formatting'");
		System.exit(0);
	}
  }
  try {
  if(args[1].equalsIgnoreCase("diag")){
	diagSwitch = true;
    System.out.println("csvData.size(): " + csvData.size());
    for(int i = 0; i < csvData.size(); i++){
    	System.out.println("csvData.get(i).length: " + csvData.get(i).length);
    	for(int y = 0; y < csvData.get(i).length; y++) {
    		System.out.println("y: " + y + " : " + csvData.get(i)[y]);
    	}
    }
  }
  }
  catch(IndexOutOfBoundsException e) {
	  //Do nothing
  }
  
  boolean isCsvValid = FormatVerifier.checkFormat(csvData);
  if(!isCsvValid) {
	  System.out.println("-------------------------");
	  System.out.println("There were errors in your csv. Please fix them and run the program again.");
	  System.out.println("The program will now exit.");
	  System.out.println("-------------------------");
	  System.exit(0);
  }
  else {
	  System.out.println("-------------------------");
	  System.out.println("Your CSV has been Verified as having a Valid Format");
  }
  System.out.println("-------------------------");
  System.out.println("Converting your recipe...");
  System.out.println("-------------------------");
  //Create a final data structure
  ArrayList<ArrayList<String>> finalDataStructure = new ArrayList<ArrayList<String>>();
  finalDataStructure = ConversionHelper.convert(csvData, diagSwitch);
  //Print the final data structure
  //Safety Check
  if(finalDataStructure.get(0).size() == finalDataStructure.get(1).size() && finalDataStructure.get(0).size() == finalDataStructure.get(2).size()) {
	  System.out.println("-------------------------");
	  System.out.println("Your recipe for: " + args[0]);
	  System.out.println("-------------------------");
	  System.out.println("Dough Yield: " + finalDataStructure.get(3).get(0) + " " + csvData.get(1)[3]);
	  System.out.println("-------------------------");
	  for(int i = 0; i < finalDataStructure.get(0).size(); i++) {
		  System.out.println(finalDataStructure.get(0).get(i) + " " + finalDataStructure.get(1).get(i) + " " + finalDataStructure.get(2).get(i));
	  }
	  System.out.println("-------------------------");
	  Scanner kbd = new Scanner(System.in);
	  System.out.println("Would you like to export this recipe as a csv in same directory as the program? [Y/N]");
	  String testInput;
	  boolean doLoop = true;
	  do {
		  
		  testInput = kbd.next();
		  if(testInput.equalsIgnoreCase("y") || testInput.equalsIgnoreCase("n") || testInput.equalsIgnoreCase("yes") || testInput.equalsIgnoreCase("no")) {
			  if(testInput.equalsIgnoreCase("n") || testInput.equalsIgnoreCase("no")) {
				  doLoop = false;
				  System.exit(0);
			  }
			  else {
				  csvExport = true;
				  System.out.println("-------------------------");
				  System.out.println("What would you like to name your file? It will be YOUR_NAME_AS_ENTERED_HERE.csv :");
				  csvName = kbd.next();
				  System.out.println("-------------------------");
				  System.out.println("Selected filename is '" + csvName + ".csv'");
				  System.out.println("-------------------------");
				  doLoop = false;
				  //TODO: Prompt user for csv name and shit.
			  }
		  }
		  else {
			  System.out.println(testInput + " is not a valid input. Please type either yes, y, no, n");
			  System.out.println("Please try again: ");
			  doLoop = true;
		  }
		  
		  
	  }
	  while(doLoop);;
  }
  else {
	  System.out.println("-------------------------");
	  System.out.println("I'm Sorry. Something went wrong. FMFL.");
	  System.out.println("-------------------------");
  }
  
  if(csvExport) {
	  ExportHelper.export(finalDataStructure, csvName);
  }
  
}//End of Main
} //EOF