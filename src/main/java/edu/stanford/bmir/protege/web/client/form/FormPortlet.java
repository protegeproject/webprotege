package edu.stanford.bmir.protege.web.client.form;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@Portlet(id = "portlets.form", title = "Form", tooltip = "Displays a form")
public class FormPortlet extends AbstractWebProtegePortlet {

    private FormPresenter formPresenter;

    @Inject
    public FormPortlet(SelectionModel selectionModel, ProjectId projectId, FormPresenter formPresenter) {
        super(selectionModel, projectId);
        this.formPresenter = formPresenter;
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        formPresenter.start(portletUi);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        if (entityData.isPresent()) {
            formPresenter.setSubject(entityData.get());
        }
        else {
            formPresenter.clear();
        }
    }
}
