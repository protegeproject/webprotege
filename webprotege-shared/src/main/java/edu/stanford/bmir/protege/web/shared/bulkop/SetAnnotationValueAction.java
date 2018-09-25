package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueAction implements ProjectAction<SetAnnotationValueResult> {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    private OWLAnnotationProperty property;

    private OWLAnnotationValue value;

    public SetAnnotationValueAction(ProjectId projectId, ImmutableSet<OWLEntity> entities, OWLAnnotationProperty property, OWLAnnotationValue value) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.property = checkNotNull(property);
        this.value = checkNotNull(value);
    }

    @GwtSerializationConstructor
    private SetAnnotationValueAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<OWLEntity> getEntities() {
        return entities;
    }

    @Nonnull
    public OWLAnnotationProperty getProperty() {
        return property;
    }

    @Nonnull
    public OWLAnnotationValue getValue() {
        return value;
    }
}
