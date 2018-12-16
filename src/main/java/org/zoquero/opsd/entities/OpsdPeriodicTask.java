/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * @author agalindo
 *
 */
public class OpsdPeriodicTask {

	/* Fields */
	
	/** Field "name" */
	private String name;

	/** Periodicity in crontab (5) format.
	 **/
	private String periodicity;

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
	 * @return the periodicity
	 */
	public String getPeriodicity() {
		return periodicity;
	}

	/**
	 * @param periodicity the periodicity to set
	 */
	private void setAuthorized(String periodicity) {
		this.periodicity = periodicity;
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
	 * @param periodicity
	 * @param procedure
	 * @param scaleTo
	 */
	public OpsdPeriodicTask(String name, String periodicity, String procedure,
			String scaleTo) {
		this.name = name;
		this.periodicity = periodicity;
		this.procedure = procedure;
		this.scaleTo = scaleTo;
	}

	public String toString() {
		return getClass().getSimpleName() + " with " +
				"name = '" + getName()
				+ "', periodicity = '"
				+ (getPeriodicity() == null ? "null" : getPeriodicity())
			    + "' procedure = '"
				+ (getProcedure() == null ? " null " : getProcedure())
			    + "' scaleTo = '"
				+ (getScaleTo()   == null ? " null " : getScaleTo());
	}

}
