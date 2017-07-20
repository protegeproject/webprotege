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
@Entity(noClassnameStored = true, value = "CollectionData")
@Indexes(
        {
                @Index(fields = {
                        @Field(CollectionElementData.COLLECTION_ID),
                        @Field(CollectionElementData.ELEMENT_ID)
                }, options = @IndexOptions(unique = true))
        }
)
public class CollectionElementData {

    public static final String COLLECTION_ID = "collectionId";

    public static final String ELEMENT_ID = "elementId";

    public static final String FORM_DATA = "formData";

    @Property(COLLECTION_ID)
    private CollectionId collectionId;

    @Property(ELEMENT_ID)
    private CollectionElementId elementId;

    @Nullable
    @Property(FORM_DATA)
    private FormData formData;

    public CollectionElementData(@Nonnull CollectionId collectionId,
                                 @Nonnull CollectionElementId elementId,
                                 @Nonnull FormData formData) {
        this.collectionId = checkNotNull(collectionId);
        this.elementId = checkNotNull(elementId);
        this.formData = checkNotNull(formData);
    }

    public CollectionElementData(@Nonnull CollectionId collectionId,
                                 @Nonnull CollectionElementId elementId) {
        this.collectionId = checkNotNull(collectionId);
        this.elementId = checkNotNull(elementId);
        this.formData = null;
    }

    @GwtSerializationConstructor
    private CollectionElementData() {
    }

    @Nonnull
    public CollectionId getCollectionId() {
        return collectionId;
    }

    @Nonnull
    public CollectionElementId getElementId() {
        return elementId;
    }

    @Nonnull
    public Optional<FormData> getFormData() {
        return Optional.ofNullable(formData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(collectionId, elementId, formData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollectionElementData)) {
            return false;
        }
        CollectionElementData other = (CollectionElementData) obj;
        return this.collectionId.equals(other.collectionId)
                && this.elementId.equals(other.elementId)
                && Objects.equal(this.formData, other.formData);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionElementData")
                .addValue(collectionId)
                .addValue(elementId)
                .addValue(formData)
                .toString();
    }
}
