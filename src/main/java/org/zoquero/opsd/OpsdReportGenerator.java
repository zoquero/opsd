/**
 * 
 */
package org.zoquero.opsd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.dao.OpsdPoiDao;
import org.zoquero.opsd.entities.OpsdFilePolicy;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdMonitoredService;
import org.zoquero.opsd.entities.OpsdPeriodicTask;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRequest;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdServiceTemplate;
import org.zoquero.opsd.entities.OpsdSystem;
import org.zoquero.opsd.entities.OpsdFilePolicy.ACTION_TYPE;
import org.zoquero.opsd.entities.vo.OpsdFilePolicyVO;
import org.zoquero.opsd.entities.vo.OpsdMonitoredServiceWikiVO;
import org.zoquero.opsd.entities.vo.OpsdPeriodicTaskVO;
import org.zoquero.opsd.entities.vo.OpsdRequestVO;


import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * Generates a file with the output
 * @author agalindo
 *
 */
public class OpsdReportGenerator {

	private OpsdFullProjectData fullProjectData;
	private static Logger LOGGER = Logger.getLogger(OpsdPoiDao.class.getName());
	
	/**
	 * @return the fullProjectData
	 */
	public OpsdFullProjectData getFullProjectData() {
		return fullProjectData;
	}

	/**
	 * @param fullProjectData the fullProjectData to set
	 */
	public void setFullProjectData(OpsdFullProjectData fullProjectData) {
		this.fullProjectData = fullProjectData;
	}

	public OpsdReportGenerator(OpsdFullProjectData ofpd) {
		this.setFullProjectData(ofpd);
	}
	
	public static Path createDirectory(String prefix) throws OpsdException {
		Path tempDirWithPrefix = null;
		try {
			tempDirWithPrefix = Files.createTempDirectory(prefix);
		} catch (IOException e) {
			throw new OpsdException("Can't create a temporary folder: " + e.getMessage(), e);
		}
		return tempDirWithPrefix;
	}

	/**
	 * Generate an output file with:
	 * <ul>
	 *   <li>Source of scripts to set up the monitoring</li>
	 *   <li>Source for mediawiki articles describing
	 *   		the project and its procedures</li>
	 * </ul>
	 * @param directory
	 * @return
	 * @throws OpsdException
	 */
	public String getOutputFile(Path directory) throws OpsdException {
		// 1. Configure FreeMarker
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		// We'll load the templates from org.zoquero.opsd.templates:
		cfg.setClassForTemplateLoading(this.getClass(), "templates");
		// Some other recommended settings:
		cfg.setIncompatibleImprovements(new Version(2, 3, 28));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.getDefault());
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		String now = sdf.format(new GregorianCalendar().getTime());

		// Let's process the template
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("title", "Validation of the project "
				+ "and generation of monitoring and documentation");
		input.put("wikiUrlBase",      OpsdConf.getWikiUrlBase());
		input.put("genDate",          now);
		input.put("project",          getFullProjectData().getProject());
		input.put("wikiProject",      getWikiFromProject());
		input.put("report",           getFullProjectData().getReport());
		input.put("roles",            getFullProjectData().getRoles());
		input.put("roles2wiki",       getWikiFromRoles());
		input.put("systems",          getFullProjectData().getSystems());
		input.put("systems2wiki",     getWikiFromSystems());
		
		// env > role > system
		input.put("env2role2systems", getEnvToRoleToSystems());
		input.put("env2role2monitoredHosts",   getEnvToRoleToMonitoredHosts());
		input.put("assetArticleNamePrefix", OpsdConf.getProperty("assetArticleNamePrefix"));
		
		input.put("monitoredHosts",   getFullProjectData().getMonitoredHosts());
		input.put("monitoredHosts2wiki", getWikiFromMonitoredHosts());
		input.put("roleServices",     getFullProjectData().getRoleServices());
		input.put("role2servicesMap", getFullProjectData().getRole2servicesMap());
		input.put("hostServices",     getFullProjectData().getHostServices());
		input.put("host2servicesMap", getFullProjectData().getHost2servicesMap());
		input.put("hosteffectiveServicesMap",
				getFullProjectData().getHost2effectiveServicesMap());
		for(OpsdMonitoredHost aHost: getFullProjectData().getHost2effectiveServicesMap().keySet()) {
			LOGGER.finer("Pushing to the template the services for the host '" + aHost.getName() + "'");
			for(OpsdMonitoredService aService: getFullProjectData().getHost2effectiveServicesMap().get(aHost)) {
				LOGGER.finer("* Service: '" + aService.getName() + "'");				
			}
		}
		
//		input.put("effectiveService2wikiMap",
//				getEffectiveService2wikiMap());

		// Wiki String is lazy initialized in serviceWiki value objects.
		fillWikiInHost2effectiveServiceWikiVOMap();
		// setHost2effectiveServiceWikiVOMap
		input.put("host2effectiveServiceWikiVOMap",
				getFullProjectData().getHost2effectiveServiceWikiVOMap());
		
		input.put("requests",         getFullProjectData().getRequests());
		// Wiki String is lazy initialized in requestWiki value objects.
		fillWikiInRequestList();
		input.put("requestVOs",
				getFullProjectData().getRequestVOs());
		
		input.put("periodicTasks",         getFullProjectData().getPeriodicTasks());
		// Wiki String is lazy initialized in periodicTaskWiki value objects.
		fillWikiInPeriodicTaskList();
		input.put("periodicTaskVOs",
				getFullProjectData().getPeriodicTaskVOs());
		
		input.put("filePolicies",   getFullProjectData().getFilePolicies());
		input.put("wikiFilePolicies",      getWikiFromFilePolicies());

		input.put("monitoredHost2script", getFullProjectData().getMonitoredHost2script());
		input.put("serviceTemplates", getFullProjectData().getServiceTemplates());


		// Let's get the template
		try {
			Template template = cfg.getTemplate("project.ftl");
			String filename = new File(directory.toString())
								+ File.separator + "project.html";
			Writer fileWriter = new FileWriter(filename);
		    template.process(input, fileWriter);
		    fileWriter.close();
		    return filename;
		    
		} catch (TemplateNotFoundException e) {
			throw new OpsdException("TemplateNotFoundException thrown", e);
		} catch (MalformedTemplateNameException e) {
			throw new OpsdException("MalformedTemplateNameException thrown", e);
		} catch (ParseException e) {
			throw new OpsdException("ParseException thrown", e);
		} catch (IOException e) {
			throw new OpsdException("IOException thrown", e);
		} catch (TemplateException e) {
			throw new OpsdException("TemplateException thrown", e);
		}
	}
	
	private void fillWikiInRequestList() {
		List<OpsdRequestVO> requestVOs = getFullProjectData().getRequestVOs();
		if(requestVOs == null) {
			LOGGER.severe("The list of requestVOs is null");
			return;
		}
		for(OpsdRequestVO requestVO: requestVOs) {
			if(requestVO != null) {
					if(requestVO.getRequest() == null) {
						LOGGER.finer("The list of requestVOs has a component with null request object field");
						continue;
					}
					requestVO.setWiki(request2wiki(requestVO.getRequest()));
			}
		}
	}
	
	private void fillWikiInPeriodicTaskList() {
		List<OpsdPeriodicTaskVO> periodicTaskVOs = getFullProjectData().getPeriodicTaskVOs();
		if(periodicTaskVOs == null) {
			LOGGER.severe("The list of periodicTaskVOs is null");
			return;
		}
		for(OpsdPeriodicTaskVO periodicTaskVO: periodicTaskVOs) {
			if(periodicTaskVO != null) {
					if(periodicTaskVO.getPeriodicTask() == null) {
						LOGGER.finer("The list of periodicTaskVOs has a component with null periodicTask object field");
						continue;
					}
					periodicTaskVO.setWiki(periodicTask2wiki(periodicTaskVO.getPeriodicTask()));
			}
		}
	}
	
	private void fillWikiInFilePolicyList() {
		List<OpsdFilePolicyVO> filePolicyVOs = getFullProjectData().getFilePolicyVOs();
		if(filePolicyVOs == null) {
			LOGGER.severe("The list of filePolicyVOs is null");
			return;
		}
		for(OpsdFilePolicyVO filePolicyVO: filePolicyVOs) {
			if(filePolicyVO != null) {
					if(filePolicyVO.getFilePolicy() == null) {
						LOGGER.finer("The list of filePolicyVOs has a component with null filePolicy object field");
						continue;
					}
					filePolicyVO.setWiki(filePolicy2wiki(filePolicyVO.getFilePolicy()));
			}
		}
	}

	private void fillWikiInHost2effectiveServiceWikiVOMap() {
		HashMap<OpsdMonitoredHost, List<OpsdMonitoredServiceWikiVO>> h
			= getFullProjectData().getHost2effectiveServiceWikiVOMap();
		if(h == null) {
			LOGGER.severe("The map Host > ServicesWiki is null");
			return;
		}
		for(List<OpsdMonitoredServiceWikiVO> serviceVOs: h.values()) {
			if(serviceVOs != null) {
				for(OpsdMonitoredServiceWikiVO serviceWikiVO: serviceVOs) {
					if(serviceWikiVO == null) {
						LOGGER.finer("The map Host > ServicesWiki contains a null service list for a host");
						continue;
					}
					if(serviceWikiVO.getService() == null) {
						LOGGER.finer("The map Host > ServicesWiki contains a null service on a VO");
						continue;
					}
					serviceWikiVO.setWiki(service2wiki(serviceWikiVO.getService()));
				}
			}
		}
		
	}

	private Map<OpsdMonitoredService, String> getEffectiveService2wikiMap() {
		Map<OpsdMonitoredService, String> effectiveService2wikiMap = new HashMap<OpsdMonitoredService, String>(); 
		HashMap<OpsdMonitoredHost, List<OpsdMonitoredService>> host2effectiveServicesMap = getFullProjectData().getHost2effectiveServicesMap();
		for(List<OpsdMonitoredService> serviceList: host2effectiveServicesMap.values()) {
			for(OpsdMonitoredService service: serviceList) {
				effectiveService2wikiMap.put(service, service2wiki(service));
			}
		}
		return effectiveService2wikiMap;
	}

	private String service2wiki(OpsdMonitoredService service) {
		if(service == null) {
			return MSG_NULL;
		}
		StringBuilder s = new StringBuilder();
		// Name from its base class
		s.append("{{" + OpsdConf.getWikiTemplateName(OpsdMonitoredService.class.getSimpleName()));
		s.append("|name="
					+ toNonNullableString(service.getName()));
		s.append("|project="
				+ toNonNullableString(getFullProjectData().getProject().getName()));
		s.append("|description="
				+ toNullableString(service.getDescription()));
		s.append("|procedure="
				+ toNullableString(service.getProcedure()));
		if(service.getCriticity() == null) {
			s.append("|criticity=" + MSG_NULL);
		}
		else {
			s.append("|criticity="
					+ toNullableString(service.getCriticity().getName()));
		}
		if(service.getServiceTemplate() == null) {
			s.append("|serviceTemplate=" + MSG_NULL);
		}
		else {
			s.append("|serviceTemplate="
					+ toNullableString(service.getServiceTemplate().getName()));
		}
		if(service.getScaleTo() == null || service.getScaleTo().equals("")) {
			if(getFullProjectData().getProject().getResponsible() == null) {
				s.append("|scaleTo=" + MSG_NULL);
			}
			else {
				s.append("|scaleTo=[["
						+ toNonNullableString(getFullProjectData().getProject().getResponsible().getName())
						+ "]]");
			}
		}
		else {
			s.append("|scaleTo=" + toNonNullableString(service.getScaleTo()));
		}
		s.append("}}");
		return s.toString();
	}

	/**
	 * Get a hashmap from String (env) to a hashmap of role to System
	 * @return
	 */
	private HashMap<String, HashMap<OpsdRole, List<OpsdSystem>>> getEnvToRoleToSystems() {
		HashMap<String, HashMap<OpsdRole, List<OpsdSystem>>> h = new HashMap<String, HashMap<OpsdRole, List<OpsdSystem>>>();
		
		// First let's create the first level hashmap
		for(String aEnv: getFullProjectData().getEnvironments()) {
			h.put(aEnv, new HashMap<OpsdRole, List<OpsdSystem>>());
		}
		
		/* Now let's traverse all the systems
		 * and let's push each role+system to its environment
		 */
		for(OpsdSystem system: getFullProjectData().getSystems()) {
			/* Create the HashMap role2system if doesn't exist 
			 * and push it to the corresponding environment.
			 */
			boolean systemRoleFound = false;
			/* Will not print wrong data,
			 * it will be detected previously, during the validation
			 */
			if(system == null)
				continue;
			if(system.getEnvironment() == null
					|| system.getEnvironment().equals(""))
				continue;
			if(system.getRole() == null
					|| system.getRole().getName() == null
					|| system.getRole().getName().equals(""))
				continue;
			if(h.get(system.getEnvironment()) != null) {
				for(OpsdRole aRole: h.get(system.getEnvironment()).keySet()) {
					if(aRole == null || aRole.getName() == null)
						continue;
					if(aRole.getName().equals(system.getRole().getName())) {
						systemRoleFound = true;
					}
				}				
			}
			// Let's create the ArrayLists if needed
			if(! systemRoleFound) {
				if(h.get(system.getEnvironment()) == null) {
					// Probably an 'application host'
					continue;
				}
				h.get(system.getEnvironment()).put(system.getRole(), new ArrayList<OpsdSystem>());
			}
			h.get(system.getEnvironment()).get(system.getRole()).add(system);

		}
		return h;
	}
	

	/**
	 * Get a hashmap from String (env) to a hashmap of role to MonitoredHost
	 * @return
	 */
	private HashMap<String, HashMap<OpsdRole, List<OpsdMonitoredHost>>> getEnvToRoleToMonitoredHosts() {
		HashMap<String, HashMap<OpsdRole, List<OpsdMonitoredHost>>> h = new HashMap<String, HashMap<OpsdRole, List<OpsdMonitoredHost>>>();
		
		// First let's create the first level hashmap
		for(String aEnv: getFullProjectData().getEnvironments()) {
			h.put(aEnv, new HashMap<OpsdRole, List<OpsdMonitoredHost>>());
		}
		
		/* Now let's traverse all the monitoredHosts
		 * and let's push each role+monitoredHost to its environment
		 */
		for(OpsdMonitoredHost monitoredHost: getFullProjectData().getMonitoredHosts()) {
			/* Create the HashMap role2monitoredHost if doesn't exist 
			 * and push it to the corresponding environment.
			 */
			boolean monitoredHostRoleFound = false;
			/* Will not print wrong data,
			 * it will be detected previously, during the validation
			 */
			if(monitoredHost == null)
				continue;
			if(monitoredHost.getEnvironment() == null
					|| monitoredHost.getEnvironment().equals(""))
				continue;
			if(monitoredHost.getRole() == null
					|| monitoredHost.getRole().getName() == null
					|| monitoredHost.getRole().getName().equals(""))
				continue;
			if(h.get(monitoredHost.getEnvironment()) != null) {
				for(OpsdRole aRole: h.get(monitoredHost.getEnvironment()).keySet()) {
					if(aRole == null || aRole.getName() == null)
						continue;
					if(aRole.getName().equals(monitoredHost.getRole().getName())) {
						monitoredHostRoleFound = true;
					}
				}				
			}
			// Let's create the ArrayLists if needed
			if(! monitoredHostRoleFound) {
				if(h.get(monitoredHost.getEnvironment()) == null) {
					// Probably an 'application host'
					continue;
				}
				h.get(monitoredHost.getEnvironment()).put(monitoredHost.getRole(), new ArrayList<OpsdMonitoredHost>());
			}
			h.get(monitoredHost.getEnvironment()).get(monitoredHost.getRole()).add(monitoredHost);

		}
		return h;
	}

	private final static String MSG_EMPTY = OpsdConf.getProperty("msg.empty");
	private final static String MSG_NULL  = OpsdConf.getProperty("msg.null");

	/**
	 * Return "Error" if string is null, else return the string.
	 * @param s
	 * @return
	 */
	private String toNonNullableString(String s) {
		if(s == null || s.equals("")) {
			return MSG_NULL;
		}
		return string2escapedMediawiki(s);
	}
	
	/**
	 * Return "empty" if string is null, else return the string.
	 * @param s
	 * @return
	 */
	private String toNullableString(String s) {
		if(s == null || s.equals("")) {
			return MSG_EMPTY;
		}
		return string2escapedMediawiki(s);
	}

	/**
	 * Get wiki code for a Project.
	 * It deals with empty values.
	 * @return
	 */
	private String getWikiFromProject() {
		OpsdProject project = getFullProjectData().getProject();
		if(project == null) return MSG_NULL;
		
		StringBuilder s = new StringBuilder();
		s.append("{{" + OpsdConf.getWikiTemplateName(project.getClass().getSimpleName()));
		s.append("|name="
					+ toNonNullableString(project.getName()));
		s.append("|description="
					+ toNonNullableString(project.getDescription()));
		s.append("|dependencies="
					+ toNullableString(project.getDependencies()));
		s.append("|moreInfo="
					+ toNullableString(project.getMoreInfo()));
		s.append("|recoveryProcedure="
					+ project.getRecoveryProcedure());
		if(project.getResponsible() == null) {
			s.append("|responsible=" + MSG_NULL);
		}
		else {
			s.append("|responsible=[["
					+ toNonNullableString(project.getResponsible().getName())
					+ "]]");
		}
		
		String dateInStr = null;
		Calendar dateInCal = getFullProjectData().getProject().getDateIn();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		if (dateInCal != null) {
			dateInStr = sdf.format(dateInCal.getTime());
		}
		else {
			dateInStr = MSG_NULL;
		}
		s.append("|dateIn=" + dateInStr);
		
		String dateOutStr = null;
		Calendar dateOutCal = getFullProjectData().getProject().getDateOut();
		if (dateOutCal != null) {
			dateOutStr = sdf.format(dateOutCal.getTime());
		}
		else {
			dateOutStr = MSG_NULL;
		}
		s.append("|dateOut=" + dateOutStr);

		s.append("}}");
		return s.toString();
	}
	
	/**
	 * Get wiki code for a List of Roles
	 * It deals with empty values.
	 * @return
	 */
	private Map<OpsdRole, String> getWikiFromRoles() {
		List<OpsdRole> roles = getFullProjectData().getRoles();
		HashMap<OpsdRole, String> roles2string = new HashMap<OpsdRole, String>();
		for(OpsdRole aRole: roles) {
			if(aRole == null) {
				roles2string.put(aRole, MSG_NULL);
				continue;
			}
			StringBuilder s = new StringBuilder();
			s.append("{{" + OpsdConf.getWikiTemplateName(aRole.getClass().getSimpleName()));
			s.append("|name="
						+ toNonNullableString(aRole.getName()));
			s.append("|project="
					+ toNonNullableString(getFullProjectData().getProject().getName()));
			s.append("|description="
					+ toNullableString(aRole.getDescription()));
			s.append("}}");
			roles2string.put(aRole, s.toString());
		}
		return roles2string;
	}
	
	/**
	 * Get wiki code for a List of Systems
	 * It deals with empty values.
	 * @return
	 */
	private Map<OpsdSystem, String> getWikiFromSystems() {
		HashMap<OpsdSystem, String> systems2string = new HashMap<OpsdSystem, String>();
		for(OpsdSystem aSystem: getFullProjectData().getSystems()) {
			String s = system2wiki(aSystem);
			if(s != null)
				systems2string.put(aSystem, s);
		}
		return systems2string;
	}
	
	/**
	 * Get wiki code for a List of MonitoredHosts
	 * It deals with empty values.
	 * @return
	 */
	private Map<OpsdMonitoredHost, String> getWikiFromMonitoredHosts() {
		HashMap<OpsdMonitoredHost, String> monitoredHosts2string = new HashMap<OpsdMonitoredHost, String>();
		for(OpsdMonitoredHost aMonitoredHost: getFullProjectData().getMonitoredHosts()) {
			String s = monitoredHost2wiki(aMonitoredHost);
			if(s != null)
				monitoredHosts2string.put(aMonitoredHost, s);
		}
		return monitoredHosts2string;
	}
	
	/**
	 * Get template-based mediawiki code for a System
	 * @param system
	 * @return
	 */
	private String system2wiki(OpsdSystem system) {
		if(system == null)
			return null;
		StringBuilder s = new StringBuilder();
		s.append("{{" + OpsdConf.getWikiTemplateName(system.getClass().getSimpleName()));
		s.append("|name=" + toNonNullableString(system.getName()));
		s.append("|project="
				+ toNonNullableString(getFullProjectData().getProject().getName()));
		s.append("|alias=" + toNullableString(system.getAlias()));
		s.append("|fqdnOrIp=" + toNonNullableString(system.getFqdnOrIp()));
		if(system.getDeviceType() == null) {
			s.append("|deviceType=" + MSG_NULL);
		}
		else {
			s.append("|deviceType=" + toNonNullableString(system.getDeviceType().getName()));
		}
		if(system.getOs() == null) {
			s.append("|os=" + MSG_NULL);
		}
		else {
			s.append("|os=" + toNonNullableString(system.getOs().getName()));
		}
		if(system.getDeviceType() != null && ! system.getDeviceType().isVirtual()) {
			s.append("|lomIP=" + toNonNullableString(system.getLomIP()));
			s.append("|lomAccess=" + toNonNullableString(system.getLomAccess()));
		}
		else {
			s.append("|lomIP=" + MSG_EMPTY);
			s.append("|lomAccess=" + MSG_EMPTY);
		}
		s.append("|osAccess=" + toNonNullableString(system.getOsAccess()));
		s.append("|moreInfo=" + toNonNullableString(system.getMoreInfo()));
		s.append("|environment=" + toNonNullableString(system.getEnvironment()));
		if(system.getRole() == null) {
			s.append("|role=" + MSG_NULL);
		}
		else {
			s.append("|role=" + toNonNullableString(system.getRole().getName()));
		}
		s.append("|hostDownRecoveryProcedure=" + toNullableString(system.getHostDownRecoveryProcedure()));
		if(system.getResponsible() == null) {
			s.append("|responsible=" + toNonNullableString(getFullProjectData().getProject().getResponsible().getName()));
		}
		else {
			s.append("|responsible=" + toNonNullableString(system.getResponsible().getName()));
		}
		if(system.getScaleTo() == null || system.getScaleTo().equals("")) {
			s.append("|scaleTo=" + toNonNullableString(OpsdConf.getProperty("defaults.scaleSystemTo")));
		}
		else {
			s.append("|scaleTo=" + toNonNullableString(system.getScaleTo()));
		}
		s.append("}}");
		return s.toString();
	}
	
	/**
	 * Get template-based mediawiki code for a MonitoredHost
	 * @param monitoredHost
	 * @return
	 */
	private String monitoredHost2wiki(OpsdMonitoredHost monitoredHost) {
		if(monitoredHost == null)
			return null;
		StringBuilder s = new StringBuilder();
		s.append("{{" + OpsdConf.getWikiTemplateName(monitoredHost.getClass().getSimpleName()));
		s.append("|name=" + toNonNullableString(monitoredHost.getName()));
		s.append("|project="
				+ toNonNullableString(getFullProjectData().getProject().getName()));
		s.append("|ip=" + toNonNullableString(monitoredHost.getIp()));
		if(monitoredHost.getSystem() == null) {
			s.append("|system=" + MSG_NULL);
		}
		else {
			s.append("|system=" + toNonNullableString(monitoredHost.getSystem().getName()));
		}
		if(monitoredHost.isForManaging() == null) {
			s.append("|isForManaging=" + MSG_NULL);
		}
		else {
			s.append("|isForManaging=" + toNonNullableString(monitoredHost.isForManaging().toString()));
		}
		if(monitoredHost.isForService() == null) {
			s.append("|isForService=" + MSG_NULL);
		}
		else {
			s.append("|isForService=" + toNonNullableString(monitoredHost.isForService().toString()));
		}
		if(monitoredHost.isForBackup() == null) {
			s.append("|isForBackup=" + MSG_NULL);
		}
		else {
			s.append("|isForBackup=" + toNonNullableString(monitoredHost.isForBackup().toString()));
		}
		if(monitoredHost.isForNas() == null) {
			s.append("|isForNas=" + MSG_NULL);
		}
		else {
			s.append("|isForNas=" + toNonNullableString(monitoredHost.isForNas().toString()));
		}
		if(monitoredHost.isDefaultChecksNeeded() == null) {
			s.append("|isDefaultChecksNeeded=" + MSG_NULL);
		}
		else {
			s.append("|isDefaultChecksNeeded=" + toNonNullableString(monitoredHost.isDefaultChecksNeeded().toString()));
		}
		s.append("|moreInfo=" + toNonNullableString(monitoredHost.getMoreInfo()));
		s.append("|environment=" + toNonNullableString(monitoredHost.getEnvironment()));
		if(monitoredHost.getRole() == null) {
			s.append("|role=" + MSG_NULL);
		}
		else {
			s.append("|role=" + toNonNullableString(monitoredHost.getRole().getName()));
		}
		if(monitoredHost.getScaleTo() == null || monitoredHost.getScaleTo().equals("")) {
			s.append("|scaleTo=" + toNonNullableString(OpsdConf.getProperty("defaults.scaleMonitoredHostTo")));
		}
		else {
			s.append("|scaleTo=" + toNonNullableString(monitoredHost.getScaleTo()));
		}
		s.append("}}");
		return s.toString();
	}
	
	private String request2wiki(OpsdRequest request) {
		if(request == null)
			return null;
		StringBuilder s = new StringBuilder();
		s.append("{{" + OpsdConf.getWikiTemplateName(request.getClass().getSimpleName()));
		s.append("|name=" + toNonNullableString(request.getName()));
		s.append("|project="
				+ toNonNullableString(getFullProjectData().getProject().getName()));
		s.append("|authorized=" + toNullableString(request.getAuthorized()));
		s.append("|procedure=" + toNonNullableString(request.getProcedure()));
		if(request.getScaleTo() == null || request.getScaleTo().equals("")) {
			if(getFullProjectData().getProject().getResponsible() == null) {
				s.append("|scaleTo=" + MSG_NULL);
			}
			else {
				s.append("|scaleTo=[["
						+ toNonNullableString(getFullProjectData().getProject().getResponsible().getName())
						+ "]]");
			}
		}
		else {
			s.append("|scaleTo=" + toNonNullableString(request.getScaleTo()));
		}		
		s.append("}}");
		return s.toString();
	}


	private String periodicTask2wiki(OpsdPeriodicTask periodicTask) {
		if(periodicTask == null)
			return null;
		StringBuilder s = new StringBuilder();
		s.append("{{" + OpsdConf.getWikiTemplateName(periodicTask.getClass().getSimpleName()));
		s.append("|name=" + toNonNullableString(periodicTask.getName()));
		s.append("|project="
				+ toNonNullableString(getFullProjectData().getProject().getName()));
		s.append("|periodicity=" + toNullableString(periodicTask.getPeriodicity()));
		s.append("|procedure=" + toNonNullableString(periodicTask.getProcedure()));
		if(periodicTask.getScaleTo() == null || periodicTask.getScaleTo().equals("")) {
			if(getFullProjectData().getProject().getResponsible() == null) {
				s.append("|scaleTo=" + MSG_NULL);
			}
			else {
				s.append("|scaleTo=[["
						+ toNonNullableString(getFullProjectData().getProject().getResponsible().getName())
						+ "]]");
			}
		}
		else {
			s.append("|scaleTo=" + toNonNullableString(periodicTask.getScaleTo()));
		}		
		s.append("}}");
		return s.toString();
	}
	
	
	/**
	 * Get wiki code for a List of FilePolicies
	 * It deals with empty values.
	 * @return
	 */
	private Map<OpsdFilePolicy, String> getWikiFromFilePolicies() {
		HashMap<OpsdFilePolicy, String> filePolicies2string = new HashMap<OpsdFilePolicy, String>();
		for(OpsdFilePolicy aFilePolicy: getFullProjectData().getFilePolicies()) {
			String s = filePolicy2wiki(aFilePolicy);
			if(s != null)
				filePolicies2string.put(aFilePolicy, s);
		}
		return filePolicies2string;
	}
	
	private String filePolicy2wiki(OpsdFilePolicy filePolicy) {
		if(filePolicy == null)
			return null;
		StringBuilder s = new StringBuilder("find \"");
		s.append(filePolicy.getBaseFolder());
		s.append("\" -name \"");
		s.append( ( filePolicy.getPrefix() == null ? "" : filePolicy.getPrefix() ) );
		s.append("*");
		s.append( ( filePolicy.getSufix() == null ? "" : filePolicy.getSufix() ) );
		s.append("\" -type f -mtime +");
		s.append(filePolicy.getMinDays().toString());
		s.append(" -exec ");
		if(filePolicy.getAction() == ACTION_TYPE.COMPRESS) {
			s.append("gzip {} \\; ");			
		}
		else if(filePolicy.getAction() == ACTION_TYPE.DELETE) {
			s.append("rm -f {} \\; ");			
		}
		else {
			s.append(" ERROR ");			
		}
		return s.toString();
	}

	/**
	 * Scape special characters in a String to be correctly dealed by Mediawiki.
	 * Mmmmmm it seems it's not working...
	 * 
     *      (   "\\",  "{{", "}}",   "|",   "<",   ">",  "\n" )
     *      >> 
     *      ( "\\\\", "\\o", "\\c", "\\p", "\\l", "\\g", "\\n" )
	 * @param s
	 * @return
	 * @see https://www.mediawiki.org/wiki/Extension:Character_Escapes
	 */
	private String string2escapedMediawiki(String s) {
		/*
		// s=s.replace("|", "{{!}}");
		return s.replace("\\", "\\\\")
		 .replace("{{", "\\o")
		 .replace("}}", "\\c")
		 .replace("|", "\\p")
		 .replace("<", "\\l")
		 .replace(">", "\\g")
		 .replace("\n", "\\n");
		 */
		return s;
	}
}
