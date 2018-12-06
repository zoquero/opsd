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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.dao.OpsdPoiDao;
import org.zoquero.opsd.entities.OpsdHostService;
import org.zoquero.opsd.entities.OpsdMonitoredHost;
import org.zoquero.opsd.entities.OpsdMonitoredService;
import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;
import org.zoquero.opsd.entities.OpsdRoleService;
import org.zoquero.opsd.entities.OpsdSystem;


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

		// Let's process the template
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("title", "Validation of the project "
				+ "and generation of monitoring and documentation");
		input.put("project",          getFullProjectData().getProject());
		input.put("wikiProject",      getWikiFromEntity());
		input.put("report",           getFullProjectData().getReport());
		input.put("roles",            getFullProjectData().getRoles());
		input.put("roles2wiki",       getWikiFromRoles());
		input.put("systems",          getFullProjectData().getSystems());
		input.put("systems2wiki",     getWikiFromSystems());
		input.put("monitoredHosts",   getFullProjectData().getMonitoredHosts());
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
		input.put("requests",         getFullProjectData().getRequests());
		input.put("wikiUrlBase",      OpsdConf.getWikiUrlBase());

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
		return s;
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
		return s;
	}

	/**
	 * Get wiki code for an Entity.
	 * It deals with empty values.
	 * @return
	 */
	private String getWikiFromEntity() {
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
		List<OpsdSystem> systems = getFullProjectData().getSystems();
		OpsdProject project = getFullProjectData().getProject();
		HashMap<OpsdSystem, String> systems2string = new HashMap<OpsdSystem, String>();
		for(OpsdSystem aSystem: systems) {
			if(aSystem == null) {
				systems2string.put(aSystem, MSG_NULL);
				continue;
			}
			StringBuilder s = new StringBuilder();
			s.append("{{" + OpsdConf.getWikiTemplateName(aSystem.getClass().getSimpleName()));
			s.append("|name=" + toNonNullableString(aSystem.getName()));
			s.append("|alias=" + toNullableString(aSystem.getAlias()));
			s.append("|fqdnOrIp=" + toNonNullableString(aSystem.getFqdnOrIp()));
			if(aSystem.getDeviceType() == null) {
				s.append("|deviceType=" + MSG_NULL);
			}
			else {
				s.append("|deviceType=" + toNonNullableString(aSystem.getDeviceType().getName()));
			}
			if(aSystem.getOs() == null) {
				s.append("|os=" + MSG_NULL);
			}
			else {
				s.append("|os=" + toNonNullableString(aSystem.getOs().getName()));
			}
			if(aSystem.getDeviceType() != null && ! aSystem.getDeviceType().isVirtual()) {
				s.append("|lomIP=" + toNonNullableString(aSystem.getLomIP()));
				s.append("|lomAccess=" + toNonNullableString(aSystem.getLomAccess()));
			}
			else {
				s.append("|lomIP=" + MSG_EMPTY);
				s.append("|lomAccess=" + MSG_EMPTY);
			}
			s.append("|osAccess=" + toNonNullableString(aSystem.getOsAccess()));
			s.append("|moreInfo=" + toNonNullableString(aSystem.getMoreInfo()));
			s.append("|environment=" + toNonNullableString(aSystem.getEnvironment()));
			if(aSystem.getRole() == null) {
				s.append("|role=" + MSG_NULL);
			}
			else {
				s.append("|role=" + toNonNullableString(aSystem.getRole().getName()));
			}
			s.append("|hostDownRecoveryProcedure=" + toNullableString(aSystem.getHostDownRecoveryProcedure()));
			if(aSystem.getResponsible() == null) {
				s.append("|responsible=" + toNonNullableString(project.getResponsible().getName()));
			}
			else {
				s.append("|responsible=" + toNonNullableString(aSystem.getResponsible().getName()));
			}
			if(aSystem.getScaleTo() == null || aSystem.getScaleTo().equals("")) {
				s.append("|scaleTo=" + toNonNullableString(OpsdConf.getProperty("defaults.scaleSystemTo")));
			}
			else {
				s.append("|scaleTo=" + toNonNullableString(aSystem.getScaleTo()));
			}

			s.append("}}");
			systems2string.put(aSystem, s.toString());
		}
		return systems2string;
	}
}
