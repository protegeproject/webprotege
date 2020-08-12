package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public interface AnnotationAssertionDictionaryLanguageView extends IsWidget {

    @Nonnull
    Optional<OWLAnnotationPropertyData> getAnnotationProperty();

    void setAnnotationProperty(@Nonnull OWLAnnotationPropertyData annotationProperty);

    @Nonnull
    String getLanguageTag();

    void setLanguageTag(@Nonnull String languageTag);

    void clearAnnotationProperty();

    void requestFocus();
}
