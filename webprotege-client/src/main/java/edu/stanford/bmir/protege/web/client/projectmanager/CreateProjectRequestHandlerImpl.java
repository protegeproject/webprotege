package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.project.CreateNewProjectPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class CreateProjectRequestHandlerImpl implements CreateProjectRequestHandler {

    @Nonnull
    private final Provider<CreateNewProjectPresenter> presenterProvider;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final Messages messages;

    @Inject
    public CreateProjectRequestHandlerImpl(@Nonnull Provider<CreateNewProjectPresenter> presenterProvider,
                                           @Nonnull ModalManager modalManager,
                                           @Nonnull Messages messages) {
        this.presenterProvider = checkNotNull(presenterProvider);
        this.modalManager = checkNotNull(modalManager);
        this.messages = checkNotNull(messages);

    }

    @Override
    public void handleCreateProjectRequest() {
        CreateNewProjectPresenter presenter = presenterProvider.get();
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.createProject());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        DialogButton createProjectButton = DialogButton.get(messages.createProject());
        modalPresenter.setPrimaryButton(createProjectButton);
        modalPresenter.setView(presenter.getView());
        modalPresenter.setButtonHandler(createProjectButton, closer -> {
            presenter.validateAndCreateProject(closer::closeModal);
        });
        modalManager.showModal(modalPresenter);
    }
}
