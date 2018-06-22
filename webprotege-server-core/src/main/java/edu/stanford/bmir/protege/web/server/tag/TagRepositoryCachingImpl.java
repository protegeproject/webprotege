package edu.stanford.bmir.protege.web.server.tag;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2018
 */
public class TagRepositoryCachingImpl implements TagRepository {

    @Nonnull
    private final TagRepositoryImpl delegate;

    @Nullable
    private CachedTags cachedTags;


    @Inject
    public TagRepositoryCachingImpl(@Nonnull TagRepositoryImpl delegate) {
        this.delegate = checkNotNull(delegate);
    }

    private void invalidate() {
        cachedTags = null;
    }

    private CachedTags get() {
        if (cachedTags == null) {
            cachedTags = CachedTags.build(delegate.findTags());
        }
        return cachedTags;
    }

    @Override
    public void saveTag(@Nonnull Tag tag) {
        delegate.saveTag(tag);
        invalidate();
    }

    @Override
    public void saveTags(@Nonnull Iterable<Tag> tags) {
        delegate.saveTags(tags);
        invalidate();
    }

    @Override
    public void deleteTag(@Nonnull TagId tagId) {
        delegate.deleteTag(tagId);
        invalidate();
    }

    @Nonnull
    @Override
    public List<Tag> findTags() {
        return get().getTags();
    }

    @Nonnull
    @Override
    public Optional<Tag> findTagByTagId(@Nonnull TagId tagId) {
        return Optional.ofNullable(get().getTagsByTagId().get(tagId));
    }


    private static class CachedTags {

        @Nonnull
        private final ImmutableList<Tag> cachedTags;

        @Nonnull
        private final ImmutableMap<TagId, Tag> tagsByTagId;

        public CachedTags(@Nonnull ImmutableList<Tag> cachedTags,
                     @Nonnull ImmutableMap<TagId, Tag> tagsByTagId) {
            this.cachedTags = checkNotNull(cachedTags);
            this.tagsByTagId = checkNotNull(tagsByTagId);
        }

        @Nonnull
        public ImmutableList<Tag> getTags() {
            return cachedTags;
        }

        @Nonnull
        public ImmutableMap<TagId, Tag> getTagsByTagId() {
            return tagsByTagId;
        }

        private static CachedTags build(List<Tag> tags) {
            ImmutableList<Tag> cachedTags = ImmutableList.copyOf(tags);
            ImmutableMap.Builder<TagId, Tag> builder = ImmutableMap.builder();
            cachedTags.forEach(tag -> builder.put(tag.getTagId(), tag));
            ImmutableMap<TagId, Tag> tagsByTagId = builder.build();
            return new CachedTags(cachedTags, tagsByTagId);
        }
    }
}
