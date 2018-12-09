/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import org.zoquero.opsd.entities.OpsdPeriodicTask;

/**
 * Value object containing a OpsdPeriodicTask and String (wiki) pair.
 * It's needed because Freemarker templates
 * can't interpolate using variables as keys.
 * @author agalindo
 *
 */
public class OpsdPeriodicTaskVO {

	private OpsdPeriodicTask periodicTask;
	private String wiki;
	/**
	 * @return the periodicTask
	 */
	public OpsdPeriodicTask getPeriodicTask() {
		return periodicTask;
	}
	/**
	 * @param periodicTask the periodicTask to set
	 */
	public void setPeriodicTask(OpsdPeriodicTask periodicTask) {
		this.periodicTask = periodicTask;
	}
	/**
	 * @return the wiki
	 */
	public String getWiki() {
		return wiki;
	}
	/**
	 * @param wiki the wiki to set
	 */
	public void setWiki(String wiki) {
		this.wiki = wiki;
	}
	/**
	 * @param periodicTask
	 * @param wiki
	 */
	public OpsdPeriodicTaskVO(OpsdPeriodicTask periodicTask, String wiki) {
		this.periodicTask = periodicTask;
		this.wiki = wiki;
	}
}
