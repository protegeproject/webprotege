package edu.stanford.bmir.protege.web.shared.collection;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2017
 */
@Entity(noClassnameStored = true, value = "CollectionItemData")
@Indexes(
        {
                @Index(fields = {
                        @Field(CollectionItemData.COLLECTION_ID),
                        @Field(CollectionItemData.ITEM)
                }, options = @IndexOptions(unique = true))
        }
)
public class CollectionItemData {

    public static final String COLLECTION_ID = "collectionId";

    public static final String ITEM = "item";

    public static final String FORM_DATA = "formData";

    @Property(COLLECTION_ID)
    private CollectionId collectionId;

    @Property(ITEM)
    private CollectionItem item;

    @Nullable
    @Property(FORM_DATA)
    private FormData formData;

    public CollectionItemData(@Nonnull CollectionId collectionId,
                              @Nonnull CollectionItem item,
                              @Nonnull FormData formData) {
        this.collectionId = checkNotNull(collectionId);
        this.item = checkNotNull(item);
        this.formData = checkNotNull(formData);
    }

    public CollectionItemData(@Nonnull CollectionId collectionId,
                              @Nonnull CollectionItem item) {
        this.collectionId = checkNotNull(collectionId);
        this.item = checkNotNull(item);
        this.formData = null;
    }

    @GwtSerializationConstructor
    private CollectionItemData() {
    }

    @Nonnull
    public CollectionId getCollectionId() {
        return collectionId;
    }

    @Nonnull
    public CollectionItem getItem() {
        return item;
    }

    @Nonnull
    public Optional<FormData> getFormData() {
        return Optional.ofNullable(formData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(collectionId, item, formData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollectionItemData)) {
            return false;
        }
        CollectionItemData other = (CollectionItemData) obj;
        return this.collectionId.equals(other.collectionId)
                && this.item.equals(other.item)
                && Objects.equal(this.formData, other.formData);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionItemData")
                .addValue(collectionId)
                .addValue(item)
                .addValue(formData)
                .toString();
    }
}
