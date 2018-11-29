package org.zoquero.opsd.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;

/**
 * Static configuration for accessing Opsd data through POI
 * 
 * @author agalindo
 * 
 */
public class OpsdConf {
	private final static String CONF_PROPERTIES = "org.zoquero.opsd.OpsdConf";

	/**
	 * Gets the minimum log level
	 * 
	 * @param className
	 * @return
	 */
	public static int getMinLogLevel() throws OpsdException {
		ResourceBundle rb = ResourceBundle.getBundle(CONF_PROPERTIES);
		String propertyName = "minLogLevel";
		int i = Integer.parseInt(rb.getString(propertyName));
		if (i < 0) {
			throw new RuntimeException("Wrong value (negative) looking for "
					+ propertyName + " in bundle");
		}
		return i;
	}

}
