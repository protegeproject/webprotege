package edu.stanford.bmir.protege.web.shared.search;

import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchResultEntityItem extends SearchResultItem {

    private OWLEntity subject;

    private SearchResultEntityItem(String preview) {
        super(preview);
    }

    public SearchResultEntityItem(String preview, OWLEntity subject) {
        super(preview);
        this.subject = subject;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }
}
