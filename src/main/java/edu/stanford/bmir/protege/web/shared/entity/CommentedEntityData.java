package edu.stanford.bmir.protege.web.shared.entity;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Mar 2017
 */
public class CommentedEntityData implements IsSerializable, Comparable<CommentedEntityData> {

    public static final transient Comparator<CommentedEntityData> byEntity = Comparator.naturalOrder();

    public static final transient Comparator<CommentedEntityData> byLastModified =
            Comparator.comparing(CommentedEntityData::getLastModified, Comparator.reverseOrder())
                      .thenComparing(Comparator.naturalOrder());


    private OWLEntityData entityData;

    private int totalThreadCount;

    private int openThreadCount;

    private int totalCommentCount;

    private long lastModified;

    private UserId lastModifiedBy;

    private List<UserId> partipants;

    private static transient Comparator<CommentedEntityData> comparator =
            Comparator.comparing((CommentedEntityData c) -> c.getOpenThreadCount() > 0)
                      .reversed()
                      .thenComparing(c -> c.getEntityData().getBrowserText().toLowerCase());

    @GwtSerializationConstructor
    private CommentedEntityData() {
    }

    public CommentedEntityData(@Nonnull OWLEntityData entityData,
                               int totalThreadCount,
                               int openThreadCount,
                               int totalCommentCount,
                               long lastModified,
                               @Nonnull UserId lastModifiedBy,
                               @Nonnull List<UserId> participants) {
        this.entityData = checkNotNull(entityData);
        this.totalThreadCount = totalThreadCount;
        this.openThreadCount = openThreadCount;
        this.totalCommentCount = totalCommentCount;
        this.lastModified = lastModified;
        this.lastModifiedBy = checkNotNull(lastModifiedBy);
        this.partipants = new ArrayList<>(participants);
    }

    @Nonnull
    public OWLEntityData getEntityData() {
        return entityData;
    }

    public int getTotalThreadCount() {
        return totalThreadCount;
    }

    public int getOpenThreadCount() {
        return openThreadCount;
    }

    public int getTotalCommentCount() {
        return totalCommentCount;
    }

    public long getLastModified() {
        return lastModified;
    }

    @Nonnull
    public UserId getLastModifiedBy() {
        return lastModifiedBy;
    }

    public List<UserId> getPartipants() {
        return new ArrayList<>(partipants);
    }

    @Override
    public int compareTo(@Nonnull CommentedEntityData o) {
        return comparator.compare(this, o);
    }
}
