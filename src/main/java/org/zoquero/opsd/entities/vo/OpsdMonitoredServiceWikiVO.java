/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import org.zoquero.opsd.entities.OpsdMonitoredService;

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
	private String addServiceCommand = null;

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
	 * @return the addServiceCommand
	 */
	public String getAddCommand() {
		return addServiceCommand;
	}
	/**
	 * @param addServiceCommand the addServiceCommand to set
	 */
	public void setAddCommand(String addServiceCommand) {
		this.addServiceCommand = addServiceCommand;
	}
	
	/**
	 * @param service
	 * @param wiki
	 * @param addServiceCommand
	 */
	public OpsdMonitoredServiceWikiVO(OpsdMonitoredService service,
			String wiki, String addServiceCommand) {
		this.service = service;
		this.wiki = wiki;
		this.addServiceCommand = addServiceCommand;
	}
}
