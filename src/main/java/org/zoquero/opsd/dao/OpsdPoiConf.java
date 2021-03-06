package org.zoquero.opsd.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;

import org.zoquero.opsd.OpsdException;

/**
 * Static configuration for accessing Opsd data through POI
 * @author agalindo
 *
 */
public class OpsdPoiConf {
	private static final Map<String, Integer> classnameToSheetPositionMap;
	private static final Map<String, String> classnameToSheetNameMap;
	private final static String CONF_PROPERTIES
			= "org.zoquero.opsd.dao.OpsdPoiConf";
	static {
		/* Initialize the classnameToSheetPositionMap static map */
		classnameToSheetPositionMap = new HashMap<String, Integer>();
		classnameToSheetNameMap     = new HashMap<String, String>();

		ResourceBundle rb = ResourceBundle.getBundle(CONF_PROPERTIES);
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.startsWith("sp.")) {
				String theClassName = key.substring(3);
				String value = rb.getString(key);
				classnameToSheetPositionMap.put(theClassName, Integer.parseInt(value));
			}
			else if(key.startsWith("sn.")) {
				String theClassName = key.substring(3);
				String value = rb.getString(key);
				classnameToSheetNameMap.put(theClassName, value);
			}
		}
		if(classnameToSheetPositionMap.size() < 9) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ classnameToSheetPositionMap.size() + " sheet positions "
					+ "reading 'sp.' fields from "
					+ CONF_PROPERTIES + ".properties");
		}
		if(classnameToSheetNameMap.size() < 9) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ classnameToSheetPositionMap.size() + " sheet positions "
					+ "reading 'sn.' fields from "
					+ CONF_PROPERTIES + ".properties");
		}
		
	}
	
	/** The sheets are accessed by position. There can be more sheets,
	 * because them are accessed by position number. */
	static public int getMinimumNumberOfSpreedsheetSheetsNeeded() {
		return classnameToSheetPositionMap.size();
	}

	/**
	 * Gets the position of the sheet that contains certain objects
	 * @param className
	 * @return
	 */
	public static int getSheetPosition(String className) throws OpsdException {
		Integer i = classnameToSheetPositionMap.get(className);
		if(i == null || i < 0)
			throw new OpsdException("Wrong configuration: "
					+ "Can't get sheet position for " + className
					+ " in classnameToSheetPositionMap.");
		return i.intValue();
	}
	
	/**
	 * Gets the name of the sheet that contains certain objects
	 * @param className
	 * @return
	 */
	public static String getSheetName(String className) throws OpsdException {
		String s = classnameToSheetNameMap.get(className);
		if(s == null || s.equals(""))
			throw new OpsdException("Wrong configuration: "
					+ "Can't get sheet name for " + className
					+ " in classnameToSheetNameMap.");
		return s;
	}

	/**
	 * Get the first row number for all sheets (0..N-1)
	 * @return
	 */
	public static int getFirstRow() {
		ResourceBundle rb
			= ResourceBundle.getBundle(CONF_PROPERTIES);
		int i = Integer.parseInt(rb.getString("firstRow"));
		if(i < 0) {
			throw new RuntimeException(
				"Wrong value (negative) looking for firstRow in bundle");
		}
		return i;
	}

	/**
	 * Get the name of the systems to represent the MonitoredHosts
	 * that have no fix Systems underlying it.
	 * @return
	 */
	public static String getSystemNameForFloatingMonitoredHosts() {
		ResourceBundle rb
			= ResourceBundle.getBundle(CONF_PROPERTIES);
		String s = rb.getString("systemNameForFloatingMonitoredHosts");
		if(s == null) {
			throw new RuntimeException(
				"Can't find systemNameForFloatingMonitoredHosts in "
						+ CONF_PROPERTIES + ".properties");
		}
		return s;
	}

}
