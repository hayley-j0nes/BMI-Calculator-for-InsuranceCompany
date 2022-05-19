package javacourseproject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.cert.CRLException;
import java.util.Queue;

/**
 * 
 * @author Hayley Jones Project: Insurance Company Course Project
 *
 */

//Store all the patient name in the queue data structure using the enqueue method and use 
//the dequeue to remove patient name from the queue data structure and print it. 

//You should notice the first in first out concept

//https://www.youtube.com/watch?v=91CMnJeHJVc&list=PL59LTecnGM1Mg6I4i_KbS0w5bPcDjl7oz&index=18&ab_channel=AlexLee

//This class is accessible throughout the program so it's public
public class InsuranceCompany implements BMICaluculation {



	@Override
	public double BMI(String height, String weight) {

		// Squaring the input height for the BMI calculation
		// The input variable's were stored as strings so now they have to be converted
		// to doubles to do the BMI calculation
		// The Math.pow function allows you to create an exponential number
		// BMI calculation
		double calculateBMI = (Double.parseDouble(weight) / Math.pow(Double.parseDouble(height), 2)) * 703;

		return calculateBMI;
	}

	public static void main(String[] args) {
		
		String finalMessage = "Program ended successfully.";
		String outputFilePath = "files/CustomerInformation.csv";

		// First you have to create a scanner in order to use it
		Scanner scan = new Scanner(System.in);

		// Creates buffered stream for better efficiency.
		// Uses try-with-resources to make sure resources get closed properly.
		// Specified path is in a folder named "files"
		// InsuranceCompanyFile is the new file name

		try {

			// Create list
			Queue<List<String>> outputValuesTable = new LinkedList<List<String>>();
			
			outputValuesTable = InsuranceCompany.CreateOuputValues(scan);
			scan.close();

			InsuranceCompany.WriteOutputValuesToCSV(outputValuesTable, outputFilePath);

		} catch (Exception e) {
			// This tells you the specific error
			System.out.println("There was an error in " + e.getStackTrace()[e.getStackTrace().length - 1] + ".");

			// Change the message to this if there's an exception
			finalMessage = "Program ended with an error.";
		} finally {
			// Closes your scanner
			scan.close();

			// Print final finalMessage
			System.out.println(finalMessage);
		}
	}

	public static Queue<List<String>> CreateOuputValues(Scanner scan) {

		Queue<List<String>> outputValuesTable = new LinkedList<List<String>>();
		// Loop user questions till they want to quit
		while (true) {

			// Adds the values to an array list
			List<String> outputRow = new ArrayList<>();

			// Ask user for input
			System.out.println("What's the patient's name?");

			// Store in the value name then move to the next line
			String name = scan.nextLine();

			System.out.println("You entered the name: " + name);

			// name is added to the inputValue array
			outputRow.add(name);

			// Asks user for input
			System.out.println("What the patient's weight (in pounds)?");

			// Stored to the value weight
			String weight = scan.nextLine();

			// Added to the array
			outputRow.add(weight);

			System.out.println("You entered the weight: " + weight);

			// Asks for input
			System.out.println("What the patient's birthday?");

			// Save to string variable
			String birthday = scan.nextLine();

			// Add to array
			outputRow.add(birthday);

			System.out.println("You entered the birthday: " + birthday);

			// Asks user for input
			System.out.println("Enter patient height (in inches)?");

			// Store to string variable
			String height = scan.nextLine();

			// Add to array
			outputRow.add(height);

			System.out.println("You entered the height: " + height);

			

			// Formatting the BMI to show only one decimal point (rounded using ceiling)
			DecimalFormat df = new DecimalFormat("#.#");
			String categoryBMI = "";
			String companyCategory = "";

			InsuranceCompany insCo = new InsuranceCompany();

			double calculateBMI = insCo.BMI(height, weight);

			// Assigns BMI categories to the BMI ranges
			if (calculateBMI < 18.5) {
				categoryBMI = "underweight";
				companyCategory = "low"; // being underweight is unhealthy so it should be high
			} else if ((calculateBMI >= 18.5) && (calculateBMI < 25)) {
				categoryBMI = "normal";
				companyCategory = "low";
			} else if ((calculateBMI >= 25) && (calculateBMI <= 29.9)) {
				categoryBMI = "overweight";
				companyCategory = "high";
			} else if (calculateBMI > 29.9) {
				categoryBMI = "obese";
				companyCategory = "highest";
			}

			outputRow.add(categoryBMI);
			outputRow.add(companyCategory);
			// Add the array to an arrays
			outputValuesTable.add(outputRow);

			// Prints the patient input and specified calculations into the console area.
			System.out.println(outputRow.get(0) + " has a BMI of " + df.format(calculateBMI)
					+ " and the patient's birthday is " + outputRow.get(2) + ".");
			System.out.println("This puts " + outputRow.get(0) + " in the " + categoryBMI + " BMI category, and the "
					+ companyCategory + " insurance category.");

			// Ask user input
			System.out.println("Would you like to add another user?");

			System.out.println("Press any key to continue or 'q' to quit.");

			// if boolean is true then exit user input loop
			if (scan.nextLine().equals("q")) {

				System.out.println("You chose to quit the program.");

				break;

			}
		}
		return outputValuesTable;
	}

	public static void WriteOutputValuesToCSV(Queue<List<String>> outputValuesTable, String outputFilePath) throws Exception {
		
		
		try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outputFilePath))) {
			// Creates headers for the new file
			// CSV file sections are separated by commas
			StringBuilder sb = new StringBuilder();
			sb.append("Name");
			sb.append(",");
			sb.append("Weight");
			sb.append(",");
			sb.append("Birthday");
			sb.append(",");
			sb.append("Height");
			sb.append(",");
			sb.append("BMI Category");
			sb.append(",");
			sb.append("Payment Category");
			sb.append(System.lineSeparator());

			// Add the headers to the file

			buffWriter.write(sb.toString());

			
			// Loops through the queue and removes elements using FIFO
			// Produces the output
			while (!outputValuesTable.isEmpty()) {
				
				List<String> outputRow = outputValuesTable.remove();
				// Clear out the StringBuilder
				sb.delete(0, sb.length());
				
				
				// Only get the first four values because we only have four inputs
				sb.append(outputRow.get(0));
				sb.append(",");
				sb.append(outputRow.get(1));
				sb.append(",");
				sb.append(outputRow.get(2));
				sb.append(",");
				sb.append(outputRow.get(3));
				sb.append(",");
				sb.append(outputRow.get(4));
				sb.append(",");
				sb.append(outputRow.get(5));
				sb.append(System.lineSeparator());

				// Writes to the file
				buffWriter.write(sb.toString());
				

			}
		} catch (Exception e) {
			throw e;
		}
	}
}
