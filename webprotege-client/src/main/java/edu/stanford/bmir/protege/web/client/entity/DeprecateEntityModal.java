package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public class DeprecateEntityModal {

    public static final DialogButton DEPRECATE_BUTTON = DialogButton.get("Deprecate");

    @Nonnull
    private final ModalManager modalManager;

    private Provider<ModalPresenter> modalPresenterProvider;

    private DeprecateEntityPresenterFactory deprecateEntityPresenterFactory;

    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public DeprecateEntityModal(ModalManager modalManager,
                                Provider<ModalPresenter> modalPresenterProvider,
                                DeprecateEntityPresenterFactory deprecateEntityPresenterFactory,
                                DispatchServiceManager dispatch, @Nonnull ProjectId projectId) {
        this.modalManager = checkNotNull(modalManager);
        this.modalPresenterProvider = checkNotNull(modalPresenterProvider);
        this.deprecateEntityPresenterFactory = checkNotNull(deprecateEntityPresenterFactory);
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
    }

    public void showModal(@Nonnull OWLEntity entity) {
        dispatch.execute(new GetEntityRenderingAction(projectId, entity),
                         this::showModalForEntity);
    }

    private void showModalForEntity(GetEntityRenderingResult result) {
        OWLEntityData entityData = result.getEntityData();
        String entityRendering = entityData.getBrowserText();
        OWLEntity entity = entityData.getEntity();
        ModalPresenter modalPresenter = modalPresenterProvider.get();
        modalPresenter.setTitle("Deprecate " + entityRendering);
        modalPresenter.setPrimaryButton(DEPRECATE_BUTTON);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        DeprecateEntityPresenter deprecateEntityPresenter = deprecateEntityPresenterFactory.create(entity);
        SimplePanel simplePanel = new SimplePanel();
        modalPresenter.setView(simplePanel);
        deprecateEntityPresenter.start(simplePanel);
        modalManager.showModal(modalPresenter);
    }
}
