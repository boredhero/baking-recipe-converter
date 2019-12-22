//This class checks if the CSV file is of valid format
package rc.bored;

import java.util.*;

public class FormatVerifier{
  public static boolean checkFormat(ArrayList<String[]> csvData){
	  //List of valid headers
	  List<String> validHeaders = Arrays.asList("ingredients", "in-units", "values", "out-unit", "out-size", "base-unit");
	  List<String> validInUnits = Arrays.asList("lb", "oz", "gal", "qt");
	  //This boolean lets us print all error messages without returning.
	  boolean isValid = true;
	  //This checks if the first line (title) has a valid number of lines.
	  if(csvData.get(0).length < 5 || csvData.get(0).length > 6) {
		  System.out.println("Formatting Error: First Row of CSV needs 5 or 6 columns, you have " + csvData.get(0).length);
		  isValid = false;
	  }
	  //This checks that the headers are right.
	  for(int i = 0; i < csvData.get(0).length; i++) {
		  if(validHeaders.get(i).equalsIgnoreCase(csvData.get(0)[i])) {
			  //Do nothing
		  }
		  else {
			  System.out.println("Formatting Error: Your Column: " + csvData.get(0)[i] + " should be: " + validHeaders.get(i));
			  isValid = false;
		  }
	  }
	  //Check to see if anything after the first two columns has more than 3 values
	  for(int i = 2; i < csvData.size(); i++) {
		  try {
			  String test = csvData.get(i)[3];
			  isValid = false;
			  System.out.println("Formatting Error: Your Row: " + i + " should only have 3 values in it.");
		  }
		  catch(IndexOutOfBoundsException e) {
			  //Do nothing
		  }
	  }
	  //Check to make sure that everything in the second column is a valid in-unit
	  for(int i = 1; i < csvData.size(); i++) {
		  if(validInUnits.contains(csvData.get(i)[1])) {
			  //Do nothing
		  }
		  else {
			  isValid = false;
			  System.out.println("Formatting Error: Invalid Value of '" + csvData.get(i)[1] + "' detected under the header '" + csvData.get(0)[1] + "' in row " + (i+1)); 
		  }
	
	  }
	  //Verify that the values are doubles
	  for(int i = 1; i < csvData.size(); i++) {
		  try {
			  double test = Double.parseDouble(csvData.get(i)[2]);
		  }
		  catch(NumberFormatException e) {
			  isValid = false;
			  System.out.println("Formatting Error: Invalid Value of '" + csvData.get(i)[2] + "' detected under the header '" + csvData.get(0)[2] + "' in row " + (i+1) + ". Only numbers are valid. No letters and no more than one decimal place bullet is acceptable.");
		  }
	  }
	  //Verify that out-unit is valid.
	  if(csvData.get(1)[3].equalsIgnoreCase("lb") || csvData.get(1)[3].equalsIgnoreCase("oz")) {
		  //Do nothing
	  }
	  else {
		  isValid = false;
		  System.out.println("Formatting Error: Invalid Value of '" + csvData.get(1)[3] + "' detected under the header '" + csvData.get(0)[3] + "' Only 'lb' or 'oz' is acceptable here.");
		  
	  }
	  //Verify that out-size is valid.
	  try {
		  double test = Double.parseDouble(csvData.get(1)[4]);
	  }
	  catch(NumberFormatException e) {
		  isValid = false;
		  System.out.println("Formatting Error: Invalid value of '" + csvData.get(1)[4] + "' detected under the header '" + csvData.get(0)[4] + "' Only numbers are acceptable here.");
	  }
	  //Verify that if base-unit column exists, the base unit matches one of the listed ingredients.
	  //This code is kinda un-necessary since we don't verify base units anymore.
	  if(csvData.get(0).length > 5 && csvData.get(1).length > 5) {
		  ArrayList<String> ingredients = new ArrayList<String>();
		  for(int i = 1; i < csvData.size(); i++) {
			  ingredients.add(csvData.get(i)[0]);
		  }
		  if(ingredients.contains(csvData.get(1)[5])) {
			  //Do nothing
		  }
		  else {
			  isValid = false;
			  System.out.println("Formatting Error: Your value of '" + csvData.get(1)[5] + "' detected under the header '" + csvData.get(0)[5] + "' This does not match any of the values included in your ingredients column.");
		  }
	  }
	  
	  return isValid;
  }//End of checkFormat();
  public static boolean baseIngredientExists(ArrayList<String[]> csvData) {
	  if(csvData.get(0).length == 6 && csvData.get(1).length == 6) {
		  return true;
	  }
	  return false;
  }
}//EOF
