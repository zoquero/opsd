/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * A host that can be monitored.
 * 
 * System can be multihomed. Some monitoring tools tend to get
 * the availability of a host by simple pinging an IP address
 * and don't make it easy to have multiple IPs that can be used
 * as variables in it's service definitions. So let's create an object
 * to each IP that can have services and can be monitored.
 * 
 * @author agalindo
 * 
 */
public class OpsdMonitoredHost extends OpsdMonitoredHostOrService {

	/* Fields */
	/** Field "name", will be shown as hostname in monitoring */
	private String name;

	/** Field "ip", used to test host availability in monitoring */
	private String ip;

	/** Field "system" */
	private OpsdSystem system;

	/**
	 * Field "forManaging" (will be used to administer the underlaying system?)
	 */
	private Boolean forManaging;

	/**
	 * Field "forService" (will be used to offer service from the underlaying
	 * system?)
	 */
	private Boolean forService;

	/**
	 * Field "forBackup" (will be used to backup the underlaying system?)
	 */
	private Boolean forBackup;

	/**
	 * Field "forNas" (will be used to access NAS from the underlaying system?)
	 */
	private Boolean forNas;

	/**
	 * Field "defaultChecksNeeded" If true, the host will get the default checks
	 * for the type of OS of the underlying system
	 */
	private Boolean defaultChecksNeeded;

	/** Field "moreInfo", any extra information */
	private String moreInfo;

	/**
	 * Field "environment", can be used to group hosts to ease the inventory and
	 * the procedures
	 */
	private String environment;

	/** Field "role" */
	private OpsdRole role;

	/**
	 * Field "scaleTo", if the host is not reachable and it can't be solved, who
	 * should it be scaled to?
	 */
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
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	private void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the system
	 */
	public OpsdSystem getSystem() {
		return system;
	}

	/**
	 * @param system
	 *            the system to set
	 */
	private void setSystem(OpsdSystem system) {
		this.system = system;
	}

	/**
	 * @return the forManaging
	 */
	public Boolean isForManaging() {
		return forManaging;
	}

	/**
	 * @param forManaging
	 *            the forManaging to set
	 */
	private void setForManaging(Boolean forManaging) {
		this.forManaging = forManaging;
	}

	/**
	 * @return the forService
	 */
	public Boolean isForService() {
		return forService;
	}

	/**
	 * @param forService
	 *            the forService to set
	 */
	private void setForService(Boolean forService) {
		this.forService = forService;
	}

	/**
	 * @return the forBackup
	 */
	public Boolean isForBackup() {
		return forBackup;
	}

	/**
	 * @param forBackup
	 *            the forBackup to set
	 */
	private void setForBackup(Boolean forBackup) {
		this.forBackup = forBackup;
	}

	/**
	 * @return the forNas
	 */
	public Boolean isForNas() {
		return forNas;
	}

	/**
	 * @param forNas
	 *            the forNas to set
	 */
	private void setForNas(Boolean forNas) {
		this.forNas = forNas;
	}

	/**
	 * @return the defaultChecksNeeded
	 */
	public Boolean isDefaultChecksNeeded() {
		return defaultChecksNeeded;
	}

	/**
	 * @param defaultChecksNeeded
	 *            the defaultChecksNeeded to set
	 */
	private void setDefaultChecksNeeded(Boolean defaultChecksNeeded) {
		this.defaultChecksNeeded = defaultChecksNeeded;
	}

	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	/**
	 * @param moreInfo
	 *            the moreInfo to set
	 */
	private void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	private void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the role
	 */
	public OpsdRole getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	private void setRole(OpsdRole role) {
		this.role = role;
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

	/* Constructors */

	/**
	 * @param name
	 * @param ip
	 * @param system
	 * @param forManaging
	 * @param forService
	 * @param forBackup
	 * @param forNas
	 * @param defaultChecksNeeded
	 * @param moreInfo
	 * @param environment
	 * @param role
	 * @param scaleTo
	 */
	public OpsdMonitoredHost(String name, String ip, OpsdSystem system,
			Boolean forManaging, Boolean forService, Boolean forBackup,
			Boolean forNas, Boolean defaultChecksNeeded, String moreInfo,
			String environment, OpsdRole role, String scaleTo) {
		this.name = name;
		this.ip = ip;
		this.system = system;
		this.forManaging = forManaging;
		this.forService = forService;
		this.forBackup = forBackup;
		this.forNas = forNas;
		this.defaultChecksNeeded = defaultChecksNeeded;
		this.moreInfo = moreInfo;
		this.environment = environment;
		this.role = role;
		this.scaleTo = scaleTo;
	}

	public String toString() {
		return "OpsdMonitoredHost with " +
				"name = '" + name +
				"', ip = '" + ip +
				"' system name = '" +
				(system == null ? " null " : system.getName() + system.getName()) +
				"' forManaging = '" + forManaging +
				"' forService = '" + forService +
				"' forBackup = '" + forBackup +
				"' forNas = '" + forNas +
				"' defaultChecksNeeded = '" + defaultChecksNeeded +
				"' moreInfo = '" + moreInfo +
				"' environment = '" + environment +
				"'" + (role == null ? " without role " : "role.getName() = '" + role.getName()) +
				"' scaleTo = '" + scaleTo;
	}

}
