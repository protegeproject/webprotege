package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.client.ui.CreateFreshPerspectiveRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class CreateFreshPerspectiveRequestHandlerImpl implements CreateFreshPerspectiveRequestHandler {

    @Override
    public void createFreshPerspective(final Callback callback) {
        InputBox.showDialog("Enter tab name", false, "", new InputBoxHandler() {
            @Override
            public void handleAcceptInput(String input) {
                String trimmedInput = input.trim();
                if(trimmedInput.isEmpty()) {
                    return;
                }
                callback.createNewPerspective(new PerspectiveId(trimmedInput));
            }
        });
    }
}
