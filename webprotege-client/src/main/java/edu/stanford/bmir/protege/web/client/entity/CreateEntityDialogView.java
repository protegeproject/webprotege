package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public interface CreateEntityDialogView extends IsWidget, HasInitialFocusable {

    interface ResetLangTagHandler {
        void handleResetLangTag();
    }

    interface LangTagChangedHandler {
        void handleLangTagChanged();
    }

    void setEntityType(@Nonnull EntityType<?> entityType);

    @Nonnull
    String getText();

    @Nonnull
    String getLangTag();

    void setLangTag(@Nonnull String langTag);

    void clear();

    void setResetLangTagHandler(@Nonnull ResetLangTagHandler handler);

    void setLangTagChangedHandler(@Nonnull LangTagChangedHandler handler);

    void setNoDisplayLanguageForLangTagVisible(boolean visible);
}
