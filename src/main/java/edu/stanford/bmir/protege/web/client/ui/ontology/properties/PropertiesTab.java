package edu.stanford.bmir.protege.web.client.ui.ontology.properties;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.AllPropertiesPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;


public class PropertiesTab extends AbstractTab {
	protected PropertiesTreePortlet clsTreePortlet;	
	protected AllPropertiesPortlet propPortlet;

	public PropertiesTab(Project project) {		
		super(project);		
	}

	public String getLabel() {
		return "Properties";
	}
}
