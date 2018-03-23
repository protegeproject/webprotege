package edu.stanford.bmir.protege.web.client.editor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
public interface DeleteConfirmationPrompt<T> {

    /**
     * A callback for a value deletion prompt.
     */
    interface DeleteConfirmationPromptCallback {
        /**
         * Delete the value if the argument is true. Don't delete the value if the argument is
         * false.
         */
        void deleteValue(boolean b);
    }

    /**
     * Called to prompt the user when deleting a value from a value list.
     * @param value The value to be deleted.
     * @param callback A callback to signal whether the value should be delete.  This callback
     *                 must be called by the prompt code.
     */
    void shouldDeleteValue(@Nonnull T value,
                           @Nonnull DeleteConfirmationPromptCallback callback);
}
