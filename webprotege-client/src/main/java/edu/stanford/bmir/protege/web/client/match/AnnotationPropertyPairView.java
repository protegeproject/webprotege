package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public interface AnnotationPropertyPairView extends IsWidget {

    Optional<OWLAnnotationProperty> getFirstProperty();

    Optional<OWLAnnotationProperty> getSecondProperty();
}
