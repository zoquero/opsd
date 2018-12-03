package org.zoquero.opsd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.zoquero.opsd.dao.DaoFactory;
import org.zoquero.opsd.dao.OpsdConf;
import org.zoquero.opsd.dao.OpsdDataTap;

/**
 * Operations descriptor application
 *
 */
public class App {

  private static String projectFilePath;
//  private final static Logger LOGGER = Logger.getLogger(App.class.getPackage().getName());
  public final static Logger LOGGER = Logger.getLogger(App.class.getPackage().getName());
	/** The HTML file handler */
  static private FileHandler fileHTML;
  /** The HTML formatter */
  static private Formatter formatterHTML;


  /**
   * Setup the logging in the root of the hierarchy.
   * @param filePath
   * @throws IOException
   * @throws OpsdException 
   */
	static public void setupLogging(String filePath)
			throws IOException, OpsdException {

		// Log levels:
		// https://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html
		int minLogLevel = OpsdConf.getMinLogLevel();
		if (minLogLevel >= Level.SEVERE.intValue()) {
			LOGGER.setLevel(Level.SEVERE);
		} else if (minLogLevel >= Level.WARNING.intValue()) {
			LOGGER.setLevel(Level.WARNING);
		} else if (minLogLevel >= Level.INFO.intValue()) {
			LOGGER.setLevel(Level.INFO);
		} else if (minLogLevel >= Level.CONFIG.intValue()) {
			LOGGER.setLevel(Level.CONFIG);
		} else if (minLogLevel >= Level.FINE.intValue()) {
			LOGGER.setLevel(Level.FINE);
		} else if (minLogLevel >= Level.FINER.intValue()) {
			LOGGER.setLevel(Level.FINER);
		} else {
			LOGGER.setLevel(Level.FINEST);
		}

		fileHTML = new FileHandler(filePath);
		
		String contentToAddAtBody = new StringBuilder("<p>")
			.append("<a href=\"project.html\">Link to project report</a></p>")
			.toString();

		// Create the HTML formatter
		formatterHTML = new OpsdHtmlLogFormatter(contentToAddAtBody);
		fileHTML.setFormatter(formatterHTML);
		LOGGER.addHandler(fileHTML);

		// Stop in the hierarchy, so we'll avoid console output, default on root
		LOGGER.setUseParentHandlers(false);
		LOGGER.log(Level.INFO, "Log setup on " + filePath);
	}

	public static void main(String[] args) {
		System.out.println("Operations descriptor");
		if (args == null | args.length <= 1) {
			System.out
					.println("Missing path to project Excel file or projectName");
			usage();
			System.exit(1);
		}
		projectFilePath = args[0];
		String projectName = args[1];
		DaoFactory df = new DaoFactory();
		OpsdDataTap dt = null;
		try {
			// Create an output folder and output file with a normalized name
			String prefix = ("ProjectInfo."
					+ Normalizer.normalize(projectName, Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "") + ".").replaceAll(
					"[^a-zA-Z0-9\\. _-]", "").replaceAll("[ ]", "_");
			Path outputFolder = OpsdReportGenerator.createDirectory(prefix);
			String htmlLogFile = outputFolder + File.separator + "output.html";

			// Initialize logging
			try {
				setupLogging(htmlLogFile);
			} catch (IOException e) {
				throw new OpsdException("Can't create output file");
			}
			LOGGER.log(Level.INFO, "Using project file " + projectFilePath
					+ " to get info from the project '" + projectName + "'");

			// Let' extract info from project:
			dt = df.getDao(projectFilePath);
			dt.connect();
			OpsdExtractor oe = new OpsdExtractor(dt);
			OpsdFullProjectData ofpd = oe.getFullProjectData(projectName);
			// System.out.println("We got this project: " + ofpd.getProject());
			dt.disconnect();

			// Let's validate it
			System.out.println("Report:");
			System.out.println("With errors:    "
					+ ofpd.getReport().isWithErrors());
			for (String anError : ofpd.getReport().getErrors()) {
				System.out.println("* " + anError);
			}
			System.out.println("With warnings:  "
					+ ofpd.getReport().isWithWarnings());
			for (String aWarning : ofpd.getReport().getWarnings()) {
				System.out.println("* " + aWarning);
			}

			OpsdReportGenerator org = new OpsdReportGenerator(ofpd);
			// Let's generate the output
			// (generated docs with monitoring and documentation)
			String outputFile = "";
			try {
				outputFile = org.getOutputFile(outputFolder);
			} catch (Exception e) {
				System.out.println("Exception thrown in template, probably by wrong data: " + e.getMessage());
				LOGGER.log(Level.SEVERE, "Exception thrown in template, probably by wrong data: " + e.getMessage(), e);
			}
			
			System.out.println("Output files:");
			System.out.println("* Generated docs: " + outputFile);
			System.out.println("* Execution log:  " + htmlLogFile);
			// htmlLogFile
		} catch (OpsdException e) {
			System.out.println("Errors accessing data: " + e.getMessage());
			LOGGER.log(Level.SEVERE, "Exception thrown in template, probably by wrong data: " + e.getMessage(), e);
		}		
	}

	/**
	 * Usage explanation.
	 */
	public static void usage() {
		String newLine = System.getProperty("line.separator");
		String s = new StringBuilder()
				.append("Usage:" + newLine)
				.append("java -cp target/opsd-...-...jar org.zoquero.opsd.App /path/to/project_file.xlsx projectName")
				.toString();
		System.out.println(s);
	}
}
