/**
 * 
 */
package org.zoquero.opsd.entities;

import java.util.List;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

/**
 * @author agalindo
 *
 */
public class OpsdPollerType implements Comparable<OpsdPollerType> {

	private int id;
	private String name;
	private String description;
	private List<String> networks;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the networks
	 */
	public List<String> getNetworks() {
		return networks;
	}
	/**
	 * @param networks the networks to set
	 */
	public void setNetworks(List<String> networks) {
		this.networks = networks;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param networks
	 */
	public OpsdPollerType(int id, String name, String description,
			List<String> networks) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.networks = networks;
	}
	
	public String toString() {
		String networks = "";
		if(getNetworks() != null) {
			networks = String.join(", ", getNetworks());
		}
		return getClass().getSimpleName() + " of type " + getId() + " '" + getName()
				+ "' with description '" + getDescription() + "'"
				+ " and networks " + networks;
	}

	/**
	 * Will check PollerTypes one by one, last one is catch-all.
	 * @param ip
	 * @param pollerTypes
	 * @return
	 */
	public static OpsdPollerType getPollerForIp(String ip, List<OpsdPollerType> pollerTypes) {
		if(ip == null)
			return null;
		if(pollerTypes == null)
			return null;
		
		for(int i = 0; i < pollerTypes.size(); i++) {
			OpsdPollerType aPollertype = pollerTypes.get(i);
			
			// Last one is catch-all.
			if(i == pollerTypes.size() -1) {
				return aPollertype;
			}
			for(String aNetwork: aPollertype.getNetworks()) {
				if(aNetwork != null) {
					String[] parts = aNetwork.split("/", -1);
					if(parts != null && parts.length > 1) {
						String net, mask;
						net  = parts[0];
						mask = parts[1];
						if(isIpInNetwork(ip, net, mask)) {
							return aPollertype;
						}						
					}
				}
			}
		}
		return null;
	}
	private static boolean isIpInNetwork(String ip, String network, String mask) {
		SubnetInfo subnet = (new SubnetUtils(network, mask)).getInfo();
		return subnet.isInRange(ip);
	}
	
	@Override
	public int compareTo(OpsdPollerType pt) {
	     return(getId() - pt.getId());
	}
}
