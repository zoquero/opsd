package org.zoquero.opsd.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;

/**
 * Static configuration for accessing Opsd data through POI
 * @author agalindo
 *
 */
public class OpsdPoiConf {
	private static final Map<String, Integer> classnameToSheetPositionMap;
	static {
		/* Initialize the classnameToSheetPositionMap static map */
		classnameToSheetPositionMap = new HashMap<String, Integer>();

		ResourceBundle rb = ResourceBundle.getBundle("org.zoquero.opsd.dao.OpsdPoiConf");
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.startsWith("sp.")) {
				String theClassName = key.substring(3);
				String value = rb.getString(key);
				classnameToSheetPositionMap.put(theClassName, Integer.parseInt(value));
			}
		}
		if(classnameToSheetPositionMap.size() < 9) {
			throw new RuntimeException("OpsdPoiConf just could initialize "
					+ classnameToSheetPositionMap.size() + " sheet positions "
					+ "reading 'sp.' fields from "
					+ "org.zoquero.opsd.dao.OpsdPoiConf.properties");
		}
		
	}

	/**
	 * Gets the name of the sheet that contains certain objects
	 * @param className
	 * @return
	 */
	public static int getSheetPosition(String className) throws OpsdException {
		Integer i = classnameToSheetPositionMap.get(className);
		if(i == null)
			throw new OpsdException("Wrong configuration: "
					+ "Can't get sheet position for " + className
					+ " in classnameToSheetPositionMap.");
		return i.intValue();
	}

	/**
	 * Get the first row number for all sheets (0..N-1)
	 * @return
	 */
	public static int getFirstRow() {
		ResourceBundle rb
			= ResourceBundle.getBundle("org.zoquero.opsd.dao.OpsdPoiConf");
		int i = Integer.parseInt(rb.getString("firstRow"));
		if(i < 0) {
			throw new RuntimeException(
				"Wrong value (negative) looking for firstRow in bundle");
		}
		return i;
	}

}
