package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class ParentClassAxiomParser {

    private final RegExp classAssertionRegExp = RegExp.compile("ClassAssertion\\(<([^>]+)>\\s+\\$\\{subject\\.iri\\}\\)");

    private final RegExp subClassOfRegExp = RegExp.compile("SubClassOf\\(\\$\\{subject\\.iri\\} <([^>]+)>\\)");

    private final ImmutableList<RegExp> axiomPatterns = ImmutableList.of(classAssertionRegExp, subClassOfRegExp);

    public Optional<OWLClass> parseParentAxiom(@Nonnull String axiom) {
        checkNotNull(axiom);
        return axiomPatterns.stream()
                            .map(axiomPattern -> parseAxiom(axiom, axiomPattern))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .findFirst();
    }

    public Optional<OWLClass> parseAxiom(@Nonnull String axiom, RegExp classAssertionRegExp) {
        MatchResult matchResult = classAssertionRegExp.exec(axiom);
        if(matchResult == null) {
            return Optional.empty();
        }
        else {
            String parentIri = matchResult.getGroup(1);
            return Optional.of(DataFactory.getOWLClass(parentIri));
        }
    }
}
