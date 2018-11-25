package org.zoquero.opsd.entities;

/**
 * Project responsible
 * @author agalindo
 */
public class Responsible {

	/* Fields */

	/** Field "name" */
	private String name;

	/** Field "email" */
	private String email;

	/** Field "department" */
	private String department;

	/** Field "centreonResourceAccess" */
	private String centreonResourceAccess;

	/** Field "moreInfo" */
	private String moreInfo;
	
	/* Constructors */
	
	/**
	 * Responsible constructor.
	 * @param name
	 * @param email
	 * @param department
	 * @param centreonResourceAccess
	 * @param moreInfo
	 */
	public Responsible(String name, String email, String department,
			String centreonResourceAccess, String moreInfo) {
		this.name = name;
		this.email = email;
		this.department = department;
		this.centreonResourceAccess = centreonResourceAccess;
		this.moreInfo = moreInfo;
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
	 * @return the centreonResourceAccess
	 */
	public String getCentreonResourceAccess() {
		return centreonResourceAccess;
	}

	/**
	 * @param centreonResourceAccess the centreonResourceAccess to set
	 */
	private void setCentreonResourceAccess(String centreonResourceAccess) {
		this.centreonResourceAccess = centreonResourceAccess;
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

}
