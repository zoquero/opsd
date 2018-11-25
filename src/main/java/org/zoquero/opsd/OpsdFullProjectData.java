package org.zoquero.opsd;

import org.zoquero.opsd.entities.OpsdProject;

class OpsdFullProjectData {

	private OpsdProject project;
	
	OpsdFullProjectData() {
		System.out.println("OpsdFullProjectData constructor");
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
