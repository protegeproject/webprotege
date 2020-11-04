package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.event.LargeNumberOfChangesEvent;
import edu.stanford.bmir.protege.web.shared.event.LargeNumberOfChangesHandler;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
@ProjectSingleton
public class LargeNumberOfChangesManager implements LargeNumberOfChangesHandler {

    private Set<LargeNumberOfChangesEvent> handledEvents = new HashSet<>();

    private MessageBox messageBox;

    @Inject
    public LargeNumberOfChangesManager(MessageBox messageBox) {
        this.messageBox = checkNotNull(messageBox);
    }

    @Override
    public void handleLargeNumberOfChanges(LargeNumberOfChangesEvent event) {
        if(handledEvents.contains(event)) {
            return;
        }
        if(handledEvents.size() > 5) {
            handledEvents.clear();
        }
        handledEvents.add(event);
        displayLargeNumberOfChangesMessage();
    }

    private void displayLargeNumberOfChangesMessage() {
        messageBox.showConfirmBox("Refresh?",
                                  "A large number of changes have just happened.  Would you like to refresh the view (recommended)?",
                                  DialogButton.NO,
                                  DialogButton.YES,
                                  this::handleRefreshRequest,
                                  DialogButton.YES);
    }

    private void handleRefreshRequest() {

    }
}
