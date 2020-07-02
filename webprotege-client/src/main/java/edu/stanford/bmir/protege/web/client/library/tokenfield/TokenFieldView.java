package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public interface TokenFieldView extends IsWidget {

    void add(IsWidget view);

    void remove(IsWidget view);

    void clear();

    void setPlaceholder(String placeholder);

    void setPlaceholderVisible(boolean visible);

    interface AddTokenHandler {
        void handleAddToken(ClickEvent e);
    }

    void setAddTokenHandler(@Nonnull AddTokenHandler handler);
}
