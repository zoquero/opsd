/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * Something that can be requested by a Ticket
 * 
 * @author agalindo
 * 
 */
public class OpsdRequest {

	/* Fields */
	
	/** Field "name" */
	private String name;

	/** Field "authorized", who can launch this request.
	 * In a future release it all will be in the database
	 * and the authorized ones will be a list of identities.
	 **/
	private String authorized;

	/** Field "procedure", how to serve this request */
	private String procedure;

	/**
	 * Field "scaleTo", if the procedure fails and it can't be solved,
	 * who should it be scaled to?
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
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the authorized
	 */
	public String getAuthorized() {
		return authorized;
	}

	/**
	 * @param authorized the authorized to set
	 */
	private void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	/**
	 * @return the procedure
	 */
	public String getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure the procedure to set
	 */
	private void setProcedure(String procedure) {
		this.procedure = procedure;
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
	 * @param authorized
	 * @param procedure
	 * @param scaleTo
	 */
	public OpsdRequest(String name, String authorized, String procedure,
			String scaleTo) {
		this.name = name;
		this.authorized = authorized;
		this.procedure = procedure;
		this.scaleTo = scaleTo;
	}

	public String toString() {
		return "OpsdRequest with " +
				"name = '" + getName()
				+ "', authorized = '"
				+ (getAuthorized() == null? "null" : getAuthorized())
			    + "' procedure = '"
				+ (getProcedure() == null ? " null " : getProcedure())
				+ (getScaleTo()   == null ? " null " : getScaleTo());
	}

}
