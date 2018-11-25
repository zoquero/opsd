package org.zoquero.opsd.dao;

import org.zoquero.opsd.OpsdReport;
import org.zoquero.opsd.entities.OpsdProject;

public interface OpsdDataTap {

	/**
	 * Gets info about a project
	 * @param projectName
	 * @param oReport 
	 * @return
	 * @throws OpsdDaoException
	 */
	OpsdProject getProject(String projectName) throws OpsdDaoException;

	/**
	 * Connect to backend
	 * @throws OpsdDaoException
	 */
	void connect() throws OpsdDaoException;


	/**
	 * Disconnect from backend
	 * @throws OpsdDaoException
	 */
	void disconnect() throws OpsdDaoException;

}
