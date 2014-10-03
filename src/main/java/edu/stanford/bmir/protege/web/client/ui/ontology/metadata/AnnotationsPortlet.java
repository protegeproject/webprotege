package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Jennifer Vendetti
 */
public class AnnotationsPortlet extends AbstractOWLEntityPortlet {

	protected AnnotationsGrid annotationsGrid;
	
	public AnnotationsPortlet(Project project) {
		super(project);
	}

	public void initialize() {
		setTitle("Ontology Annotations");
		this.annotationsGrid = new AnnotationsGrid(getProjectId());
		add(annotationsGrid);
	}

	public void reload() { 
		if (getEntity() != null) {
			String title = getEntity().getBrowserText();
			if (title.length() > 20) {
				title = "   ..." + title.substring(title.length() - 20, title.length());
			}
			setTitle("Ontology Annotations for " + title);
		}
		
		annotationsGrid.setEntity(getEntity());
	}
}
