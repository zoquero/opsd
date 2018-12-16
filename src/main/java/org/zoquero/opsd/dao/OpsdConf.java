package org.zoquero.opsd.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;

import org.zoquero.opsd.OpsdException;

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
	
	/**
	 * Gets the URL base to construct the URL of an article.
	 * 
	 * @return
	 */
	public static String getWikiUrlBase() throws OpsdException {
		return getProperty("wiki.urlbase", false);
	}
	
	/**
	 * Gets a value from the conf properties file
	 * 
	 * @return
	 */
	public static String getProperty(String name) {
		return getProperty(name, false);
	}
	
	/**
	 * Gets a value from the conf properties file
	 * 
	 * @return
	 */
	public static String getProperty(String name, boolean allowNull) {
		ResourceBundle rb = ResourceBundle.getBundle(CONF_PROPERTIES);
		String s = rb.getString(name);
		if(s == null) {
			throw new RuntimeException("Can't fine " + name
				+ " in " + CONF_PROPERTIES + ".properties");
		}
		return s;
	}

	public static String getWikiTemplateName(String name) {
		String propertyName = "wiki.template." + name;		
		return getProperty(propertyName, false);
	}

	public static int getNumMacros() throws OpsdException {
		int numMacros;
		try {
			numMacros = Integer.parseInt(getProperty("monitoring.serviceTemplate.numMacros"));
		} catch (Throwable e) {
			throw new OpsdException("Can't get numMacros: " + e.getMessage(), e);
		}
		return numMacros;
	}
}
