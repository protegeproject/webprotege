package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonymScope;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getIRI;
import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getStringValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermSynonymsManager {


    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory df;

    @Nonnull
    private final AnnotationToXRefConverter xRefConverter;

    @Nonnull
    private final XRefExtractor xRefExtractor;

    @Nonnull
    private final ChangeManager changeManager;



    @Inject
    public TermSynonymsManager(@Nonnull OWLOntology rootOntology,
                               @Nonnull OWLDataFactory df,
                               @Nonnull AnnotationToXRefConverter xRefConverter,
                               @Nonnull XRefExtractor xRefExtractor,
                               @Nonnull ChangeManager changeManager) {
        this.rootOntology = rootOntology;
        this.df = df;
        this.xRefConverter = xRefConverter;
        this.xRefExtractor = xRefExtractor;
        this.changeManager = changeManager;
    }

    public Collection<OBOTermSynonym> getSynonyms(OWLEntity term) {
        Set<OBOTermSynonym> result = new HashSet<>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(term.getIRI());
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                OBOTermSynonymScope synonymScope = getSynonymScope(ax);
                if (synonymScope != null) {
                    OBOTermSynonym termSynonym = new OBOTermSynonym(xRefExtractor.getXRefs(ax),
                                                                    getStringValue(ax),
                                                                    synonymScope);
                    result.add(termSynonym);
                }

            }
        }

        return result;
    }

    public void setSynonyms(@Nonnull UserId userId,
                            @Nonnull OWLEntity term,
                            @Nonnull Collection<OBOTermSynonym> synonyms) {
        List<OWLOntologyChange> changes = new ArrayList<>();
        rootOntology.getAnnotationAssertionAxioms(term.getIRI()).stream()
                .filter(ax -> getSynonymScope(ax) != null)
                .map(ax -> new RemoveAxiom(rootOntology, ax))
                .forEach(changes::add);

        for (OBOTermSynonym synonym : synonyms) {
            OWLAnnotationProperty synonymProperty = getSynonymAnnoationProperty(df, synonym.getScope());
            OWLLiteral synonymNameLiteral = df.getOWLLiteral(synonym.getName());
            Set<OWLAnnotation> synonymXRefs = synonym.getXRefs().stream()
                    .filter(x -> !x.isEmpty())
                    .map(xRefConverter::toAnnotation)
                    .collect(Collectors.toSet());
            OWLAnnotationAssertionAxiom synonymAnnotationAssertion = df.getOWLAnnotationAssertionAxiom(synonymProperty,
                                                                                                       term.getIRI(),
                                                                                                       synonymNameLiteral,
                                                                                                       synonymXRefs);
            changes.add(new AddAxiom(rootOntology, synonymAnnotationAssertion));
        }
        if(!changes.isEmpty()) {
            changeManager.applyChanges(userId,
                                       new FixedChangeListGenerator<>(changes),
                                       new FixedMessageChangeDescriptionGenerator<>("Edited term synonyms"));
        }
    }

    public OWLAnnotationProperty getSynonymAnnoationProperty(OWLDataFactory df, OBOTermSynonymScope scope) {
        switch (scope) {
            case EXACT:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_EXACT));
            case NARROWER:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_NARROW));
            case BROADER:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_BROAD));
            case RELATED:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_RELATED));
            default:
                throw new RuntimeException("Unknown synonym scope: " + scope);
        }
    }


    @Nullable
    private OBOTermSynonymScope getSynonymScope(OWLAnnotationAssertionAxiom ax) {
        IRI iri = ax.getProperty().getIRI();

        if (isExactSynonymIRI(iri)) {
            return OBOTermSynonymScope.EXACT;
        }
        else if (isRelatedSynonymIRI(iri)) {
            return OBOTermSynonymScope.RELATED;
        }
        else if (isNarrowSynonymIRI(iri)) {
            return OBOTermSynonymScope.NARROWER;
        }
        else if (isBroadSynonymIRI(iri)) {
            return OBOTermSynonymScope.BROADER;
        }
        else {
            return null;
        }

    }

    private boolean isBroadSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_BROAD);
    }

    private boolean isNarrowSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_NARROW);
    }

    private boolean isRelatedSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_RELATED);
    }

    private boolean isExactSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_EXACT);
    }

    private boolean isOBOIRI(IRI iriToCheck, OBOFormatConstants.OboFormatTag tag) {
        final Obo2OWLConstants.Obo2OWLVocabulary obo2OWLVocab = Obo2OWLConstants.getVocabularyObj(tag.getTag());
        return obo2OWLVocab != null && obo2OWLVocab.getIRI().equals(iriToCheck);
    }
}
