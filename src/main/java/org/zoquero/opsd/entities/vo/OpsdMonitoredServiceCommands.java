/**
 * 
 */
package org.zoquero.opsd.entities.vo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.RuntimeErrorException;

import org.zoquero.opsd.OpsdException;
import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.dao.OpsdPoiDao;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdMonitoredService;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdServiceMacroDefinition;

/**
 * Object to hold commands to add and delete MonitoredServices
 * @author agalindo
 *
 */
public class OpsdMonitoredServiceCommands {

	private static Logger LOGGER = Logger.getLogger(OpsdMonitoredServiceCommands.class.getName());
	
	/** Service to which these commands are related .*/
	private OpsdMonitoredService monitoredService;
	
	/** MonitoredHost to which this service is associated. */
	private OpsdMonitoredHost monitoredHost;
	
	/** The relate Project */
	private OpsdProject project;
	
	/** Command to add the service to the monitoring system*/
	private String addServiceCommand;
	
	/** Command to remove the service from the monitoring system*/
	private String delServiceCommand;
	
	/**
	 * @return the monitoredService
	 */
	public OpsdMonitoredService getMonitoredService() {
		return monitoredService;
	}
	/**
	 * @param monitoredService the monitoredService to set
	 */
	private void setMonitoredService(OpsdMonitoredService monitoredService) {
		this.monitoredService = monitoredService;
	}
	/**
	 * @return the monitoredHost
	 */
	public OpsdMonitoredHost getMonitoredHost() {
		return monitoredHost;
	}
	/**
	 * @param monitoredHost the monitoredHost to set
	 */
	public void setMonitoredHost(OpsdMonitoredHost monitoredHost) {
		this.monitoredHost = monitoredHost;
	}
	/**
	 * @return the addServiceCommand
	 */
	public String getAddServiceCommand() {
		return addServiceCommand;
	}
	/**
	 * @param addServiceCommand the addServiceCommand to set
	 */
	private void setAddServiceCommand(String addServiceCommand) {
		this.addServiceCommand = addServiceCommand;
	}
	/**
	 * @return the delServiceCommand
	 */
	public String getDelServiceCommand() {
		return delServiceCommand;
	}
	/**
	 * @param delServiceCommand the delServiceCommand to set
	 */
	private void setDelServiceCommand(String delServiceCommand) {
		this.delServiceCommand = delServiceCommand;
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
		String _addServiceFormat =
					OpsdConf.getProperty("monitoring.command.addService");
		
		String _delServiceFormat = OpsdConf.getProperty("monitoring.command.delService");
		
		int numMacros;
		try {
			numMacros = OpsdConf.getNumMacros();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, "Can't read the number of macros: "
					+ e.getMessage(), e);
			// Will be detected
			numMacros = 100;
		}
		
		String _name;
		if(getMonitoredService() == null
				|| getMonitoredService().getName() == null
				|| getMonitoredService().getName().equals("")) {
			_name = "ERROR_NULL";
		}
		else {
			_name = getMonitoredService().getName();
		}

		String _host;
		if(getMonitoredHost() == null
				|| getMonitoredHost().getName() == null) {
			_host = "ERROR_NULL";
		}
		else {
			_host = getMonitoredHost().getName();
		}
		
		String st;
		if(getMonitoredService() == null
			|| getMonitoredService().getServiceTemplate() == null
			|| getMonitoredService().getServiceTemplate().getName() == null) {
			st = "ERROR_NULL";
		}
		else {
			st = getMonitoredService().getServiceTemplate().getName();
		}
		
		String premiumStr;
		if(getMonitoredService() == null) {
			premiumStr = "ERROR_NULL";
		}
		else {
			premiumStr = getMonitoredService().isPremium() ? "-P" : "";
		}
		
		StringBuilder macros = new StringBuilder("-M ");
		String macrosStr;
		boolean found = false;
		
		for(int i = 0; i < numMacros; i ++) {
			String[] mva = getMonitoredService().getMacroValuesArray();
			if( mva != null && mva[i] != null) {
				if(getMonitoredService().getServiceTemplate() != null
						&& getMonitoredService().getServiceTemplate().getMacroDefinitions() != null 
						&& getMonitoredService().getServiceTemplate().getMacroDefinitions().get(i) != null
						&& getMonitoredService().getServiceTemplate().getMacroDefinitions().get(i).getName() != null
						&& ! getMonitoredService().getServiceTemplate().getMacroDefinitions().get(i).getName().equals("")) {
					OpsdServiceMacroDefinition smd = getMonitoredService().getServiceTemplate().getMacroDefinitions().get(i);
					macros.append(smd.getName() + "=\"" + mva[i] + "\" ");
					found = true;
				}
			}
		}
		if(found) {
			macrosStr = macros.toString();
		}
		else {
			macrosStr = "";
		}
		
		// addService
		// AddService.pl -H %s -s %s -T %s %s
		String _addServiceCommand = String.format(_addServiceFormat,
				_host, _name, st, macrosStr, premiumStr);
		setAddServiceCommand(_addServiceCommand);
		
		// delService
		String _delServiceCommand = String.format(_delServiceFormat, _name, _host);
		setDelServiceCommand(_delServiceCommand);
	}
	
	/**
	 * Constructor with fields
	 * @param monitoredService
	 * @param project
	 */
	public OpsdMonitoredServiceCommands(OpsdMonitoredService monitoredService,
			OpsdMonitoredHost host, OpsdProject project) {
		setMonitoredService(monitoredService);
		setMonitoredHost(host);
		setProject(project);
		fillCommands();
	}
	
}
