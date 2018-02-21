package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 * <p>
 *     Provides pertinent information about something being edited.  This minimally provides information about a selected
 *     entity.
 * </p>
 */
public class EditorContext<T> {

    private ProjectId projectId;

    private Optional<OWLEntityData> selectedEntityData;

    /**
     * Constructs a context object for an editor.
     * @param projectId The id of the project currently being edited
     * @param selectedEntity The selected entity.  Not {@code null}.
     * @throws NullPointerException if {@code selectedEntity} is {@code null} or if {@code projectId} is {@code null}.
     */
    public EditorContext(ProjectId projectId, Optional<OWLEntityData> selectedEntity) {
        this.projectId = checkNotNull(projectId, "projectId must not be null");
        this.selectedEntityData = checkNotNull(selectedEntity, "selectedEntity must not be null");
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the selected entity.
     * @return The selected entity as an {@link Optional}.  Not {@code null}.
     */
    public Optional<OWLEntityData> getSelectedEntity() {
        return selectedEntityData;
    }

    /**
     * Get the selected entity which has the specified type.
     * @param entityType The entity type.  Not {@code null}.
     * @param <E> The type of {@link OWLEntity}.
     * @return The {@link Optional} selection of the specified type.  The value will only be present if there is a
     * selection that is present and that selection has an entity type corresponding to the specified {@code entityType}.
     * @throws NullPointerException if {@code entityType} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <E extends OWLEntity> Optional<OWLEntityData> getSelectedEntityOfType(EntityType<E> entityType) {
        checkNotNull(entityType);
        if(!selectedEntityData.isPresent()) {
            return Optional.empty();
        }
        if(selectedEntityData.get().getEntity().getEntityType() != entityType) {
            return Optional.empty();
        }
        return Optional.of(selectedEntityData.get());
    }

    @Override
    public int hashCode() {
        return "EditorContext".hashCode() + projectId.hashCode() + selectedEntityData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EditorContext)) {
            return false;
        }
        EditorContext other = (EditorContext) obj;
        return this.projectId.equals(other.projectId) && this.selectedEntityData.equals(other.selectedEntityData);
    }
}
