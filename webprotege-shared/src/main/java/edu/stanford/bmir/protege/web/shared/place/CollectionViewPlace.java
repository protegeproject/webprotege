package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public class CollectionViewPlace extends Place implements HasProjectId {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CollectionId collectionId;

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final Optional<CollectionItem> selection;

    public CollectionViewPlace(@Nonnull ProjectId projectId,
                               @Nonnull CollectionId collectionId,
                               @Nonnull FormId formId,
                               @Nonnull Optional<CollectionItem> selection) {
        this.projectId = checkNotNull(projectId);
        this.collectionId = checkNotNull(collectionId);
        this.formId = checkNotNull(formId);
        this.selection = checkNotNull(selection);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public CollectionId getCollectionId() {
        return collectionId;
    }

    @Nonnull
    public FormId getFormId() {
        return formId;
    }

    @Nonnull
    public Optional<CollectionItem> getSelection() {
        return selection;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, collectionId, formId, selection);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollectionViewPlace)) {
            return false;
        }
        CollectionViewPlace other = (CollectionViewPlace) obj;
        return this.projectId.equals(other.projectId)
                && this.collectionId.equals(other.collectionId)
                && this.formId.equals(other.formId)
                && this.selection.equals(other.selection);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionViewPlace")
                .addValue(projectId)
                .addValue(collectionId)
                .addValue(formId)
                .addValue(selection)
                .toString();
    }



}
