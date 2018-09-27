package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public interface AnnotationSimpleMatchingCriteriaView extends IsWidget {

    @Nonnull
    Optional<OWLAnnotationPropertyData> getProperty();


    boolean isMatchValue();

    @Nonnull
    String getValue();

    boolean isValueRegularExpression();

    boolean isMatchLang();

    @Nonnull
    String getLang();
}
