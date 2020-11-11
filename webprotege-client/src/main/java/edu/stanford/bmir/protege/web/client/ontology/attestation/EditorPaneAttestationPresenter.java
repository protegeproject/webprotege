package edu.stanford.bmir.protege.web.client.ontology.attestation;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.EditorPanePresenter;
import edu.stanford.bmir.protege.web.client.editor.EditorPresenter;
import edu.stanford.bmir.protege.web.client.editor.OWLEntityContext;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditorPaneAttestationPresenter implements EditorPanePresenter {
    @Nonnull
    private final ProjectId projectId;
    @Nonnull
    private final EditorPresenter editorPresenter;
    @Nonnull
    private final WebProtegeClientBundle clientBundle;
    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;
    private final EditorAttestationPane view;

    @Inject
    public EditorPaneAttestationPresenter(@Nonnull ProjectId projectId,
                                          @Nonnull EditorPresenter editorPresenter,
                                          @Nonnull WebProtegeClientBundle clientBundle,
                                          @Nonnull DispatchServiceManager dispatchServiceManager) {

        this.projectId = checkNotNull(projectId);
        this.editorPresenter = checkNotNull(editorPresenter);
        this.clientBundle = checkNotNull(clientBundle);
        this.dispatchServiceManager = dispatchServiceManager;

        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        view = new EditorAttestationPane(projectId, dispatchServiceManager);
    }

    @Nonnull
    @Override
    public String getCaption() {
        return "Attestation";
    }

    @Nonnull
    @Override
    public String getAdditionalStyles() {
        return clientBundle.buttons().cross();
    }

    @Nonnull
    @Override
    public BuiltInAction getRequiredAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
//        editorPresenter.start(container, eventBus);
//        editorPresenter.updatePermissionBasedItems();
        container.setWidget(view);
    }

    @Override
    public void setEntity(@Nonnull OWLEntity entity) {
        editorPresenter.setEditorContext(Optional.of(new OWLEntityContext(projectId, entity)));
        GetClassFrameAction action = new GetClassFrameAction(entity.asOWLClass(), projectId);
        dispatchServiceManager.execute(action, (r) -> view.setValue(r.getFrame()));
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
