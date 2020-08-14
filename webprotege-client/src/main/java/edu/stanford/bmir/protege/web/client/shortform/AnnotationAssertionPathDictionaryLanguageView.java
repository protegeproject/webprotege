package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public interface AnnotationAssertionPathDictionaryLanguageView extends IsWidget {

    @Nonnull
    ImmutableList<OWLAnnotationPropertyData> getPath();

    @Nonnull
    String getLangTag();

    void setLangTag(@Nonnull String langTag);

    void setPath(@Nonnull ImmutableList<OWLAnnotationPropertyData> props);
}
