package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public interface DisplayLanguageEditorView extends IsWidget {

    void setPrimaryDisplayLanguage(@Nonnull OWLAnnotationPropertyData property,
                                   @Nonnull String lang);

    @Nonnull
    Optional<OWLAnnotationPropertyData> getPrimaryLanguageProperty();

    @Nonnull
    String getPrimaryLanguageTag();

    void setSecondaryDisplayLanguage(@Nonnull OWLAnnotationPropertyData property,
                                     @Nonnull String lang);

    @Nonnull
    Optional<OWLAnnotationPropertyData> getSecondaryLanguageProperty();

    @Nonnull
    String getSecondaryLanguageTag();

    void setChangeHandler(@Nonnull ChangeHandler changeHandler);


    interface ChangeHandler {

        void handleDisplayLanguageChanged();
    }
}
