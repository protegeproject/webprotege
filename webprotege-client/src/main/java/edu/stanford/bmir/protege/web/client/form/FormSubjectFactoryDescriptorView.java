package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public interface FormSubjectFactoryDescriptorView extends IsWidget {

    @Nonnull
    EntityType<?> getEntityType();

    void setEntityType(@Nonnull EntityType<?> entityType);

    void setParentClass(@Nonnull OWLClassData parent);

    @Nonnull
    Optional<OWLClassData> getParentClass();

    void clear();


}
