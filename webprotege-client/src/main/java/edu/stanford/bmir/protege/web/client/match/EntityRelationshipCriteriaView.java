package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public interface EntityRelationshipCriteriaView extends IsWidget {

    void clearProperty();

    void setProperty(@Nonnull OWLPropertyData propertyData);

    @Nonnull
    Optional<OWLPropertyData> getProperty();

    void setPropertyChangeHandler(@Nonnull Runnable handler);


    @Nonnull
    AcceptsOneWidget getValueCriteriaContainer();
}
