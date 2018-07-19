package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public interface DefaultDictionaryLanguageView extends IsWidget {

    @Nonnull
    Optional<OWLAnnotationPropertyData> getAnnotationProperty();

    void setAnnotationProperty(@Nonnull OWLAnnotationPropertyData annotationProperty);

    @Nonnull
    String getLanguageTag();

    void setLanguageTag(@Nonnull String languageTag);

    void clearAnnotationProperty();
}
