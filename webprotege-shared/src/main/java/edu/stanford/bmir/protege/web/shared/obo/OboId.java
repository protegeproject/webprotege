package edu.stanford.bmir.protege.web.shared.obo;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
public class OboId {

    private final static RegExp regExp = RegExp.compile("/([A-Z|a-z]+(_[A-Z|a-z]+)?)_([0-9]+)$");

    @Nonnull
    public static Optional<String> getOboId(@Nonnull IRI iri) {
        regExp.setLastIndex(0);
        MatchResult matchResult = regExp.exec(iri.toString());
        if(matchResult == null) {
            return Optional.empty();
        }
        String idSpace = matchResult.getGroup(1);
        String id = matchResult.getGroup(3);
        return Optional.of(idSpace + ":" + id);
    }
}
