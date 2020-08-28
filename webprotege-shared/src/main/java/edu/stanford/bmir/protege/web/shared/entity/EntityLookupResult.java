package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;
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
public abstract class EntityLookupResult {

    public static EntityLookupResult get(@Nonnull DictionaryLanguage language,
                                         @Nonnull EntityNode entityNode,
                                         @Nonnull SearchResultMatch matchResult,
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
    public abstract SearchResultMatch getMatchResult();

    @Nonnull
    public abstract String getDirectLink();
}
