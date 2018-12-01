/**
 * 
 */
package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdSystem;

/**
 * Validates the objects got from the project.
 * @author agalindo
 *
 */
public class OpsdValidator {
	
	private static void validateProject(OpsdFullProjectData fpd) {
		OpsdReport oReport = fpd.getReport();
		OpsdProject project = fpd.getProject();
		if(project == null) {
			oReport.pushError("Project is null");
		}
		if(project.getName() == null
				|| project.getName().trim().equals("")) {
			oReport.pushError("Project has null name");
		}
		if(project.getDescription() == null
				|| project.getDescription().trim().equals("")) {
			oReport.pushError("Project has null description");
		}
		if(project.getResponsible() == null
				|| project.getResponsible().getName().trim().equals("")) {
			oReport.pushError("Project has null responsible name");
		}
		if(project.getDateIn() == null) {
			oReport.pushError("Project has null dateIn");
		}
		// null dateOut means 'still up'
//		if(project.getDateOut() == null) {
//			oReport.pushWarning("Project has null dateOut");
//		}
		if(project.getDependencies() == null
				|| project.getDependencies().trim().equals("")) {
			oReport.pushWarning("Project has null dependencies");
		}
		if(project.getRecoveryProcedure() == null
				|| project.getRecoveryProcedure().trim().equals("")) {
			oReport.pushWarning("Project has null recoveryProcedure");
		}
		if(project.getMoreInfo() == null
				|| project.getMoreInfo().trim().equals("")) {
			oReport.pushWarning("Project has null moreInfo");
		}
	}
	private static void validateRoles(OpsdFullProjectData fpd) {
		OpsdReport oReport = fpd.getReport();
		List<OpsdRole> roles = fpd.getRoles();
		for (int i = 0; i < roles.size(); i++) {
			OpsdRole role = roles.get(i);
			if(role == null) {
				oReport.pushError("Role #" + i + " is null");
			}
			if(role.getName() == null || role.getName().trim().equals("")) {
				oReport.pushError("Role #" + i + " has null name");
			}
			if(role.getDescription() == null
					|| role.getDescription().trim().equals("")) {
				oReport.pushWarning("Role #" + i + " has null description");
			}
		}
		
	}
	private static void validateSystems(OpsdFullProjectData fpd) {
		OpsdReport oReport = fpd.getReport();
		List<OpsdSystem> systems = fpd.getSystems();
		for (int i = 0; i < systems.size(); i++) {
			OpsdSystem system = systems.get(i);
			if(system == null) {
				oReport.pushError("System #" + i + " is null");
			}
			if(system.getName() == null
					|| system.getName().trim().equals("")) {
				oReport.pushError("System #" + i + " has null name");
			}
			if(system.getAlias() != null
					&& ! system.getAlias().trim().equals("")) {
				oReport.pushError("System #" + i + " has an alias."
						+ " Alias support is still not implemented");
			}
			if(system.getFqdnOrIp() == null
					|| system.getFqdnOrIp().trim().equals("")) {
				oReport.pushError("System #" + i + " has null fqdnOrIp");
			}
			if(system.getDeviceType() == null) {
				oReport.pushError("System #" + i + " has null deviceType or it can't be found");
			}
			if(system.getOs() == null) {
				oReport.pushError("System #" + i + " has null OS or it can't be found");
			}
			if(system.getOsAccess() == null
					|| system.getOsAccess().trim().equals("")) {
				oReport.pushError("System #" + i + " has null osAccess");
			}
			if(system.getDeviceType() != null
					&& ! system.getDeviceType().isVirtual()) {
				if(system.getLomIP() == null
						|| system.getLomIP().trim().equals("")) {
					oReport.pushError("System #" + i
							+ " has null lomIP and it's a physical device");
				}
				if(system.getLomAccess() == null
						|| system.getLomAccess().trim().equals("")) {
					oReport.pushError("System #" + i + " has null lomAccess"
							+ " and it's a physical device");
				}
			}		
			if(system.getMoreInfo() == null
					|| system.getMoreInfo().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null moreInfo");
			}
			if(system.getEnvironment() == null
					|| system.getEnvironment().trim().equals("")) {
				oReport.pushWarning("System #" + i + " has null environment");
			}
			if(system.getRole() == null) {
				oReport.pushWarning("System #" + i + " has null role or it can't be found");
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
	}
	private static void validateMonitoredHosts(OpsdFullProjectData fpd) {
		OpsdReport oReport = fpd.getReport();
		List<OpsdMonitoredHost> monitoredHosts = fpd.getMonitoredHosts();
		for (int i = 0; i < monitoredHosts.size(); i++) {
			OpsdMonitoredHost monHost = monitoredHosts.get(i);
			if(monHost == null) {
				oReport.pushError("MonitoredHost #" + i + " is null");
			}
			if(monHost.getName() == null
					|| monHost.getName().trim().equals("")) {
				oReport.pushError("MonitoredHost #" + i + " has null name");
			}
			if(monHost.getIp() == null || monHost.getIp().trim().equals("")) {
				oReport.pushError("MonitoredHost #" + i + " has null ip");
			}
			if(monHost.getSystem() == null) {
				oReport.pushError("MonitoredHost #" + i + " has null system or it can't be found");
			}
			if(monHost.isForManaging() == null) {
				oReport.pushError("MonitoredHost #"
						+ i + " has null isForManaging");
			}
			if(monHost.isForService() == null) {
				oReport.pushError("MonitoredHost #" 
						+ i + " has null isForService");
			}
			if(monHost.isForBackup() == null) {
				oReport.pushError("MonitoredHost #"
						+ i + " has null isForBackup");
			}
			if(monHost.isForNas() == null) {
				oReport.pushError("MonitoredHost #"
						+ i + " has null isForNas");
			}
			if(monHost.isDefaultChecksNeeded() == null) {
				oReport.pushError("MonitoredHost #"
						+ i + " has null isDefaultChecksNeeded");
			}
			if(monHost.getMoreInfo() == null || monHost.getMoreInfo().trim().equals("")) {
				oReport.pushWarning("MonitoredHost #"
						+ i + " has null moreInfo");
			}
			if(monHost.getEnvironment() == null || monHost.getEnvironment().trim().equals("")) {
				oReport.pushWarning("MonitoredHost #"
						+ i + " has null environment");
			}
			if(monHost.getRole() == null) {
				oReport.pushWarning("MonitoredHost #"
						+ i + " has null role or it can't be found");
			}
//			if(monHost.getScaleTo() == null || monHost.getScaleTo().trim().equals("")) {
//				oReport.pushWarning("MonitoredHost #" + i + " has null scaleTo");
//			}
		}
	}
	private static void validateRoleServices(OpsdFullProjectData fpd) {
		OpsdReport oReport = fpd.getReport();
		List<OpsdRoleService> roleServices = fpd.getRoleServices();
		for (int i = 0; i < roleServices.size(); i++) {
			OpsdRoleService roleService = roleServices.get(i);
			if(roleService == null) {
				oReport.pushError("RoleService #" + i + " is null");
			}
			if(roleService.getName() == null
						|| roleService.getName().trim().equals("")) {
				oReport.pushError("RoleService #" + i + " has null name");
			}
			if(roleService.getDescription() == null
						|| roleService.getDescription().trim().equals("")) {
				oReport.pushError("RoleService #" + i + " has null description");
			}
			if(roleService.getProcedure() == null
						|| roleService.getProcedure().equals("")) {
				oReport.pushWarning("RoleService #" + i
						+ " has null procedure."
						+ " Incidences just will be able to be scaled out");
			}
			if(roleService.getCriticity() == null) {
				oReport.pushError("RoleService #"
						+ i + " has null criticity or it can't be found");
			}
			if(roleService.getRole() == null) {
				oReport.pushError("RoleService #"
						+ i + " has null role or it can't be found");
			}
			if(roleService.getServiceTemplate() == null) {
				oReport.pushError("RoleService #"
						+ i + " has null ServiceTemplate or it can't be found");
			}
			if(roleService.getMacroAndValueArray() == null
						|| roleService.getMacroAndValueArray().length < 1) {
				oReport.pushError("RoleService #"
						+ i + " has null or empty MacroAndValueArray");
			}
//			if(roleService.getScaleTo() == null
//						|| roleService.getScaleTo().equals("")) {
//				oReport.pushWarning("RoleService #"
//						+ i + " has null scaleTo");
//			}
		}
	}
	public static void validate(OpsdFullProjectData fpd) {
		validateProject(fpd);
		validateRoles(fpd);
		validateSystems(fpd);
		validateMonitoredHosts(fpd);
		validateRoleServices(fpd);
	}
}
