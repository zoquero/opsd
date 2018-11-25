package org.zoquero.opsd.dao;

/**
 * Provides an implementation of the Interface OpsdDataTap
 * to access to the Project objects.
 * @author agalindo
 *
 */
public class DaoFactory {
	
	public OpsdDataTap getDao(String path) {
		return new OpsdPoiDao(path);
	}

}
