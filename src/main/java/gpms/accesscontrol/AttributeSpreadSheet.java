package gpms.accesscontrol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AttributeSpreadSheet {

	private static List<AttributeRecord> allAttributeRecords = new ArrayList<AttributeRecord>();
	private HSSFWorkbook workBook1;
	private HSSFWorkbook workBook2;

	public List<AttributeRecord> getAllAttributeRecords() {
		return allAttributeRecords;
	}

	// public List<AttributeRecord> getTestRecord() {
	// return allAttributeRecords;
	// }

	public AttributeSpreadSheet(InputStream spreadSheetFile) throws Exception {
		workBook1 = new HSSFWorkbook(spreadSheetFile);
		Sheet sheet = workBook1.getSheetAt(0);
		for (Row row : sheet) {
			loadAttributeRow(row);
		}
	}

	public static List<AttributeRecord> readExcelData(String fileName) {
		List<AttributeRecord> policyRequestsList = new ArrayList<AttributeRecord>();

		try {
			// Create the input stream from the xlsx/xls file
			FileInputStream fis = new FileInputStream(fileName);

			// Create Workbook instance for xlsx/xls file input stream
			Workbook workbook = null;
			if (fileName.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (fileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			}

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets();

			// loop through each of the sheets
			for (int i = 0; i < numberOfSheets; i++) {

				// Get the nth sheet from the workbook
				Sheet sheet = workbook.getSheetAt(i);

				// every sheet has rows, iterate over them
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					String attributeName = "";
					String fullAttributeName = "";
					String category = "";
					String dataType = "";
					String values = "";

					// Get the row object
					Row row = rowIterator.next();

					// Omit first row as Column Titles
					if (row.getRowNum() != 0) {

						// Every row has columns, get the column iterator and
						// iterate over them
						Iterator<Cell> cellIterator = row.cellIterator();

						while (cellIterator.hasNext()) {
							// Get the Cell object
							Cell cell = cellIterator.next();

							// check the cell type and process accordingly
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								if (attributeName.equalsIgnoreCase("")) {
									attributeName = cell.getStringCellValue()
											.trim();
								} else if (fullAttributeName
										.equalsIgnoreCase("")) {
									// 1nd column
									fullAttributeName = cell
											.getStringCellValue().trim();
								} else if (category.equalsIgnoreCase("")) {
									// 2nd column
									category = cell.getStringCellValue().trim();
								} else if (dataType.equalsIgnoreCase("")) {
									// 3nd column
									dataType = cell.getStringCellValue().trim();
								} else if (values.equalsIgnoreCase("")) {
									// 4th column
									values = cell.getStringCellValue().trim();
								}
								break;
							}
						} // end of cell iterator
						AttributeRecord attributeRecord = new AttributeRecord();
						attributeRecord.setAttributeName(attributeName);
						attributeRecord.setFullAttributeName(fullAttributeName);
						attributeRecord.setCategory(category);
						attributeRecord.setDataType(dataType);
						attributeRecord.setValues(values);
						policyRequestsList.add(attributeRecord);

					}
				} // end of rows iterator

			} // end of sheets for loop

			// close file input stream
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return policyRequestsList;
	}

	public void readAttributeSpreadSheet(InputStream inputStream)
			throws Exception {
		workBook2 = new HSSFWorkbook(inputStream);
		Sheet sheet = workBook2.getSheetAt(0);
		for (Row row : sheet) {
			loadAttributeRow(row);
		}
	}

	private static void loadAttributeRow(Row row) {
		if (row.getRowNum() != 0) {
			if (row.getCell(0) == null || row.getCell(1) == null)
				return;
			String attributeName = row.getCell(0).toString().trim();
			if (attributeName.equals("") || attributeName.startsWith("//"))
				return;
			String fullAttributeName = row.getCell(1) != null ? row.getCell(1)
					.toString() : "";

			String category = row.getCell(2) != null ? row.getCell(2)
					.toString() : "";
			String dataType = row.getCell(3) != null ? row.getCell(3)
					.toString() : "";
			String values = row.getCell(4) != null ? row.getCell(4).toString()
					: "";
			allAttributeRecords.add(new AttributeRecord(attributeName,
					fullAttributeName, category, dataType, values));
		}
	}

	public AttributeRecord findAttributeRecord(String attributeName) {
		for (AttributeRecord record : allAttributeRecords) {
			if (record.getAttributeName().equalsIgnoreCase(attributeName))
				return record;
		}
		return null;
	}
}
