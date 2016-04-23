package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormPortlet extends AbstractWebProtegePortlet {

    private FormPresenter formPresenter;

    @Inject
    public FormPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider, FormPresenter formPresenter) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.formPresenter = formPresenter;
        setTitle("Form");
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        formPresenter.start(this);
        if (entityData.isPresent()) {
            formPresenter.setSubject(entityData.get());
        }
        else {
            formPresenter.clear();
        }
    }
}
