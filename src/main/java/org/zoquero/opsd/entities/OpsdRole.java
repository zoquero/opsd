/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdRole extends OpsdMonitoredHostOrService {
	
	/* Fields */

	/** Field "name" */
	private String name;

	/** Field "description" */
	private String description;

	/* Constructors */

	/**
	 * @param name
	 * @param description
	 */
	public OpsdRole(String name, String description) {
		this.name = name;
		this.description = description;
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

	public String toString() {
		String r = "(Still unnamed responsible)";
		return "OpsdRole with name = " + name
				+ ", description = " + description;
	}

}
