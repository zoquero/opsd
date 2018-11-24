package org.zoquero.opsd;

import org.zoquero.opsd.dao.DaoFactory;

/**
 * Operations descriptor
 *
 */
public class App {

  private static String projectFilePath;

  public static void main(String[] args) {
    System.out.println("Operations descriptor");
    if(args == null | args.length <= 0) {
      System.out.println("Missing path to project Excel file");
      usage();
      System.exit(1);
    }
    projectFilePath = args[0];
    System.out.println("Using project file " + projectFilePath);
    DaoFactory df = new DaoFactory();
    df.getDao(projectFilePath);
  }

  public static void usage() {
    String s = new StringBuilder()
      .append("Usage:\n")
      .append("java -cp target/opsd-...-...jar org.zoquero.opsd.App /path/to/project_file.xlsx")
      .toString();
    System.out.println(s);
  }
}
