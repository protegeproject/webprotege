package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonymScope;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getIRI;
import static edu.stanford.bmir.protege.web.server.obo.OboUtil.getStringValue;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermSynonymsManager {

    @Nonnull
    private final OWLDataFactory df;

    @Nonnull
    private final AnnotationToXRefConverter xRefConverter;

    @Nonnull
    private final XRefExtractor xRefExtractor;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;


    @Inject
    public TermSynonymsManager(@Nonnull OWLDataFactory df,
                               @Nonnull AnnotationToXRefConverter xRefConverter,
                               @Nonnull XRefExtractor xRefExtractor,
                               @Nonnull ChangeManager changeManager,
                               @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                               @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex) {
        this.df = df;
        this.xRefConverter = xRefConverter;
        this.xRefExtractor = xRefExtractor;
        this.changeManager = changeManager;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionsIndex;
    }

    public Collection<OBOTermSynonym> getSynonyms(OWLEntity term) {
        var subject = term.getIRI();
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(subject, ontId))
                                     .filter(ax -> getSynonymScope(ax) != null)
                                     .map(ax -> new OBOTermSynonym(xRefExtractor.getXRefs(ax),
                                                                   getStringValue(ax),
                                                                   getSynonymScope(ax)))
                                     .collect(toSet());
    }

    @Nullable
    private OBOTermSynonymScope getSynonymScope(OWLAnnotationAssertionAxiom ax) {
        IRI iri = ax.getProperty()
                    .getIRI();

        if(isExactSynonymIRI(iri)) {
            return OBOTermSynonymScope.EXACT;
        }
        else if(isRelatedSynonymIRI(iri)) {
            return OBOTermSynonymScope.RELATED;
        }
        else if(isNarrowSynonymIRI(iri)) {
            return OBOTermSynonymScope.NARROWER;
        }
        else if(isBroadSynonymIRI(iri)) {
            return OBOTermSynonymScope.BROADER;
        }
        else {
            return null;
        }

    }

    private boolean isExactSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_EXACT);
    }

    private boolean isRelatedSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_RELATED);
    }

    private boolean isNarrowSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_NARROW);
    }

    private boolean isBroadSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_BROAD);
    }

    private boolean isOBOIRI(IRI iriToCheck, OBOFormatConstants.OboFormatTag tag) {
        final Obo2OWLConstants.Obo2OWLVocabulary obo2OWLVocab = Obo2OWLConstants.getVocabularyObj(tag.getTag());
        return obo2OWLVocab != null && obo2OWLVocab.getIRI()
                                                   .equals(iriToCheck);
    }

    public void setSynonyms(@Nonnull UserId userId,
                            @Nonnull OWLEntity term,
                            @Nonnull Collection<OBOTermSynonym> synonyms) {

        var changes = new ArrayList<OntologyChange>();
        var subject = term.getIRI();
        projectOntologiesIndex.getOntologyIds()
                              .forEach(ontId -> {
                                  annotationAssertionsIndex.getAxiomsForSubject(subject, ontId)
                                                           .filter(ax -> getSynonymScope(ax) != null)
                                                           .map(ax -> RemoveAxiomChange.of(ontId, ax))
                                                           .forEach(changes::add);
                                  for(var oboTermSynonym : synonyms) {
                                      var synonymProperty = getSynonymAnnoationProperty(df, oboTermSynonym.getScope());
                                      var synonymLiteral = df.getOWLLiteral(oboTermSynonym.getName());
                                      var synonymXRefs = oboTermSynonym.getXRefs()
                                                                .stream()
                                                                .filter(x -> !x.isEmpty())
                                                                .map(xRefConverter::toAnnotation)
                                                                .collect(toSet());
                                      var synonymAxiom = df.getOWLAnnotationAssertionAxiom(synonymProperty,
                                                                                           term.getIRI(),
                                                                                           synonymLiteral,
                                                                                           synonymXRefs);
                                      changes.add(AddAxiomChange.of(ontId, synonymAxiom));
                                  }
                              });

        if(!changes.isEmpty()) {
            changeManager.applyChanges(userId,
                                       new FixedChangeListGenerator<>(changes, term, "Edited term synonyms")
            );
        }
    }

    public OWLAnnotationProperty getSynonymAnnoationProperty(OWLDataFactory df, OBOTermSynonymScope scope) {
        switch(scope) {
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
}
