package org.zoquero.opsd.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.zoquero.opsd.entities.OpsdProject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Opsd Datatap implementation over Excel files using POI.
 * The most expensive operation is opening the filestream
 * and creating the Workbook object, so it will be cache,
 * we'll do it just in "connect()"
 * @author agalindo
 *
 */
public class OpsdPoiDao implements OpsdDataTap {

	/** Path to the Excel file that cointains Opsd data */
	private String path;
	/** POI Workbook instance for xlsx/xls file input stream */
	private Workbook workbook = null;
    /** Input stream from the xlsx/xls file */
	private FileInputStream fis = null;
	
	public OpsdPoiDao(String path) {
		System.out.println("OpsdPoiDao created, path=" + path);
		this.path = path;
	}

	@Override
	public OpsdProject getProject(String projectName) throws OpsdDaoException {
		System.out.println("OpsdPoiDao.getProject");

			//Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets();
			int sheetPosition = OpsdPoiConf.getSheetPosition("OpsdProject");
			if(sheetPosition > numberOfSheets - 1) {
				throw new OpsdDaoException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdProject objects should be"
						+ " in sheet position # " + sheetPosition
						+ " (0..N-1)");
			}
	
			//Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow();
			System.out.println("Opening the sheet '" + sheet.getSheetName()
					+ "' (position #" + sheetPosition + ")"
					+ " row #" + firstRow);
			
			Row row = sheet.getRow(firstRow);
			if(row == null) {
				throw new OpsdDaoException("Can't find Project data in row #" + firstRow);
			}
			
//			// Get the column iterator and iterate over it
//			Iterator<Cell> cellIterator = row.cellIterator();
//			while (cellIterator.hasNext()) {
//				// Get the Cell object
//				Cell cell = cellIterator.next();
//
//				// Check the cell type and process accordingly
//				// getStringCellValue , getNumericCellValue , ...
//				String s = cell.getStringCellValue();
//				System.out.println("  * cel·la = " + s);
//			}
			
			int i = 1;
			String name = row.getCell(i++).getStringCellValue();
			String description = row.getCell(i++).getStringCellValue();
			String dateInStr = row.getCell(i++).getStringCellValue();
			String dateOutStr = row.getCell(i++).getStringCellValue();
			String responsibleName = row.getCell(i++).getStringCellValue();
			String dependencies = row.getCell(i++).getStringCellValue();
			String recoveryProcedure = row.getCell(i++).getStringCellValue();
			String moreInfo = row.getCell(i++).getStringCellValue();
			
			// Let's set dates
			Calendar dateIn  = Calendar.getInstance();
			Calendar dateOut = Calendar.getInstance();
			// Dates are in dd.MM.yyyy format
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			try {
				dateIn.setTime(sdf.parse(dateInStr));
			} catch (ParseException e) {
				throw new OpsdDaoException("Can't parse the initial date '" + dateInStr + "' from the project data");
			}
			if(dateOutStr.equals("Encara actiu") || dateOutStr.equals("")) {
				dateOut = null;
			}
			else {
				try {
					dateOut.setTime(sdf.parse(dateOutStr));
				} catch (ParseException e) {
					throw new OpsdDaoException("Can't parse the end date '" + dateOutStr + "' from the project data");
				}
			}
			
			// Let's create the OpsdProject object:
			OpsdProject op = new OpsdProject(name, description, responsibleName, dateIn, dateOut, dependencies, recoveryProcedure, moreInfo);

//			Iterator<Row> rowIterator = sheet.iterator();
//			while (rowIterator.hasNext()) {
//				System.out.println("\n== Nova fila ==");
//				
//				// Get the row object
//				Row row = rowIterator.next();
//
//				// Get the column iterator and iterate over it
//				Iterator<Cell> cellIterator = row.cellIterator();
//				while (cellIterator.hasNext()) {
//					// Get the Cell object
//					Cell cell = cellIterator.next();
//
//					// Check the cell type and process accordingly
//					// getStringCellValue , getNumericCellValue , ...
//					String s = cell.getStringCellValue();
//					System.out.println("  * cel·la = " + s);
//				}
//				
//			}
	
        return op;
	}

	@Override
	public void connect() throws OpsdDaoException {
		System.out.println("OpsdPoiDao.connect");

		try {
			//Create the input stream from the xlsx/xls file
			fis = new FileInputStream(path);

			// Create Workbook instance for xlsx/xls file input stream
			workbook = null;
			if(path.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}
			else if(path.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			else {
				throw new OpsdDaoException("The file " + path +
						" doesn't look like an Excel file");
			}

		}
		catch (IOException e) {
			throw new OpsdDaoException("Can't open the file " + path, e);
		}

	}

	@Override
	public void disconnect() throws OpsdDaoException {
		System.out.println("OpsdPoiDao.disconnect");
		
		// Close file input stream
		try {
			fis.close();
		}
		catch (IOException e) {
	    	throw new OpsdDaoException("Errors closing the file: " +
			e.getMessage(), e);
		}
		
	}
}
