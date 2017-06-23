package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.client.place.Item;
import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.client.place.PlaceKey;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class ProjectViewPlace extends Place implements HasProjectId {

    private final ProjectId projectId;

    private final PerspectiveId perspectiveId;

    private final ItemSelection selection;

    public ProjectViewPlace(ProjectId projectId, PerspectiveId perspectiveId, ItemSelection selection) {
        this.projectId = checkNotNull(projectId);
        this.perspectiveId = checkNotNull(perspectiveId);
        this.selection = checkNotNull(selection);
    }

    public static Builder builder(ProjectId projectId, PerspectiveId perspectiveId) {
        return new Builder(projectId, perspectiveId);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    public ItemSelection getItemSelection() {
        return selection;
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectViewPlace")
                .addValue(projectId)
                .addValue(perspectiveId)
                .addValue(selection).toString();
    }

    public PlaceKey getKey() {
        return new Key(projectId, perspectiveId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, perspectiveId, selection);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectViewPlace)) {
            return false;
        }
        ProjectViewPlace other = (ProjectViewPlace) obj;
        return this.projectId.equals(other.projectId)
                && this.perspectiveId.equals(other.perspectiveId)
                && this.selection.equals(other.selection);
    }

    public Builder builder() {
        Builder builder = new Builder(projectId, perspectiveId);
        builder.withSelectedItems(selection);
        return builder;
    }

    public static class Builder {

        private ProjectId projectId;

        private PerspectiveId perspectiveId;

        private ItemSelection.Builder itemSelectionBuilder;

        public Builder(ProjectId projectId, PerspectiveId perspectiveId) {
            this.projectId = checkNotNull(projectId);
            this.perspectiveId = checkNotNull(perspectiveId);
            this.itemSelectionBuilder = ItemSelection.builder();
        }



        public Builder withSelectedItem(Item<?> item) {
            itemSelectionBuilder.addItem(item);
            return this;
        }

        public Builder withSelectedItems(ItemSelection items) {
            for(Item<?> item : items) {
                itemSelectionBuilder.addItem(item);
            }
            return this;
        }

        public Builder withSelectedItemsFromPlace(Place place) {
            if(!(place instanceof ProjectViewPlace)) {
                return this;
            }
            withSelectedItems(((ProjectViewPlace) place).getItemSelection());
            return this;
        }

        public Builder withPerspectiveId(PerspectiveId perspectiveId) {
            this.perspectiveId = checkNotNull(perspectiveId);
            return this;
        }

        public Builder clearSelection() {
            itemSelectionBuilder.clear();
            return this;
        }

        public ProjectViewPlace build() {
            ItemSelection selection = itemSelectionBuilder.build();
            return new ProjectViewPlace(projectId, perspectiveId, selection);
        }
    }


    private static class Key implements PlaceKey {

        private ProjectId projectId;

        private PerspectiveId perspectiveId;

        private Key(ProjectId projectId, PerspectiveId perspectiveId) {
            this.projectId = projectId;
            this.perspectiveId = perspectiveId;
        }

        @Override
        public int hashCode() {
//            return "ProjectPerspectivePlace.Key".hashCode() + projectId.hashCode() + perspectiveId.hashCode();
            return "ProjectPerspectivePlace.Key".hashCode() + perspectiveId.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof Key)) {
                return false;
            }
            Key other = (Key) o;
//            return this.projectId.equals(other.projectId) && this.perspectiveId.equals(other.perspectiveId);
            return this.perspectiveId.equals(other.perspectiveId);
        }
    }




}
