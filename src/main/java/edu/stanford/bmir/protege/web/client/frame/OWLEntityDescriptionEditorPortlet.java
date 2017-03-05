package edu.stanford.bmir.protege.web.client.frame;

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
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */

@Portlet(id = "portlets.owl.EntityDescriptionEditor",
        title = "OWL Entity Description Editor",
        tooltip = "Allows the description of the selected entity to be edited in Manchester Syntax.  The complete OWL 2 syntax is supported.")
public class OWLEntityDescriptionEditorPortlet extends AbstractWebProtegePortlet {

    private final ManchesterSyntaxFrameEditorPresenter presenter;

    private Optional<PortletUi> portletUi = Optional.empty();

    @Inject
    public OWLEntityDescriptionEditorPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, ManchesterSyntaxFrameEditorPresenter presenter) {
        super(selectionModel, projectId);
        this.presenter = presenter;
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        presenter.start(eventBus);
        portletUi.setWidget(presenter.getView());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if (selectedEntity.isPresent()) {
            presenter.setSubject(selectedEntity.get());
            portletUi.ifPresent(ui -> ui.setViewTitle(entity.get().getEntityType().getPrintName() + " Description"));
        }
        else {
            presenter.clearSubject();
        }
    }
}
