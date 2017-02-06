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
		int columns = repoSheet.getColumns();

		Workbook source = Workbook.getWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + "inputs.xls"));
		WritableWorkbook workbook = Workbook
				.createWorkbook(new File(System.getProperty("user.dir") + "\\files\\" + "Output.xls"), source);
		WritableSheet DSheet = workbook.createSheet("output", 0);
		Label Value = null;

		HashMap<String, ArrayList<String>> pool = new HashMap<String, ArrayList<String>>();
		HashMap<String, String> ideal = new HashMap<String, String>();
		HashMap<Integer, HashMap<String, String>> idealBIG = new HashMap<Integer, HashMap<String, String>>();
		HashMap<Integer, HashMap<String, String>> freezerBIG = new HashMap<Integer, HashMap<String, String>>();
		// ENTERTING DATA INTO POOL HASHMAP//
		for (int i = 0; i < columns; i++) {
			ArrayList<String> temp = new ArrayList<String>();
			for (int j = 1; j < repoSheet.getColumn(i).length; j++) {
				temp.add(repoSheet.getCell(i, j).getContents());
			}
			pool.put(repoSheet.getCell(i, 0).getContents(), temp);
			temp = null;
		}
		// CHECKING POOL HASHMAP BY PRINTING//
		for (String i : pool.keySet()) {
			System.out.println("POOL :- * " + i + " *");
			ArrayList<String> temp = new ArrayList<String>();
			temp = pool.get(i);
			for (int j = 0; j < temp.size(); j++) {
				System.out.println(pool.get(i).get(j));
			}
			System.out.println("\n");
		}
		// ENTERTING DATA INTO IDEAL HASHMAP//
		loadSheet("inputs", "ideal");
		for (int i = 0; i < columns; i++) {
			ideal.put(repoSheet.getCell(i, 0).getContents(), repoSheet.getCell(i, 1).getContents());
		}
		// CHECKING IDEAL HASHMAP BY PRINTING
		for (String i : ideal.keySet()) {
			System.out.println("IDEAL :- * " + i + " *");
			System.out.println(ideal.get(i) + "\n");
		}

		// ************************************************************************************************************************//
		// ENTERTING DATA INTO IDEALBIG HASHMAP//
		loadSheet("inputs", "ideal");
		for (int i = 1; i < repoSheet.getRows(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			for (int j = 0; j < repoSheet.getColumns(); j++) {
				System.out.println("KEY:-" + repoSheet.getCell(j, 0).getContents());
				System.out.println("VALUE:-" + repoSheet.getCell(j, i).getContents());
				temp.put(repoSheet.getCell(j, 0).getContents(), repoSheet.getCell(j, i).getContents());
			}
			idealBIG.put(i, temp);
		}
		// CHECKING IDEALBIG HASHMAP BY PRINTING
		for (Integer i : idealBIG.keySet()) {
			for (String s : idealBIG.get(i).keySet()) {
				System.out.println("Ideal BIG :: " + s + " :- " + idealBIG.get(i).get(s));
			}
		}
		// ENTERTING DATA INTO FREEZERBIG HASHMAP//
		loadSheet("inputs", "freezer");
		for (int i = 1; i < repoSheet.getRows(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			for (int j = 0; j < repoSheet.getColumns(); j++) {
				if (repoSheet.getCell(j, i).getContents().equalsIgnoreCase("")) {
					temp.put(repoSheet.getCell(j, 0).getContents(), null);
				} else {
					temp.put(repoSheet.getCell(j, 0).getContents(), repoSheet.getCell(j, i).getContents());
				}
			}
			freezerBIG.put(i, temp);
		}
		// CHECKING FREEZERBIG HASHMAP BY PRINTING
		for (Integer i : freezerBIG.keySet()) {
			for (String s : freezerBIG.get(i).keySet()) {
				System.out.println("freezer BIG :: " + i + "  - " + s + " :- " + freezerBIG.get(i).get(s));
			}
		}
		// HEADER FOR THE OUTPUT
		int a = 0;
		for (String i : pool.keySet()) {
			Value = new Label(a, 0, i);
			DSheet.addCell(Value);
			a++;
		}
		// LOGIC ENGINE//
		int row = 1;
		int column = 0;
		for (Integer w : freezerBIG.keySet()) {
			boolean idealRow = true;
			if (idealRow) {
				for (String s : idealBIG.get(w).keySet()) {
					Value = new Label(column, row, idealBIG.get(w).get(s));
					column++;
					DSheet.addCell(Value);
				}
				row++;
				column = 0;
				idealRow = false;
			}
			HashMap<Integer, String> cacheBIG = new HashMap<Integer, String>();
			int tracker = 0;
			for (String i : pool.keySet()) {

				if (freezerBIG.get(w).get(i) == null) {

					if (pool.get(i).size() == 1) {
					} else {
						for (int j = 0; j < pool.get(i).size(); j++) {
							if (pool.get(i).get(j).equalsIgnoreCase(idealBIG.get(w).get(i))) {

							} else {
								cacheBIG.put(tracker, i + "::" + pool.get(i).get(j));
								tracker++;
							}
						}
					}

				}
			}
			System.out.println("tracker = " + tracker);
			for (int i = 0; i < cacheBIG.size(); i++) {
				for (String j : pool.keySet()) {
					System.out.println("printing in " + column + "," + row);
					if (j.contains(cacheBIG.get(i).split("::")[0])) {
						System.out.println("*XOXO* " + cacheBIG.get(i).split("::")[1]);
						Value = new Label(column, row, cacheBIG.get(i).split("::")[1]);
						column++;
						DSheet.addCell(Value);
					} else {
						System.out.println("*XOXO* " + idealBIG.get(w).get(j));
						Value = new Label(column, row, idealBIG.get(w).get(j));
						column++;
						DSheet.addCell(Value);
					}
				}
				column = 0;
				row++;
				System.out.println("FINALL :-" + w + "," + i);
				System.out.println("\n");
			}

		}
		workbook.write();
		workbook.close();
	}
	
	public void tokenize() {
		
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