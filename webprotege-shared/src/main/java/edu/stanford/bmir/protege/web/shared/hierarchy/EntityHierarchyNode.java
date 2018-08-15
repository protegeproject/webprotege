package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.protege.gwt.graphtree.shared.tree.HasTextRendering;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityHierarchyNode implements IsSerializable, Serializable, Comparable<EntityHierarchyNode>, HasTextRendering {

    public static EntityHierarchyNode get(@Nonnull OWLEntity entity,
                                          @Nonnull String browserText,
                                          @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                          boolean deprecated,
                                          @Nonnull Set<Watch> watches,
                                          int openCommentCount,
                                          Collection<Tag> tags) {
        return new AutoValue_EntityHierarchyNode(entity,
                                                 browserText,
                                                 ImmutableSet.copyOf(tags),
                                                 deprecated,
                                                 ImmutableSet.copyOf(watches),
                                                 openCommentCount,
                                                 shortForms);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract String getBrowserText();

    @Nonnull
    @Override
    public String getText() {
        return getBrowserText();
    }

    public String getText(@Nonnull DictionaryLanguage prefLang) {
        return getText(prefLang, getBrowserText());
    }

    public String getText(@Nonnull DictionaryLanguage prefLang, String defaultText) {
        ImmutableMap<DictionaryLanguage, String> shortForms = getShortForms();
        return shortForms.entrySet().stream()
                         .filter(e -> e.getKey().isAnnotationBased())
                         .filter(e -> e.getKey().equals(prefLang))
                         .findFirst()
                         .map(Map.Entry::getValue)
                         .orElse(defaultText);
    }

    public OWLEntityData getEntityData() {
        return DataFactory.getOWLEntityData(getEntity(),
                                            getBrowserText(),
                                            getShortForms());
    }

    public abstract ImmutableSet<Tag> getTags();

    public abstract boolean isDeprecated();

    public abstract ImmutableSet<Watch> getWatches();

    public abstract int getOpenCommentCount();

    @Nonnull
    public abstract ImmutableMap<DictionaryLanguage, String> getShortForms();

    @Override
    public int compareTo(EntityHierarchyNode o) {
        return this.getBrowserText().compareToIgnoreCase(o.getBrowserText());
    }


}
