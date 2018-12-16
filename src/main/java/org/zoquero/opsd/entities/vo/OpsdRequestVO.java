/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import org.zoquero.opsd.entities.OpsdRequest;

/**
 * Value object containing a OpsdRequest and String (wiki) pair.
 * It's needed because Freemarker templates
 * can't interpolate using variables as keys.
 * @author agalindo
 *
 */
public class OpsdRequestVO {

	private OpsdRequest request;
	private String wiki;
	/**
	 * @return the request
	 */
	public OpsdRequest getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(OpsdRequest request) {
		this.request = request;
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
	 * @param request
	 * @param wiki
	 */
	public OpsdRequestVO(OpsdRequest request, String wiki) {
		this.request = request;
		this.wiki = wiki;
	}
}
