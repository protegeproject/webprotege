package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalButtonHandler;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.DeprecateEntityByFormAction;
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

    public void showModal(@Nonnull OWLEntity entity,
                          @Nonnull Runnable entityDeprecatedHandler,
                          @Nonnull Runnable cancelHandler) {
        dispatch.execute(new GetEntityRenderingAction(projectId, entity),
                         result -> showModalForEntity(result, entityDeprecatedHandler, cancelHandler));
    }

    private void showModalForEntity(GetEntityRenderingResult result,
                                    @Nonnull Runnable entityDeprecatedHandler,
                                    @Nonnull Runnable cancelHandler) {
        OWLEntityData entityData = result.getEntityData();
        String entityRendering = entityData.getBrowserText();
        OWLEntity entity = entityData.getEntity();
        ModalPresenter modalPresenter = modalPresenterProvider.get();
        modalPresenter.setTitle("Deprecate " + entityRendering);
        modalPresenter.setPrimaryButton(DEPRECATE_BUTTON);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButtonFocusedOnShow(false);

        DeprecateEntityPresenter deprecateEntityPresenter = deprecateEntityPresenterFactory.create(entity);
        SimplePanel simplePanel = new SimplePanel();
        modalPresenter.setView(simplePanel);
        deprecateEntityPresenter.start(simplePanel);


        modalPresenter.setButtonHandler(DEPRECATE_BUTTON, closer -> {
            dispatch.execute(new DeprecateEntityByFormAction(entity,
                                                             deprecateEntityPresenter.getDeprecationFormData(),
                                                             deprecateEntityPresenter.getReplacementEntity(),
                                                             projectId),
                             r -> {closer.closeModal(); entityDeprecatedHandler.run();});
        });
        modalPresenter.setButtonHandler(DialogButton.CANCEL,
                                        closer -> {closer.closeModal(); cancelHandler.run();});

        modalManager.showModal(modalPresenter);
    }
}
