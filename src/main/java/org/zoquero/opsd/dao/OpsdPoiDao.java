package org.zoquero.opsd.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zoquero.opsd.OpsdException;
import org.zoquero.opsd.entities.OpsdCriticity;
import org.zoquero.opsd.entities.OpsdDeviceType;
import org.zoquero.opsd.entities.OpsdFilePolicy;
import org.zoquero.opsd.entities.OpsdFilePolicy.ACTION_TYPE;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdPeriodicTask;
import org.zoquero.opsd.entities.OpsdPollerType;
import org.zoquero.opsd.entities.OpsdRequest;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdHostService;
import org.zoquero.opsd.entities.OpsdOSType;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdResponsible;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdServiceMacroDefinition;
import org.zoquero.opsd.entities.OpsdServiceTemplate;
import org.zoquero.opsd.entities.OpsdSystem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Opsd Datatap implementation over Excel files using POI. The most expensive
 * operation is opening the filestream and creating the Workbook object, so it
 * will be cache, we'll do it just in "connect()"
 * 
 * @author agalindo
 * 
 */
public class OpsdPoiDao implements OpsdDataTap {

	private static Logger LOGGER = Logger.getLogger(OpsdPoiDao.class.getName());

	/** Path to the Excel file that cointains Opsd data */
	private String path;
	/** POI Workbook instance for xlsx/xls file input stream */
	private Workbook workbook = null;
	/** Input stream from the xlsx/xls file */
	private FileInputStream fis = null;
	/** Lazy initialized list of valid responsibles */
	private List<OpsdResponsible> responsibles = null;
	/** Lazy initialized list of valid criticies */
	private List<OpsdCriticity> criticities = null;

	/**
	 * Static map to read device types from the properties. Such a field should
	 * not be set in the Excel file. name => description
	 */
	private static final Map<String, String> staticDeviceTypeDesc;
	/**
	 * Static map to read if 'OS Type is virt' from the properties. Such a field
	 * should not be set in the Excel file. 0==phis, 1==virt . name => 0|1
	 */
	private static final Map<String, Boolean> staticDeviceTypeIsVirt;
	/**
	 * Static map to read 'OS Type description' from the properties. Such a
	 * field should not be set in the Excel file. name => description
	 */
	private static final Map<String, String> staticOSTypeDesc;

	private List<OpsdRole> cachedRoles = null;
	private List<OpsdSystem> cachedSystems = null;
	private List<OpsdRoleService> cachedRoleServices = null;
	private List<OpsdHostService> cachedHostServices = null;
	private List<OpsdServiceTemplate> cachedServiceTemplates = null;
	private List<OpsdRequest> cachedRequests = null;
	private List<String> cachedEnvironments = null;
	private List<OpsdPeriodicTask> cachedPeriodicTasks = null;
	private List<OpsdFilePolicy> cachedFilePolicies = null;
	private List<OpsdPollerType> cachedPollerTypes = null;

	static public OpsdSystem FLOATING_HOST = null;

	static {
		if (FLOATING_HOST == null) {
			String FLOATING_HOST_NAME = OpsdPoiConf
					.getSystemNameForFloatingMonitoredHosts();
			FLOATING_HOST = new OpsdSystem(FLOATING_HOST_NAME, null, null,
					null, null, null, null, null, null, null, null, null, null,
					null);

		}

		/* Initialize the static maps */
		staticDeviceTypeDesc = new HashMap<String, String>();
		staticDeviceTypeIsVirt = new HashMap<String, Boolean>();
		staticOSTypeDesc = new HashMap<String, String>();

		String devTypeDescPrefix = "deviceTypeDesc.";
		String devTypeIsVirtPrefix = "deviceTypeIsVirt.";
		String osTypeDescPrefix = "osTypeDesc.";

		String opsdPoiConfProperties = "org.zoquero.opsd.dao.OpsdPoiConf";
		ResourceBundle rb = ResourceBundle.getBundle(opsdPoiConfProperties);
		Enumeration<String> keys = rb.getKeys();

		// deviceTypes
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.startsWith(devTypeDescPrefix)) {
				String devName = key.substring(devTypeDescPrefix.length());
				String value = rb.getString(key).trim();
				staticDeviceTypeDesc.put(devName, value);
			} else if (key.startsWith(devTypeIsVirtPrefix)) {
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
			} else if (key.startsWith(osTypeDescPrefix)) {
				String osName = key.substring(osTypeDescPrefix.length());
				String value = rb.getString(key).trim();
				staticOSTypeDesc.put(osName, value);
			}

		}
		// sanity checks
		if (staticDeviceTypeDesc.size() < 3) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ staticDeviceTypeDesc.size() + " device types "
					+ "reading '" + devTypeDescPrefix + "' fields from "
					+ opsdPoiConfProperties + " properties");
		}
		if (staticDeviceTypeIsVirt.size() < 3) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ staticDeviceTypeIsVirt.size() + " osTypeIsVirt "
					+ "reading '" + devTypeIsVirtPrefix + "' fields from "
					+ opsdPoiConfProperties + " properties");
		}
		if (staticOSTypeDesc.size() < 3) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ staticOSTypeDesc.size() + " osTypeDesc " + "reading '"
					+ osTypeDescPrefix + "' fields from "
					+ opsdPoiConfProperties + " properties");
		}
	}

	public OpsdPoiDao(String path) {
		this.path = path;
		LOGGER.log(Level.INFO, "OpsdPoiDao created for file '" + path + "'");
	}

	@Override
	public OpsdProject getProject(String projectName) throws OpsdException {
		LOGGER.log(Level.FINE, this.getClass().getName() + ": Getting project "
				+ projectName);
		DataFormatter formatter = new DataFormatter();

		// Get the number of sheets in the xlsx file
		int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
		int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdProject.class.getSimpleName());
		if (sheetPosition > numberOfSheets - 1) {
			throw new OpsdException("The sheet " + path + " has "
					+ numberOfSheets + " and OpsdProject objects should be"
					+ " in sheet position # " + sheetPosition + " (0..N-1)");
		}

		// Get the nth sheet from the workbook
		Sheet sheet = workbook.getSheetAt(sheetPosition);
		int firstRow = OpsdPoiConf.getFirstRow(); // cachable
		LOGGER.log(Level.FINEST, "Opening the sheet '" + sheet.getSheetName()
				+ "' (position #" + sheetPosition + ")" + " row #" + firstRow);

		Row row = sheet.getRow(firstRow);
		if (row == null) {
			throw new OpsdException("Can't find Project data in row #"
					+ firstRow);
		}

		// // Get the column iterator and iterate over it
		// Iterator<Cell> cellIterator = row.cellIterator();
		// while (cellIterator.hasNext()) {
		// // Get the Cell object
		// Cell cell = cellIterator.next();
		//
		// // Check the cell type and process accordingly
		// // getStringCellValue , getNumericCellValue , ...
		// String s = cell.getStringCellValue();
		// System.out.println("  * cel·la = " + s);
		// }

		int i = 1;
		String name = formatter.formatCellValue(row.getCell(i++));
		String description = formatter.formatCellValue(row.getCell(i++));
		String dateInStr = formatter.formatCellValue(row.getCell(i++));
		String dateOutStr = formatter.formatCellValue(row.getCell(i++));
		String responsibleName = formatter.formatCellValue(row.getCell(i++));
		String dependencies = formatter.formatCellValue(row.getCell(i++));
		String recoveryProcedure = formatter.formatCellValue(row.getCell(i++));
		String moreInfo = formatter.formatCellValue(row.getCell(i++));

		// Let's set dates
		Calendar dateIn = Calendar.getInstance();
		Calendar dateOut = Calendar.getInstance();
		// Dates are in dd.MM.yyyy format
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try {
			dateIn.setTime(sdf.parse(dateInStr));
		} catch (ParseException e) {
			throw new OpsdException("Can't parse the initial date '"
					+ dateInStr + "' from the project data");
		}
		if (dateOutStr.equals("Encara actiu") || dateOutStr.equals("")) {
			dateOut = null;
		} else {
			try {
				dateOut.setTime(sdf.parse(dateOutStr));
			} catch (ParseException e) {
				throw new OpsdException("Can't parse the end date '"
						+ dateOutStr + "' from the project data");
			}
		}

		OpsdResponsible responsible = getResponsible(responsibleName);
		// Let's create the OpsdProject object:
		OpsdProject op = new OpsdProject(name, description, responsible,
				dateIn, dateOut, dependencies, recoveryProcedure, moreInfo);

		// Iterator<Row> rowIterator = sheet.iterator();
		// while (rowIterator.hasNext()) {
		// System.out.println("\n== Nova fila ==");
		//
		// // Get the row object
		// Row row = rowIterator.next();
		//
		// // Get the column iterator and iterate over it
		// Iterator<Cell> cellIterator = row.cellIterator();
		// while (cellIterator.hasNext()) {
		// // Get the Cell object
		// Cell cell = cellIterator.next();
		//
		// // Check the cell type and process accordingly
		// // getStringCellValue , getNumericCellValue , ...
		// String s = cell.getStringCellValue();
		// System.out.println("  * cel·la = " + s);
		// }
		//
		// }

		return op;
	}

	/**
	 * Get the Responsible object from his name. On a future release all data
	 * will be on a Relational Database. By now, the allowed project
	 * responsibles must be described here, not in the Excel file.
	 * 
	 * @param responsibleName
	 * @return
	 */
	private OpsdResponsible getResponsible(String responsibleName) {
		if (responsibles == null) {
			/* Lazy initialization of the array, read from properties file */
			responsibles = new ArrayList<OpsdResponsible>();

			ResourceBundle rb = ResourceBundle
					.getBundle("org.zoquero.opsd.entities.Responsible");
			Enumeration<String> keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.startsWith("responsible.")) {
					String value = rb.getString(key);
					StringTokenizer st = new StringTokenizer(value, ";");
					String name = (String) st.nextElement();
					String email = (String) st.nextElement();
					String department = (String) st.nextElement();
					String resourceAcl = (String) st.nextElement();
					String moreInfo = (String) st.nextElement();
					if (name == null || email == null || department == null
							|| resourceAcl == null || moreInfo == null) {
						return null;
					}
					OpsdResponsible or = new OpsdResponsible(name, email,
							department, resourceAcl, moreInfo);
					responsibles.add(or);
				}
			}
			for (OpsdResponsible aResponsible : responsibles) {
				if (aResponsible.getName().equals(responsibleName)) {
					return aResponsible;
				}
			}
		}
		return null;
	}

	/**
	 * Get all the Criticity objects. On a future release all data
	 * will be on a Relational Database. By now, the allowed
	 * criticities must be described here, not in the Excel file.
	 * 
	 * @return
	 * @throws OpsdException 
	 */
	private List<OpsdCriticity> getCriticities() throws OpsdException {
		if (criticities == null) {
			LOGGER.log(Level.FINER, "getCriticities: Loading lazily the criticities");
			/* Lazy initialization of the array, read from properties file */
			criticities = new ArrayList<OpsdCriticity>();

			ResourceBundle rb = ResourceBundle
					.getBundle("org.zoquero.opsd.entities.Criticity");
			Enumeration<String> keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.startsWith("criticity.")) {
					String value = rb.getString(key);
					StringTokenizer st = new StringTokenizer(value, ";");
					String premiumStr  = (String) st.nextElement();
					String name        = (String) st.nextElement();
					String description = (String) st.nextElement();
					if (premiumStr == null || name == null || description == null) {
						LOGGER.log(Level.SEVERE, "Can't read the criticity with value '" + value + "'");
						throw new OpsdException(
								"Can't read the criticity with value '"
									+ value + "'");
					}
					boolean premium;
					if(premiumStr.equals("0")) {
						premium = false;
					}
					else if(premiumStr.equals("1")) {
						premium = true;
					}
					else {
						LOGGER.log(Level.SEVERE, "Can't read the criticity with value '" + value + "'");
						throw new OpsdException(
								"Can't read the criticity with value '"
										+ value + "'");
					}
					OpsdCriticity aCriticity = new OpsdCriticity(name, description, premium);
					criticities.add(aCriticity);
				}
			}
		}
		return criticities;
	}


	@Override
	public List<OpsdServiceTemplate> getServiceTemplates() throws OpsdException {
		if (cachedServiceTemplates == null) {
			/* Lazy initialization of the array, read from properties file */
			cachedServiceTemplates = new ArrayList<OpsdServiceTemplate>();

			ResourceBundle rb = ResourceBundle
					.getBundle("org.zoquero.opsd.entities.ServiceTemplate");
			Enumeration<String> keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.startsWith("serviceTemplate.")) {
					String value = rb.getString(key);
					String[] parts = value.split(";", -1);
					if(parts.length < 11) {
						throw new OpsdException(
								"Can't parse the ServiceTemplate with value '"
								+ value + "', few many fields ("
								+ parts.length + ")");
					}
					String name = parts[0];
					String nrpeStr = parts[1];
					boolean nrpe;
					if(nrpeStr != null && nrpeStr.equals("0")) {
						nrpe = false;
					}
					else if(nrpeStr != null && nrpeStr.equals("1")) {
						nrpe = true;
					}
					else {
						throw new OpsdException(
								"Can't parse the ServiceTemplate with value '"
										+ value + "'");
					}
					String defaultName = parts[2];
					String description = parts[3];
					String macroName, macroDescription, macroDefaultValue;
					List<OpsdServiceMacroDefinition> osmdList
						= new ArrayList<OpsdServiceMacroDefinition>();
					for (int i = 0; i < 7; i++) {
						macroName = parts[4+3*i];
						macroDescription = parts[5+3*i];
						macroDefaultValue = parts[6+3*i];
						OpsdServiceMacroDefinition aServiceTemplate =
							new OpsdServiceMacroDefinition(macroName,
									macroDescription, macroDefaultValue);
						osmdList.add(aServiceTemplate);
					}
					if (name == null || defaultName == null 
							|| description == null || osmdList == null) {
						throw new OpsdException(
								"Can't read the ServiceTemplate with value '"
									+ value + "'");
					}
					OpsdServiceTemplate aServiceTemplate = new OpsdServiceTemplate(name, nrpe, defaultName, description, osmdList);
					cachedServiceTemplates.add(aServiceTemplate);
				}
			}
		}
		return cachedServiceTemplates;
	}

	@Override
	public OpsdServiceTemplate getServiceTemplate(String serviceTemplateName) throws OpsdException {
		for(OpsdServiceTemplate aServiceTemplate: getServiceTemplates()) {
			if(aServiceTemplate.getName().equals(serviceTemplateName)) {
				return aServiceTemplate;
			}
		}
		return null;
	}


	/**
	 * Get the Criticity object from his name. On a future release all data
	 * will be on a Relational Database. By now, the allowed
	 * criticities must be described here, not in the Excel file.
	 * 
	 * @param criticityName
	 * @throws OpsdException
	 * @return
	 */
	private OpsdCriticity getCriticity(String criticityName) throws OpsdException {
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getCriticity: looking for " + criticityName);
		for (OpsdCriticity aCriticity: getCriticities()) {
			if (aCriticity.getName().equals(criticityName)) {
				LOGGER.log(Level.FINEST, "OpsdPoiDao.getCriticity: "
						+ criticityName + " found");
				return aCriticity;
			}
		}
		LOGGER.log(Level.WARNING, "OpsdPoiDao.getCriticity: "
				+ "couldn't get criticity object for " + criticityName);
		return null;
	}


	@Override
	public void connect() throws OpsdException {
		LOGGER.log(Level.INFO, "OpsdPoiDao.connect");
		try {
			// Create the input stream from the xlsx/xls file
			fis = new FileInputStream(path);

			// Create Workbook instance for xlsx/xls file input stream
			workbook = null;
			if (path.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if (path.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			} else {
				throw new OpsdException("The file " + path
						+ " doesn't look like an Excel file");
			}

		} catch (IOException e) {
			throw new OpsdException("Can't open the file " + path, e);
		}

	}

	@Override
	public void disconnect() throws OpsdException {
		LOGGER.log(Level.INFO, "OpsdPoiDao.disconnect");

		// Close file input stream
		try {
			fis.close();
		} catch (IOException e) {
			throw new OpsdException("Errors closing the file: "
					+ e.getMessage(), e);
		}

	}

	@Override
	public List<OpsdRole> getRoles(OpsdProject project) throws OpsdException {
		LOGGER.log(Level.FINE, "OpsdPoiDao.getRoles");
		DataFormatter formatter = new DataFormatter();

		if (cachedRoles == null) {
			cachedRoles = new ArrayList<OpsdRole>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdRole.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdRole objects should be"
						+ " in sheet position # " + sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = formatter.formatCellValue(row.getCell(i++));
				String description = formatter.formatCellValue(row.getCell(i++));

				// We will drop empty rows
				if ((name == null || name.equals(""))
						&& (description == null || description.equals(""))) {
					continue;
				}

				// Let's create the OpsdRole object:
				OpsdRole role = new OpsdRole(name, description);
				cachedRoles.add(role);
				LOGGER.log(Level.FINEST, "Role added: " + role);

			}
		}
		return cachedRoles;
	}

	@Override
	public List<OpsdSystem> getSystems(OpsdProject project)
			throws OpsdException {
		LOGGER.log(Level.FINE, "OpsdPoiDao.getSystems");
		DataFormatter formatter = new DataFormatter();
		if (cachedSystems == null) {
			cachedSystems = new ArrayList<OpsdSystem>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdSystem.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdSystem objects should be"
						+ " in sheet position # " + sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = formatter.formatCellValue(row.getCell(i++));
				// We'll drop rows without name
				if (name == null || name.equals(""))
					continue;
				String alias = formatter.formatCellValue(row.getCell(i++));
				String fqdnOrIp = formatter.formatCellValue(row.getCell(i++));
				String deviceTypeStr = formatter.formatCellValue(row.getCell(i++));
				String osStr = formatter.formatCellValue(row.getCell(i++));
				String osAccess = formatter.formatCellValue(row.getCell(i++));
				String lomIP = formatter.formatCellValue(row.getCell(i++));
				String lomAccess = formatter.formatCellValue(row.getCell(i++));
				String moreInfo = formatter.formatCellValue(row.getCell(i++));
				String environment = formatter.formatCellValue(row.getCell(i++));
				String roleName = formatter.formatCellValue(row.getCell(i++));
				String hostDownRecoveryProcedure = row.getCell(i++)
						.getStringCellValue();
				String responsibleName = formatter.formatCellValue(row.getCell(i++));
				String scaleTo = formatter.formatCellValue(row.getCell(i++));

				OpsdDeviceType dt = getDeviceTypeByName(deviceTypeStr);
				OpsdOSType ost = getOSTypeByName(osStr);
				OpsdRole role = getRoleByName(project, roleName);
				OpsdResponsible responsible = getResponsible(responsibleName);

				// Let's create the OpsdSystem object:
				OpsdSystem system = new OpsdSystem(name, alias, fqdnOrIp, dt,
						ost, osAccess, lomIP, lomAccess, moreInfo, environment,
						role, hostDownRecoveryProcedure, responsible, scaleTo);
				cachedSystems.add(system);
				LOGGER.log(Level.FINEST, "* System added: " + system);

			}
		}
		return cachedSystems;
	}

	@Override
	public OpsdDeviceType getDeviceTypeByName(String devTypeName)
			throws OpsdException {
		OpsdDeviceType odt;
		String desc;
		boolean isVirt;
		if (staticDeviceTypeDesc.containsKey(devTypeName)) {
			desc = staticDeviceTypeDesc.get(devTypeName);
			if (staticDeviceTypeIsVirt.containsKey(devTypeName)) {
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
		if (staticOSTypeDesc.containsKey(osNameStr)) {
			desc = staticOSTypeDesc.get(osNameStr);
			oot = new OpsdOSType(osNameStr, desc);
			return oot;
		}
		return null;
	}

	@Override
	public OpsdRole getRoleByName(OpsdProject project, String roleName)
			throws OpsdException {
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getRoleByName loading role "
			+ roleName);
		for (OpsdRole aRole : getRoles(project)) {
			if (aRole.getName().equals(roleName)) {
				LOGGER.log(Level.FINEST, "OpsdPoiDao.getRoleByName found role "
					+ roleName);
				return aRole;
			}
		}
		LOGGER.log(Level.WARNING, "OpsdPoiDao.getRoleByName hasn't found the role "
				+ roleName);
		return null;
	}
	
	@Override
	public OpsdMonitoredHost getHostByName(OpsdProject project, String hostName)
			throws OpsdException {
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getHostByName loading host "
			+ hostName);
		for (OpsdMonitoredHost aHost : getMonitoredHosts(project)) {
			if (aHost.getName().equals(hostName)) {
				LOGGER.log(Level.FINEST, "OpsdPoiDao.getHostByName found host "
					+ hostName);
				return aHost;
			}
		}
		LOGGER.log(Level.WARNING, "OpsdPoiDao.getHostByName hasn't found the host "
				+ hostName);
		return null;
	}

	@Override
	public List<OpsdMonitoredHost> getMonitoredHosts(OpsdProject project)
			throws OpsdException {
		DataFormatter formatter = new DataFormatter();
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getOpsdMonitoredHosts");
		List<OpsdMonitoredHost> monitoredHosts = new ArrayList<OpsdMonitoredHost>();

		// Get the number of sheets in the xlsx file
		int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
		int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdMonitoredHost.class.getSimpleName());
		if (sheetPosition > numberOfSheets - 1) {
			throw new OpsdException("The sheet " + path + " has "
					+ numberOfSheets
					+ " and OpsdMonitoredHost objects should be"
					+ " in sheet position # " + sheetPosition + " (0..N-1)");
		}

		// Get the nth sheet from the workbook
		Sheet sheet = workbook.getSheetAt(sheetPosition);
		int firstRow = OpsdPoiConf.getFirstRow(); // cachable

		for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

			// Get the row object
			Row row = sheet.getRow(rowNum);

			int i = 0;
			// String fields:
			String name = formatter.formatCellValue(row.getCell(i++));
			// We'll drop rows without name
			if (name == null || name.equals(""))
				continue;
			String ip = formatter.formatCellValue(row.getCell(i++));
			String systemStr = formatter.formatCellValue(row.getCell(i++));
			String forManagingStr = formatter.formatCellValue(row.getCell(i++));
			String forServiceStr = formatter.formatCellValue(row.getCell(i++));
			String forBackupStr = formatter.formatCellValue(row.getCell(i++));
			String forNasStr = formatter.formatCellValue(row.getCell(i++));
			String defaultChecksNeededStr = formatter.formatCellValue(row
					.getCell(i++));
			String moreInfo = formatter.formatCellValue(row.getCell(i++));
			String environment = formatter.formatCellValue(row.getCell(i++));
			String roleStr = formatter.formatCellValue(row.getCell(i++));
			String scaleTo = formatter.formatCellValue(row.getCell(i++));

			OpsdSystem system;
			Boolean forManaging = null;
			Boolean forService = null;
			Boolean forBackup = null;
			Boolean forNas = null;
			Boolean defaultChecksNeeded = null;
			OpsdRole role;

			// Non-String fields:
			if (systemStr.equals(FLOATING_HOST.getName())) {
				system = FLOATING_HOST;
			} else {
				system = getSystemByName(project, systemStr);
			}
			if (forManagingStr == null)
				forManaging = null;
			else if (forManagingStr.equals("0"))
				forManaging = new Boolean(false);
			else if (forManagingStr.equals("1"))
				forManaging = new Boolean(true);
			else
				forManaging = null;
			if (forServiceStr == null)
				forService = null;
			else if (forServiceStr.equals("0"))
				forService = new Boolean(false);
			else if (forServiceStr.equals("1"))
				forService = new Boolean(true);
			else
				forService = null;
			if (forBackupStr == null)
				forBackup = null;
			else if (forBackupStr.equals("0"))
				forBackup = new Boolean(false);
			else if (forBackupStr.equals("1"))
				forBackup = new Boolean(true);
			else
				forBackup = null;
			if (forNasStr == null)
				forNas = null;
			else if (forNasStr.equals("0"))
				forNas = new Boolean(false);
			else if (forNasStr.equals("1"))
				forNas = new Boolean(true);
			else
				forNas = null;
			if (defaultChecksNeededStr == null)
				defaultChecksNeeded = null;
			else if (defaultChecksNeededStr.equals("0"))
				defaultChecksNeeded = new Boolean(false);
			else if (defaultChecksNeededStr.equals("1"))
				defaultChecksNeeded = new Boolean(true);
			else
				defaultChecksNeeded = null;
			role = getRoleByName(project, roleStr);

			// Let's create the OpsdSystem object:
			OpsdMonitoredHost monitoredHost = new OpsdMonitoredHost(name, ip,
					system, forManaging, forService, forBackup, forNas,
					defaultChecksNeeded, moreInfo, environment, role, scaleTo);
			monitoredHosts.add(monitoredHost);
		}
		return monitoredHosts;
	}

	@Override
	public OpsdSystem getSystemByName(OpsdProject project, String systemName)
			throws OpsdException {
		List<OpsdSystem> systems = getSystems(project);
		for (OpsdSystem aSystem : systems) {
			if (aSystem != null && aSystem.getName() != null
					&& aSystem.getName().equals(systemName)) {
				return aSystem;
			}
		}
		// not found
		return null;
	}

	@Override
	public List<OpsdRoleService> getRoleServices(
			OpsdProject project) throws OpsdException {
		DataFormatter formatter = new DataFormatter();		
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getRoles");
		if (cachedRoleServices == null) {
			cachedRoleServices =
					new ArrayList<OpsdRoleService>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdRoleService.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets
						+ " and OpsdRoleService objects should be"
						+ " in sheet position # "
						+ sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
				
				/* Nom proposat pel servei monitorat (opcional)
				 * Descripció del servei monitorat
				 * Procediment
				 * Criticitat
				 * Nom de Rol monitorat
				 * Service Template per crear el Service al monitoratge
				 * Macro1=Value1
				 * Macro2=Value2
				 * ...
				 * Macro7=Value7
				 * A qui escalar
				 */

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = null;
				String proposedName = formatter.formatCellValue(row.getCell(i++));
				String description = formatter.formatCellValue(row.getCell(i++));
				String procedure = formatter.formatCellValue(row.getCell(i++));
				String criticityName = formatter.formatCellValue(row.getCell(i++));
				OpsdCriticity criticity = null;
				String roleName = formatter.formatCellValue(row.getCell(i++));
				OpsdRole role = null;
				String serviceTemplateName = formatter.formatCellValue(row.getCell(i++));
				OpsdServiceTemplate serviceTemplate = null;
				String[] macroAndValueArray = new String[7];
				macroAndValueArray[0] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[1] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[2] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[3] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[4] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[5] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[6] = formatter.formatCellValue(row.getCell(i++));
				String scaleTo = formatter.formatCellValue(row.getCell(i++));
				
				if((name == null || name.equals(""))
						&& (serviceTemplateName == null
						||  serviceTemplateName.equals(""))) {
					// It must be an empty row
					continue;
				}
				
				if(serviceTemplateName == null || serviceTemplateName.equals("")) {
					// it will appear as an error in validation
					LOGGER.log(Level.SEVERE, "OpsdPoiDao.getRoleServices: "
							+ "Can't find the "
							+ "service template serviceTemplate called "
							+ serviceTemplateName
							+ " when loading the RoleService #" + rowNum);
					serviceTemplate = null;
				}
				else {
					serviceTemplate = getServiceTemplate(serviceTemplateName);
				}
				if (proposedName == null || proposedName.equals("")) {
					// it will appear as an error in validation
					if(serviceTemplate == null) {
						name = null;
					}
					else {
						name = serviceTemplate.getDefaultName();
					}
				}
				else {
					name = proposedName;
				}
				if(criticityName == null || criticityName.equals("")) {
					// it will appear as an error in validation
					criticity = null;
				}
				else {
					criticity = getCriticity(criticityName);
				}
				if(roleName == null || roleName.equals("")) {
					// it will appear as an error in validation
					role = null;
				}
				else {
					role = getRoleByName(project, roleName);
				}
				if(role == null) {
					LOGGER.log(Level.SEVERE, "OpsdPoiDao.getRoleServices: "
						+ "the RoleService #" + rowNum + " has null role");
				}
				
				serviceTemplate = getServiceTemplate(serviceTemplateName);

				// Let's create the OpsdSystem object:
				OpsdRoleService roleService = new OpsdRoleService(name,
						description, procedure, criticity, role,
						serviceTemplate, macroAndValueArray, scaleTo);
				cachedRoleServices.add(roleService);
				LOGGER.log(Level.FINEST, "RoleService added: " + roleService);
			}
		}
		return cachedRoleServices;
	}

	@Override
	public List<OpsdRoleService> getRoleServicesByRole(OpsdProject project,
			OpsdRole aRole) throws OpsdException {
		List<OpsdRoleService> roleServices = new ArrayList<OpsdRoleService>();
		LOGGER.log(Level.FINE, "OpsdPoiDao.getRoleServicesByRole for role "
				+ aRole.getName());
		if (cachedRoleServices == null) {
			cachedRoleServices = getRoleServices(project);
		}
		for(OpsdRoleService roleService: cachedRoleServices) {
			if(roleService.getRole() == null) {
				LOGGER.log(Level.SEVERE,
						"getRoleServicesByRole: got a roleService ("
						+ roleService.getName() + ") with null role");
			}
			else {
				LOGGER.log(Level.FINEST,
						"getRoleServicesByRole: comparing with roleService: "
						+ roleService.getName() + " that's for role "
						+ roleService.getRole().getName());
			}
			if(roleService.getRole() != null
				&& roleService.getRole().getName().equals(aRole.getName())) {
				roleServices.add(roleService);
				LOGGER.log(Level.FINEST, "getRoleServicesByRole: ADDED roleService " + roleService.getName());
			}
		}
		return roleServices;
	}
	
	@Override
	public List<OpsdHostService> getHostServices(
			OpsdProject project) throws OpsdException {
		DataFormatter formatter = new DataFormatter();		
		LOGGER.log(Level.FINEST, "OpsdPoiDao.getHosts");
		if (cachedHostServices == null) {
			cachedHostServices =
					new ArrayList<OpsdHostService>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdHostService.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets
						+ " and OpsdHostService objects should be"
						+ " in sheet position # "
						+ sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
				
				/* Nom proposat pel servei monitorat (opcional)
				 * Descripció del servei monitorat
				 * Procediment
				 * Criticitat
				 * Nom de Rol monitorat
				 * Service Template per crear el Service al monitoratge
				 * Macro1=Value1
				 * Macro2=Value2
				 * ...
				 * Macro7=Value7
				 * A qui escalar
				 */

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = null;
				String proposedName = formatter.formatCellValue(row.getCell(i++));
				String description = formatter.formatCellValue(row.getCell(i++));
				String procedure = formatter.formatCellValue(row.getCell(i++));
				String criticityName = formatter.formatCellValue(row.getCell(i++));
				OpsdCriticity criticity = null;
				String hostName = formatter.formatCellValue(row.getCell(i++));
				OpsdMonitoredHost host = null;
				String serviceTemplateName = formatter.formatCellValue(row.getCell(i++));
				OpsdServiceTemplate serviceTemplate = null;
				String[] macroAndValueArray = new String[7];
				macroAndValueArray[0] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[1] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[2] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[3] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[4] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[5] = formatter.formatCellValue(row.getCell(i++));
				macroAndValueArray[6] = formatter.formatCellValue(row.getCell(i++));
				String scaleTo = formatter.formatCellValue(row.getCell(i++));
				
				if((name == null || name.equals(""))
						&& (serviceTemplateName == null
						||  serviceTemplateName.equals(""))) {
					// It must be an empty row
					continue;
				}
				
				if(serviceTemplateName == null || serviceTemplateName.equals("")) {
					// it will appear as an error in validation
					LOGGER.log(Level.SEVERE, "OpsdPoiDao.getHostServices: "
							+ "Can't find the "
							+ "service template serviceTemplate called "
							+ serviceTemplateName
							+ " when loading the HostService #" + rowNum);
					serviceTemplate = null;
				}
				else {
					serviceTemplate = getServiceTemplate(serviceTemplateName);
				}
				if (proposedName == null || proposedName.equals("")) {
					// it will appear as an error in validation
					if(serviceTemplate == null) {
						name = null;
					}
					else {
						name = serviceTemplate.getDefaultName();
					}
				}
				else {
					name = proposedName;
				}
				if(criticityName == null || criticityName.equals("")) {
					// it will appear as an error in validation
					criticity = null;
				}
				else {
					criticity = getCriticity(criticityName);
				}
				if(hostName == null || hostName.equals("")) {
					// it will appear as an error in validation
					host = null;
				}
				else {
					host = getHostByName(project, hostName);
				}
				if(host == null) {
					LOGGER.log(Level.SEVERE, "OpsdPoiDao.getHostServices: "
						+ "the HostService #" + rowNum
						+ " has null monitoredHost");
				}

				// Let's create the OpsdSystem object:
				OpsdHostService hostService = new OpsdHostService(name,
						description, procedure, criticity, host,
						serviceTemplate, macroAndValueArray, scaleTo);
				cachedHostServices.add(hostService);
				LOGGER.log(Level.FINEST, "HostService added: " + hostService);
			}
		}

		return cachedHostServices;
		
	}

	@Override
	public List<OpsdHostService> getHostServicesByHost(OpsdProject project,
			OpsdMonitoredHost aHost) throws OpsdException {
		List<OpsdHostService> hostServices = new ArrayList<OpsdHostService>();
		LOGGER.log(Level.FINE, "OpsdPoiDao.getHostServicesByHost "
				+ "for monitoredHost " + aHost.getName());
		if (cachedHostServices == null) {
			cachedHostServices = getHostServices(project);
		}
		for(OpsdHostService hostService: cachedHostServices) {
			if(hostService.getHost() == null) {
				LOGGER.log(Level.SEVERE,
						"getHostServicesByHost: got a hostService ("
						+ hostService.getName() + ") with null host");
			}
			else {
				LOGGER.log(Level.FINEST,
						"getHostServicesByHost: comparing with hostService: "
						+ hostService.getName() + " that's for host "
						+ hostService.getHost().getName());
			}
			if(hostService.getHost() != null
				&& hostService.getHost().getName().equals(aHost.getName())) {
				hostServices.add(hostService);
				LOGGER.log(Level.FINEST, "getHostServicesByHost: ADDED hostService " + hostService.getName());
			}
		}
		return hostServices;
	}

	@Override
	public List<OpsdRequest> getRequests(OpsdProject project) throws OpsdException{
		LOGGER.log(Level.FINE, "OpsdPoiDao.getRequests");
		DataFormatter formatter = new DataFormatter();
		if (cachedRequests == null) {
			cachedRequests = new ArrayList<OpsdRequest>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdRequest.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdRequest objects should be"
						+ " in sheet position # " + sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = formatter.formatCellValue(row.getCell(i++));
				// We'll drop rows without name
				if (name == null || name.equals(""))
					continue;
				String authorized = formatter.formatCellValue(row.getCell(i++));
				String procedure = formatter.formatCellValue(row.getCell(i++));
				String scaleTo = formatter.formatCellValue(row.getCell(i++));

				// Let's create the OpsdRequest object:
				OpsdRequest request = new OpsdRequest(name, authorized, procedure, scaleTo);
				cachedRequests.add(request);
				LOGGER.log(Level.FINEST, "* Request added: " + request);
			}
		}
		return cachedRequests;
	}

	@Override
	public List<String> getEnvironments(OpsdProject project)
			throws OpsdException {
		if(cachedEnvironments == null) {
			cachedEnvironments = new ArrayList<String>(); 
			// push environments from Systems
			for(OpsdSystem system: getSystems(project)) {
				String env = system.getEnvironment();
				if(env != null && ! env.equals("")) {
					for(String aCachedEnv: cachedEnvironments) {
						if(env.equals(aCachedEnv)) {
							continue;
						}
					}
					cachedEnvironments.add(env);
				}
			}
			// push environments from MonitoredHosts
			for(OpsdMonitoredHost monitoredHost: getMonitoredHosts(project)) {
				String env = monitoredHost.getEnvironment();
				if(env != null && ! env.equals("")) {
					for(String aCachedEnv: cachedEnvironments) {
						if(env.equals(aCachedEnv)) {
							continue;
						}
					}
					cachedEnvironments.add(env);
				}
			}
		}
		return cachedEnvironments;
	}

	@Override
	public List<OpsdPeriodicTask> getPeriodicTasks(OpsdProject project)
			throws OpsdException {
		LOGGER.log(Level.FINE, "OpsdPoiDao.getPeriodicTasks");
		DataFormatter formatter = new DataFormatter();
		if (cachedPeriodicTasks == null) {
			cachedPeriodicTasks = new ArrayList<OpsdPeriodicTask>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdPeriodicTask.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdRequest objects should be"
						+ " in sheet position # " + sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String name = formatter.formatCellValue(row.getCell(i++));
				// We'll drop rows without name
				if (name == null || name.equals(""))
					continue;
				String periodicity = formatter.formatCellValue(row.getCell(i++));
				String procedure = formatter.formatCellValue(row.getCell(i++));
				String scaleTo = formatter.formatCellValue(row.getCell(i++));

				// Let's create the OpsdRequest object:
				OpsdPeriodicTask periodicTask = new OpsdPeriodicTask(name, periodicity, procedure, scaleTo);
				cachedPeriodicTasks.add(periodicTask);
				LOGGER.log(Level.FINEST, "* PeriodicTask added: " + periodicTask);
			}
		}
		return cachedPeriodicTasks;
	}

	@Override
	public List<OpsdFilePolicy> getFilePolicies(OpsdProject project)
			throws OpsdException {
		LOGGER.log(Level.FINE, "OpsdPoiDao.getFilePolicies");
		DataFormatter formatter = new DataFormatter();
		if (cachedFilePolicies == null) {
			cachedFilePolicies = new ArrayList<OpsdFilePolicy>();

			// Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets(); // cachable ...
			int sheetPosition = OpsdPoiConf.getSheetPosition(OpsdFilePolicy.class.getSimpleName());
			if (sheetPosition > numberOfSheets - 1) {
				throw new OpsdException("The sheet " + path + " has "
						+ numberOfSheets + " and OpsdRequest objects should be"
						+ " in sheet position # " + sheetPosition + " (0..N-1)");
			}

			// Get the nth sheet from the workbook
			Sheet sheet = workbook.getSheetAt(sheetPosition);
			int firstRow = OpsdPoiConf.getFirstRow(); // cachable

			for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {

				// Get the row object
				Row row = sheet.getRow(rowNum);

				int i = 0;
				String systemStr = formatter.formatCellValue(row.getCell(i++));
				String roleStr = formatter.formatCellValue(row.getCell(i++));
				String baseFolder = formatter.formatCellValue(row.getCell(i++));
				String prefix = formatter.formatCellValue(row.getCell(i++));
				String sufix = formatter.formatCellValue(row.getCell(i++));
				String minDaysStr = formatter.formatCellValue(row.getCell(i++));
				String actionStr = formatter.formatCellValue(row.getCell(i++));
				
				OpsdSystem system = getSystemByName(project, systemStr);
				OpsdRole role = getRoleByName(project, roleStr);
				if(system == null && role == null)
					continue;
				
				Integer minDays;
				if (minDaysStr == null) {
					minDays = null;
				}
				else {
					try {
						minDays = Integer.parseInt(minDaysStr);
					} catch (Throwable t) {
						minDays = null;
					}
				}
				ACTION_TYPE action;
				if (actionStr == null) {
					action = null;
				}
				else if(actionStr.equals("rm")) {
					action = ACTION_TYPE.DELETE;
				}
				else if(actionStr.equals("gz")) {
					action = ACTION_TYPE.COMPRESS;
				}
				else {
					throw new OpsdException("Wrong action code (" + actionStr + ") for FilePolicy #" + rowNum);
				}
				// Let's create the OpsdFilePolicy object:
				OpsdFilePolicy FilePolicy = new OpsdFilePolicy(system, role, baseFolder, prefix, sufix, minDays, action);
				cachedFilePolicies.add(FilePolicy);
				LOGGER.log(Level.FINEST, "* FilePolicy added: " + FilePolicy);
			}
		}
		return cachedFilePolicies;
	}

	@Override
	public boolean hasPremiumServices(OpsdProject project, OpsdMonitoredHost aHost)
			throws OpsdException {
		if(aHost == null) {
			LOGGER.log(Level.WARNING, "OpsdPoiDao.hasPremiumServices got a empty host");
			return false;
		}
		// Let's look for Premium hostServices
		List<OpsdHostService> hostServices = getHostServicesByHost(project, aHost);
		for(OpsdHostService aService: hostServices) {
			if(aService.isPremium()) {
				LOGGER.log(Level.FINER, "OpsdPoiDao.hasPremiumServices: "
						+ aHost.getName() + " has at least a Premium service: "
						+ aService.getName());
				return true;
			}
		}
		// Let's look for Premium roleServices
		if(aHost.getRole() == null) {
			LOGGER.log(Level.WARNING, "OpsdPoiDao.hasPremiumServices got a host with empty role");
			return false;
		}
		List<OpsdRoleService> roleServices = getRoleServicesByRole(project, aHost.getRole());
		for(OpsdRoleService aService: roleServices) {
			if(aService.isPremium()) {
				LOGGER.log(Level.FINER, "OpsdPoiDao.hasPremiumServices: "
						+ aHost.getName() + " has at least a Premium service: "
						+ aService.getName());
				return true;
			}
		}
		LOGGER.log(Level.FINER, "OpsdPoiDao.hasPremiumServices: "
				+ aHost.getName() + " hasn't any Premium service, "
				+ "it's not a Premium Host");
		return false;
	}
	
	@Override
	public List<OpsdPollerType> getPollerTypes() throws OpsdException {
		if (cachedPollerTypes == null) {
			LOGGER.log(Level.FINER, "getPollerTypes: Loading lazily the cachedPollerTypes");
			/* Lazy initialization of the array, read from properties file */
			cachedPollerTypes = new ArrayList<OpsdPollerType>();

			ResourceBundle rb = ResourceBundle
					.getBundle("org.zoquero.opsd.entities.PollerType");
			Enumeration<String> keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				if (key.startsWith("pollerType.")) {
					String value = rb.getString(key);
					StringTokenizer st = new StringTokenizer(value, ";");
					int id = -1;
					String idStr       = (String) st.nextElement();
					String name        = (String) st.nextElement();
					String description = (String) st.nextElement();
					String networksStr = null;
					if(st.hasMoreElements()) {
						networksStr = (String) st.nextElement();						
					}
					
					try {
						id = Integer.parseInt(idStr);
					} catch (NumberFormatException e) {
						throw new OpsdException("Reading the Pollertype " + key
								+ " can't convert " + idStr + " to integer", e);
					}
					String[] parts = null;
					List<String> networks = null;
					if(networksStr != null) {
						parts = networksStr.split(",", -1);
						networks = Arrays.asList(parts);
					}

					OpsdPollerType aPollertype = new OpsdPollerType(id, name, description, networks);
					cachedPollerTypes.add(aPollertype);
				}
			}
			Collections.sort(cachedPollerTypes);
		}
		return cachedPollerTypes;
	}
	
	@Override
	public OpsdPollerType getPollerType(int id) throws OpsdException {
		for(OpsdPollerType pollerType: getPollerTypes()) {
			if(pollerType != null && pollerType.getId() == id) {
				return pollerType;
			}
		}
		return null;
	}
}
