package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;
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
    private final Optional<OWLAnnotationProperty> property;

    @Nonnull
    private final String matchExpression;

    private final boolean regEx;

    @Nonnull
    private final ImmutableSet<PropertyAnnotationValue> replacement;

    @AutoFactory
    public ReplaceAnnotationValuesChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                      @Nonnull ImmutableSet<OWLEntity> entities,
                                                      @Provided @Nonnull OWLOntology rootOntology,
                                                      @Nonnull Optional<OWLAnnotationProperty> property,
                                                      @Nonnull String matchExpression,
                                                      boolean regEx,
                                                      @Nonnull ImmutableSet<PropertyAnnotationValue> replacementValues) {
        this.dataFactory = checkNotNull(dataFactory);
        this.entities = checkNotNull(entities);
        this.rootOntology = checkNotNull(rootOntology);
        this.property = checkNotNull(property);
        this.matchExpression = checkNotNull(matchExpression);
        this.regEx = regEx;
        this.replacement = checkNotNull(replacementValues);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
        String p;
        if (regEx) {
            p = matchExpression;
        }
        else {
            p = Pattern.quote(matchExpression);
        }
        Pattern pattern = Pattern.compile(p, Pattern.DOTALL);
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            for(OWLEntity entity : entities) {
                for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                    processAnnotationAssertion(ontology, entity, ax, pattern, builder);
                }
            }
        }
        return builder.build(true);
    }

    private void processAnnotationAssertion(OWLOntology ontology, OWLEntity entity, OWLAnnotationAssertionAxiom ax, Pattern pattern, OntologyChangeList.Builder<Boolean> builder) {
        if (!ax.getValue().isLiteral()) {
            return;
        }
        OWLLiteral value = (OWLLiteral) ax.getValue();
        if (property.isPresent() && !ax.getProperty().equals(property.get())) {
            return;
        }
        Matcher matcher = pattern.matcher(value.getLiteral());
        if (!matcher.matches()) {
            return;
        }
        return;
//        MatchResult matchResult = matcher.toMatchResult();
//        String replacementLexicalValue;
//        if(regEx) {
//            replacementLexicalValue = matcher.replaceAll(replacement);
//        }
//        else {
//            replacementLexicalValue = replacement;
//        }
//        OWLLiteral replacement;
//        if(value.hasLang()) {
//            replacement = dataFactory.getOWLLiteral(replacementLexicalValue, value.getLang());
//        }
//        else {
//            replacement = dataFactory.getOWLLiteral(replacementLexicalValue, value.getDatatype());
//        }
//        OWLAnnotationProperty prop = property.orElse(ax.getProperty());
//        OWLAxiom replacementAx = dataFactory.getOWLAnnotationAssertionAxiom(prop,
//                                                                            entity.getIRI(),
//                                                                            replacement,
//                                                                            ax.getAnnotations());
//        if (replacementAx.equals(ax)) {
//            return;
//        }
//        builder.removeAxiom(ontology, ax);
//        builder.addAxiom(ontology, replacementAx);
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
