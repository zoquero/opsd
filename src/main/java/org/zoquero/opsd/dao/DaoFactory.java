package org.zoquero.opsd.dao;

public class DaoFactory {
	public OpsdDatatap getDao(String path) {
		return new OpsdPoiDao(path);
	}

}
