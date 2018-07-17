package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public interface DictionaryLanguageView extends IsWidget {

    @Nullable
    OWLAnnotationPropertyData getAnnotationProperty();

    void setAnnotationProperty(@Nullable OWLAnnotationPropertyData annotationPropertyData);

    @Nonnull
    String getLang();

    void setLang(@Nonnull String lang);
}
