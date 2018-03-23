package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.HasEnabled;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/06/2013
 */
public interface ValueListEditor<O> extends ValueEditor<List<O>>, HasEnabled {

    void setDeleteConfirmationPrompt(@Nonnull DeleteConfirmationPrompt<O> prompt);
}
