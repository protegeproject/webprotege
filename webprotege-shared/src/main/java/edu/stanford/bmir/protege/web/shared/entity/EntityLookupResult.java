package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
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
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityLookupResult implements Comparable<EntityLookupResult> {

    public static EntityLookupResult get(@Nonnull DictionaryLanguage language,
                                         @Nonnull EntityNode entityNode,
                                         @Nonnull EntityNameMatchResult matchResult,
                                         @Nonnull String directLink) {
        return new AutoValue_EntityLookupResult(language, entityNode, matchResult, directLink);
    }

    @Nonnull
    public abstract DictionaryLanguage getLanguage();

    @Nonnull
    public abstract EntityNode getEntityNode();

    @Nonnull
    public OWLEntityData getOWLEntityData() {
        return getEntityNode().getEntityData();
    }

    @Nonnull
    public abstract EntityNameMatchResult getMatchResult();

    @Nonnull
    public abstract String getDirectLink();

    public String getDisplayText() {
        EntityNameMatchResult matchResult = getMatchResult();
        int browserTextMatchStart = matchResult.getStart();
        int browserTextMatchEnd = matchResult.getEnd();
        StringBuilder sb = new StringBuilder();
        String browserText = getEntityNode().getBrowserText();
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
    public int compareTo(@Nonnull EntityLookupResult other) {
        return this.getMatchResult().compareTo(other.getMatchResult());
    }
}
