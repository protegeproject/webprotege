package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public interface SubFormFieldDescriptorView extends IsWidget {

    void clear();

    @Nonnull
    AcceptsOneWidget getSubFormContainer();

    @Nonnull
    EntityType<?> getEntityType();

    void setEntityType(@Nonnull EntityType<?> entityType);

    void setParents(@Nonnull List<OWLPrimitiveData> parents);

    void setAnnotationPropertyValues(@Nonnull List<PropertyAnnotationValue> propertyAnnotationValues);

    List<PropertyAnnotationValue> getAnnotationPropertyValues();
}
