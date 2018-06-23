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

    private synchronized void invalidate() {
        cachedTags = null;
    }

    private synchronized CachedTags get() {
        if (cachedTags == null) {
            cachedTags = CachedTags.build(delegate.findTags());
        }
        return cachedTags;
    }

    @Override
    public synchronized void saveTag(@Nonnull Tag tag) {
        invalidate();
        delegate.saveTag(tag);
    }

    @Override
    public synchronized void saveTags(@Nonnull Iterable<Tag> tags) {
        invalidate();
        delegate.saveTags(tags);
    }

    @Override
    public synchronized void deleteTag(@Nonnull TagId tagId) {
        invalidate();
        delegate.deleteTag(tagId);
    }

    @Nonnull
    @Override
    public synchronized List<Tag> findTags() {
        return get().getTags();
    }

    @Nonnull
    @Override
    public synchronized Optional<Tag> findTagByTagId(@Nonnull TagId tagId) {
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
