package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
@Portlet(
        id = "portlet.classeditor",
        title = "Class Editor",
        tooltip = "Provides an editor that allows class descriptions to be edited"
)
public class ClassEditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final EditorPortletPresenter editorPresenter;

    @Inject
    public ClassEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EditorPortletPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.editorPresenter = checkNotNull(editorPresenter);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        editorPresenter.setDisplayedTypes(CLASS);
        editorPresenter.start(portletUi, eventBus);
    }
}
