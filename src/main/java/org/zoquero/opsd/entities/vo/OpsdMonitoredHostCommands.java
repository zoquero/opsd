/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import javax.management.RuntimeErrorException;

import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdProject;

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
	
	/** Has premium services? */
	private boolean premium;
	
	/** The relate Project */
	private OpsdProject project;
	
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
	private void setHostTemplate(String hostTemplate) {
		this.hostTemplate = hostTemplate;
	}
	/**
	 * @return the premium
	 */
	public boolean isPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	private void setPremium(boolean premium) {
		this.premium = premium;
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
	 * @return the project
	 */
	public OpsdProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	private void setProject(OpsdProject project) {
		this.project = project;
	}
	/**
	 * Initiate the fields with the commands, just called from the constructor
	 */
	private void fillCommands() {
		String _addHostFormat =
					OpsdConf.getProperty("monitoring.command.addHost");
		
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
		
		String ht = getHostTemplate();
		if(ht == null) {
			ht = "ERROR_NULL";
		}
		
		String resourceAcl;
		if(getProject() == null
				|| getProject().getResponsible() == null
				|| getProject().getResponsible().getResourceAcl() == null) {
			resourceAcl = "ERROR_NULL";
		}
		else {
			resourceAcl = getProject().getResponsible().getResourceAcl();
		}
		String premiumStr = isPremium() ? " -P" : "";
		
		// addHost
		String _addHostCommand = String.format(_addHostFormat,
					_name, _ip, ht, resourceAcl, premiumStr);
		setAddHostCommand(_addHostCommand);
		
		// delHost
		String _delHostCommand = String.format(_delHostFormat, _name);
		setDelHostCommand(_delHostCommand);
	}
	
	/**
	 * Constructor with fields
	 * @param monitoredHost
	 * @param hostTemplate
	 * @param premium
	 * @param project
	 */
	public OpsdMonitoredHostCommands(OpsdMonitoredHost monitoredHost, String hostTemplate, boolean premium, OpsdProject project) {
		setMonitoredHost(monitoredHost);
		setHostTemplate(hostTemplate);
		setPremium(premium);
		setProject(project);
		fillCommands();
	}
	
}
