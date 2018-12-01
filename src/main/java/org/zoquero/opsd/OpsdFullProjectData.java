package org.zoquero.opsd;

import java.util.HashMap;
import java.util.List;

import org.zoquero.opsd.dao.OpsdPoiConf;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdSystem;

class OpsdFullProjectData {

	/** Report with errors and warnings */
	private OpsdReport  report;
	/** Project data */
	private OpsdProject project;
	/** Role data */
	private List<OpsdRole> roles;
	/** Systems data */
	private List<OpsdSystem> systems;
	/** MonitoredHosts data */
	private List<OpsdMonitoredHost> monitoredHosts;
	/** RoleServices data */
	private List<OpsdRoleService> roleServices;
	/** Map Role > RoleService
	 * that will be very usefull generating the output */
	private HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap = null;
	
	OpsdFullProjectData(OpsdReport oReport) {
		System.out.println("OpsdFullProjectData constructor");
		setReport(oReport);
	}
	
	/**
	 * @return the report
	 */
	public OpsdReport getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	private void setReport(OpsdReport report) {
		this.report = report;
	}

	public void setProject(OpsdProject project) {
		this.project = project;
	}

	/**
	 * @return the project
	 */
	public OpsdProject getProject() {
		return project;
	}

	/**
	 * @return the roles
	 */
	public List<OpsdRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles the role to set
	 */
	public void setRoles(List<OpsdRole> roles) {
		this.roles = roles;
	}
	
	/**
	 * @return the systems
	 */
	public List<OpsdSystem> getSystems() {
		return systems;
	}

	/**
	 * @param systems the system to set
	 */
	public void setSystems(List<OpsdSystem> systems) {
		this.systems = systems;
	}

	/**
	 * @return the monitoredHosts
	 */
	public List<OpsdMonitoredHost> getMonitoredHosts() {
		return monitoredHosts;
	}

	/**
	 * @param monitoredHosts the monitoredHosts to set
	 */
	public void setMonitoredHosts(List<OpsdMonitoredHost> monitoredHosts) {
		this.monitoredHosts = monitoredHosts;
	}

	/**
	 * @return the roleServices
	 */
	public List<OpsdRoleService> getRoleServices() {
		return roleServices;
	}

	/**
	 * @param roleServices the roleServices to set
	 */
	public void setRoleServices(List<OpsdRoleService> roleServices) {
		this.roleServices = roleServices;
	}

	/**
	 * @return the role2servicesMap
	 */
	public HashMap<OpsdRole, List<OpsdRoleService>> getRole2servicesMap() {
		return role2servicesMap;
	}

	/**
	 * @param role2servicesMap the role2servicesMap to set
	 */
	public void setRole2servicesMap(
			HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap) {
		this.role2servicesMap = role2servicesMap;
	}

}
