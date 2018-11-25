package org.zoquero.opsd.dao;

import org.zoquero.opsd.entities.OpsdProject;

public interface OpsdDataTap {

	/**
	 * Gets info about a project
	 * @param projectName
	 * @return
	 */
	OpsdProject getProject(String projectName);

	/**
	 * Connect to backend
	 */
	void connect();


	/**
	 * Disconnect from backend
	 */
	void disconnect();

}
