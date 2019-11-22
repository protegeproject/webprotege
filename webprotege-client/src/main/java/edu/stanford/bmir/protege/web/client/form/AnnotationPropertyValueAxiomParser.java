package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;

import javax.annotation.Nonnull;
import javax.annotation.RegEx;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class AnnotationPropertyValueAxiomParser {

    private final RegExp annotationLiteralPattern = RegExp.compile("AnnotationAssertion\\(<([^>]+)> \\$\\{subject\\.iri\\} \"([^\"]+)\"(\\@[a-zA-Z\\-]+)?");

    private final RegExp annotationIriPattern = RegExp.compile("AnnotationAssertion\\(<([^>]+)> \\$\\{subject\\.iri\\} <([^>]+)>");

    @Nonnull
    Optional<OWLAnnotation> parseAnnotation(@Nonnull String axiom) {
        Optional<OWLAnnotation> annotationLiteral = parseAnnotationLiteral(axiom);
        if(annotationLiteral.isPresent()) {
            return annotationLiteral;
        }
        return parseAnnotationIri(axiom);
    }

    private Optional<OWLAnnotation> parseAnnotationIri(@Nonnull String axiom) {
        MatchResult matchResult = annotationIriPattern.exec(axiom);
        if(matchResult == null) {
            return Optional.empty();
        }
        String propertyIRI = matchResult.getGroup(1);
        OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty(propertyIRI);

        String iri = matchResult.getGroup(2);
        return Optional.of(new OWLAnnotationImpl(property, IRI.create(iri), Collections.emptySet()));
    }

    private Optional<OWLAnnotation> parseAnnotationLiteral(@Nonnull String axiom) {
        MatchResult matchResult = annotationLiteralPattern.exec(axiom);
        if(matchResult == null) {
            return Optional.empty();
        }
        String propertyIRI = matchResult.getGroup(1);
        OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty(propertyIRI);

        String lexicalForm = matchResult.getGroup(2);
        String langTag = matchResult.getGroup(3);
        OWLLiteral literal;
        if(langTag == null) {
            literal = DataFactory.getOWLLiteral(lexicalForm, "");
        }
        else {
            literal = DataFactory.getOWLLiteral(lexicalForm, langTag);
        }
        return Optional.of(new OWLAnnotationImpl(property, literal, Collections.emptySet()));
    }
}
