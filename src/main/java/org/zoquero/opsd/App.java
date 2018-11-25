package org.zoquero.opsd;

import org.zoquero.opsd.dao.DaoFactory;
import org.zoquero.opsd.dao.OpsdDaoException;
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
		dt.connect();
		OpsdExtractor oe = new OpsdExtractor(dt);
		OpsdFullProjectData ope = oe.getFullProjectData(projectName);
		dt.disconnect();
	}
    catch (OpsdDaoException e) {
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
