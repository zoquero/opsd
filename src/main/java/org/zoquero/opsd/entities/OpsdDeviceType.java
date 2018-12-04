/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdDeviceType {
	private String name;
	private String description;
	private boolean isVirtual;
	
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
	 * @return the isVirtual
	 */
	public boolean isVirtual() {
		return isVirtual;
	}
	/**
	 * @param isVirtual the isVirtual to set
	 */
	private void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	
	/* Constructor */
	
	/**
	 * @param name
	 * @param description
	 * @para isVirtual
	 */
	public OpsdDeviceType(String name, String description, boolean isVirtual) {
		this.name = name;
		this.description = description;
		this.isVirtual = isVirtual;
	}
	
	public String toString(){
		return "Device with name '" + getName() + "' that's " + ( isVirtual() ? "" : "not " ) + "virtual with description '" + getDescription() + "'"; 
	}
	
}
