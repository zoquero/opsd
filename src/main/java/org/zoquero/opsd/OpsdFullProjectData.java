package org.zoquero.opsd;

import org.zoquero.opsd.entities.OpsdProject;

class OpsdFullProjectData {

	/** Report with errors and warnings */
	private OpsdReport  oReport;
	/** Project data */
	private OpsdProject project;
	
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

}
