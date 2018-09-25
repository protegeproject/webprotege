package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class ReplaceAnnotationValuesChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ImmutableSet<OWLEntity> entities;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLAnnotationProperty property;

    @Nonnull
    private final String matchExpression;

    @Nonnull
    private final String replacement;

    @AutoFactory
    public ReplaceAnnotationValuesChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                      @Nonnull ImmutableSet<OWLEntity> entities,
                                                      @Provided @Nonnull OWLOntology rootOntology,
                                                      @Nonnull OWLAnnotationProperty property,
                                                      @Nonnull String matchExpression,
                                                      @Nonnull String replacement) {
        this.dataFactory = checkNotNull(dataFactory);
        this.entities = checkNotNull(entities);
        this.rootOntology = checkNotNull(rootOntology);
        this.property = checkNotNull(property);
        this.matchExpression = checkNotNull(matchExpression);
        this.replacement = checkNotNull(replacement);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
        Pattern pattern = Pattern.compile(matchExpression);
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            for(OWLEntity entity : entities) {
                for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                    if (ax.getValue().isLiteral()) {
                        OWLLiteral value = (OWLLiteral) ax.getValue();
                        if(ax.getProperty().equals(property)) {
                            Matcher matcher = pattern.matcher(value.getLiteral());
                            if(matcher.matches()) {
                               MatchResult matchResult = matcher.toMatchResult();
                               String replacementLexicalValue = matcher.replaceAll(replacement);
                               OWLLiteral replacement;
                               if(value.hasLang()) {
                                   replacement = dataFactory.getOWLLiteral(replacementLexicalValue, value.getLang());
                               }
                               else {
                                   replacement = dataFactory.getOWLLiteral(replacementLexicalValue, value.getDatatype());
                               }
                               OWLAxiom replacementAx = dataFactory.getOWLAnnotationAssertionAxiom(property,
                                                                                                   entity.getIRI(),
                                                                                                   replacement,
                                                                                                   ax.getAnnotations());
                                if (!replacementAx.equals(ax)) {
                                    builder.removeAxiom(ontology, ax);
                                    builder.addAxiom(ontology, replacementAx);
                                }
                            }
                        }
                    }
                }
            }
        }
        return builder.build(true);
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Replaced annotation values";
    }
}
