package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public interface RelationshipEdgePropertyEqualsCriteriaView extends IsWidget {

    void setProperty(@Nonnull OWLPropertyData propertyData);

    @Nonnull
    Optional<OWLPropertyData> getProperty();
}
