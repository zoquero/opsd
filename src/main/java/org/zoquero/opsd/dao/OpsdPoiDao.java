package org.zoquero.opsd.dao;

import java.io.FileInputStream;
import java.io.IOException;
import org.zoquero.opsd.entities.OpsdProject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
			System.out.println("Opening the sheet '" + sheet.getSheetName()
								+ "' (position #" + sheetPosition + ")");
	
	//			//every sheet has rows, iterate over them
	//			Iterator<Row> rowIterator = sheet.iterator();
	//			while (rowIterator.hasNext()) {
	//				String name = "";
	//				String shortCode = "";
	//
	//				//Get the row object
	//				Row row = rowIterator.next();
	//
	//				//Every row has columns, get the column iterator and iterate over them
	//				Iterator<Cell> cellIterator = row.cellIterator();
	//				while (cellIterator.hasNext()) {
	//					//Get the Cell object
	//					Cell cell = cellIterator.next();
	//
	//					//check the cell type and process accordingly
	//					switch(cell.getCellType()){
	//					case Cell.CELL_TYPE_STRING:
	//						if(shortCode.equalsIgnoreCase("")){
	//							shortCode = cell.getStringCellValue().trim();
	//						}else if(name.equalsIgnoreCase("")){
	//							//2nd column
	//							name = cell.getStringCellValue().trim();
	//						}else{
	//							//random data, leave it
	//							System.out.println("Random data::"+cell.getStringCellValue());
	//						}
	//						break;
	//					case Cell.CELL_TYPE_NUMERIC:
	//						System.out.println("Random data::"+cell.getNumericCellValue());
	//					}
	//				} //end of cell iterator
	//				Country c = new Country(name, shortCode);
	//				countriesList.add(c);
	//			} //end of rows iterator
	
        return null;
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
