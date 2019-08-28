package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
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
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final ImmutableSet<OWLEntity> entities;

    @Nonnull
    private final Operation operation;

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
                                              @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                              @Provided @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                              @Nonnull ImmutableSet<OWLEntity> entities,
                                              @Nonnull Operation operation,
                                              @Nonnull Optional<OWLAnnotationProperty> matchProperty,
                                              @Nonnull Optional<String> matchLexicalValue,
                                              boolean regEx,
                                              @Nonnull Optional<String> matchLangTag,
                                              @Nonnull NewAnnotationData newAnnotationData,
                                              @Nonnull String commitMessage) {
        this.dataFactory = checkNotNull(dataFactory);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.entities = checkNotNull(entities);
        this.operation = checkNotNull(operation);
        this.property = checkNotNull(matchProperty);
        this.valueExpression = checkNotNull(matchLexicalValue);
        this.matchLang = checkNotNull(matchLangTag);
        this.regEx = regEx;
        this.newAnnotationData = checkNotNull(newAnnotationData);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        var builder = OntologyChangeList.<Boolean>builder();
        String p;
        if(valueExpression.isPresent()) {
            if(regEx) {
                p = valueExpression.get();
            }
            else {
                p = "^" + Pattern.quote(valueExpression.get()) + "$";
            }
        }
        else {
            p = ".*";
        }
        var pattern = Pattern.compile(p, Pattern.DOTALL);
        projectOntologiesIndex.getOntologyIds()
                              .forEach(ontId -> {
                                  entities.forEach(entity -> {
                                      annotationAssertionsIndex.getAxiomsForSubject(entity.getIRI(), ontId)
                                                               .forEach(ax -> processAnnotationAssertion(ontId,
                                                                                                         entity,
                                                                                                         ax,
                                                                                                         pattern,
                                                                                                         builder));
                                  });
                              });
        return builder.build(true);
    }

    private void processAnnotationAssertion(OWLOntologyID ontologyId,
                                            OWLEntity entity,
                                            OWLAnnotationAssertionAxiom ax,
                                            Pattern pattern,
                                            OntologyChangeList.Builder<Boolean> builder) {
        if(!ax.getValue()
              .isLiteral()) {
            return;
        }
        var literal = (OWLLiteral) ax.getValue();
        if(!matchesProperty(ax)) {
            return;
        }
        var matcher = pattern.matcher(literal.getLiteral());
        if(!matcher.find()) {
            return;
        }
        if(!matchesLangTag(literal)) {
            return;
        }
        var newProperty = getNewProperty(ax);
        var newLexicalValue = getNewLexicalValue(literal, matcher);
        var newLangTag = getNewLangTag(literal);
        OWLLiteral newLiteral;
        if(newLangTag.isEmpty()) {
            newLiteral = dataFactory.getOWLLiteral(newLexicalValue, literal.getDatatype());
        }
        else {
            newLiteral = dataFactory.getOWLLiteral(newLexicalValue, newLangTag);
        }
        var newAx = dataFactory.getOWLAnnotationAssertionAxiom(newProperty,
                                                               entity.getIRI(),
                                                               newLiteral,
                                                               ax.getAnnotations());
        if(operation == Operation.REPLACE || operation == Operation.DELETE) {
            builder.add(RemoveAxiomChange.of(ontologyId, ax));
        }
        if(operation == Operation.REPLACE || operation == Operation.AUGMENT) {
            builder.add(AddAxiomChange.of(ontologyId, newAx));
        }
    }

    private Boolean matchesProperty(OWLAnnotationAssertionAxiom ax) {
        return property.map(prop -> ax.getProperty()
                                      .equals(prop))
                       .orElse(true);
    }

    private Boolean matchesLangTag(OWLLiteral val) {
        return matchLang.map(lang -> lang.equalsIgnoreCase(val.getLang()))
                        .orElse(true);
    }

    private OWLAnnotationProperty getNewProperty(OWLAnnotationAssertionAxiom ax) {
        return newAnnotationData.getProperty()
                                .orElse(ax.getProperty());
    }

    private String getNewLexicalValue(OWLLiteral literal, Matcher matcher) {
        return newAnnotationData.getValue()
                                .map(lv -> regEx ? matcher.replaceAll(lv) : lv)
                                .orElse(literal.getLiteral());
    }

    private String getNewLangTag(OWLLiteral literal) {
        return newAnnotationData.getLanguageTag()
                                .orElse(literal.getLang());
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
