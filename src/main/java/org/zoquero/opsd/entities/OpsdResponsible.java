/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
/**
 * @author agalindo
 *
 */
public class OpsdResponsible {

	private String name;
	private String email;
	private String department;
	/** Tag used to give visibility to the manager
	 * to the monitoring of the resources of his projects. */
	private String resourceAcl;
	private String moreInfo;
	
	/**
	 * @param name
	 * @param email
	 * @param department
	 * @param resourceAcl
	 * @param moreInfo
	 */
	public OpsdResponsible(String name, String email, String department,
			String resourceAcl, String moreInfo) {
		this.name = name;
		this.email = email;
		this.department = department;
		this.resourceAcl = resourceAcl;
		this.moreInfo = moreInfo;
	}
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	private void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	private void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the resourceAcl
	 */
	public String getResourceAcl() {
		return resourceAcl;
	}
	/**
	 * @param resourceAcl the resourceAcl to set
	 */
	private void setResourceAcl(String resourceAcl) {
		this.resourceAcl = resourceAcl;
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
	
	public String toString() {
		return "Responsible with name " + getName() + ", email = " + getEmail()
				+ ", department = " + getDepartment() + ", resourceAcl = "
				+ getResourceAcl() + ", moreInfo = " + getMoreInfo();
	}
}
