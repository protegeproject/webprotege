package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public interface EditAnnotationsView extends IsWidget {

    @Nonnull
    Operation getOperation();

    @Nonnull
    Optional<OWLAnnotationPropertyData> getAnnotationProperty();

    @Nonnull
    String getMatch();

    boolean isRegEx();

    @Nonnull
    ImmutableSet<PropertyAnnotationValue> getReplacement();
}
