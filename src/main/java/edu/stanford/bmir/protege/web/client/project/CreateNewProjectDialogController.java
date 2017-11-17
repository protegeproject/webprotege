package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyRegisteredException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class CreateNewProjectDialogController extends WebProtegeOKCancelDialogController<NewProjectInfo> {

    @Nonnull
    private final CreateNewProjectPresenter presenter;

    @Inject
    public CreateNewProjectDialogController(@Nonnull CreateNewProjectPresenter projectPresenter,
                                            @Nonnull Messages messages) {
        super(messages.createProject());
        this.presenter = checkNotNull(projectPresenter);
        setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            if(presenter.validate()) {
                presenter.submitCreateProjectRequest(closer::hide);
            }
        });
    }

    @Override
    public Widget getWidget() {
        presenter.start();
        return presenter.getView().asWidget();
    }

    @Nonnull
    public Optional<HasRequestFocus> getInitialFocusable() {
        return presenter.getView().getInitialFocusable();
    }

    public NewProjectInfo getData() {
        return presenter.getNewProjectInfo();
    }
}
