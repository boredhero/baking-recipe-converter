package rc.bored;

import java.util.*;
import java.text.*;

public class ConversionHelper {
	
	public static ArrayList<ArrayList<String>> convert(ArrayList<String[]> csvData, boolean diagSwitch){
		
		final String oz = "oz";
		final String lb = "lb";
		final String gal = "gal";
		final String qt = "qt";
		final String gram = "gram";
		final String outUnit = csvData.get(1)[3];
		diagPrint(("Out Unit: " + outUnit), diagSwitch);
		//final boolean baseIngredientExists = FormatVerifier.baseIngredientExists(csvData);
		//System.out.println("baseIngredientExists: " + baseIngredientExists);
		final Double outSize = Double.parseDouble(csvData.get(1)[4]);
		diagPrint(("Out Size: " + outSize), diagSwitch);
		String baseIngredient = null;
		//boolean wasBaseShifted = false;
		double baseIngredientWeight = 0;
		double totalDoughYield = 0;
		ArrayList<Double> values = new ArrayList<Double>();
		ArrayList<Double> convertedValues = new ArrayList<Double>();
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<String> inUnits = new ArrayList<String>();
		ArrayList<Double> ratioToBase = new ArrayList<Double>();
		ArrayList<ArrayList<String>> finalDataStructure = new ArrayList<ArrayList<String>>();
		
		//Populate ingredients, inUnits, and values.
		for(int i = 1; i < csvData.size(); i++) {
			ingredients.add(csvData.get(i)[0]);

			inUnits.add(csvData.get(i)[1]);

			values.add(Double.parseDouble(csvData.get(i)[2]));

		}
		diagPrint("Ingredients Populated", diagSwitch);
		diagPrintArr("Ingredients: ", ingredients, diagSwitch);
		diagPrint("inUnits Populated", diagSwitch);
		diagPrintArr("inUnits: ", inUnits, diagSwitch);
		diagPrint("Values Populated", diagSwitch);
		diagPrintArr("values: ", values, diagSwitch);
		
		//Handle liquids from the get go.
		for(int i = 0; i < inUnits.size(); i++) {
			if(inUnits.get(i).equalsIgnoreCase(gal)) {
				values.set(i, galToOz(values.get(i)));
				inUnits.set(i, oz);
				diagPrint("Liquids in gal form detected and converted to oz",diagSwitch);
			}
			else if(inUnits.get(i).equalsIgnoreCase(qt)) {
				values.set(i, galToOz(quartsToGal(values.get(i))));
				inUnits.set(i, oz);
				diagPrint("Liquids in qt form detected and converted to oz",diagSwitch);
			}
		}
		//Convert to oz if necessary
		if(outUnit.equalsIgnoreCase(oz)) {
			for(int i = 0; i < values.size(); i++) {
				if(inUnits.get(i).equalsIgnoreCase(lb)) {
					values.set(i, lbsToOz(values.get(i)));
					inUnits.set(i, oz);
				}
				else if(inUnits.get(i).equalsIgnoreCase(gram)) {
					values.set(i, gramToOz(values.get(i)));
					inUnits.set(i, oz);
				}
			}
			diagPrint ("Values and inUnits converted to oz", diagSwitch);
			diagPrintArr("values: ",values,diagSwitch);
			diagPrintArr("inUnits: ",inUnits,diagSwitch);
		}
		//Convert to lb if necessary
		if(outUnit.equalsIgnoreCase(lb)) {
			for(int i = 0; i < values.size(); i++) {
				if(inUnits.get(i).equalsIgnoreCase(oz)) {
					values.set(i, ozToLbs(values.get(i)));
					inUnits.set(i, lb);
				}
				else if(inUnits.get(i).equalsIgnoreCase(gram)) {
					values.set(i, ozToLbs(gramToOz(values.get(i))));
					inUnits.set(i, lb);
				}
			}
			diagPrint("Values and inUnits converted to lb", diagSwitch);
			diagPrintArr("values: ",values,diagSwitch);
			diagPrintArr("inUnits: ",inUnits,diagSwitch);
		}
		//Convert to grams if necessary
		if(outUnit.equalsIgnoreCase(gram)) {
			for(int i = 0; i < values.size(); i++) {
				if(inUnits.get(i).contentEquals(oz)) {
					values.set(i, ozToGram(values.get(i)));
					inUnits.set(i, gram);
				}
				else if(inUnits.get(i).contentEquals(lb)) {
					values.set(i, lbsToOz(ozToGram(values.get(i))));
					inUnits.set(i, gram);
				}
			}
			diagPrint("Values and inUnits converted to gram", diagSwitch);
			diagPrintArr("values: ",values,diagSwitch);
			diagPrintArr("inUnits: ",inUnits,diagSwitch);
		}
		//Handle the base ingredient stuff - This code has been disabled until further notice. There are shuffle issues.
		/*
		if(baseIngredientExists) { //This is fired if there is a custom base ingredient
			baseIngredient = csvData.get(1)[5];
			System.out.println("Base Ingredient: " + baseIngredient);
			if(!(ingredients.get(0).equalsIgnoreCase(baseIngredient))) {
				int location = getStringALLoc(baseIngredient, ingredients);
				diagPrint("Pre Value Shuffle: ", values);
				values = doubleShuffler(location, values);
				diagPrint("Post Value Shuffle: ", values);
				diagPrint("Pre Ingredients Shuffle: ", ingredients);
				ingredients = stringShuffler(location, ingredients);
				diagPrint("Post Ingredients Shuffle: ", ingredients);
				diagPrint("Pre inUnits Shuffle: ", inUnits);
				inUnits = stringShuffler(location, inUnits);
				diagPrint("Post inUnits Shuffle: ", inUnits);
				baseIngredientWeight = values.get(0);
				System.out.println("If: Base Ingredient Weight: " + baseIngredientWeight);
				System.out.println("You entered the custom baseIngredient '" + baseIngredient + "'. Because of this, we have re-arranged the data such that it appears at the top of the recipe.");
				}
			else {
				baseIngredient = csvData.get(1)[0];
				baseIngredientWeight = Double.parseDouble(csvData.get(1)[2]);
			}
		}*/	
		//If there is no custom base ingredient, this is the default
		baseIngredient = csvData.get(1)[0];
		diagPrint("Base Ingredient: " + baseIngredient, diagSwitch); 
		baseIngredientWeight = values.get(0);
		diagPrint("Base Ingredient Weight: " + baseIngredientWeight + " " + outUnit, diagSwitch);
		//Generate ratios
		for(int i = 0; i < values.size(); i++) {
			ratioToBase.add(ratioFactory(values.get(i), baseIngredientWeight));
		}
		diagPrint("Ratios to base generated",diagSwitch);
		diagPrintArr("RTB: ", ratioToBase,diagSwitch);
		//Sanity check
		if(ratioToBase.get(0) != 1.0) {
			diagPrint("ratioToBase.get(0): " + ratioToBase.get(0), diagSwitch);
			System.out.println("Error: An unknown error has occured during data organization. Please make sure that your custom ingredient is at the top of the list in your csv, not somewhere else");
			System.out.println("The program will now exit. We apologize for this unanticipated issue.");
			System.exit(0);
		}
		//Do the conversion, finally
		for(int i = 0; i < values.size(); i++) {
			convertedValues.add((ratioToBase.get(i)*outSize));
		}
		diagPrint("Converted values", diagSwitch);
		diagPrintArr("convertedValues: ", convertedValues, diagSwitch);
		//Make an AL of new headers
		ArrayList<String> newHeaders = new ArrayList<String>();
		newHeaders.add("ingredients"); newHeaders.add("amount");
		newHeaders.add("unit");
		diagPrint("Created AL of new headers", diagSwitch);
		diagPrintArr("newHeaders: ", newHeaders, diagSwitch);
		//Convert all ALs to AL<String>
		ArrayList<String> newValues = new ArrayList<String>();
		ArrayList<String> doughYield = new ArrayList<String>();
		DecimalFormat two = new DecimalFormat("#.##");
		totalDoughYield = Double.parseDouble(two.format(sumAL(convertedValues)));
		diagPrint("totalDoughYield: " + totalDoughYield, diagSwitch);
		doughYield.add(Double.toString(totalDoughYield));
		diagPrint("Added total dough yield to an array so it can be passed in the final DS", diagSwitch);
		newValues = doubleALToStringAL(convertedValues);
		diagPrint("Changed doubles in convertedValues to strings in newValues", diagSwitch);
		//Add headers to stuff
		ingredients.add(0, newHeaders.get(0));
		diagPrint("Added ingredients header to ingredients string list", diagSwitch);
		newValues.add(0, newHeaders.get(1));
		diagPrint("Added amount header to newValues string list", diagSwitch);
		inUnits.add(0, newHeaders.get(2));
		diagPrint("Added unit header to inUnits string list", diagSwitch);
		//Add the AL<String> to final data structure AL of ALs
		finalDataStructure.add(ingredients);
		finalDataStructure.add(newValues);
		finalDataStructure.add(inUnits);
		finalDataStructure.add(doughYield);
		diagPrint("Final Data Structure Assembled Successfully", diagSwitch);
		//Return our final data structure to main.
		return finalDataStructure;
		
	}//End of convert()
	public static double lbsToOz(double num) {
		return num*16;
	}//End of lbsToOz()
	public static double ozToLbs(double num) {
		return num/16;
	}//End of ozToLbs()
	public static double quartsToGal(double num) {
		return num*0.25;
	}//Ends of quartsToGal()
	public static double galToOz(double num) {
		return num*128;
	}//End of galToOz()
	public static double ozToGram(double num) {
		return num*28.35;
	}//End of ozToGram
	public static double gramToOz(double num) {
		return num/28.35;
	}//End of gramToOz
	public static double ratioFactory(double num, double base) {
		return num/base;
	}//End of ratioFactory()
	public static int getStringALLoc(String searchObject, ArrayList<String> searchDatabase) {
		int count = 0;
		for(int i = 0; i < searchDatabase.size(); i++) {
			if(!(searchDatabase.get(i).equalsIgnoreCase(searchObject))) {
				count++;
			}
			else if(searchDatabase.get(i).equalsIgnoreCase(searchObject)) {
				return count;
			}
		}
		//This should never actually be returned but the compiler wants it there.
		return 0;
	}//End of getStringALLoc()
	public static int getDoubleALLoc(double searchObject, ArrayList<Double> searchDatabase) {
		int count = 0;
		for(int i = 0; i < searchDatabase.size(); i++) {
			double searchDatabaseGetI = searchDatabase.get(i);
			if(!(searchDatabaseGetI == searchObject)) {
				count++;
			}
			else if(searchDatabaseGetI == searchObject) {
				return count;
			}
		}
		//This should never actually be returned but the compiler wants it there.
		return 0;
	}
	//Disabled stringShuffler because we don't support custom base ingredient at this time and the code has too many issues to work in the wild.
	/*public static ArrayList<String> stringShuffler(int frontVal, ArrayList<String> list){
		if(list.get(frontVal).equalsIgnoreCase(list.get(0))) {
			return list;
		}
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(list.get(frontVal));
		for(int i = 0; i < list.size(); i++) {
			if(!(list.get(i).equalsIgnoreCase(list.get(frontVal)))) {
				temp.add(list.get(i));
			}
		}
		return temp;
	}//End of stringShuffler
	*/ 
	/*
	//Disabled doubleShuffler because we don't support custom base ingredients at this time and the code has WAY too many issues to work in the wild.
	//String shuffler worked okay but double shuffler has so so many issues, especially with duplicate entries in lists.
	/*
	public static ArrayList<Double> doubleShuffler(int frontVal, ArrayList<Double> list){
		if(list.get(0) == list.get(frontVal)) {
			System.out.println("doubleShuffler: frontVal = .get(0), returning list unmodified.");
			return list;
		}
		ArrayList<Double> temp = new ArrayList<Double>();
		temp.add(list.get(frontVal));
		int dupeCounter = 0;
		//We need to check for dupes
		for(int i = 0; i < list.size(); i++) {
			double doubleListGetI = list.get(i);
			double listGetFrontVal = list.get(frontVal);
			if(doubleListGetI == listGetFrontVal) {
				dupeCounter++;
			}
		}
		if(dupeCounter <= 1) {
		//DO the thing
		for(int i = 0; i < list.size(); i++) {
			double doubleListGetI = list.get(i);
			double listGetFrontVal = list.get(frontVal);
			if(!(doubleListGetI == listGetFrontVal)) {
				System.out.println("i: " + i + " list.get(i): " + list.get(i));
				System.out.println("List.get(i): " + list.get(i) + " != list.get(frontVal): " + list.get(frontVal));
				temp.add(list.get(i));
			}
		}
		}
		else {
			System.out.println("A duplicate has been detected. We are taking...special measures. This may reduce accuracy");
			double listGetFrontVal = list.get(frontVal);
			int firstLoc = getDoubleALLoc(listGetFrontVal, list);
			for(int i = 0; i < (firstLoc + 1); i++) {
				double doubleListGetI = list.get(i);
				if(!(doubleListGetI == listGetFrontVal)) {
					temp.add(list.get(i));
				}
			}
			for(int i = (firstLoc + 1); i > list.size(); i++) {
				temp.add(list.get(i));
			}
			return temp;
		}
		System.out.println("DoubleShuffler: We shuffled a thing, see if its right");
		diagPrint("DoubleShuffler: ", temp);
		return temp;
	}//End of doubleShuffler()
	*/
	public static ArrayList<String> doubleALToStringAL(ArrayList<Double> list){
		DecimalFormat two = new DecimalFormat("#.##");
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i = 0; i < list.size(); i++) {
			temp.add(two.format(list.get(i)));
		}
		return temp;
	}//End doubleALToStringAL
	public static double sumAL(ArrayList<Double> list) {
		double total = 0.0;
		for(int i = 0; i < list.size(); i++) {
			total = total + list.get(i);
		}
		return total;
	}//End sumAL
	public static void diagPrintArr(String info, ArrayList list, boolean diagSwitch) {
		
		if(diagSwitch){
			for(int i = 0; i < list.size(); i++) {
				System.out.println(info + list.get(i));
			}
		}
		else {
			return;
		}
	}//End diagPrintArr
	public static void diagPrint(String text, boolean diagSwitch) {
		if(diagSwitch) {
			System.out.println("diag: " + text);
		}
	}
	//This method is obsolete but i'm loathe to remove it because it's kinda cool tbh.
	/*
	public static ArrayList<Double> dupeBreaker(double dupeValue, ArrayList<Double> listWithDupes){
		//We don't use this.
		double dupeNukerVal = 0.000;
		//This method prevents dupe sorting issues by adding a tiny number to each dupe in a list so that nothing is equal.
		for(int i = 0; i < listWithDupes.size(); i++) {
			double listWithDupesGetI = listWithDupes.get(i);
			if(listWithDupesGetI == dupeValue) {
				listWithDupes.set(i, (listWithDupesGetI + dupeNukerVal));
				dupeNukerVal = dupeNukerVal + 0.001;
			}
		}
		return listWithDupes;
	}//End of dupeBreaker
	*/
}//EOF