package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public interface GeneratedAnnotationDescriptorView extends IsWidget {

    interface SelectedTypeNameChangedHandler {
        void handleTypeNameChanged(String typeName);
    }

    void setSelectedTypeNameChangedHandler(@Nonnull SelectedTypeNameChangedHandler handler);

    void setTypeNames(ImmutableList<String> typeNames);

    void setSelectedTypeName(@Nonnull String choiceName);

    String getSelectedTypeName();

    void setProperty(@Nonnull OWLAnnotationPropertyData property);

    @Nonnull
    Optional<OWLAnnotationPropertyData> getProperty();

    @Nonnull
    AcceptsOneWidget getValueDescriptorContainer();

    @Nonnull
    AcceptsOneWidget getActivatedByCriteriaContainer();
}
