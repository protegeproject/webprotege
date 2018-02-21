package edu.stanford.bmir.protege.web.shared.collection;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Aug 2017
 */
public class CreateCollectionItemsAction implements ProjectAction<CreateCollectionItemsResult> {

    private ProjectId projectId;

    private CollectionId collectionId;

    private List<String> items;

    public CreateCollectionItemsAction(@Nonnull ProjectId projectId,
                                       @Nonnull CollectionId collectionId,
                                       @Nonnull List<String> items) {
        this.projectId = checkNotNull(projectId);
        this.collectionId = checkNotNull(collectionId);
        this.items = new ArrayList<>(checkNotNull(items));
    }

    @GwtSerializationConstructor
    private CreateCollectionItemsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public CollectionId getCollectionId() {
        return collectionId;
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, collectionId, items);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateCollectionItemsAction)) {
            return false;
        }
        CreateCollectionItemsAction other = (CreateCollectionItemsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.collectionId.equals(other.collectionId)
                && this.items.equals(other.items);
    }


    @Override
    public String toString() {
        return toStringHelper("CreateCollectionItemsAction")
                .addValue(projectId)
                .addValue(collectionId)
                .add("items", items)
                .toString();
    }
}
