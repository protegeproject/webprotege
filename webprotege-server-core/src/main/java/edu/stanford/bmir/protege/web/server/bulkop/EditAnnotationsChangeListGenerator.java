package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.bulkop.NewAnnotationData;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class EditAnnotationsChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ImmutableSet<OWLEntity> entities;

    @Nonnull
    private final Operation operation;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final Optional<OWLAnnotationProperty> property;

    @Nonnull
    private final Optional<String> valueExpression;

    @Nonnull
    private final Optional<String> matchLang;

    private final boolean regEx;

    @Nonnull
    private final NewAnnotationData newAnnotationData;

    @Nonnull
    private final String commitMessage;

    @AutoFactory
    public EditAnnotationsChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                              @Nonnull ImmutableSet<OWLEntity> entities,
                                              @Nonnull Operation operation,
                                              @Provided @Nonnull OWLOntology rootOntology,
                                              @Nonnull Optional<OWLAnnotationProperty> matchProperty,
                                              @Nonnull Optional<String> matchLexicalValue,
                                              boolean regEx, @Nonnull Optional<String> matchLangTag,
                                              @Nonnull NewAnnotationData newAnnotationData,
                                              @Nonnull String commitMessage) {
        this.dataFactory = checkNotNull(dataFactory);
        this.entities = checkNotNull(entities);
        this.operation = checkNotNull(operation);
        this.rootOntology = checkNotNull(rootOntology);
        this.property = checkNotNull(matchProperty);
        this.valueExpression = checkNotNull(matchLexicalValue);
        this.matchLang = checkNotNull(matchLangTag);
        this.regEx = regEx;
        this.newAnnotationData = checkNotNull(newAnnotationData);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
        String p;
        if(valueExpression.isPresent()) {
            if (regEx) {
                p = valueExpression.get();
            }
            else {
                p = "^" + Pattern.quote(valueExpression.get()) + "$";
            }
        }
        else {
            p = ".*";
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
        OWLLiteral literal = (OWLLiteral) ax.getValue();
        if (!matchesProperty(ax)) {
            return;
        }
        Matcher matcher = pattern.matcher(literal.getLiteral());
        if(!matcher.find()) {
            return;
        }
        if(!matchesLangTag(literal)) {
            return;
        }
        OWLAnnotationProperty newProperty = getNewProperty(ax);
        String newLexicalValue = getNewLexicalValue(literal, matcher);
        String newLangTag = getNewLangTag(literal);
        OWLLiteral newLiteral;
        if(newLangTag.isEmpty()) {
            newLiteral = dataFactory.getOWLLiteral(newLexicalValue, literal.getDatatype());
        }
        else {
            newLiteral = dataFactory.getOWLLiteral(newLexicalValue, newLangTag);
        }
        OWLAxiom newAx = dataFactory.getOWLAnnotationAssertionAxiom(newProperty,
                                                                    entity.getIRI(),
                                                                    newLiteral,
                                                                    ax.getAnnotations());
        if (newAnnotationData.equals(ax)) {
            return;
        }
        if (operation == Operation.REPLACE || operation == Operation.DELETE) {
            builder.removeAxiom(ontology, ax);
        }
        if (operation == Operation.REPLACE || operation == Operation.AUGMENT) {
            builder.addAxiom(ontology, newAx);
        }
    }

    private String getNewLangTag(OWLLiteral literal) {
        return newAnnotationData.getLanguageTag().orElse(literal.getLang());
    }

    private String getNewLexicalValue(OWLLiteral literal, Matcher matcher) {
        return newAnnotationData.getValue().map(lv -> regEx ? matcher.replaceAll(lv) : lv).orElse(literal.getLiteral());
    }

    private OWLAnnotationProperty getNewProperty(OWLAnnotationAssertionAxiom ax) {
        return newAnnotationData.getProperty().orElse(ax.getProperty());
    }

    private Boolean matchesLangTag(OWLLiteral val) {
        return matchLang.map(lang -> lang.equalsIgnoreCase(val.getLang())).orElse(true);
    }


    private Boolean matchesProperty(OWLAnnotationAssertionAxiom ax) {
        return property.map(prop -> ax.getProperty().equals(prop)).orElse(true);
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return commitMessage;
    }
}
