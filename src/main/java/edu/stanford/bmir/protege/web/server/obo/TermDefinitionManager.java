package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.obo.IAOVocabulary;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermDefinition;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getIRI;
import static edu.stanford.bmir.protege.web.server.obo.OboUtil.isXRefProperty;
import static org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag.TAG_DEF;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermDefinitionManager {

    private static final OWLAnnotationValueVisitorEx<String> ANNOTATION_VALUE_TO_STRING = new OWLAnnotationValueVisitorEx<String>() {
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
    };

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory df;

    @Nonnull
    private final AnnotationToXRefConverter xRefConverter;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final XRefExtractor extractor;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public TermDefinitionManager(@Nonnull OWLOntology rootOntology,
                                 @Nonnull OWLDataFactory df,
                                 @Nonnull AnnotationToXRefConverter xRefConverter,
                                 @Nonnull ChangeManager changeManager,
                                 @Nonnull XRefExtractor extractor,
                                 @Nonnull RenderingManager renderingManager) {
        this.rootOntology = rootOntology;
        this.df = df;
        this.xRefConverter = xRefConverter;
        this.changeManager = changeManager;
        this.extractor = extractor;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    public OBOTermDefinition getTermDefinition(@Nonnull OWLEntity term) {
        if (!(term.isOWLClass())) {
            return OBOTermDefinition.empty();
        }
        OWLAnnotationAssertionAxiom definitionAnnotation = null;
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(term.getIRI());
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                OWLAnnotationProperty property = ax.getProperty();
                if (isOBODefinitionProperty(property)) {
                    definitionAnnotation = ax;
                    break;
                }
            }
        }

        if (definitionAnnotation != null) {
            String value = definitionAnnotation.getValue().accept(ANNOTATION_VALUE_TO_STRING);
            List<OBOXRef> xrefs = extractor.getXRefs(definitionAnnotation);
            return new OBOTermDefinition(xrefs, value);
        }
        else {
            return OBOTermDefinition.empty();
        }
    }

    public void setTermDefinition(@Nonnull UserId userId,
                                  @Nonnull OWLEntity term,
                                  @Nonnull OBOTermDefinition definition) {
        List<OBOXRef> xRefs = definition.getXRefs();
        Set<OWLAnnotation> xrefAnnotations = xRefs.stream()
                                                  .filter(x -> !x.isEmpty())
                                                  .map(xRefConverter::toAnnotation)
                                                  .collect(Collectors.toSet());

        IRI subject = term.getIRI();
        final IRI defIRI = getIRI(OBOFormatConstants.OboFormatTag.TAG_DEF);
        OWLAnnotationProperty defAnnotationProperty = df.getOWLAnnotationProperty(defIRI);
        OWLLiteral defLiteral = df.getOWLLiteral(definition.getDefinition());
        OWLAnnotationAssertionAxiom definitionAssertion = df.getOWLAnnotationAssertionAxiom(defAnnotationProperty,
                                                                                            subject,
                                                                                            defLiteral,
                                                                                            xrefAnnotations);

        List<OWLOntologyChange> changes = new ArrayList<>();
        for (OWLAnnotationAssertionAxiom existingAx : rootOntology.getAnnotationAssertionAxioms(subject)) {
            if (existingAx.getProperty().getIRI().equals(defIRI)) {
                changes.add(new RemoveAxiom(rootOntology, existingAx));
                Set<OWLAnnotation> nonXRefAnnotations = getAxiomAnnotationsExcludingXRefs(existingAx);
                OWLAxiom fullyAnnotatedDefinitionAssertion = definitionAssertion.getAnnotatedAxiom(nonXRefAnnotations);
                changes.add(new AddAxiom(rootOntology, fullyAnnotatedDefinitionAssertion));
            }
        }
        if (changes.isEmpty()) {
            // New
            changes.add(new AddAxiom(rootOntology, definitionAssertion));
        }
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes),
                                   new FixedMessageChangeDescriptionGenerator<>("Edited the term definition for " + renderingManager
                                           .getRendering(term)
                                           .getBrowserText()));
    }

    private boolean isOBODefinitionProperty(OWLAnnotationProperty property) {
        IRI propertyIRI = property.getIRI();
        IRI defIRI = Obo2OWLConstants.getVocabularyObj(TAG_DEF.getTag()).getIRI();
        if (propertyIRI.equals(defIRI)) {
            return true;
        }
        String fragment = propertyIRI.getFragment();
        return fragment.endsWith(IAOVocabulary.DEFINITION.getSuffix());
    }

    private Set<OWLAnnotation> getAxiomAnnotationsExcludingXRefs(OWLAnnotationAssertionAxiom existingAx) {
        Set<OWLAnnotation> annotationsToCopy = new HashSet<OWLAnnotation>();
        for (OWLAnnotation existingAnnotation : existingAx.getAnnotations()) {
            if (!isXRefProperty(existingAnnotation.getProperty())) {
                annotationsToCopy.add(existingAnnotation);
            }
        }
        return annotationsToCopy;
    }
}
