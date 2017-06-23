package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class OboUtil {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public OboUtil(@Nonnull OWLOntology rootOntology, @Nonnull OWLDataFactory dataFactory) {
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    public static IRI getIRI(OBOFormatConstants.OboFormatTag tag) {
        return Obo2OWLConstants.getVocabularyObj(tag.getTag()).getIRI();
    }


    @Nonnull
    public String getStringAnnotationValue(@Nonnull IRI annotationSubject,
                                           @Nonnull IRI annotationPropertyIRI,
                                           @Nonnull String defaultValue) {
        OWLAnnotationAssertionAxiom labelAnnotation = null;
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(annotationSubject);
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                if (ax.getProperty().getIRI().equals(annotationPropertyIRI)) {
                    labelAnnotation = ax;
                    break;
                }
            }
        }

        String label = defaultValue;
        if (labelAnnotation != null) {
            label = getStringValue(labelAnnotation);
        }
        return label;
    }

    @Nonnull
    public List<OWLOntologyChange> replaceStringAnnotationValue(@Nonnull IRI annotationSubject,
                                                                @Nonnull IRI annotationPropertyIRI,
                                                                @Nonnull String replaceWith) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLAnnotationProperty property = dataFactory.getOWLAnnotationProperty(annotationPropertyIRI);
        OWLLiteral value = dataFactory.getOWLLiteral(replaceWith);


        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(annotationSubject)) {
                if (ax.getProperty().getIRI().equals(annotationPropertyIRI)) {
                    changes.add(new RemoveAxiom(ontology, ax));
                    if (!replaceWith.isEmpty()) {
                        changes.add(new AddAxiom(rootOntology, dataFactory.getOWLAnnotationAssertionAxiom(property, annotationSubject, value, ax.getAnnotations())));
                    }
                }
            }
        }
        if (!replaceWith.isEmpty() && changes.isEmpty()) {
            // No previous value, so set new one
            changes.add(new AddAxiom(rootOntology, dataFactory.getOWLAnnotationAssertionAxiom(property, annotationSubject, value)));
        }
        return changes;
    }

    public static String getStringValue(OWLAnnotationAssertionAxiom labelAnnotation) {
        return labelAnnotation.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
            @Nonnull
            public String visit(@Nonnull IRI iri) {
                return iri.toString();
            }

            @Nonnull
            public String visit(@Nonnull OWLAnonymousIndividual individual) {
                return individual.getID().getID();
            }

            @Nonnull
            public String visit(@Nonnull OWLLiteral literal) {
                return literal.getLiteral();
            }
        });
    }


    public static boolean isXRefProperty(OWLAnnotationProperty property) {
        IRI iri = getXRefPropertyIRI();
        return property.getIRI().equals(iri);
    }

    public static IRI getXRefPropertyIRI() {
        return getIRI(OBOFormatConstants.OboFormatTag.TAG_XREF);
    }


}
