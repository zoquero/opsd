/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdFilePolicy {
	  private OpsdSystem system;
	  private OpsdRole role;
	  private String baseFolder;
	  private String prefix;
	  private String sufix;
	  private Integer minDays;
	  private ACTION_TYPE action;

	  public enum ACTION_TYPE {
		  DELETE,
		  COMPRESS;
	  }

	/**
	 * @return the system
	 */
	public OpsdSystem getSystem() {
		return system;
	}

	/**
	 * @param system the system to set
	 */
	private void setSystem(OpsdSystem system) {
		this.system = system;
	}

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
	 * @return the baseFolder
	 */
	public String getBaseFolder() {
		return baseFolder;
	}

	/**
	 * @param baseFolder the baseFolder to set
	 */
	private void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	private void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the sufix
	 */
	public String getSufix() {
		return sufix;
	}

	/**
	 * @param sufix the sufix to set
	 */
	private void setSufix(String sufix) {
		this.sufix = sufix;
	}

	/**
	 * @return the minDays
	 */
	public Integer getMinDays() {
		return minDays;
	}

	/**
	 * @param minDays the minDays to set
	 */
	private void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}

	/**
	 * @return the action
	 */
	public ACTION_TYPE getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	private void setAction(ACTION_TYPE action) {
		this.action = action;
	}

	/**
	 * @param system
	 * @param role
	 * @param baseFolder
	 * @param prefix
	 * @param sufix
	 * @param minDays
	 * @param action
	 */
	public OpsdFilePolicy(OpsdSystem system, OpsdRole role, String baseFolder,
			String prefix, String sufix, Integer minDays, ACTION_TYPE action) {
		this.system = system;
		this.role = role;
		this.baseFolder = baseFolder;
		this.prefix = prefix;
		this.sufix = sufix;
		this.minDays = minDays;
		this.action = action;
	}

	
	public String toString() {
		return getClass().getSimpleName()
				+ " for system = "
				+ ((getSystem() == null || getSystem().getName() == null) ? "null" : getSystem().getName()) 
				+ " for role = "
				+ ((getRole() == null || getRole().getName() == null) ? "null" : getRole().getName())
				+ ", baseFolder = "
				+ (getBaseFolder() == null ? "null" : getBaseFolder())
				+ ", prefix = "
				+ (getPrefix() == null ? "null" : getPrefix())
				+ ", sufix = "
				+ (getSufix() == null ? "null" : getSufix())
				+ ", mindays = "
				+ (getMinDays() == null ? "null" : getMinDays().intValue())
				+ ", action = "
				+ (getAction() == null ? "null" : OpsdFilePolicy.actionToStr(getAction()));
	}

	private static String actionToStr(ACTION_TYPE action) {
		if(action == null)
			return null;
		if(action == ACTION_TYPE.DELETE) {
			return "rm";
		}
		if(action == ACTION_TYPE.COMPRESS) {
			return "gz";
		}
		return null;
	}
	
}
