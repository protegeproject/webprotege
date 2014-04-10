package edu.stanford.bmir.protege.web.shared.search;

import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/03/2014
 */
public class EntitySearchResult {

    private EntityNameMatchResult matchResult;

    private OWLEntity entity;

    private String shortForm;

    public EntitySearchResult(EntityNameMatchResult matchResult, OWLEntity entity, String shortForm) {
        this.matchResult = matchResult;
        this.entity = entity;
        this.shortForm = shortForm;
    }

    public EntityNameMatchResult getMatchResult() {
        return matchResult;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public String getShortForm() {
        return shortForm;
    }
}
