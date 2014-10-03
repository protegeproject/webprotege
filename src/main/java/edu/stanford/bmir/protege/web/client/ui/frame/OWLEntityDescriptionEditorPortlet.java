package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Collection;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class OWLEntityDescriptionEditorPortlet extends AbstractOWLEntityPortlet {

    private ManchesterSyntaxFrameEditorPresenter presenter;

    public OWLEntityDescriptionEditorPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        ManchesterSyntaxFrameEditorImpl editor = new ManchesterSyntaxFrameEditorImpl();
        presenter = new ManchesterSyntaxFrameEditorPresenter(editor, getProjectId(), Application.get(), Application.get(), DispatchServiceManager.get());
        presenter.attach(this);
        add(editor);
        setHeight(500);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        Optional<OWLEntityData> selectedEntityData = getSelectedEntityData();
        if (selectedEntityData.isPresent()) {
            presenter.setSubject(entityData.get().getEntity());
            setTitle("Description for " + entityData.get().getBrowserText());
        }
        else {
            presenter.clearSubject();
        }
    }

}
