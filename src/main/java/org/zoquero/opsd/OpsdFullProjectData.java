package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdSystem;

class OpsdFullProjectData {

	/** Report with errors and warnings */
	private OpsdReport  oReport;
	/** Project data */
	private OpsdProject project;
	/** Role data */
	private List<OpsdRole> roles;
	/** Systems data */
	private List<OpsdSystem> systems;
	
	OpsdFullProjectData(OpsdReport oReport) {
		System.out.println("OpsdFullProjectData constructor");
		setReport(oReport);
	}
	
	/**
	 * @return the oReport
	 */
	public OpsdReport getReport() {
		return oReport;
	}

	/**
	 * @param oReport the oReport to set
	 */
	private void setReport(OpsdReport oReport) {
		this.oReport = oReport;
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

}
