package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/06/2013
 */
public interface ValueListEditor<O> extends ValueEditor<List<O>>, HasEnabled, HasRequestFocus, HasPlaceholder {

    enum NewRowMode {
        AUTOMATIC,
        MANUAL
    }

    void setDeleteConfirmationPrompt(@Nonnull DeleteConfirmationPrompt<O> prompt);

    void setNewRowMode(@Nonnull NewRowMode newRowMode);

    void setReorderEnabled(boolean enabled);

    void forEachEditor(@Nonnull Consumer<ValueEditor<O>> consumer);

    void firstEditor(@Nonnull Consumer<ValueEditor<O>> consumer);
}
