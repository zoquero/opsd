/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * Definition of a macro that can be used to configure a monitored service.
 * This allows to parameterize the monitored services
 * and set the concrete values on each host,
 * but sharing the same service definition (template)
 * @author agalindo
 *
 */
public class OpsdServiceMacroDefinition {
	private String name;
	private String description;
	private String defaultValue;
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
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	private void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @param name
	 * @param description
	 * @param defaultValue
	 */
	public OpsdServiceMacroDefinition(String name, String description,
			String defaultValue) {
		this.name = name;
		this.description = description;
		this.defaultValue = defaultValue;
	}
	
}
