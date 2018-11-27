package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.dao.OpsdDaoException;
import org.zoquero.opsd.dao.OpsdDataTap;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;

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
	 * @param dt
	 */
	public OpsdExtractor(OpsdDataTap dt) {
		System.out.println("Running OpsdExtractor constructor");
		this.dt = dt;
	}

	/**
	 * Get all the info from a Project
	 * @param projectName
	 * @return
	 * @throws OpsdDaoException 
	 */
	public OpsdFullProjectData getFullProjectData(String projectName) throws OpsdDaoException {
		OpsdReport oReport = new OpsdReport();
		System.out.println("Running OpsdExtractor.getFullProjectData()");
		OpsdFullProjectData fpd = new OpsdFullProjectData(oReport);
		
		// OpsdProject
		OpsdProject project = dt.getProject(projectName);
		fpd.setProject(project);
		
		// OpsdRole
		List<OpsdRole> roles = dt.getRoles(project);
		fpd.setRoles(roles);
		
		// Validation
		OpsdValidator.validate(project, oReport);
		OpsdValidator.validate(roles, oReport);
		
		return fpd;
	}

	
}
