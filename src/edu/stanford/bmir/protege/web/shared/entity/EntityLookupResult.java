package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntityLookupResult implements Serializable, Comparable<EntityLookupResult> {

    private OWLEntityData visualEntity;
    
    private EntityNameMatchResult matchResult;

    private EntityLookupResult() {
    }

    public EntityLookupResult(OWLEntityData visualEntity, EntityNameMatchResult matchResult) {
        this.visualEntity = visualEntity;
        this.matchResult = matchResult;
    }

    public OWLEntityData getOWLEntityData() {
        return visualEntity;
    }


    public EntityNameMatchResult getMatchResult() {
        return matchResult;
    }

    public String getDisplayText() {
        int browserTextMatchStart = matchResult.getStart();
        int browserTextMatchEnd = matchResult.getEnd();
        StringBuilder sb = new StringBuilder();
        String browserText = visualEntity.getBrowserText();
        if (browserTextMatchStart < browserText.length() && browserTextMatchEnd <= browserText.length()) {
            sb.append(browserText.substring(0, browserTextMatchStart));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(browserText.substring(browserTextMatchStart, browserTextMatchEnd));
            sb.append("</span>");
            sb.append(browserText.substring(browserTextMatchEnd));
        }
        else {
            sb.append(browserText);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(EntityLookupResult other) {
        int diff =  this.matchResult.compareTo(other.matchResult);
        if(diff != 0) {
            return diff;
        }
        return visualEntity.compareToIgnorePrefixNames(other.visualEntity);
    }
}
