package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;

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
