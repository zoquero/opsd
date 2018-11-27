package org.zoquero.opsd;

import org.zoquero.opsd.dao.DaoFactory;
import org.zoquero.opsd.dao.OpsdException;
import org.zoquero.opsd.dao.OpsdDataTap;

/**
 * Operations descriptor
 *
 */
public class App {

  private static String projectFilePath;

  public static void main(String[] args) {
    System.out.println("Operations descriptor");
    if(args == null | args.length <= 1) {
      System.out.println("Missing path to project Excel file or projectName");
      usage();
      System.exit(1);
    }
    projectFilePath    = args[0];
    String projectName = args[1];
    System.out.println("Using project file " + projectFilePath + " to get info from " + projectName);
    DaoFactory df = new DaoFactory();
    OpsdDataTap dt = df.getDao(projectFilePath);
    try {
    	// Let' extract info from project:
		dt.connect();
		OpsdExtractor oe = new OpsdExtractor(dt);
		OpsdFullProjectData ofpd = oe.getFullProjectData(projectName);
		// System.out.println("We got this project: " + ofpd.getProject());
		dt.disconnect();
		
		// Let's validate it
		System.out.println("\nReport:");
		System.out.println("With errors:    " + ofpd.getReport().isWithErrors());
		for (String anError: ofpd.getReport().getErrors()) {
			System.out.println("* " + anError);
		}
		System.out.println("With warnings:  " + ofpd.getReport().isWithWarnings());
		for (String aWarning: ofpd.getReport().getWarnings()) {
			System.out.println("* " + aWarning);
		}
		
		// Let's generate the output
		OpsdOutputGenerator oog = new OpsdOutputGenerator(ofpd);
		String output = oog.getOutputFile();
		System.out.println("output = " + output);
	}
    catch (OpsdException e) {
		System.out.println("Errors accessing data: " + e.getMessage());
		e.printStackTrace();
	}
  }

  /**
   * Usage explanation.
   */
  public static void usage() {
    String s = new StringBuilder()
      .append("Usage:\n")
      .append("java -cp target/opsd-...-...jar org.zoquero.opsd.App /path/to/project_file.xlsx projectName")
      .toString();
    System.out.println(s);
  }
}
