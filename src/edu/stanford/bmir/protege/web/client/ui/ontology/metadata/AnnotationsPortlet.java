package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti
 */
public class AnnotationsPortlet extends AbstractEntityPortlet {

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
		if (_currentEntity != null) {
			String title = _currentEntity.getBrowserText();
			if (title.length() > 20) {
				title = "   ..." + title.substring(title.length() - 20, title.length());
			}
			setTitle("Ontology Annotations for " + title);
		}
		
		annotationsGrid.setEntity(_currentEntity);
	}

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}
