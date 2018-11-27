/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdSystem {

	/* Fields */

	  /** Field "name" */
	  private String name;

	  /** Field "alias"
	   * @deprecated Supported but its use is un-adviced
	   *  */
	  private String alias;

	  /** Field "fqdnOrIp" */
	  private String fqdnOrIp;

	  /** Field "deviceType" */
	  private OpsdDeviceType deviceType;

	  /** Field "os" */
	  private OpsdOSType os;

	  /** Field "osAccess" */
	  private String osAccess;

	  /** Field "lomIP" */
	  private String lomIP;

	  /** Field "lomAccess" */
	  private String lomAccess;

	  /** Field "moreInfo" */
	  private String moreInfo;

	  /** Field "environment" */
	  private String environment;

	  /** Field "role" */
	  private OpsdRole role;

	  /** Field "hostDownRecoveryProcedure" */
	  private String hostDownRecoveryProcedure;

	  /** Field "responsible" */
	  private OpsdResponsible responsible;

	  /** Field "scaleTo" */
	  private String scaleTo;


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
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	private void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the fqdnOrIp
	 */
	public String getFqdnOrIp() {
		return fqdnOrIp;
	}

	/**
	 * @param fqdnOrIp the fqdnOrIp to set
	 */
	private void setFqdnOrIp(String fqdnOrIp) {
		this.fqdnOrIp = fqdnOrIp;
	}

	/**
	 * @return the deviceType
	 */
	public OpsdDeviceType getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	private void setDeviceType(OpsdDeviceType deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the os
	 */
	public OpsdOSType getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	private void setOs(OpsdOSType os) {
		this.os = os;
	}

	/**
	 * @return the osAccess
	 */
	public String getOsAccess() {
		return osAccess;
	}

	/**
	 * @param os the os to set
	 */
	private void setOsAccess(String osAccess) {
		this.osAccess = osAccess;
	}

	/**
	 * @return the lomIP
	 */
	public String getLomIP() {
		return lomIP;
	}

	/**
	 * @param lomIP the lomIP to set
	 */
	private void setLomIP(String lomIP) {
		this.lomIP = lomIP;
	}

	/**
	 * @return the lomAccess
	 */
	public String getLomAccess() {
		return lomAccess;
	}

	/**
	 * @param lomAccess the lomAccess to set
	 */
	private void setLomAccess(String lomAccess) {
		this.lomAccess = lomAccess;
	}

	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	/**
	 * @param moreInfo the moreInfo to set
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
	 * @param environment the environment to set
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
	 * @param role the role to set
	 */
	private void setRole(OpsdRole role) {
		this.role = role;
	}

	/**
	 * @return the hostDownRecoveryProcedure
	 */
	public String getHostDownRecoveryProcedure() {
		return hostDownRecoveryProcedure;
	}

	/**
	 * @param hostDownRecoveryProcedure the hostDownRecoveryProcedure to set
	 */
	private void setHostDownRecoveryProcedure(String hostDownRecoveryProcedure) {
		this.hostDownRecoveryProcedure = hostDownRecoveryProcedure;
	}

	/**
	 * @return the responsible
	 */
	public OpsdResponsible getResponsible() {
		return responsible;
	}

	/**
	 * @param responsible the responsible to set
	 */
	private void setResponsible(OpsdResponsible responsible) {
		this.responsible = responsible;
	}

	/**
	 * @return the scaleTo
	 */
	public String getScaleTo() {
		return scaleTo;
	}

	/**
	 * @param scaleTo the scaleTo to set
	 */
	private void setScaleTo(String scaleTo) {
		this.scaleTo = scaleTo;
	}

	/* Constructors */

	/**
	 * @param name
	 * @param alias
	 * @param fqdnOrIp
	 * @param deviceType
	 * @param os
	 * @param lomIP
	 * @param lomAccess
	 * @param moreInfo
	 * @param environment
	 * @param role
	 * @param hostDownRecoveryProcedure
	 * @param responsible
	 * @param scaleTo
	 */
	public OpsdSystem(String name, String alias, String fqdnOrIp,
			OpsdDeviceType deviceType, OpsdOSType os, String osAccess,
			String lomIP, String lomAccess, String moreInfo,
			String environment, OpsdRole role,
			String hostDownRecoveryProcedure, OpsdResponsible responsible,
			String scaleTo) {
		this.name = name;
		this.alias = alias;
		this.fqdnOrIp = fqdnOrIp;
		this.deviceType = deviceType;
		this.os = os;
		this.osAccess = osAccess;
		this.lomIP = lomIP;
		this.lomAccess = lomAccess;
		this.moreInfo = moreInfo;
		this.environment = environment;
		this.role = role;
		this.hostDownRecoveryProcedure = hostDownRecoveryProcedure;
		this.responsible = responsible;
		this.scaleTo = scaleTo;
	}

	public String toString() {
		return "OpsdSystsem with name = '" + name +
				"' alias = '" + alias +
				"' fqdnOrIp = '" + fqdnOrIp +
				"' deviceType = '" + (deviceType == null ? "" : deviceType.getName()) +
				"' os = '" + (os == null ? "" : os.getName()) +
				"' osAccess = '" + (osAccess == null ? "" : osAccess) +
				"' lomIP = '" + lomIP +
				"' lomAccess = '" + lomAccess +
				"' moreInfo = '" + moreInfo +
				"' environment = '" + environment +
				"' role = '" + (role == null ? "" : role.getName()) +
				"' hostDownRecoveryProcedure = '" + hostDownRecoveryProcedure +
				"' responsible = '" + (responsible == null ? "" : responsible.getName()) +
				"' scaleTo = '" + scaleTo + "'";

	}
	
}
