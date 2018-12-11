package org.zoquero.opsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.dao.OpsdDataTap;
import org.zoquero.opsd.dao.OpsdPoiConf;
import org.zoquero.opsd.dao.OpsdPoiDao;
import org.zoquero.opsd.entities.OpsdFilePolicy;
import org.zoquero.opsd.entities.OpsdHostService;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdMonitoredService;
import org.zoquero.opsd.entities.OpsdPeriodicTask;
import org.zoquero.opsd.entities.OpsdRequest;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdSystem;
import org.zoquero.opsd.entities.vo.OpsdFilePolicyVO;
import org.zoquero.opsd.entities.vo.OpsdMonitoredHostCommands;
import org.zoquero.opsd.entities.vo.OpsdMonitoredServiceWikiVO;
import org.zoquero.opsd.entities.vo.OpsdPeriodicTaskVO;
import org.zoquero.opsd.entities.vo.OpsdRequestVO;

/**
 * Accesses the Project objects throught a OpsdDataTap
 * and allows to get them in a OpsdFullProjectData object
 * @author agalindo
 *
 */
public class OpsdExtractor {

	OpsdDataTap dt;
	private static Logger LOGGER = Logger.getLogger(OpsdPoiDao.class.getName());

	/**
	 * Constructor that gets the DataTap
	 * @param dt
	 */
	public OpsdExtractor(OpsdDataTap dt) {
		this.dt = dt;
	}

	/**
	 * Get all the info from a Project
	 * @param projectName
	 * @return
	 * @throws OpsdException 
	 */
	public OpsdFullProjectData getFullProjectData(String projectName) throws OpsdException {
		OpsdReport oReport = new OpsdReport();
		LOGGER.log(Level.FINE,"Running OpsdExtractor.getFullProjectData()");
		OpsdFullProjectData fpd = new OpsdFullProjectData(oReport);
		
		// OpsdProject
		OpsdProject project = dt.getProject(projectName);
		fpd.setProject(project);
		
		// OpsdRole
		List<OpsdRole> roles = dt.getRoles(project);
		fpd.setRoles(roles);
		
		// OpsdSystem
		List<OpsdSystem> systems = dt.getSystems(project);
		fpd.setSystems(systems);

		// OpsdMonitoredHost
		List<OpsdMonitoredHost> monitoredHosts = dt.getMonitoredHosts(project);
		fpd.setMonitoredHosts(monitoredHosts);
		
		// environments (String)
		List<String> environments = dt.getEnvironments(project);
		fpd.setEnvironments(environments);
		
		
		// OpsdRoleServices
		List<OpsdRoleService> roleServices = dt.getRoleServices(project);
		fpd.setRoleServices(roleServices);

		// role2servicesMap
		HashMap<OpsdRole, List<OpsdRoleService>> role2servicesMap = new HashMap<OpsdRole, List<OpsdRoleService>>();
		for(OpsdRole aRole: roles) {
			List<OpsdRoleService> aRoleServices = dt.getRoleServicesByRole(project, aRole);
			role2servicesMap.put(aRole, aRoleServices);
		}
		fpd.setRole2servicesMap(role2servicesMap);

		// OpsdHostServices
		List<OpsdHostService> hostServices = dt.getHostServices(project);
		fpd.setHostServices(hostServices);

		// host2servicesMap
		HashMap<OpsdMonitoredHost, List<OpsdHostService>> host2servicesMap = new HashMap<OpsdMonitoredHost, List<OpsdHostService>>();
		for(OpsdMonitoredHost aHost: monitoredHosts) {
			List<OpsdHostService> aHostServices = dt.getHostServicesByHost(project, aHost);
			host2servicesMap.put(aHost, aHostServices);
		}
		fpd.setHost2servicesMap(host2servicesMap);

		
		// host2effectiveServicesMap: Mixing host2servicesMap and role2servicesMap
		// NOTE: This one may not be usefull at all finally
		HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>>
			host2effectiveServicesMap
				= new HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>>();
		for(OpsdMonitoredHost aHost: monitoredHosts) {
			List<OpsdMonitoredService> monitoredServices = new ArrayList<OpsdMonitoredService>();
			LOGGER.finer("Mixing effective services for host " + aHost.getName());
			OpsdRole role = aHost.getRole();
			if(role != null) {
				StringBuilder servicesDump = new StringBuilder("Services from its role '" + role.getName() + "': ");
				for(OpsdRoleService aRoleService: dt.getRoleServicesByRole(project, role)) {
					monitoredServices.add(aRoleService);
					servicesDump.append(aRoleService.getName() + ", ");
				}
				LOGGER.finer(servicesDump.toString());
				
			}
			monitoredServices.addAll(host2servicesMap.get(aHost));
			host2effectiveServicesMap.put(aHost, monitoredServices);

			// Troubleshooting Just for troubleshooting purposes
			StringBuilder servicesDump = new StringBuilder("Direct services: ");
			for(OpsdMonitoredService aService: host2servicesMap.get(aHost)) {
				servicesDump.append(aService.getName() + ", ");
			}
			LOGGER.finer(servicesDump.toString());
			// /Troubleshooting Just for troubleshooting purposes
		}
		fpd.setHost2effectiveServicesMap(host2effectiveServicesMap);
		

		// host2effectiveServicesMap: Mixing host2servicesMap and role2servicesMap
		HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>>
			host2effectiveServiceWikiVOMap
				= new HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>>();
		for(OpsdMonitoredHost aHost: monitoredHosts) {
			List<OpsdMonitoredServiceWikiVO> monitoredServicesAndWikis = new ArrayList<OpsdMonitoredServiceWikiVO>();
			LOGGER.finer("Mixing effective services for host " + aHost.getName());
			OpsdRole role = aHost.getRole();
			if(role != null) {
				StringBuilder servicesDump = new StringBuilder("Services from its role '" + role.getName() + "': ");
				for(OpsdRoleService aRoleService: dt.getRoleServicesByRole(project, role)) {
					// Lazy initialization, OpsdReportGenerator will set the wiki code.
					monitoredServicesAndWikis.add(new OpsdMonitoredServiceWikiVO(aRoleService, "unset, lazy init"));
					servicesDump.append(aRoleService.getName() + ", ");
				}
				LOGGER.finer(servicesDump.toString());
				
			}
			for(OpsdMonitoredService service: host2servicesMap.get(aHost)) {
				monitoredServicesAndWikis.add(new OpsdMonitoredServiceWikiVO(service, "unset, lazy init"));
			}
			host2effectiveServiceWikiVOMap.put(aHost, monitoredServicesAndWikis);

			// Troubleshooting Just for troubleshooting purposes
			StringBuilder servicesDump = new StringBuilder("Direct services: ");
			for(OpsdMonitoredService aService: host2servicesMap.get(aHost)) {
				servicesDump.append(aService.getName() + ", ");
			}
			LOGGER.finer(servicesDump.toString());
			// /Troubleshooting Just for troubleshooting purposes
		}
		fpd.setHost2effectiveServiceWikiVOMap(host2effectiveServiceWikiVOMap);
		
		// OpsdRequest
		List<OpsdRequest> requests = dt.getRequests(project);
		fpd.setRequests(requests);
		
		// OpsdRequestVO
		List<OpsdRequestVO> requestVOs = new ArrayList<OpsdRequestVO>();
		for(OpsdRequest aRequest: requests) {
			requestVOs.add(new OpsdRequestVO(aRequest, "unset, lazy init"));
		}
		fpd.setRequestVOs(requestVOs);
		
		// OpsdPeriodicTask
		List<OpsdPeriodicTask> periodicTasks = dt.getPeriodicTasks(project);
		fpd.setPeriodicTasks(periodicTasks);
		
		// OpsdPeriodicTaskVO
		List<OpsdPeriodicTaskVO> periodicTaskVOs = new ArrayList<OpsdPeriodicTaskVO>();
		for(OpsdPeriodicTask aPeriodicTask: periodicTasks) {
			periodicTaskVOs.add(new OpsdPeriodicTaskVO(aPeriodicTask, "unset, lazy init"));
		}
		fpd.setPeriodicTaskVOs(periodicTaskVOs);
		
		// OpsdFilePolicy
		List<OpsdFilePolicy> filePolicies = dt.getFilePolicies(project);
		fpd.setFilePolicies(filePolicies);
		
		// OpsdFilePolicyVO
		List<OpsdFilePolicyVO> filePolicyVOs = new ArrayList<OpsdFilePolicyVO>();
		for(OpsdFilePolicy aFilePolicy: filePolicies) {
			filePolicyVOs.add(new OpsdFilePolicyVO(aFilePolicy, "unset, lazy init"));
		}
		fpd.setFilePolicyVOs(filePolicyVOs);
		
		// monitoringScript
		Map<OpsdMonitoredHost, OpsdMonitoredHostCommands> monitoredHost2script
			= new HashMap<OpsdMonitoredHost, OpsdMonitoredHostCommands>();
		for(OpsdMonitoredHost aHost: fpd.getMonitoredHosts()) {
			String ht = getMonitoringHostTemplate(project, aHost);
			boolean premium = dt.hasPremiumServices(project, aHost);
			monitoredHost2script.put(aHost, new OpsdMonitoredHostCommands(aHost, ht, premium, project));
		}
		fpd.setMonitoredHost2script(monitoredHost2script);
		
		// Validation
		OpsdValidator.validate(fpd);
		
		return fpd;
	}

	/**
	 * Get the monitoring HostTemplate to setup that MonitoredHost in monitoring.
	 * 
	 * It returns null if there hasn't to be applied any HostTemplate:
	 * * If it's a floatingMonitoredHost (no tied to any system)
	 * * If the MonitoredHost is set to not be configured
	 * with the default services
	 * 
	 * @param project
	 * @param aHost
	 * @throws OpsdException
	 * @return
	 */
	private String getMonitoringHostTemplate(OpsdProject project, OpsdMonitoredHost aHost) throws OpsdException {
		String floatingHostname =
				OpsdPoiConf.getSystemNameForFloatingMonitoredHosts();
		
		if(aHost == null) {
			LOGGER.log(Level.SEVERE,"Asked to get a monitoring host template for a null host");
			return "ERROR_null";
		}
		if(! aHost.isDefaultChecksNeeded()) {
			LOGGER.log(Level.FINER,"Asked to get a monitoring host template "
					+ "for a host that hasn't checked 'defaultChecksNeeded'");
			return OpsdConf.getProperty("monitoring.serviceTemplate.minimum");
		}
		if(aHost.getSystem() == null) {
			LOGGER.log(Level.SEVERE,"Asked to get a monitoring host template for a host with null system");
			return "ERROR_null_02";
		}
		if(aHost.getSystem().getOs() == null) {
			if(aHost.getSystem().getName().equals(floatingHostname)) {
				LOGGER.log(Level.FINER,"Asked to get a monitoring host template "
						+ "for a floatingMonitoredHost (it's not an error)");
				return null;
			}
			else {
				LOGGER.log(Level.SEVERE,"Asked to get a monitoring host template for a host with system with null OS, so can't get the host template");
				return "ERROR_null_03";
			}
		}
		return OpsdConf.getProperty("monitoring.serviceTemplate.osPrefix")
				+ aHost.getSystem().getOs().getName();
	}

	
}
