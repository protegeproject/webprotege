package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
@Portlet(
        id = "portlet.propertyeditor",
        title = "Property Editor",
        tooltip = "Provides an editor that allows property descriptions to be edited"
)
public class PropertyEditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final EditorPortletPresenter editorPresenter;

    @Inject
    public PropertyEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull EditorPortletPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.editorPresenter = editorPresenter;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        editorPresenter.setTrackSelection(false);
        editorPresenter.start(portletUi, eventBus);
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        entityData.ifPresent(entity -> {
            if(entity.isOWLObjectProperty()
                    || entity.isOWLDataProperty()
                    || entity.isOWLAnnotationProperty()) {
                editorPresenter.handleAfterSetEntity(entityData);
            }
            else {
                editorPresenter.handleAfterSetEntity(Optional.empty());
            }
        });
        if(!entityData.isPresent()) {
            editorPresenter.handleAfterSetEntity(Optional.empty());
        }
    }

}
