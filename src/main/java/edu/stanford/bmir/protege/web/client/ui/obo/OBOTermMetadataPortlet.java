package edu.stanford.bmir.protege.web.client.ui.obo;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermMetadataPortlet extends AbstractOBOTermPortlet {

    private final OBOTermMetadataView view;

    public OBOTermMetadataPortlet(Project project) {
        super(project);
        view = new OBOTermMetadataView(getProjectId());
        add(view);
    }


    @Override
    protected boolean isDirty() {
        return view.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        view.commit(getProjectId(), entity);
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        view.reload(getProjectId(), entity);
    }

    @Override
    protected void clearDisplay() {
    }

    @Override
    protected void updateTitle() {
    }

    @Override
    public void initialize() {
    }
}
