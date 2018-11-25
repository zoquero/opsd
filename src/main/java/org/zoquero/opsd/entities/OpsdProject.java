package org.zoquero.opsd.entities;

import java.util.Calendar;

public class OpsdProject {

	/* Fields */

	/** Field "name" */
	private String name;

	/** Field "description" */
	private String description;

	/** Field "responsible" */
	private Responsible responsible;

	/** Field "dateIn" */
	private Calendar dateIn;

	/** Field "dateOut" */
	private Calendar dateOut;

	/** Field "dependencies" */
	private String dependencies;

	/** Field "recoveryProcedure" */
	private String recoveryProcedure;

	/** Field "moreInfo" */
	private String moreInfo;

	/* Constructors */
	
	/**
	 * @param name
	 * @param description
	 * @param responsible
	 * @param dateIn
	 * @param dateOut
	 * @param dependencies
	 * @param recoveryProcedure
	 * @param moreInfo
	 */
	public OpsdProject(String name, String description,
			Responsible responsible, Calendar dateIn, Calendar dateOut,
			String dependencies, String recoveryProcedure, String moreInfo) {
		this.name = name;
		this.description = description;
		this.responsible = responsible;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
		this.dependencies = dependencies;
		this.recoveryProcedure = recoveryProcedure;
		this.moreInfo = moreInfo;
	}

	/* Accessors */
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the responsible
	 */
	public Responsible getResponsible() {
		return responsible;
	}

	/**
	 * @param responsible the responsible to set
	 */
	private void setResponsible(Responsible responsible) {
		this.responsible = responsible;
	}

	/**
	 * @return the dateIn
	 */
	public Calendar getDateIn() {
		return dateIn;
	}

	/**
	 * @param dateIn the dateIn to set
	 */
	private void setDateIn(Calendar dateIn) {
		this.dateIn = dateIn;
	}

	/**
	 * @return the dateOut
	 */
	public Calendar getDateOut() {
		return dateOut;
	}

	/**
	 * @param dateOut the dateOut to set
	 */
	private void setDateOut(Calendar dateOut) {
		this.dateOut = dateOut;
	}

	/**
	 * @return the dependencies
	 */
	public String getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	private void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @return the recoveryProcedure
	 */
	public String getRecoveryProcedure() {
		return recoveryProcedure;
	}

	/**
	 * @param recoveryProcedure the recoveryProcedure to set
	 */
	private void setRecoveryProcedure(String recoveryProcedure) {
		this.recoveryProcedure = recoveryProcedure;
	}

	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	/**
	 * @param moreInfo the moreInfo to set
	 */
	private void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

}
