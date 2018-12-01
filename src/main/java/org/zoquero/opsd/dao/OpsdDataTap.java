package org.zoquero.opsd.dao;

import java.util.List;

import org.zoquero.opsd.OpsdException;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdDeviceType;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdOSType;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdProject;
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
}
