/**
 * 
 */
package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;

/**
 * Validates the objects got from the project.
 * @author agalindo
 *
 */
public class OpsdValidator {
	
	public static void validate(OpsdFullProjectData fpd) {

		OpsdReport oReport = fpd.getReport();
		// Let's validate the Project:
		OpsdProject project = fpd.getProject();
		if(project == null) {
			oReport.pushError("Project is null");
		}
		if(project.getName() == null || project.getName().trim().equals("")) {
			oReport.pushError("Project has null name");
		}
		if(project.getDescription() == null || project.getDescription().trim().equals("")) {
			oReport.pushError("Project has null description");
		}
		if(project.getResponsible() == null || project.getResponsible().getName().trim().equals("")) {
			oReport.pushError("Project has null responsible name");
		}
		if(project.getDateIn() == null) {
			oReport.pushError("Project has null dateIn");
		}
		// null dateOut means 'still up'
//		if(project.getDateOut() == null) {
//			oReport.pushWarning("Project has null dateOut");
//		}
		if(project.getDependencies() == null || project.getDependencies().trim().equals("")) {
			oReport.pushWarning("Project has null dependencies");
		}
		if(project.getRecoveryProcedure() == null || project.getRecoveryProcedure().trim().equals("")) {
			oReport.pushWarning("Project has null recoveryProcedure");
		}
		if(project.getMoreInfo() == null || project.getMoreInfo().trim().equals("")) {
			oReport.pushWarning("Project has null moreInfo");
		}
		
		// Let's validate the Roles
		List<OpsdRole> roles = fpd.getRoles();
		for (int i = 0; i < roles.size(); i++) {
			OpsdRole role = roles.get(i);
			if(role == null) {
				oReport.pushError("Role #" + i + " is null");
			}
			if(role.getName() == null || role.getName().trim().equals("")) {
				oReport.pushError("Role #" + i + " has null name");
			}
			if(role.getDescription() == null || role.getDescription().trim().equals("")) {
				oReport.pushWarning("Role #" + i + " has null description");
			}
		}

		// MonitoredHosts
		List<OpsdMonitoredHost> monitoredHosts = fpd.getMonitoredHosts();
		for (int i = 0; i < monitoredHosts.size(); i++) {
			OpsdMonitoredHost monHost = monitoredHosts.get(i);
			if(monHost == null) {
				oReport.pushError("MonitoredHost #" + i + " is null");
			}
			if(monHost.getName() == null || monHost.getName().trim().equals("")) {
				oReport.pushError("MonitoredHost #" + i + " has null name");
			}
			if(monHost.getIp() == null || monHost.getIp().trim().equals("")) {
				oReport.pushError("MonitoredHost #" + i + " has null ip");
			}
			if(monHost.getSystem() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null system");
			}
			if(monHost.isForManaging() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null isForManaging");
			}
			if(monHost.isForService() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null isForService");
			}
			if(monHost.isForBackup() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null isForBackup");
			}
			if(monHost.isForNas() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null isForNas");
			}
			if(monHost.isDefaultChecksNeeded() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null isDefaultChecksNeeded");
			}
			if(monHost.getMoreInfo() == null || monHost.getMoreInfo().trim().equals("")) {
				oReport.pushWarning("MonitoredHost #" + i + " has null moreInfo");
			}
			if(monHost.getEnvironment() == null || monHost.getEnvironment().trim().equals("")) {
				oReport.pushWarning("MonitoredHost #" + i + " has null environment");
			}
			if(monHost.getRole() == null) {
				oReport.pushWarning("MonitoredHost #" + i + " has null role");
			}
//			if(monHost.getScaleTo() == null || monHost.getScaleTo().trim().equals("")) {
//				oReport.pushWarning("MonitoredHost #" + i + " has null scaleTo");
//			}
		}
	}

}
