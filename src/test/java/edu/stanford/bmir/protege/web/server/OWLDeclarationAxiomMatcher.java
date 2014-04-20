package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class OWLDeclarationAxiomMatcher extends TypeSafeMatcher<OWLDeclarationAxiom> {

    private OWLEntity entity;

    public OWLDeclarationAxiomMatcher(OWLEntity entity) {
        this.entity = entity;
    }

    @Override
    protected boolean matchesSafely(OWLDeclarationAxiom item) {
        return entity.equals(item.getEntity());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("declaration with entity");
    }

    public static OWLDeclarationAxiomMatcher declarationFor(OWLEntity entity) {
        return new OWLDeclarationAxiomMatcher(entity);
    }
}
