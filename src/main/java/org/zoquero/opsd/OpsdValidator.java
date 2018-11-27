/**
 * 
 */
package org.zoquero.opsd;

import java.util.List;

import org.zoquero.opsd.entities.OpsdProject;
import org.zoquero.opsd.entities.OpsdRole;

/**
 * Validates the objects got from the project.
 * @author agalindo
 *
 */
public class OpsdValidator {

	public static void validate(OpsdProject project, OpsdReport oReport) {
		
		// Let's validate the Project:
		if(project == null) {
			oReport.pushError("Project is null");
		}
		if(project.getName() == null || project.getName().trim().equals("")) {
			oReport.pushError("Project has null name");
		}
		if(project.getDescription() == null || project.getDescription().trim().equals("")) {
			oReport.pushError("Project has null description");
		}
		if(project.getResponsible() == null || project.getResponsible().getName().trim().equals("")) {
			oReport.pushError("Project has null responsible name");
		}
		if(project.getDateIn() == null) {
			oReport.pushError("Project has null dateIn");
		}
		// null dateOut means 'still up'
//		if(project.getDateOut() == null) {
//			oReport.pushWarning("Project has null dateOut");
//		}
		if(project.getDependencies() == null || project.getDependencies().trim().equals("")) {
			oReport.pushWarning("Project has null dependencies");
		}
		if(project.getRecoveryProcedure() == null || project.getRecoveryProcedure().trim().equals("")) {
			oReport.pushWarning("Project has null recoveryProcedure");
		}
		if(project.getMoreInfo() == null || project.getMoreInfo().trim().equals("")) {
			oReport.pushWarning("Project has null moreInfo");
		}
	}
	

	public static void validate(List<OpsdRole> roles, OpsdReport oReport) {
		
		// Let's validate the Roles:
		for (OpsdRole role : roles) {
			if(role == null) {
				oReport.pushError("Role is null");
			}
			if(role.getName() == null || role.getName().trim().equals("")) {
				oReport.pushError("Role has null name");
			}
			if(role.getDescription() == null || role.getDescription().trim().equals("")) {
				oReport.pushWarning("Role has null description");
			}			
		}
	}

}
