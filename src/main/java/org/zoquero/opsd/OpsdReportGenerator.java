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
		input.put("wikiProject",      getWikiFronEntity(getFullProjectData().getProject()));
		input.put("report",           getFullProjectData().getReport());
		input.put("roles",            getFullProjectData().getRoles());
		input.put("roles2wiki",       getWikiFromRoles(getFullProjectData().getRoles()));
		input.put("systems",          getFullProjectData().getSystems());
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
	 * @param project
	 * @return
	 */
	private String getWikiFronEntity(OpsdProject project) {
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
	 * @param list of roles
	 * @return
	 */
	private Map<OpsdRole, String> getWikiFromRoles(List<OpsdRole> roles) {
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
}
