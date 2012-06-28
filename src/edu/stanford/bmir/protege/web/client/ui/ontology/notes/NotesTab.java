package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;

public class NotesTab extends AbstractTab {

	public NotesTab(Project project) {
		super(project);		
	}
	
	/*
	protected void buildUI() {			
		if (loadSpecial()) { return; }
		
		ClassTreePortlet clsTreePortlet = new ClassTreePortlet(project);		
		addPortlet(clsTreePortlet, 0);		

		NotesPortlet ontologyNotesPortlet = new NotesPortlet(project);
		ontologyNotesPortlet.setTopLevel(true);
		addPortlet(ontologyNotesPortlet, 1);
		ontologyNotesPortlet.setHeight(300);
		
		NotesPortlet entityNotesPortlet = new NotesPortlet(project);
		entityNotesPortlet.setTopLevel(false);
		addPortlet(entityNotesPortlet, 1);
		entityNotesPortlet.setHeight(300);

		setControllingPortlet(clsTreePortlet);		
	}

	
	private boolean loadSpecial() {		
		if (!specialPrj()) { return false;}

		ClassTreePortlet clsTreePortlet = new ClassTreePortlet(project);		
		addPortlet(clsTreePortlet, 0);		

		NotesPortlet ontologyNotesPortlet = new NotesPortlet(project);
		ontologyNotesPortlet.setTopLevel(false);
		addPortlet(ontologyNotesPortlet, 1);
		ontologyNotesPortlet.setHeight(610);
		
		setControllingPortlet(clsTreePortlet);		
		
		return true;
	}

	private boolean specialPrj() {
		String prjName = project.getEscapedProjectName();
		return prjName.startsWith("ICD");
	}
	 */
}
