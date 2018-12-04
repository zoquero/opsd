/**
 * 
 */
package org.zoquero.opsd.entities;

/**
 * Macro (name=value) for a montored service.
 * This allows to parameterize the monitored services
 * and set the concrete values on each host,
 * but sharing the same service definition (template)
 * @author agalindo
 *
 */
public class OpsdServiceMacro {
	private String name;
	private String value;
	
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	private void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public OpsdServiceMacro(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
}
