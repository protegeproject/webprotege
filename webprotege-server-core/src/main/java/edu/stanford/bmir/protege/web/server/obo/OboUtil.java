package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class OboUtil {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public OboUtil(@Nonnull OWLDataFactory dataFactory,
                   @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                   @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                   @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.dataFactory = checkNotNull(dataFactory);
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    public static IRI getIRI(OBOFormatConstants.OboFormatTag tag) {
        return Obo2OWLConstants.getVocabularyObj(tag.getTag()).getIRI();
    }


    @Nonnull
    public String getStringAnnotationValue(@Nonnull IRI annotationSubject,
                                           @Nonnull IRI annotationPropertyIRI,
                                           @Nonnull String defaultValue) {
        var labelAnnotation = projectOntologiesIndex.getOntologyIds()
                                                    .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(annotationSubject, ontId))
                                                    .filter(ax -> ax.getProperty().getIRI().equals(annotationPropertyIRI))
                                                    .findFirst();
        return labelAnnotation.map(OboUtil::getStringValue).orElse(defaultValue);
    }

    @Nonnull
    public List<OntologyChange> replaceStringAnnotationValue(@Nonnull IRI annotationSubject,
                                                                @Nonnull IRI annotationPropertyIRI,
                                                                @Nonnull String replaceWith) {
        List<OntologyChange> changes = new ArrayList<>();
        OWLAnnotationProperty property = dataFactory.getOWLAnnotationProperty(annotationPropertyIRI);
        OWLLiteral value = dataFactory.getOWLLiteral(replaceWith);
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> {
            annotationAssertionsIndex.getAxiomsForSubject(annotationSubject, ontId).forEach(ax -> {
                changes.add(RemoveAxiomChange.of(ontId, ax));
                if (!replaceWith.isEmpty()) {
                    changes.add(AddAxiomChange.of(ontId, dataFactory.getOWLAnnotationAssertionAxiom(property, annotationSubject, value, ax.getAnnotations())));
                }
            });
        });
        if (!replaceWith.isEmpty() && changes.isEmpty()) {
            // No previous value, so set new one
            var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
            changes.add(AddAxiomChange.of(defaultOntologyId, dataFactory.getOWLAnnotationAssertionAxiom(property, annotationSubject, value)));
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
