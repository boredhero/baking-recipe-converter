//This class exports the data to a csv!
package rc.bored;

import java.util.*;
import java.io.*;

public class ExportHelper {
	public static void export(ArrayList<ArrayList<String>> finalDataStructure, String fileName) {
		//Declare as null so we can try/catch the exception
		FileWriter csvWriter = null;
		//Try/catch the exception
		try {
			csvWriter = new FileWriter(fileName + ".csv");
			csvWriter.append(fileName);
			csvWriter.append("\n");
		} catch (IOException e) {
			System.out.println("-------------------------");
			System.out.println("An unknwon exception has occured");
			System.out.println("Make sure you have appropriate read/write permissions to create a file in the directory in which you ran this program.");
			System.out.println("-------------------------");
			System.out.println("Stacktrace:");
			e.printStackTrace();
			System.out.println("-------------------------");
			System.out.println("The program will now halt.");
			System.out.println("-------------------------");
			System.exit(0);
		}
		//Do the stuff, assuming the program hasn't been force quit due to an error.
		//for(int i = 0; i < finalDataStructure.size()-1; i++) {
		for(int y = 0; y < finalDataStructure.get(0).size(); y++) {
			try {
				csvWriter.append(finalDataStructure.get(0).get(y));
				csvWriter.append(",");
				csvWriter.append(finalDataStructure.get(1).get(y));
				csvWriter.append(",");
				csvWriter.append(finalDataStructure.get(2).get(y));
				csvWriter.append(",");
				//csvWriter.append(finalDataStructure.get(i).get(y));
				csvWriter.append(",");
			} catch (IOException e) {
				System.out.println("-------------------------");
				System.out.println("An unknwon exception has occured");
				System.out.println("Make sure you have appropriate read/write permissions to create a file in the directory in which you ran this program.");
				System.out.println("-------------------------");
				System.out.println("Stacktrace:");
				e.printStackTrace();
				System.out.println("-------------------------");
				System.out.println("The program will now halt.");
				System.out.println("-------------------------");
				System.exit(0);
			}
			try {
				csvWriter.append("\n");
			} catch (IOException e) {
				System.out.println("-------------------------");
				System.out.println("An unknwon exception has occured");
				System.out.println("Make sure you have appropriate read/write permissions to create a file in the directory in which you ran this program.");
				System.out.println("-------------------------");
				System.out.println("Stacktrace:");
				e.printStackTrace();
				System.out.println("-------------------------");
				System.out.println("The program will now halt.");
				System.out.println("-------------------------");
				System.exit(0);
			}
		}
		try {
			csvWriter.append("\n");
			csvWriter.append("Total Dough Yield");
			csvWriter.append(",");
			csvWriter.append(finalDataStructure.get(3).get(0));
			csvWriter.append(",");
			csvWriter.append(finalDataStructure.get(2).get(1));
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			System.out.println("-------------------------");
			System.out.println("An unknwon exception has occured");
			System.out.println("Make sure you have appropriate read/write permissions to create a file in the directory in which you ran this program.");
			System.out.println("-------------------------");
			System.out.println("Stacktrace:");
			e.printStackTrace();
			System.out.println("-------------------------");
			System.out.println("The program will now halt.");
			System.out.println("-------------------------");
			System.exit(0);
		}
		System.out.println("Your file has been successfully saved to the same directory as the program.");
	}//End of export()
}//EOF