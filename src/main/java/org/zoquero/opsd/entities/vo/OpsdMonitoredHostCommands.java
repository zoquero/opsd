/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import javax.management.RuntimeErrorException;

import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.entities.OpsdMonitoredHost;

/**
 * Object to hold commands to add and delete MonitoredHosts
 * @author agalindo
 *
 */
public class OpsdMonitoredHostCommands {

	/** Host to which these commands are related .*/
	private OpsdMonitoredHost monitoredHost;
	
	/** ServiceTemplate in monitoring */
	private String hostTemplate;
	
	/** Command to add the host to the monitoring system*/
	private String addHostCommand;
	
	/** Command to remove the host from the monitoring system*/
	private String delHostCommand;
	
	/**
	 * @return the monitoredHost
	 */
	public OpsdMonitoredHost getMonitoredHost() {
		return monitoredHost;
	}
	/**
	 * @param monitoredHost the monitoredHost to set
	 */
	private void setMonitoredHost(OpsdMonitoredHost monitoredHost) {
		this.monitoredHost = monitoredHost;
	}
	/**
	 * @return the hostTemplate
	 */
	public String getHostTemplate() {
		return hostTemplate;
	}
	/**
	 * @param hostTemplate the hostTemplate to set
	 */
	public void setHostTemplate(String hostTemplate) {
		this.hostTemplate = hostTemplate;
	}
	/**
	 * @return the addHostCommand
	 */
	public String getAddHostCommand() {
		return addHostCommand;
	}
	/**
	 * @param addHostCommand the addHostCommand to set
	 */
	private void setAddHostCommand(String addHostCommand) {
		this.addHostCommand = addHostCommand;
	}
	/**
	 * @return the delHostCommand
	 */
	public String getDelHostCommand() {
		return delHostCommand;
	}
	/**
	 * @param delHostCommand the delHostCommand to set
	 */
	private void setDelHostCommand(String delHostCommand) {
		this.delHostCommand = delHostCommand;
	}
	
	/**
	 * Initiate the fields with the commands, just called from the constructor
	 */
	private void fillCommands() {
		String _addHostFormat =
					OpsdConf.getProperty("monitoring.command.addHost");
		String _addHostFormatWithServiceTemplate =
					OpsdConf.getProperty("monitoring.command.addHostWithServiceTemplate");
		
		String _delHostFormat = OpsdConf.getProperty("monitoring.command.delHost");
		
		String _name, _ip;
		if(getMonitoredHost() == null
				|| getMonitoredHost().getName() == null
				|| getMonitoredHost().getName().equals("")) {
			_name = "ERROR_NULL";
		}
		else {
			_name = getMonitoredHost().getName();
		}
		if(getMonitoredHost() == null
				|| getMonitoredHost().getIp() == null
				|| getMonitoredHost().getIp().equals("")) {
			_ip = "ERROR_NULL";
		}
		else {
			_ip = getMonitoredHost().getIp();
		}
		
		String _addHostCommand;
		if(getHostTemplate() == null) {
			_addHostCommand = String.format(_addHostFormat,
					_name, _ip, getHostTemplate());
		}
		else {
			_addHostCommand = String.format(_addHostFormatWithServiceTemplate,
					_name, _ip, getHostTemplate());
		}
		setAddHostCommand(_addHostCommand);
		
		String _delHostCommand = String.format(_delHostFormat, _name);
		setDelHostCommand(_delHostCommand);
	}
	
	/**
	 * @param monitoredHost
	 * @param _hostTemplate
	 */
	public OpsdMonitoredHostCommands(OpsdMonitoredHost monitoredHost, String _hostTemplate) {
		setMonitoredHost(monitoredHost);
		setHostTemplate(_hostTemplate);
		fillCommands();
	}
	
}
