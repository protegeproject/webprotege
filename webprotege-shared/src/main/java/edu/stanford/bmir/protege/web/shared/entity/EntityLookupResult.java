package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@SuppressWarnings("GwtInconsistentSerializableClass")
public class EntityLookupResult implements Serializable, Comparable<EntityLookupResult> {

    private DictionaryLanguage language;

    private EntityNode entityNode;
    
    private EntityNameMatchResult matchResult;

    private String directLink;

    @GwtSerializationConstructor
    private EntityLookupResult() {
    }

    public EntityLookupResult(DictionaryLanguage language, EntityNode entityNode, EntityNameMatchResult matchResult, String directLink) {
        this.language = language;
        this.entityNode = entityNode;
        this.matchResult = matchResult;
        this.directLink = directLink;
    }

    public DictionaryLanguage getLanguage() {
        return language;
    }

    public OWLEntityData getOWLEntityData() {
        return entityNode.getEntityData();
    }


    public EntityNameMatchResult getMatchResult() {
        return matchResult;
    }

    public String getDirectLink() {
        return directLink;
    }

    @Nonnull
    public EntityNode getEntityNode() {
        return entityNode;
    }

    public String getDisplayText() {
        int browserTextMatchStart = matchResult.getStart();
        int browserTextMatchEnd = matchResult.getEnd();
        StringBuilder sb = new StringBuilder();
        String browserText = entityNode.getBrowserText();
        if (browserTextMatchStart < browserText.length() && browserTextMatchEnd <= browserText.length()) {
            sb.append("<div>");
            sb.append(browserText.substring(0, browserTextMatchStart));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(browserText.substring(browserTextMatchStart, browserTextMatchEnd));
            sb.append("</span>");
            sb.append(browserText.substring(browserTextMatchEnd));
            sb.append("</div>");
        }
        else {
            sb.append(browserText);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(EntityLookupResult other) {
        return this.matchResult.compareTo(other.matchResult);
    }
}
