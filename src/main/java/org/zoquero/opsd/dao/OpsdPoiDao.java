package org.zoquero.opsd.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.zoquero.opsd.entities.OpsdDeviceType;
import org.zoquero.opsd.entities.OpsdOSType;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdResponsible;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdSystem;
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
	/** Lazy initialized list of valid responsibles */
	private List<OpsdResponsible> responsibles = null;
	
	/**
	 * Static map to read device types from the properties.
	 * Such a field should not be set in the Excel file.
	 * name => description
	 */
    private static final Map<String, String>  staticDeviceTypeDesc;
    /**
	 * Static map to read if 'OS Type is virt' from the properties.
	 * Such a field should not be set in the Excel file.
	 * 0==phis, 1==virt .
	 * name => 0|1
     */
    private static final Map<String, Boolean> staticDeviceTypeIsVirt;
    /**
	 * Static map to read 'OS Type description' from the properties.
	 * Such a field should not be set in the Excel file.
	 * name => description
     */
    private static final Map<String, String>  staticOSTypeDesc;
    
	List<OpsdRole> cachedRoles = null;
    
    static {
    	/* Initialize the static maps */
    	staticDeviceTypeDesc    = new HashMap<String, String>();
    	staticDeviceTypeIsVirt  = new HashMap<String, Boolean>();
    	staticOSTypeDesc        = new HashMap<String, String>();

    	String devTypeDescPrefix   = "deviceTypeDesc.";
    	String devTypeIsVirtPrefix = "deviceTypeIsVirt.";
    	String osTypeDescPrefix    = "osTypeDesc.";

    	String opsdPoiConfProperties = "org.zoquero.opsd.dao.OpsdPoiConf";
    	ResourceBundle rb = ResourceBundle.getBundle(opsdPoiConfProperties);
    	Enumeration<String> keys = rb.getKeys();

    	// deviceTypes
    	while (keys.hasMoreElements()) {
    		String key = keys.nextElement();
    		if(key.startsWith(devTypeDescPrefix)) {
    			String devName = key.substring(devTypeDescPrefix.length());
    			String value = rb.getString(key).trim();
    			staticDeviceTypeDesc.put(devName, value);
    		}
    		else if(key.startsWith(devTypeIsVirtPrefix)) {
    			String osName = key.substring(devTypeIsVirtPrefix.length());
    			String value = rb.getString(key).trim();
    			Boolean b;
    			if (value.equals("0"))
    				b = new Boolean(false);
    			else if (value.equals("1"))
    				b = new Boolean(true);
    			else {
    				throw new RuntimeException("Misconfiguration: "
    						+ "Can't read 0|1 value on value " + value
    						+ " for key " + devTypeIsVirtPrefix + osName
    						+ " on properties " + opsdPoiConfProperties);
    			}
    			staticDeviceTypeIsVirt.put(osName, b);
    		}
    		else if(key.startsWith(osTypeDescPrefix)) {
    			String osName = key.substring(osTypeDescPrefix.length());
    			String value = rb.getString(key).trim();
    			staticOSTypeDesc.put(osName, value);
    		}

    	}
    	// sanity checks
    	if(staticDeviceTypeDesc.size() < 3) {
    		throw new RuntimeException("OpsdPoiConf just could initialize "
    				+ staticDeviceTypeDesc.size() + " device types "
    				+ "reading '" + devTypeDescPrefix + "' fields from "
    				+ opsdPoiConfProperties + " properties");
    	}
    	if(staticDeviceTypeIsVirt.size() < 3) {
    		throw new RuntimeException("OpsdPoiConf just could initialize "
    				+ staticDeviceTypeIsVirt.size() + " osTypeIsVirt "
    				+ "reading '" + devTypeIsVirtPrefix + "' fields from "
    				+ opsdPoiConfProperties + " properties");
    	}
    	if(staticOSTypeDesc.size() < 3) {
    		throw new RuntimeException("OpsdPoiConf just could initialize "
    				+ staticOSTypeDesc.size() + " osTypeDesc "
    				+ "reading '" + osTypeDescPrefix + "' fields from "
    				+ opsdPoiConfProperties + " properties");
    	}
    }
	
	public OpsdPoiDao(String path) {
		System.out.println("OpsdPoiDao created, path=" + path);
		this.path = path;
	}

	@Override
	public OpsdProject getProject(String projectName) throws OpsdException {
		System.out.println("OpsdPoiDao.getProject");

			//Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition("OpsdProject");
			if(sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdProject objects should be"
						+ " in sheet position # " + sheetPosition
						+ " (0..N-1)");
			}
	
			//Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable
			System.out.println("Opening the sheet '" + sheet.getSheetName()
					+ "' (position #" + sheetPosition + ")"
					+ " row #" + firstRow);
			
			Row row = sheet.getRow(firstRow);
			if(row == null) {
				throw new OpsdException("Can't find Project data in row #" + firstRow);
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
				throw new OpsdException("Can't parse the initial date '" + dateInStr + "' from the project data");
			}
			if(dateOutStr.equals("Encara actiu") || dateOutStr.equals("")) {
				dateOut = null;
			}
			else {
				try {
					dateOut.setTime(sdf.parse(dateOutStr));
				} catch (ParseException e) {
					throw new OpsdException("Can't parse the end date '" + dateOutStr + "' from the project data");
				}
			}
			
			OpsdResponsible responsible = getResponsible(responsibleName);
			// Let's create the OpsdProject object:
			OpsdProject op = new OpsdProject(name, description, responsible, dateIn, dateOut, dependencies, recoveryProcedure, moreInfo);

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

	/**
	 * Get the Responsible object from his name.
	 * On a future release all data will be on a Relational Database.
	 * By now, the allowed project responsibles must be described here,
	 * not in the Excel file.
	 * @param responsibleName
	 * @return
	 */
	private OpsdResponsible getResponsible(String responsibleName) {
		if(responsibles == null) {
			/* Lazy initialization of the array, read from properties file */
			responsibles = new ArrayList<OpsdResponsible>();
			
			ResourceBundle rb = ResourceBundle.getBundle("org.zoquero.opsd.entities.Responsible");
			Enumeration<String> keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if(key.startsWith("responsible.")) {
					String value = rb.getString(key);
					StringTokenizer st = new StringTokenizer(value, ";");
					String name        = (String) st.nextElement();
					String email       = (String) st.nextElement();
					String department  = (String) st.nextElement();
					String resourceAcl = (String) st.nextElement();
					String moreInfo    = (String) st.nextElement();
					if(name == null || email == null || department == null
							|| resourceAcl == null || moreInfo == null) {
						return null;
					}
					OpsdResponsible or = new OpsdResponsible(name, email,
											department, resourceAcl, moreInfo);
					responsibles.add(or);
				}
			}
			for(OpsdResponsible aResponsible: responsibles) {
				if(aResponsible.getName().equals(responsibleName)) {
					return aResponsible;
				}
			}
		}
		return null;
	}

	@Override
	public void connect() throws OpsdException {
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
				throw new OpsdException("The file " + path +
						" doesn't look like an Excel file");
			}

		}
		catch (IOException e) {
			throw new OpsdException("Can't open the file " + path, e);
		}

	}

	@Override
	public void disconnect() throws OpsdException {
		System.out.println("OpsdPoiDao.disconnect");
		
		// Close file input stream
		try {
			fis.close();
		}
		catch (IOException e) {
	    	throw new OpsdException("Errors closing the file: " +
			e.getMessage(), e);
		}
		
	}

	@Override
	public List<OpsdRole> getRoles(OpsdProject project) throws OpsdException {
		System.out.println("OpsdPoiDao.getRoles");
		
		if(cachedRoles == null) {
			cachedRoles = new ArrayList<OpsdRole>();

			//Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition("OpsdRole");
			if(sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdRole objects should be"
						+ " in sheet position # " + sheetPosition
						+ " (0..N-1)");
			}

			//Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable
			
			for(int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
				
				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = row.getCell(i++).getStringCellValue();
				String description = row.getCell(i++).getStringCellValue();
				
				// We will drop empty rows
				if((name == null || name.equals(""))
						&& (description == null || description.equals(""))) {
					continue;
				}

				// Let's create the OpsdRole object:
				OpsdRole role = new OpsdRole(name, description);
				cachedRoles.add(role);
				System.out.println("Role added: " + role);

			}
		}
		return cachedRoles;
	}

	@Override
	public List<OpsdSystem> getSystems(OpsdProject project)
			throws OpsdException {
		
		System.out.println("OpsdPoiDao.getSystems");
		List<OpsdSystem> systems = new ArrayList<OpsdSystem>();

		//Get the number of sheets in the xlsx file
		int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
		int sheetPosition = OpsdPoiConf.getSheetPosition("OpsdSystem");
		if(sheetPosition > numberOfSheets - 1) {
			throw new OpsdException("The sheet " + path + " has "
					+ numberOfSheets + " and OpsdSystem objects should be"
					+ " in sheet position # " + sheetPosition
					+ " (0..N-1)");
		}

		//Get the nth sheet from the workbook
		Sheet sheet = workbook.getSheetAt(sheetPosition);
		int firstRow = OpsdPoiConf.getFirstRow(); // cachable
		
		for(int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
			
			// Get the row object
			Row row = sheet.getRow(rowNum);

			int i = 0;
			String name = row.getCell(i++).getStringCellValue();
			// We'll drop rows without name
			if(name == null || name.equals("")) continue;
			String alias = row.getCell(i++).getStringCellValue();
			String fqdnOrIp = row.getCell(i++).getStringCellValue();
			OpsdDeviceType dt = null;
			String deviceTypeStr = row.getCell(i++).getStringCellValue();
			String osStr = row.getCell(i++).getStringCellValue();
			OpsdOSType ost = null;
			String osAccess = row.getCell(i++).getStringCellValue();
			String lomIP = row.getCell(i++).getStringCellValue();
			String lomAccess = row.getCell(i++).getStringCellValue();
			String moreInfo = row.getCell(i++).getStringCellValue();
			String environment = row.getCell(i++).getStringCellValue();
			String roleName = row.getCell(i++).getStringCellValue();
			OpsdRole role = null;
			String hostDownRecoveryProcedure = row.getCell(i++).getStringCellValue();
			String responsibleName = row.getCell(i++).getStringCellValue();
			OpsdResponsible responsible = null;
			String scaleTo = row.getCell(i++).getStringCellValue();
			
			dt   = getDeviceTypeByName(deviceTypeStr);
			ost  = getOSTypeByName(osStr);
			role = getRoleByName(project, roleName);
			responsible = getResponsible(responsibleName);
			
			System.out.println("* system: " + name + " alias = " + alias);
			// Let's create the OpsdSystem object:
			OpsdSystem system = new OpsdSystem(name, alias, fqdnOrIp, dt, ost, osAccess, lomIP, lomAccess, moreInfo, environment, role, hostDownRecoveryProcedure, responsible, scaleTo);
			systems.add(system);
			System.out.println("* System added: " + system);

		}
		return systems;
	}

	@Override
	public OpsdDeviceType getDeviceTypeByName(String devTypeName)
			throws OpsdException {
		OpsdDeviceType odt;
		String desc;
		boolean isVirt;
		if(staticDeviceTypeDesc.containsKey(devTypeName)) {
			desc = staticDeviceTypeDesc.get(devTypeName);
			if(staticDeviceTypeIsVirt.containsKey(devTypeName)) {
				isVirt = staticDeviceTypeIsVirt.get(devTypeName);
				odt = new OpsdDeviceType(devTypeName, desc, isVirt);
				return odt;
			}
		}
		return null;
	}

	@Override
	public OpsdOSType getOSTypeByName(String osNameStr) throws OpsdException {
		OpsdOSType oot;
		String desc;
		if(staticOSTypeDesc.containsKey(osNameStr)) {
			desc = staticOSTypeDesc.get(osNameStr);
				oot = new OpsdOSType(osNameStr, desc);
				return oot;
		}
		return null;
	}

	@Override
	public OpsdRole getRoleByName(OpsdProject project, String roleName) throws OpsdException {
		for(OpsdRole aRole: getRoles(project)) {
			if(aRole.getName().equals(roleName)) {
				return aRole;
			}
		}
		return null;
	}
}
