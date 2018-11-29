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
	 * if it's not set when it's instantiated. */
	private String defaultName;
	/** Description of the ServiceTemplate and some help to who
	 * is thinking about the possibility of instantiating
	 * a service based in this template. */
	private String description;
	/** List of macros */
	private List<OpsdServiceMacroDefinition> macros;
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
	 * @return the macros
	 */
	public List<OpsdServiceMacroDefinition> getMacros() {
		return macros;
	}
	/**
	 * @param macros the macros to set
	 */
	private void setMacros(List<OpsdServiceMacroDefinition> macros) {
		this.macros = macros;
	}
	/**
	 * @param name
	 * @param nrpe
	 * @param defaultName
	 * @param description
	 * @param macros
	 */
	public OpsdServiceTemplate(String name, boolean nrpe, String defaultName,
			String description, List<OpsdServiceMacroDefinition> macros) {
		this.name = name;
		this.nrpe = nrpe;
		this.defaultName = defaultName;
		this.description = description;
		this.macros = macros;
	}

}
