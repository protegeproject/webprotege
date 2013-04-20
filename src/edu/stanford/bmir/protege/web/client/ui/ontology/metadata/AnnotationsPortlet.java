package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

import java.util.Collection;
import java.util.Collections;

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
