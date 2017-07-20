package edu.stanford.bmir.protege.web.shared.collection;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

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
@Entity(noClassnameStored = true, value = "CollectionEntries")
public class CollectionElementData {

    public static final String COLLECTION_ID = "collectionId";

    public static final String ELEMENT_ID = "elementId";

    @Id
    @Property(COLLECTION_ID)
    private CollectionId collectionId;

    @Property(ELEMENT_ID)
    private CollectionElementId element;

    @Nullable
    private FormData formData;

    public CollectionElementData(@Nonnull CollectionId collectionId,
                                 @Nonnull CollectionElementId element,
                                 @Nonnull FormData formData) {
        this.collectionId = checkNotNull(collectionId);
        this.element = checkNotNull(element);
        this.formData = checkNotNull(formData);
    }

    public CollectionElementData(@Nonnull CollectionId collectionId,
                                 @Nonnull CollectionElementId element) {
        this.collectionId = checkNotNull(collectionId);
        this.element = checkNotNull(element);
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
    public CollectionElementId getElement() {
        return element;
    }

    @Nonnull
    public Optional<FormData> getFormData() {
        return Optional.ofNullable(formData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(collectionId, element, formData);
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
                && this.element.equals(other.element)
                && Objects.equal(this.formData, other.formData);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionElementData")
                .addValue(collectionId)
                .addValue(element)
                .addValue(formData)
                .toString();
    }
}
