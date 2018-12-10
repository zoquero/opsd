/**
 * 
 */
package org.zoquero.opsd.entities;

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;

/**
 * A Monitored Service that will be held
 * by a croncret MonitoredHost.
 * 
 * If multiple hosts should have the same service then it would be better
 * to group them together in a OpsdRole and create a OpsdRoleServcie instead.
 * @author agalindo
 *
 */
public class OpsdHostService extends OpsdMonitoredService {
	
	/**
	 * The host
	 */
	private OpsdMonitoredHost host;

	/**
	 * @return the host
	 */
	public OpsdMonitoredHost getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	private void setHost(OpsdMonitoredHost host) {
		this.host = host;
	}

	/**
	 * @param name
	 * @param description
	 * @param procedure
	 * @param criticity
	 * @param host
	 * @param serviceTemplate
	 * @param macroAndValueArray
	 * @param scaleTo
	 */
	public OpsdHostService(String name, String description,
			String procedure, OpsdCriticity criticity,
			OpsdMonitoredHost host,
			OpsdServiceTemplate serviceTemplate, String[] macroAndValueArray,
			String scaleTo) {
		super(name, description, procedure, criticity,
				serviceTemplate, macroAndValueArray, scaleTo);
		setHost(host);
	}
}
