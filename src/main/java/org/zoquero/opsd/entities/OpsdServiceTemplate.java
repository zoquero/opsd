/**
 * 
 */
package org.zoquero.opsd.entities;

import java.util.List;

/**
 * @author agalindo
 *
 */
public class OpsdServiceTemplate {
	
	/** name (key used to set a service based on this ServiceTemplate) */
	private String name;
	/** Will it be a service invoked though NRPE? */
	private boolean nrpe;
	/** Default name to be shown in monitoring,
	 * if it's not set when it's instantiated.
	 * It can be a format string to be substituted by macro values,
	 * macro values can be $MACRO1$ ,  $MACRO2$ , ...  $MACRO7$
	 * Ex.: Service_$MACRO1$ or Check_Jmeter_$MACRO2$ */
	private String defaultName;
	/** Description of the ServiceTemplate and some help to who
	 * is thinking about the possibility of instantiating
	 * a service based in this template. */
	private String description;
	/** List of macroDefinitions */
	private List<OpsdServiceMacroDefinition> macroDefinitions;
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
	 * @return the nrpe
	 */
	public boolean isNrpe() {
		return nrpe;
	}
	/**
	 * @param nrpe the nrpe to set
	 */
	private void setNrpe(boolean nrpe) {
		this.nrpe = nrpe;
	}
	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}
	/**
	 * @param defaultName the defaultName to set
	 */
	private void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
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
	 * @return the macroDefinitions
	 */
	public List<OpsdServiceMacroDefinition> getMacroDefinitions() {
		return macroDefinitions;
	}
	/**
	 * @param macroDefinitions the macroDefinitions to set
	 */
	private void setMacroDefinitions(List<OpsdServiceMacroDefinition> macroDefinitions) {
		this.macroDefinitions = macroDefinitions;
	}
	/**
	 * @param name
	 * @param nrpe
	 * @param defaultName
	 * @param description
	 * @param macroDefinitions
	 */
	public OpsdServiceTemplate(String name, boolean nrpe, String defaultName,
			String description, List<OpsdServiceMacroDefinition> macroDefinitions) {
		this.name = name;
		this.nrpe = nrpe;
		this.defaultName = defaultName;
		this.description = description;
		this.macroDefinitions = macroDefinitions;
	}
	
	public String toString() {
		StringBuilder mDefs = new StringBuilder("Macro definitions: ");
		for(OpsdServiceMacroDefinition smd: getMacroDefinitions()) {
			mDefs.append("* name = " + smd.getName()
						+ " * description = " + smd.getDescription()
						+ " * default value = " + smd.getDefaultValue());
		}
		return getClass().getSimpleName() + " with name = " + getName()
				+ ", nrpe = " + nrpe + ", defaultName = " + defaultName
				+ " description = " + description
				+ " and ServiceMacroDefinitions= " + mDefs.toString();
	}

}
