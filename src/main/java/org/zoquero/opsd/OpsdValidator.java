/**
 * 
 */
package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdSystem;

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
		

		// Systems
		List<OpsdSystem> systems = fpd.getSystems();
		for (int i = 0; i < systems.size(); i++) {
			OpsdSystem system = systems.get(i);
			if(system == null) {
				oReport.pushError("System #" + i + " is null");
			}
			if(system.getName() == null || system.getName().trim().equals("")) {
				oReport.pushError("System #" + i + " has null name");
			}
			if(system.getFqdnOrIp() == null || system.getFqdnOrIp().trim().equals("")) {
				oReport.pushError("System #" + i + " has null fqdnOrIp");
			}
			if(system.getDeviceType() == null) {
				oReport.pushError("System #" + i + " has null deviceType");
			}
			if(system.getOs() == null) {
				oReport.pushError("System #" + i + " has null OS");
			}
			if(system.getOsAccess() == null || system.getOsAccess().trim().equals("")) {
				oReport.pushError("System #" + i + " has null osAccess");
			}
			if(system.getLomIP() == null || system.getLomIP().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null lomIP (if it's not physical there's no problem)");
			}
			if(system.getLomAccess() == null || system.getLomAccess().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null lomAccess (if it's not physical there's no problem)");
			}		
			if(system.getMoreInfo() == null || system.getMoreInfo().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null moreInfo");
			}
			if(system.getEnvironment() == null || system.getEnvironment().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null environment");
			}
			if(system.getRole() == null) {
				oReport.pushWarning("System #" + i + " has null role");
			}
//			if(system.getHostDownRecoveryProcedure() == null || system.getHostDownRecoveryProcedure().trim().equals("")) {
//				oReport.pushWarning("System #" + i + " has null hostDownRecoveryProcedure");
//			}
//			if(system.getResponsible() == null) {
//				oReport.pushWarning("System #" + i + " has null responsible");
//			}
//			if(system.getScaleTo() == null || system.getScaleTo().trim().equals("")) {
//				oReport.pushWarning("System #" + i + " has null scaleTo");
//			}
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
