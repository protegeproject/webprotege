package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public interface RelationshipPropertyMatcher extends Matcher<OWLProperty> {

    boolean isObjectProperty();

    boolean isDataProperty();

    boolean isAny();
}
