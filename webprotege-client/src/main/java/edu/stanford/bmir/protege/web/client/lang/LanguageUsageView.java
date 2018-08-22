package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
public interface LanguageUsageView extends IsWidget {

    void setProperty(@Nonnull OWLAnnotationPropertyData property);

    void setLanguageTag(@Nonnull String langTag);

    void setUsageCount(int usageCount);
}
