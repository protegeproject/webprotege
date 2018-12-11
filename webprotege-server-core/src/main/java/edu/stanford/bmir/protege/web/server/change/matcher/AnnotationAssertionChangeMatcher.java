package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.description.AddedAnnotation;
import edu.stanford.bmir.protege.web.server.change.description.DeprecatedEntities;
import edu.stanford.bmir.protege.web.server.change.description.RemovedAnnotation;
import edu.stanford.bmir.protege.web.server.change.description.UndeprecatedEntities;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class AnnotationAssertionChangeMatcher extends AbstractAxiomMatcher<OWLAnnotationAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public AnnotationAssertionChangeMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLAnnotationAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLAnnotationAssertionAxiom axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        if(!axiom.getSubject().isIRI()) {
            return Optional.empty();
        }
        if(axiom.getProperty().isDeprecated()) {
            return Optional.of(ChangeSummary.get(DeprecatedEntities.get(ImmutableSet.of((IRI) axiom.getSubject()))));
        }
        return Optional.of(ChangeSummary.get(AddedAnnotation.get((IRI) axiom.getSubject(),
                                                                 axiom.getProperty(),
                                                                 axiom.getValue())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        if(!axiom.getSubject().isIRI()) {
            return Optional.empty();
        }
        var subject = (IRI) axiom.getSubject();
        if(axiom.getProperty().isDeprecated()) {
            return Optional.of(ChangeSummary.get(UndeprecatedEntities.get(ImmutableSet.of(subject))));
        }
        return Optional.of(ChangeSummary.get(RemovedAnnotation.get(subject,
                                                                   axiom.getProperty(),
                                                                   axiom.getValue())));
    }
}
