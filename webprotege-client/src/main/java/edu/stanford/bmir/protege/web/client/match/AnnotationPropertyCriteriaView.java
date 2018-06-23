package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public interface AnnotationPropertyCriteriaView extends IsWidget {

    void setProperty(@Nonnull OWLAnnotationPropertyData property);

    Optional<OWLAnnotationProperty> getProperty();

    void clear();
}
