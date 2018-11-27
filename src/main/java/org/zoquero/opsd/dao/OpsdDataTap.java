package org.zoquero.opsd.dao;

import java.util.List;

import org.zoquero.opsd.OpsdReport;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdProject;

public interface OpsdDataTap {

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

	/**
	 * Gets info about a project
	 * @param projectName
	 * @return
	 * @throws OpsdDaoException
	 */
	OpsdProject getProject(String projectName) throws OpsdDaoException;

	/**
	 * Gets the roles of a Project
	 * @param project
	 * @return List of roles
	 */
	List<OpsdRole> getRoles(OpsdProject project) throws OpsdDaoException;

}
