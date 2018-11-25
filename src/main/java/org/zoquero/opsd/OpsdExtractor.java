package org.zoquero.opsd;

import org.zoquero.opsd.dao.OpsdDataTap;
import org.zoquero.opsd.entities.OpsdProject;

/**
 * Accesses the Project objects throught a OpsdDataTap
 * and allows to get them in a OpsdFullProjectData object
 * @author agalindo
 *
 */
public class OpsdExtractor {

	OpsdDataTap dt;

	/**
	 * Constructor that gets the DataTap
	 * @param _dt
	 */
	public OpsdExtractor(OpsdDataTap _dt) {
		System.out.println("Running OpsdExtractor constructor");
		dt = _dt;
	}

	/**
	 * Get all the info from a Project
	 * @param projectName
	 * @return
	 */
	public OpsdFullProjectData getFullProjectData(String projectName) {
		System.out.println("Running OpsdExtractor.getFullProjectData()");
		OpsdFullProjectData fpd = new OpsdFullProjectData();
		
		dt.connect();
		OpsdProject project = dt.getProject(projectName);
		fpd.setProject(project);
		dt.disconnect();
		return fpd;
	}

	
}
