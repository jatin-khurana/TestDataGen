package genworth.TestDataGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class App {
	static Workbook repoWB;
	static Sheet repoSheet;
	static WritableWorkbook DWB;

	public static void main(String[] args) throws Exception {
		checkXLS("inputs");
		checkSheet("inputs", "inputs");
		loadSheet("inputs", "inputs");

		int noOfCols = repoSheet.getColumns();
		int totalCombinations = 1;
		Workbook source = Workbook.getWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + "inputs.xls"));
		WritableWorkbook workbook = Workbook
				.createWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + "Output.xls"), source);
		WritableSheet DSheet;
		Label Value = null;
		// workbook.createSheet("OUT", 0);
		DSheet = workbook.getSheet(0);

		ArrayList<Integer> valuesInEachCol = new ArrayList<Integer>();
		ArrayList<Integer> noOfCopiesOfEachItemInASet = new ArrayList<Integer>();
		ArrayList<Integer> noOfCopiesOfEachSet = new ArrayList<Integer>();
		ArrayList<Integer> multiplier = new ArrayList<Integer>();

		HashMap<Integer, ArrayList<String>> output = new HashMap<Integer, ArrayList<String>>();

		for (int i = 0; i < noOfCols; i++) {
			valuesInEachCol.add(repoSheet.getColumn(i).length - 1);
		}
		for (int i = 0; i < valuesInEachCol.size(); i++) {
			totalCombinations = totalCombinations * valuesInEachCol.get(i);
		}
		for (int i = 0; i < valuesInEachCol.size(); i++) {
			int divisor = 1;
			int iterator;
			for (int q = 0; q <= i; q++) {
				divisor = divisor * valuesInEachCol.get(q);

			}
			iterator = totalCombinations / divisor;
			noOfCopiesOfEachItemInASet.add(iterator); // NO OF COPIES
		}
		for (int i = 0; i < valuesInEachCol.size(); i++) {
			multiplier.add(totalCombinations / valuesInEachCol.get(i));
		}
		for (int i = 0; i < valuesInEachCol.size(); i++) {
			System.out.println("valuesInEachCol = " + valuesInEachCol.get(i));
		}
		System.out.println("\n");
		for (int i = 0; i < multiplier.size(); i++) {
			System.out.println("multiplier = " + multiplier.get(i));
		}
		System.out.println("\n");
		for (int i = 0; i < noOfCopiesOfEachItemInASet.size(); i++) {
			System.out.println("noOfCopiesOfEachItemInASet = " + noOfCopiesOfEachItemInASet.get(i));
		}
		System.out.println("\n");
		for (int i = 0; i < valuesInEachCol.size(); i++) {
			noOfCopiesOfEachSet.add(multiplier.get(i) / noOfCopiesOfEachItemInASet.get(i));
			System.out.println("noOfCopiesOfEachSet = " + noOfCopiesOfEachSet.get(i));
		}
		System.out.println("\n");

		for (int i = 0; i < noOfCols; i++) {
			ArrayList<String> temp = new ArrayList<String>();
			for (int j = 0; j < valuesInEachCol.get(i); j++) {
				for (int j2 = 0; j2 < noOfCopiesOfEachItemInASet.get(i); j2++)
				{
					System.out.println(repoSheet.getCell(i, j + 1).getContents());
					temp.add(repoSheet.getCell(i, j + 1).getContents());
				}
			}
			output.put(i, temp);
		}
		int a = 0;
		int b = 1;
		for (int i = 0; i < output.size(); i++) {
			for (int j = 0; j < noOfCopiesOfEachSet.get(i); j++) {
				for (int j2 = 0; j2 < output.get(i).size(); j2++) {
					Value = new Label(a, b, output.get(i).get(j2));
					DSheet.addCell(Value);
					b++;
				}
			}
			a++;
			b = 1;
		}
		workbook.write();
		workbook.close();
	}

	public static void checkXLS(String xlsFileName) throws Exception {
		try {
			Workbook temp = Workbook
					.getWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + xlsFileName + ".xls"));
			temp.close();
		} catch (java.io.FileNotFoundException e) {
			throw new Exception("\nNo file found with the name:- " + "'" + xlsFileName + "'" + "\n");
		}
	}

	public static void checkSheet(String xlsFileName, String sheetName) throws Exception {
		Workbook temp;
		checkXLS(xlsFileName);
		temp = Workbook.getWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + xlsFileName + ".xls"));
		if (temp.getSheet(sheetName) == null) {
			throw new Exception("\nNo sheet found with the Name:- " + "'" + sheetName + "'" + "\n");
		}
		temp.close();
	}

	public static void loadSheet(String xlsFileName, String sheetName) throws Exception {
		checkXLS(xlsFileName);
		repoWB = Workbook.getWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + xlsFileName + ".xls"));
		repoSheet = repoWB.getSheet(sheetName);
	}
}