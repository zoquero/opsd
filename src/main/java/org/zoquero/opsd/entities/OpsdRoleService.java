/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * A Monitored Service that will be held
 * by all the Hosts belonging to this role.
 * @author agalindo
 *
 */
public class OpsdRoleService extends OpsdMonitoredService {
	
	/**
	 * The role that has the service
	 */
	private OpsdRole role;

	/**
	 * @return the role
	 */
	public OpsdRole getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	private void setRole(OpsdRole role) {
		this.role = role;
	}

	/**
	 * @param name
	 * @param description
	 * @param procedure
	 * @param criticity
	 * @param role
	 * @param serviceTemplate
	 * @param macroAndValueArray
	 * @param scaleTo
	 */
	public OpsdRoleService(String name, String description,
			String procedure, OpsdCriticity criticity,
			OpsdRole role,
			OpsdServiceTemplate serviceTemplate, String[] macroAndValueArray,
			String scaleTo) {
		super(name, description, procedure, criticity,
				serviceTemplate, macroAndValueArray, scaleTo);
		setRole(role);
	}
	
	public String toString() {
		return "RoleService with name = " + getName()
				+ ", description = "
				+ (getDescription() == null ? "null" : getDescription()) 
				+ ", procedure = "
				+ (getProcedure() == null ? "null" : getProcedure())
				+ ", criticity = "
				+ ((getCriticity() == null || getCriticity().getName() == null) ? "null" : getCriticity().getName())
				+ ", role = " +
				((getRole() == null || getRole().getName() == null) ? "null" : getRole().getName())
				+ ", serviceTemplate = + "
				+ (getServiceTemplate() == null ? "null" : getServiceTemplate())
				+ ", macroAndArray = "
				+ (getMacroValuesArray() == null ? "null" : getMacroValuesArray().length)
				+ " elements, scaleTo = "
				+ (getScaleTo() == null ? "null" : getScaleTo());
	}
}
