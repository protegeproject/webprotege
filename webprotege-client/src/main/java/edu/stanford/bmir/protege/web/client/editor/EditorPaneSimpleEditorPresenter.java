package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2018
 */
public class EditorPaneSimpleEditorPresenter implements EditorPanePresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EditorPresenter editorPresenter;

    @Nonnull
    private final WebProtegeClientBundle clientBundle;

    @Inject
    public EditorPaneSimpleEditorPresenter(@Nonnull ProjectId projectId,
                                           @Nonnull EditorPresenter editorPresenter,
                                           @Nonnull WebProtegeClientBundle clientBundle) {
        this.projectId = checkNotNull(projectId);
        this.editorPresenter = checkNotNull(editorPresenter);
        this.clientBundle = checkNotNull(clientBundle);
    }

    @Nonnull
    @Override
    public String getCaption() {
        return "Details";
    }

    @Nonnull
    @Override
    public String getAdditionalStyles() {
        return clientBundle.buttons().editor();
    }

    @Nonnull
    @Override
    public BuiltInAction getRequiredAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        editorPresenter.start(container, eventBus);
        editorPresenter.updatePermissionBasedItems();
    }

    @Override
    public void setEntity(@Nonnull OWLEntity entity) {
        editorPresenter.setEditorContext(Optional.of(new OWLEntityContext(projectId, entity)));
    }

    @Override
    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        editorPresenter.setHasBusy(hasBusy);
    }

    @Override
    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        editorPresenter.setEntityDisplay(entityDisplay);
    }

    @Override
    public void dispose() {
        editorPresenter.dispose();
    }

    @Override
    public boolean isActive() {
        return ElementalUtil.isWidgetOrDescendantWidgetActive(editorPresenter.getView());
    }
}
