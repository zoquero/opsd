/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdCriticity {

	private String name;
	private String description;
	private boolean premium;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
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
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the premium
	 */
	public boolean isPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	/**
	 * @param name
	 * @param description
	 */
	public OpsdCriticity(String name, String description, boolean premium) {
		this.name = name;
		this.description = description;
		this.premium = premium;
	}
	
	public String tosString() {
		return "Criticity '" + getName()
				+ "' with description '" + getDescription() + "'"
				+ " that" + (premium ? " IS " : " IS NOT ") + "Premium";
	}
	
}
