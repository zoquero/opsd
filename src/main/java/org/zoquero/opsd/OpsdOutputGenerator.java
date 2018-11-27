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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.zoquero.opsd.dao.OpsdException;

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
public class OpsdOutputGenerator {

	private OpsdFullProjectData fullProjectData;
	
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

	public OpsdOutputGenerator(OpsdFullProjectData ofpd) {
		this.setFullProjectData(ofpd);
	}

	public String getOutputFile() throws OpsdException {
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
		input.put("title", "Mediawiki code for Project");
		input.put("project", getFullProjectData().getProject());		
		input.put("roles", getFullProjectData().getRoles());		
		
		Calendar c = getFullProjectData().getProject().getDateIn();
		String dateIn = c.get(Calendar.DAY_OF_MONTH) + "/" +  (c.get(Calendar.MONTH) + 1) + "/" +  c.get(Calendar.YEAR);
		c = getFullProjectData().getProject().getDateOut();
		String dateOut = "still up";
		if(c != null) {
			dateOut = c.get(Calendar.DAY_OF_MONTH) + "/" +  (c.get(Calendar.MONTH) + 1) + "/" +  c.get(Calendar.YEAR);
		}
		input.put("projectDateIn", dateIn);	
		input.put("projectDateOut", dateOut);	

		// Let's get the template
		try {
			Path tempDirWithPrefix = Files.createTempDirectory("ProjectInfo.");
			Template template = cfg.getTemplate("project.ftl");

//			// Write output to the console
//			Writer consoleWriter = new OutputStreamWriter(System.out);
//			template.process(input, consoleWriter);

//			// Write output to a String
//			StringWriter stringWriter = new StringWriter();
//			template.process(input, stringWriter);
//			return stringWriter.toString();

			Writer fileWriter = new FileWriter(new File(tempDirWithPrefix.toString() + "/project.html"));
		    template.process(input, fileWriter);
		    fileWriter.close();
		    return tempDirWithPrefix.toString();
		    
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
}
