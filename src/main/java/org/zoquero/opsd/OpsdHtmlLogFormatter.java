/**
 * 
 */
package org.zoquero.opsd;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Logging formatter to generate HTML output
 * @author agalindo
 *
 */
public class OpsdHtmlLogFormatter extends Formatter {
	
	private String contentToAddInBody;
	
	public OpsdHtmlLogFormatter(String contentToAddInBody) {
		setContentToAddInBody(contentToAddInBody);
	}

	/**
	 * Get HTML color for a Log level
	 * @param l
	 * @return
	 * @see https://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html
	 */
	private String getBackgroundColor(int l) {
		if(l >= Level.SEVERE.intValue()) {
			return "#ff6666";
		}
		else if(l >= Level.WARNING.intValue()) {
			return "#ffb84d";
		}
		else if(l >= Level.INFO.intValue()) {
			return "#ffff4d";
		}
		else if(l >= Level.CONFIG.intValue()) {
			return "#ffffcc";
		}
		else if(l >= Level.FINE.intValue()) {
			return "#ffffe6";
		}
		return "white";
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
        buf.append("<tr>\n");

        // colorize any levels >= WARNING in red
//        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
//            buf.append("\t<td style=\"color:red\">");
//            buf.append("<b>");
//            buf.append(rec.getLevel());
//            buf.append("</b>");
//        } else {
//            buf.append("\t<td>");
//            buf.append(rec.getLevel());
//        }
        buf.append("\t<td style=\"background-color:" + getBackgroundColor(rec.getLevel().intValue()) + "\">");
        buf.append(rec.getLevel());

        buf.append("</td>\n");
        buf.append("\t<td>");
        buf.append(getDate(rec.getMillis()));
        buf.append("</td>\n");
        buf.append("\t<td>");
        buf.append(formatMessage(rec));
        buf.append("</td>\n");
        buf.append("</tr>\n");

        return buf.toString();
	}
	
    private String getDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }
    
    /**
     * Generate the header of the output when the formatter is created
     */
    public String getHead(Handler h) {
    	return new StringBuilder()
    	.append("<html>\n")
    	.append("  <head>\n")
    	.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n")
    	.append("    <style>\n")
    	.append("      table { width: 100% }\n")
    	.append("      th { font:bold 10pt Tahoma; }\n")
    	.append("      td { font:normal 10pt Tahoma; }\n")
    	.append("      h1 { font:normal 12pt Tahoma;}\n")
    	.append("    </style>\n")
    	.append("  </head>\n")
    	.append("  <body>\n")
    	.append("    " + getContentToAddInBody() + "\n")    	
    	.append("    <h1> Execution of project analysis done at " + (new java.util.Date()) + "</h1>\n")
    	.append("    <table border=\"1\" cellpadding=\"5\" cellspacing=\"3\">\n")
    	.append("      <tr align=\"left\">\n")
    	.append("        <th style=\"width:10%\">Loglevel</th>\n")
    	.append("        <th style=\"width:15%\">Time</th>\n")
    	.append("        <th style=\"width:75%\">Log message</th>\n")
    	.append("      </tr>\n").toString();
      }

    /**
     * Generate the tail of the output file when the handler is closed.
     */
    public String getTail(Handler h) {
    	return new StringBuilder()
    	.append("    </table>\n")
    	.append("  </body>\n")
    	.append("</html>\n").toString();
    }

	/**
	 * @return the contentToAddInBody
	 */
	public String getContentToAddInBody() {
		return contentToAddInBody;
	}

	/**
	 * @param contentToAddInBody the contentToAddInBody to set
	 */
	public void setContentToAddInBody(String contentToAddInBody) {
		this.contentToAddInBody = contentToAddInBody;
	}

}
