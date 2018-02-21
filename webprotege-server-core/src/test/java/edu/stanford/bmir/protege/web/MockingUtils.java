package edu.stanford.bmir.protege.web;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/11/2013
 */
public class MockingUtils {

    private static int counter = 0;

    private static synchronized int nextInt() {
        return counter++;
    }

    public static IRI mockIRI() {
        return IRI.create("http://stuff.com/I" + nextInt());
    }

    public static OWLClass mockOWLClass() {
        return new OWLClassImpl(mockIRI());
    }

    public static OWLObjectProperty mockOWLObjectProperty() {
        return new OWLObjectPropertyImpl(mockIRI());
    }

    public static OWLDataProperty mockOWLDataProperty() {
        return new OWLDataPropertyImpl(mockIRI());
    }

    public static OWLAnnotationProperty mockOWLAnnotationProperty() {
        return new OWLAnnotationPropertyImpl(mockIRI());
    }

    public static OWLNamedIndividual mockOWLNamedIndividual() {
        return new OWLNamedIndividualImpl(mockIRI());
    }

    public HasSignature mockHasSignature(OWLEntity ... entities) {
        HasSignature hasSignature = mock(HasSignature.class);
        when(hasSignature.getSignature()).thenReturn(new HashSet<OWLEntity>(Arrays.asList(entities)));
        return hasSignature;
    }

    public static UserId mockUserId() {
        return UserId.getUserId("User" + nextInt());
    }

}
