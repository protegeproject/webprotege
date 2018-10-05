package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class CreateFreshPerspectiveRequestHandlerImpl implements CreateFreshPerspectiveRequestHandler {

    @Nonnull
    private final InputBox inputBox;

    @Inject
    public CreateFreshPerspectiveRequestHandlerImpl(@Nonnull InputBox inputBox) {
        this.inputBox = checkNotNull(inputBox);
    }

    @Override
    public void createFreshPerspective(final Callback callback) {
        inputBox.showDialog("Enter tab name", false, "", input -> {
            String trimmedInput = input.trim();
            if(trimmedInput.isEmpty()) {
                return;
            }
            callback.createNewPerspective(PerspectiveId.get(trimmedInput));
        });
    }
}
