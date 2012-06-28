package edu.stanford.bmir.protege.web.client.ui.obo;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

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
    protected void commitChangesForEntity(Entity entity) {
        view.commit(getProjectId(), entity);
    }

    @Override
    protected void displayEntity(Entity entity) {
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

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}
