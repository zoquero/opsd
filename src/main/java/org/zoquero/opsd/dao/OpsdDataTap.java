package org.zoquero.opsd.dao;

import java.util.List;
import java.util.Set;

import org.zoquero.opsd.OpsdException;
import org.zoquero.opsd.entities.OpsdFilePolicy;
import org.zoquero.opsd.entities.OpsdHostService;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdDeviceType;
import org.zoquero.opsd.entities.OpsdPeriodicTask;
import org.zoquero.opsd.entities.OpsdRequest;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdOSType;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdServiceTemplate;
import org.zoquero.opsd.entities.OpsdSystem;

public interface OpsdDataTap {

	/**
	 * Connect to backend
	 * @throws OpsdException
	 */
	void connect() throws OpsdException;

	/**
	 * Disconnect from backend
	 * @throws OpsdException
	 */
	void disconnect() throws OpsdException;

	/**
	 * Gets info about a project
	 * @param projectName
	 * @return
	 * @throws OpsdException
	 */
	OpsdProject getProject(String projectName) throws OpsdException;

	/**
	 * Gets the roles of a Project
	 * @param project
	 * @return List of roles
	 * @throws OpsdException
	 */
	List<OpsdRole> getRoles(OpsdProject project) throws OpsdException;

	/**
	 * Get the Systems of a project
	 * @param project
	 * @return List of systems
	 * @throws OpsdException
	 */
	List<OpsdSystem> getSystems(OpsdProject project) throws OpsdException;

	/**
	 * Get a OpsdDeviceType by name
	 * @param devTypeName
	 * @return
	 * @throws OpsdException
	 */
	OpsdDeviceType getDeviceTypeByName(String devTypeName) throws OpsdException;
	
	/**
	 * Get a OpsdOSType by name
	 * @param osNameStr
	 * @return
	 * @throws OpsdException
	 */
	OpsdOSType getOSTypeByName(String osNameStr) throws OpsdException;
	
	/**
	 * Get a role by name
	 * @param project
	 * @param roleName
	 * @return
	 * @throws OpsdException
	 */
	OpsdRole getRoleByName(OpsdProject project, String roleName) throws OpsdException;
	
	/**
	 * Get a MonitoredHost by name
	 * @param project
	 * @param hostName
	 * @return
	 * @throws OpsdException
	 */
	OpsdMonitoredHost getHostByName(OpsdProject project, String hostName) throws OpsdException;

	/**
	 * Get the MonitoredHosts of a Project
	 * @param project
	 * @return
	 * @throws OpsdException
	 */
	List<OpsdMonitoredHost> getMonitoredHosts(OpsdProject project) throws OpsdException;
	
	/**
	 * Get a System from a Project by name
	 * @param project
	 * @param systemName
	 * @return
	 * @throws OpsdException
	 */
	OpsdSystem getSystemByName(OpsdProject project, String systemName) throws OpsdException;

	/**
	 * Get all OpsdRoleService objects
	 * @param project
	 * @return
	 * @throws OpsdException
	 */
	List<OpsdRoleService> getRoleServices(
			OpsdProject project) throws OpsdException;

	/**
	 * Get all the RoleServices for a Role
	 * @param project
	 * @param aRole
	 * @return
	 */
	List<OpsdRoleService> getRoleServicesByRole(OpsdProject project,
			OpsdRole aRole) throws OpsdException;
	
	/**
	 * Get all OpsdHostService objects
	 * @param project
	 * @return
	 * @throws OpsdException
	 */
	List<OpsdHostService> getHostServices(
			OpsdProject project) throws OpsdException;

	/**
	 * Get all the HostServices for a Host
	 * @param project
	 * @param aHost
	 * @return
	 * @throws OpsdException
	 */
	List<OpsdHostService> getHostServicesByHost(OpsdProject project,
			OpsdMonitoredHost aHost) throws OpsdException;

	/**
	 * Get all types of possible requests for a Project
	 * (those tasks that can be requested by ticketing)
	 * @param project
	 * @return
	 * @throws OpsdException
	 */
	List<OpsdRequest> getRequests(OpsdProject project) throws OpsdException;
	

	/**
	 * Gets the name of the environments of a Project
	 * @param project
	 * @return Set of environments
	 * @throws OpsdException
	 */
	List<String> getEnvironments(OpsdProject project) throws OpsdException;
	

	/**
	 * Gets the periodic tasks of a Project
	 * @param project
	 * @return List of periodic tasks
	 * @throws OpsdException
	 */
	List<OpsdPeriodicTask> getPeriodicTasks(OpsdProject project) throws OpsdException;
	
	/**
	 * Gets the file policies of a Project
	 * @param project
	 * @return List of file policies
	 * @throws OpsdException
	 */
	List<OpsdFilePolicy> getFilePolicies(OpsdProject project) throws OpsdException;

	/**
	 * Tells if a MonitoredHost has any premium service
	 * @param project
	 * @param aHost
	 * @return
	 * @throws OpsdException
	 */
	boolean hasPremiumServices(OpsdProject project, OpsdMonitoredHost aHost) throws OpsdException;
	
	/**
	 * Get all the OpsdServiceTemplate objects. On a future release all data
	 * will be on a Relational Database. By now, the allowed
	 * cachedServiceTemplates must be described here, not in the Excel file.
	 * 
	 * @return
	 * @throws OpsdException 
	 */
	public List<OpsdServiceTemplate> getServiceTemplates() throws OpsdException;

	/**
	 * Get a ServiceTemplate
	 * @param serviceTemplateName name of the service template
	 * @return
	 * @throws OpsdException
	 */
	public OpsdServiceTemplate getServiceTemplate(String serviceTemplateName) throws OpsdException;

}
