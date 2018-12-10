package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.inject.Inject;
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
        if(axiom.getProperty().isDeprecated()) {
            var msg = formatter.formatString("Deprecated %s", axiom.getSubject());
            return Optional.of(ChangeSummary.get(msg));
        }
        var msg = formatter.formatString("Added annotation (%s  %s) to %s",
                                axiom.getProperty(),
                                axiom.getValue(),
                                axiom.getSubject());
        return Optional.of(ChangeSummary.get(msg));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLAnnotationAssertionAxiom axiom) {
        if(axiom.getProperty().isDeprecated()) {
            var msg = formatter.formatString("Undeprecated %s", axiom.getSubject());
            return Optional.of(ChangeSummary.get(msg));
        }
        var msg = formatter.formatString("Removed annotation (%s  %s) from %s",
                                axiom.getProperty(),
                                axiom.getValue(),
                                axiom.getSubject());
        return Optional.of(ChangeSummary.get(msg));
    }
}
