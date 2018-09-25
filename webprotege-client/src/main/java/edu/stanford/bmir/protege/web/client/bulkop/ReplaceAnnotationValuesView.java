package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public interface ReplaceAnnotationValuesView extends IsWidget {

    @Nonnull
    Optional<OWLAnnotationPropertyData> getAnnotationProperty();

    @Nonnull
    String getMatch();

    @Nonnull
    String getReplacement();
}
