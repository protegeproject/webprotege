package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.core.Is.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class OWLDeclarationAxiomMatcher extends TypeSafeMatcher<OWLDeclarationAxiom> {

    private Matcher<OWLEntity> entityMatcher;

    public OWLDeclarationAxiomMatcher(Matcher<OWLEntity> entityMatcher) {
        this.entityMatcher = entityMatcher;
    }

    @Override
    protected boolean matchesSafely(OWLDeclarationAxiom item) {
        return entityMatcher.matches(item.getEntity());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("declaration with entity");
    }

    public static OWLDeclarationAxiomMatcher declarationFor(OWLEntity entity) {
        return new OWLDeclarationAxiomMatcher(is(entity));
    }
}
