package org.zoquero.opsd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zoquero.opsd.dao.OpsdPoiConf;
import org.zoquero.opsd.entities.OpsdFilePolicy;
import org.zoquero.opsd.entities.OpsdHostService;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdMonitoredService;
import org.zoquero.opsd.entities.OpsdPeriodicTask;
import org.zoquero.opsd.entities.OpsdPollerType;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRequest;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdServiceTemplate;
import org.zoquero.opsd.entities.OpsdSystem;
import org.zoquero.opsd.entities.vo.OpsdFilePolicyVO;
import org.zoquero.opsd.entities.vo.OpsdMonitoredHostCommands;
import org.zoquero.opsd.entities.vo.OpsdMonitoredServiceCommands;
import org.zoquero.opsd.entities.vo.OpsdMonitoredServiceWikiVO;
import org.zoquero.opsd.entities.vo.OpsdPeriodicTaskVO;
import org.zoquero.opsd.entities.vo.OpsdRequestVO;

class OpsdFullProjectData {

	/** Report with errors and warnings */
	private OpsdReport  report;
	/** Project data */
	private OpsdProject project;
	/** Role data */
	private List<OpsdRole> roles;
	/** Systems data */
	private List<OpsdSystem> systems;
	/** MonitoredHosts data */
	private List<OpsdMonitoredHost> monitoredHosts;
	/** Environments */
	private List<String> environments;
	/** RoleServices data */
	private List<OpsdRoleService> roleServices;
	/** Map Role > RoleService
	 * that will be very usefull generating the output */
	private HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap = null;
	/** HostServices data */
	private List<OpsdHostService> hostServices;
	/** Map MonitoredHost > HostService
	 * that will be very usefull generating the output */
	private HashMap<OpsdMonitoredHost, List<OpsdHostService>> host2servicesMap = null;
	/**
	 * Mix of host2servicesMap and role2servicesMap:
	 * it contains all the OpsdMonitoredService for a OpsdHost:
	 * Both the ones directly associated with that host
	 * and also the ones from the OpsdRole to which the host belongs.
	 */
	private HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>>
		host2effectiveServicesMap
			= new HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>>();
	
	
	private HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>> host2effectiveServiceWikiVOMap;
	
	
	/** Requests */
	private List<OpsdRequest> requests;
	/** Value Object with Requests and it's wiki representations*/
	private List<OpsdRequestVO> requestVOs;
	/** Periodic tasks */
	private List<OpsdPeriodicTask> periodicTasks;
	/** Value Object with PeriodicTasks and it's wiki representations*/
	private List<OpsdPeriodicTaskVO> periodicTaskVOs;
	/** FilePolicy */
	private List<OpsdFilePolicy> filePolicies;
	/** FilePolicyVOs */
	private List<OpsdFilePolicyVO> filePolicyVOs;
	/** Commands to setup monitoring for that MonitoredHost */
	private Map<OpsdMonitoredHost, OpsdMonitoredHostCommands> monitoredHostCommands;
	private List<OpsdServiceTemplate> serviceTemplates;
	private Map<OpsdHostService, OpsdMonitoredServiceCommands> monitoredEffectiveHostServiceCommands;
	private List<OpsdPollerType> pollerTypes;

	
	OpsdFullProjectData(OpsdReport oReport) {
		setReport(oReport);
	}
	
	/**
	 * @return the report
	 */
	public OpsdReport getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	private void setReport(OpsdReport report) {
		this.report = report;
	}

	public void setProject(OpsdProject project) {
		this.project = project;
	}

	/**
	 * @return the project
	 */
	public OpsdProject getProject() {
		return project;
	}

	/**
	 * @return the roles
	 */
	public List<OpsdRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles the role to set
	 */
	public void setRoles(List<OpsdRole> roles) {
		this.roles = roles;
	}
	
	/**
	 * @return the systems
	 */
	public List<OpsdSystem> getSystems() {
		return systems;
	}

	/**
	 * @param systems the system to set
	 */
	public void setSystems(List<OpsdSystem> systems) {
		this.systems = systems;
	}

	/**
	 * @return the monitoredHosts
	 */
	public List<OpsdMonitoredHost> getMonitoredHosts() {
		return monitoredHosts;
	}

	/**
	 * @param monitoredHosts the monitoredHosts to set
	 */
	public void setMonitoredHosts(List<OpsdMonitoredHost> monitoredHosts) {
		this.monitoredHosts = monitoredHosts;
	}

	/**
	 * @return the environments
	 */
	public List<String> getEnvironments() {
		return environments;
	}

	/**
	 * @param environments the environments to set
	 */
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}

	/**
	 * @return the roleServices
	 */
	public List<OpsdRoleService> getRoleServices() {
		return roleServices;
	}

	/**
	 * @param roleServices the roleServices to set
	 */
	public void setRoleServices(List<OpsdRoleService> roleServices) {
		this.roleServices = roleServices;
	}

	/**
	 * @return the role2servicesMap
	 */
	public HashMap<OpsdRole, List<OpsdRoleService>> getRole2servicesMap() {
		return role2servicesMap;
	}

	/**
	 * @param role2servicesMap the role2servicesMap to set
	 */
	public void setRole2servicesMap(
			HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap) {
		this.role2servicesMap = role2servicesMap;
	}

	/**
	 * @return the hostServices
	 */
	public List<OpsdHostService> getHostServices() {
		return hostServices;
	}

	/**
	 * @param hostServices the hostServices to set
	 */
	public void setHostServices(List<OpsdHostService> hostServices) {
		this.hostServices = hostServices;
	}

	/**
	 * @return the host2servicesMap
	 */
	public HashMap<OpsdMonitoredHost, List<OpsdHostService>> getHost2servicesMap() {
		return host2servicesMap;
	}

	/**
	 * @param host2servicesMap the host2servicesMap to set
	 */
	public void setHost2servicesMap(
			HashMap<OpsdMonitoredHost, List<OpsdHostService>> host2servicesMap) {
		this.host2servicesMap = host2servicesMap;
	}

	/**
	 * @return the host2effectiveServicesMap
	 */
	public HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>> getHost2effectiveServicesMap() {
		return host2effectiveServicesMap;
	}

	/**
	 * @param host2effectiveServicesMap the host2effectiveServicesMap to set
	 */
	public void setHost2effectiveServicesMap(
			HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>> host2effectiveServicesMap) {
		this.host2effectiveServicesMap = host2effectiveServicesMap;
	}

	/**
	 * @return the host2effectiveServiceWikiVOMap
	 */
	public HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>> getHost2effectiveServiceWikiVOMap() {
		return host2effectiveServiceWikiVOMap;
	}

	/**
	 * @param host2effectiveServiceWikiVOMap the host2effectiveServiceWikiVOMap to set
	 */
	public void setHost2effectiveServiceWikiVOMap(
			HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>> host2effectiveServiceWikiVOMap) {
		this.host2effectiveServiceWikiVOMap = host2effectiveServiceWikiVOMap;
	}

	/**
	 * @return the requests
	 */
	public List<OpsdRequest> getRequests() {
		return requests;
	}

	/**
	 * @param requests the requests to set, hoooo :')
	 */
	public void setRequests(List<OpsdRequest> requests) {
		this.requests = requests;
	}

	/**
	 * @return the requestVOs
	 */
	public List<OpsdRequestVO> getRequestVOs() {
		return requestVOs;
	}

	/**
	 * @param requestVOs the requestVOs to set
	 */
	public void setRequestVOs(List<OpsdRequestVO> requestVOs) {
		this.requestVOs = requestVOs;
	}

	/**
	 * @return the periodicTasks
	 */
	public List<OpsdPeriodicTask> getPeriodicTasks() {
		return periodicTasks;
	}

	/**
	 * @param periodicTasks the periodicTasks to set
	 */
	public void setPeriodicTasks(List<OpsdPeriodicTask> periodicTasks) {
		this.periodicTasks = periodicTasks;
	}

	/**
	 * @return the periodicTaskVOs
	 */
	public List<OpsdPeriodicTaskVO> getPeriodicTaskVOs() {
		return periodicTaskVOs;
	}

	/**
	 * @param periodicTaskVOs the periodicTaskVOs to set
	 */
	public void setPeriodicTaskVOs(List<OpsdPeriodicTaskVO> periodicTaskVOs) {
		this.periodicTaskVOs = periodicTaskVOs;
	}

	/**
	 * @return the filePolicies
	 */
	public List<OpsdFilePolicy> getFilePolicies() {
		return filePolicies;
	}

	/**
	 * @param filePolicies the filePolicies to set
	 */
	public void setFilePolicies(List<OpsdFilePolicy> filePolicies) {
		this.filePolicies = filePolicies;
	}

	/**
	 * @return the filePolicyVOs
	 */
	public List<OpsdFilePolicyVO> getFilePolicyVOs() {
		return filePolicyVOs;
	}

	/**
	 * @param filePolicyVOs the filePolicyVOs to set
	 */
	public void setFilePolicyVOs(List<OpsdFilePolicyVO> filePolicyVOs) {
		this.filePolicyVOs = filePolicyVOs;
	}

	/**
	 * @return the monitoredHostCommands
	 */
	public Map<OpsdMonitoredHost, OpsdMonitoredHostCommands> getMonitoredHostCommands() {
		return monitoredHostCommands;
	}

	/**
	 * @param monitoredHostCommands the monitoredHostCommands to set
	 */
	public void setMonitoredHostCommands(Map<OpsdMonitoredHost, OpsdMonitoredHostCommands> monitoredHostCommands) {
		this.monitoredHostCommands = monitoredHostCommands;
	}

	/**
	 * @return the serviceTemplates
	 */
	public List<OpsdServiceTemplate> getServiceTemplates() {
		return serviceTemplates;
	}

	/**
	 * @param serviceTemplates the serviceTemplates to set
	 */
	public void setServiceTemplates(List<OpsdServiceTemplate> serviceTemplates) {
		this.serviceTemplates = serviceTemplates;
	}

	/**
	 * @return the monitoredEffectiveHostServiceCommands
	 */
	public Map<OpsdHostService, OpsdMonitoredServiceCommands> getMonitoredEffectiveHostServiceCommands() {
		return monitoredEffectiveHostServiceCommands;
	}

	/**
	 * @param monitoredEffectiveHostServiceCommands the monitoredEffectiveHostServiceCommands to set
	 */
	public void setMonitoredEffectiveHostServiceCommands(
			Map<OpsdHostService, OpsdMonitoredServiceCommands> monitoredEffectiveHostServiceCommands) {
		this.monitoredEffectiveHostServiceCommands = monitoredEffectiveHostServiceCommands;
	}

	/**
	 * @return the pollerTypes
	 */
	public List<OpsdPollerType> getPollerTypes() {
		return pollerTypes;
	}

	/**
	 * @param pollerTypes the pollerTypes to set
	 */
	public void setPollerTypes(List<OpsdPollerType> pollerTypes) {
		this.pollerTypes = pollerTypes;
	}

}
