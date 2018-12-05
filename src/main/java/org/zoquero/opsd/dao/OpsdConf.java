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
		ResourceBundle rb = ResourceBundle.getBundle(CONF_PROPERTIES);
		String propertyName = "wiki.urlbase";
		return rb.getString(propertyName);
	}
}
