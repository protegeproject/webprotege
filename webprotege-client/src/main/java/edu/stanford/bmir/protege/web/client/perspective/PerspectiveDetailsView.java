package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public interface PerspectiveDetailsView extends IsWidget {

    interface LabelChangedHandler {
        void handleLabelChanged();
    }

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    LanguageMap getLabel();

    void setFavorite(boolean favorite);

    boolean isFavorite();

    void setLabelChangedHandler(LabelChangedHandler handler);
}
