/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import org.zoquero.opsd.entities.OpsdFilePolicy;

/**
 * @author agalindo
 *
 */
public class OpsdFilePolicyVO {

	private OpsdFilePolicy filePolicy;
	private String wiki;
	/**
	 * @return the filePolicy
	 */
	public OpsdFilePolicy getFilePolicy() {
		return filePolicy;
	}
	/**
	 * @param filePolicy the filePolicy to set
	 */
	public void setFilePolicy(OpsdFilePolicy filePolicy) {
		this.filePolicy = filePolicy;
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
	 * @param filePolicy
	 * @param wiki
	 */
	public OpsdFilePolicyVO(OpsdFilePolicy filePolicy, String wiki) {
		this.filePolicy = filePolicy;
		this.wiki = wiki;
	}
	
}
