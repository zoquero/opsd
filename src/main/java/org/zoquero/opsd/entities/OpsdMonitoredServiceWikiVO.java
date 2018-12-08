/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * Value object containing a OpsdMonitoredService and String (wiki) pair.
 * It's needed because Freemarker templates
 * can't interpolate using variables as keys.
 * @author agalindo
 *
 */
public class OpsdMonitoredServiceWikiVO {

	private OpsdMonitoredService service = null;
	private String wiki = null;

	/**
	 * @return the service
	 */
	public OpsdMonitoredService getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	private void setService(OpsdMonitoredService service) {
		this.service = service;
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
	 * @param service
	 * @param wiki
	 */
	public OpsdMonitoredServiceWikiVO(OpsdMonitoredService service, String wiki) {
		this.service = service;
		this.wiki = wiki;
	}
	
}
