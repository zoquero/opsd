/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 * 
 */
public abstract class OpsdMonitoredService {

	/* Fields */

	/** Field "name" */
	private String name;

	/** Field "description" */
	private String description;

	/** Field "procedure" */
	private String procedure;

	/** Field "criticity" */
	private OpsdCriticity criticity;

	/** Field "hostOrService" */
	private OpsdMonitoredHostOrService hostOrService;

	/** Field "serviceTemplate" */
	private OpsdServiceTemplate serviceTemplate;

	/** Field "macroAndValueArray" */
	private String[] macroAndValueArray;

	/** Field "scaleTo" */
	private String scaleTo;

	/* Accessors */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param description
	 *            the description to set
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the procedure
	 */
	public String getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure
	 *            the procedure to set
	 */
	private void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	/**
	 * @return the criticity
	 */
	public OpsdCriticity getOpsdCriticity() {
		return criticity;
	}

	/**
	 * @param criticity
	 *            the criticity to set
	 */
	private void setOpsdCriticity(OpsdCriticity criticity) {
		this.criticity = criticity;
	}

	/**
	 * @return the hostOrService
	 */
	public OpsdMonitoredHostOrService getHostOrService() {
		return hostOrService;
	}

	/**
	 * @param hostOrService
	 *            the hostOrService to set
	 */
	private void setHostOrService(OpsdMonitoredHostOrService hostOrService) {
		this.hostOrService = hostOrService;
	}

	/**
	 * @return the serviceTemplate
	 */
	public OpsdServiceTemplate getOpsdServiceTemplate() {
		return serviceTemplate;
	}

	/**
	 * @param serviceTemplate
	 *            the serviceTemplate to set
	 */
	private void setOpsdServiceTemplate(OpsdServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}

	/**
	 * @return the macroAndValueArray
	 */
	public String[] getMacroAndValueArray() {
		return macroAndValueArray;
	}

	/**
	 * @param macroAndValueArray
	 *            the macroAndValueArray to set
	 */
	private void setMacroAndValueArray(String[] macroAndValueArray) {
		this.macroAndValueArray = macroAndValueArray;
	}

	/**
	 * @return the scaleTo
	 */
	public String getScaleTo() {
		return scaleTo;
	}

	/**
	 * @param scaleTo
	 *            the scaleTo to set
	 */
	private void setScaleTo(String scaleTo) {
		this.scaleTo = scaleTo;
	}

	/**
	 * @param name
	 * @param description
	 * @param procedure
	 * @param criticity
	 * @param hostOrService
	 * @param serviceTemplate
	 * @param macroAndValueArray
	 * @param scaleTo
	 */
	public OpsdMonitoredService(String name, String description, String procedure,
			OpsdCriticity criticity, OpsdMonitoredHostOrService hostOrService,
			OpsdServiceTemplate serviceTemplate, String[] macroAndValueArray,
			String scaleTo) {
		this.name = name;
		this.description = description;
		this.procedure = procedure;
		this.criticity = criticity;
		this.hostOrService = hostOrService;
		this.serviceTemplate = serviceTemplate;
		this.macroAndValueArray = macroAndValueArray;
		this.scaleTo = scaleTo;
	}

}
