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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
@Portlet(
        id = "portlet.individualeditor",
        title = "Individual Editor",
        tooltip = "Provides an editor that allows individuals to be edited"
)
public class NamedIndividualEditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final EditorPortletPresenter editorPresenter;

    @Inject
    public NamedIndividualEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                                 @Nonnull ProjectId projectId,
                                                 @Nonnull EditorPortletPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.editorPresenter = checkNotNull(editorPresenter);
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
            if(entity.isOWLNamedIndividual()) {
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
