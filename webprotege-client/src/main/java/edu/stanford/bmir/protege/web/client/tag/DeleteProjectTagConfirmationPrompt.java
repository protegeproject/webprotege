package edu.stanford.bmir.protege.web.client.tag;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.editor.DeleteConfirmationPrompt;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.tag.TagData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle.QUESTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2018
 */
public class DeleteProjectTagConfirmationPrompt implements DeleteConfirmationPrompt<TagData> {

    private final Messages messages;

    @Inject
    public DeleteProjectTagConfirmationPrompt(@Nonnull Messages messages) {
        this.messages = checkNotNull(messages);
    }

    /**
     * Displays a prompt asking the user whether they want to delete a project tag.  The
     * prompt is only displayed if the project tag is actually used.
     * @param value The value to be deleted.
     * @param callback A callback to signal whether the value should be deleted.
     */
    @Override
    public void shouldDeleteValue(@Nonnull TagData value, @Nonnull DeleteConfirmationPromptCallback callback) {
        // Just go ahead and delete the tag if it's not used.
        if(value.getUsageCount() == 0) {
            callback.deleteValue(true);
            return;
        }
        // The tag is used so display a confirmantion box
        MessageBox.showConfirmBox(QUESTION,
                                  messages.tags_deleteTag(),
                                  messages.tags_deleteConfirmationMessage(value.getLabel(), value.getUsageCount()),
                                  CANCEL,
                                  () -> callback.deleteValue(false),
                                  DialogButton.get(messages.tags_deleteTag()),
                                  () -> callback.deleteValue(true),
                                  CANCEL);
    }
}
