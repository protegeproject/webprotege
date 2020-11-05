package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.events.RefreshUserInterfaceEvent;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.event.LargeNumberOfChangesEvent;
import edu.stanford.bmir.protege.web.shared.event.LargeNumberOfChangesHandler;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
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

    private final Set<LargeNumberOfChangesEvent> handledEvents = new HashSet<>();


    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final EventBus eventBus;

    @Inject
    public LargeNumberOfChangesManager(@Nonnull ProjectId projectId,
                                       @Nonnull MessageBox messageBox,
                                       @Nonnull EventBus eventBus) {
        this.messageBox = checkNotNull(messageBox);
        this.eventBus = checkNotNull(eventBus);
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
        eventBus.fireEvent(RefreshUserInterfaceEvent.get());
    }
}
