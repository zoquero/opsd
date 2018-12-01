package org.zoquero.opsd;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zoquero.opsd.dao.OpsdDataTap;
import org.zoquero.opsd.dao.OpsdPoiDao;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdSystem;

/**
 * Accesses the Project objects throught a OpsdDataTap
 * and allows to get them in a OpsdFullProjectData object
 * @author agalindo
 *
 */
public class OpsdExtractor {

	OpsdDataTap dt;
	private static Logger LOGGER = Logger.getLogger(OpsdPoiDao.class.getName());

	/**
	 * Constructor that gets the DataTap
	 * @param dt
	 */
	public OpsdExtractor(OpsdDataTap dt) {
		this.dt = dt;
	}

	/**
	 * Get all the info from a Project
	 * @param projectName
	 * @return
	 * @throws OpsdException 
	 */
	public OpsdFullProjectData getFullProjectData(String projectName) throws OpsdException {
		OpsdReport oReport = new OpsdReport();
		LOGGER.log(Level.FINE,"Running OpsdExtractor.getFullProjectData()");
		OpsdFullProjectData fpd = new OpsdFullProjectData(oReport);
		
		// OpsdProject
		OpsdProject project = dt.getProject(projectName);
		fpd.setProject(project);
		
		// OpsdRole
		List<OpsdRole> roles = dt.getRoles(project);
		fpd.setRoles(roles);
		
		// OpsdSystem
		List<OpsdSystem> systems = dt.getSystems(project);
		fpd.setSystems(systems);

		// OpsdMonitoredHost
		List<OpsdMonitoredHost> monitoredHosts = dt.getMonitoredHosts(project);
		fpd.setMonitoredHosts(monitoredHosts);
		
		// OpsdMonitoredHost
		List<OpsdRoleService> roleServices = dt.getRoleServices(project);
		fpd.setRoleServices(roleServices);
		
		HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap = new HashMap<OpsdRole, List<OpsdRoleService>>();
		for(OpsdRole aRole: roles) {
			List<OpsdRoleService> roleservices = dt.getRoleServicesByRole(project, aRole);
			role2servicesMap.put(aRole, roleservices);
		}
		fpd.setRole2servicesMap(role2servicesMap);


		// Validation
		OpsdValidator.validate(fpd);
		
		return fpd;
	}

	
}
