package edu.stanford.bmir.protege.web.client.ontology.annotations;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public interface AnnotationsView extends IsWidget, ValueEditor<Set<PropertyAnnotationValue>>, HasEnabled {

}
