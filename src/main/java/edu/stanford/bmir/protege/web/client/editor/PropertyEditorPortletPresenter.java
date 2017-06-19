package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
@Portlet(
        id = "portlets.PropertyEditor",
        title = "Property",
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
        editorPresenter.setDisplayedTypes(OBJECT_PROPERTY,
                                          DATA_PROPERTY,
                                          ANNOTATION_PROPERTY);
        editorPresenter.start(portletUi, eventBus);
    }
}
