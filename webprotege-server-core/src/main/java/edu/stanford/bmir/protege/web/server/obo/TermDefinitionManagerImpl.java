package edu.stanford.bmir.protege.web.server.obo;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.obo.IAOVocabulary;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermDefinition;
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
public class TermDefinitionManagerImpl implements TermDefinitionManager {

    private static final OWLAnnotationValueVisitorEx<String> ANNOTATION_VALUE_TO_STRING = new OWLAnnotationValueVisitorEx<>() {
        @Nonnull
        public String visit(@Nonnull IRI iri) {
            return iri.toString();
        }

        @Nonnull
        public String visit(@Nonnull OWLAnonymousIndividual individual) {
            return individual.getID()
                             .getID();
        }

        @Nonnull
        public String visit(@Nonnull OWLLiteral literal) {
            return literal.getLiteral();
        }
    };

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

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Inject
    public TermDefinitionManagerImpl(@Nonnull OWLDataFactory df,
                                     @Nonnull AnnotationToXRefConverter xRefConverter,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull XRefExtractor extractor,
                                     @Nonnull RenderingManager renderingManager,
                                     @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                     @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager, @Nonnull MessageFormatter messageFormatter) {
        this.df = df;
        this.xRefConverter = xRefConverter;
        this.changeManager = changeManager;
        this.extractor = extractor;
        this.renderingManager = renderingManager;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.messageFormatter = messageFormatter;
    }

    @Override
    @Nonnull
    public OBOTermDefinition getTermDefinition(@Nonnull OWLEntity term) {
        if(!(term.isOWLClass())) {
            return emptyOboTermDefinition();
        }
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(term.getIRI(),
                                                                                                     ontId))
                                     .filter(this::isOboTermDefinitionAxiom)
                                     .findFirst()
                                     .map(this::toOboTermDefinition)
                                     .orElse(emptyOboTermDefinition());
    }

    private OBOTermDefinition emptyOboTermDefinition() {
        return OBOTermDefinition.empty();
    }

    private boolean isOboTermDefinitionAxiom(OWLAnnotationAssertionAxiom ax) {
        return isOBODefinitionProperty(ax.getProperty());
    }

    private OBOTermDefinition toOboTermDefinition(OWLAnnotationAssertionAxiom ax) {
        var definition = ax.getValue()
                           .accept(ANNOTATION_VALUE_TO_STRING);
        var xrefs = extractor.getXRefs(ax);
        return new OBOTermDefinition(xrefs, definition);
    }

    private boolean isOBODefinitionProperty(OWLAnnotationProperty property) {
        IRI propertyIRI = property.getIRI();
        IRI defIRI = Obo2OWLConstants.getVocabularyObj(TAG_DEF.getTag())
                                     .getIRI();
        if(propertyIRI.equals(defIRI)) {
            return true;
        }
        String fragment = propertyIRI.getFragment();
        return fragment.endsWith(IAOVocabulary.DEFINITION.getSuffix());
    }

    @Override
    public void setTermDefinition(@Nonnull UserId userId,
                                  @Nonnull OWLEntity term,
                                  @Nonnull OBOTermDefinition definition) {
        var xRefs = definition.getXRefs();
        var xrefsAsAnnotations = xRefs.stream()
                                      .filter(xRef -> !xRef.isEmpty())
                                      .map(xRefConverter::toAnnotation)
                                      .collect(Collectors.toSet());

        var subject = term.getIRI();
        var defIRI = getIRI(OBOFormatConstants.OboFormatTag.TAG_DEF);
        var defAnnotationProperty = df.getOWLAnnotationProperty(defIRI);
        var defLiteral = df.getOWLLiteral(definition.getDefinition());
        var definitionAssertion = df.getOWLAnnotationAssertionAxiom(defAnnotationProperty,
                                                                    subject,
                                                                    defLiteral,
                                                                    xrefsAsAnnotations);

        List<OntologyChange> changes = new ArrayList<>();
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> {
            annotationAssertionsIndex.getAxiomsForSubject(subject, ontId)
                                     .filter(this::isOboTermDefinitionAxiom)
                                     .forEach(ax -> {
                                         var removeAxiom = RemoveAxiomChange.of(ontId, ax);
                                         changes.add(removeAxiom);
                                         var nonXRefAnnotations = getAxiomAnnotationsExcludingXRefs(ax);
                                         var allAnnotations = Sets.union(nonXRefAnnotations, xrefsAsAnnotations);
                                         var fullyAnnotatedDefinitionAxiom = definitionAssertion.getAnnotatedAxiom(allAnnotations);
                                         var addAxiom = AddAxiomChange.of(ontId, fullyAnnotatedDefinitionAxiom);
                                         changes.add(addAxiom);
                                     });
        });
        if(changes.isEmpty()) {
            // New
            var ontId = defaultOntologyIdManager.getDefaultOntologyId();
            changes.add(AddAxiomChange.of(ontId, definitionAssertion));
        }
        var message = messageFormatter.format("Edited the term definition for {0}", term);
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes,
                                                                  term,
                                                                  message)
        );
    }

    private Set<OWLAnnotation> getAxiomAnnotationsExcludingXRefs(OWLAnnotationAssertionAxiom existingAx) {
        Set<OWLAnnotation> annotationsToCopy = new HashSet<>();
        for(OWLAnnotation existingAnnotation : existingAx.getAnnotations()) {
            if(!isXRefProperty(existingAnnotation.getProperty())) {
                annotationsToCopy.add(existingAnnotation);
            }
        }
        return annotationsToCopy;
    }
}
