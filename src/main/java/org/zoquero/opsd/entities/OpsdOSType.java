/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdOSType {
	private String name;
	private String description;
	
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

	/* Constructor */
	
	/**
	 * @param name
	 * @param description
	 */
	public OpsdOSType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/**
	 * toString
	 */
	public String toString() {
		return "OpsdOSType with name '" + getName() + "' and description = '" + getDescription() + "'";
	}
	
}
