/**
 * 
 */
package org.zoquero.opsd;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains errors and warnings found during the extraction.
 * @author agalindo
 *
 */
public class OpsdReport {

	private List<String> errors   = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	private boolean withErrors    = false;
	private boolean withWarnings  = false;
	
	// accessors
	
	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	private void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the warnings
	 */
	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * @param warnings the warnings to set
	 */
	private void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * @return the withErrors
	 */
	public boolean isWithErrors() {
		return withErrors;
	}

	/**
	 * @param withErrors the withErrors to set
	 */
	public void setWithErrors(boolean withErrors) {
		this.withErrors = withErrors;
	}

	/**
	 * @return the withWarnings
	 */
	public boolean isWithWarnings() {
		return withWarnings;
	}

	/**
	 * @param withWarnings the withWarnings to set
	 */
	public void setWithWarnings(boolean withWarnings) {
		this.withWarnings = withWarnings;
	}
	
	// methods
	
	public void pushError(String errorString) {
		errors.add(errorString);
		setWithErrors(true);
	}

	public void pushWarning(String warningString) {
		warnings.add(warningString);
		setWithWarnings(true);
	}

}
