package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.protege.gwt.graphtree.shared.tree.HasTextRendering;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class EntityHierarchyNode implements IsSerializable, Serializable, Comparable<EntityHierarchyNode>, HasTextRendering {


    private OWLEntity entity;

    private String browserText = "";

    private boolean deprecated = false;

    private ImmutableSet<Watch> watches;

    private int openCommentCount;

    private Collection<Tag> tags;

    public EntityHierarchyNode(@Nonnull OWLEntity entity,
                               @Nonnull String browserText,
                               boolean deprecated,
                               @Nonnull Set<Watch> watches,
                               int openCommentCount,
                               Collection<Tag> tags) {
        this.entity = checkNotNull(entity);
        this.browserText = checkNotNull(browserText);
        this.deprecated = deprecated;
        this.watches = ImmutableSet.copyOf(watches);
        this.openCommentCount = openCommentCount;
        this.tags = ImmutableList.copyOf(tags);
    }

    @GwtSerializationConstructor
    private EntityHierarchyNode() {
    }

    @Override
    public String getText() {
        return getBrowserText();
    }

    public String getBrowserText() {
        return browserText;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public Collection<Tag> getTags() {
        return ImmutableList.copyOf(tags);
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public Set<Watch> getWatches() {
        return watches;
    }

    public int getOpenCommentCount() {
        return openCommentCount;
    }

    public OWLEntityData getEntityData() {
        return DataFactory.getOWLEntityData(entity, getBrowserText());
    }

    @Override
    public int compareTo(EntityHierarchyNode o) {
        return this.getBrowserText().compareToIgnoreCase(o.getBrowserText());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EntityHierarchyNode)) {
            return false;
        }
        EntityHierarchyNode other = (EntityHierarchyNode) obj;
        return this.getEntity().equals(other.getEntity())
                && this.browserText.equals(other.browserText)
                && this.deprecated == other.deprecated
                && this.openCommentCount == other.openCommentCount
                && this.watches.equals(other.watches)
                && this.tags.equals(other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, browserText, deprecated, openCommentCount, watches, tags);
    }


    @Override
    public String toString() {
        return toStringHelper("EntityHierarchyNode")
                .addValue(entity)
                .add("browserText", browserText)
                .add("deprecated", deprecated)
                .add("openComments", openCommentCount)
                .add("watches", watches)
                .add("tags", tags)
                .toString();
    }

    public static class Builder {

    }
}
