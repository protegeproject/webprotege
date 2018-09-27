package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class ReplaceAnnotationValuesAction implements ProjectAction<ReplaceAnnotationValuesResult> {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    @Nullable
    private OWLAnnotationProperty property;

    private String matchString;

    private boolean regex;

    private ImmutableSet<PropertyAnnotationValue> replacementValues;

    public ReplaceAnnotationValuesAction(@Nonnull ProjectId projectId,
                                         @Nonnull ImmutableSet<OWLEntity> entities,
                                         @Nonnull Optional<OWLAnnotationProperty> property,
                                         @Nonnull String matchString,
                                         boolean regex,
                                         @Nonnull ImmutableSet<PropertyAnnotationValue> replacementValues) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.property = checkNotNull(property).orElse(null);
        this.matchString = checkNotNull(matchString);
        this.regex = regex;
        this.replacementValues = checkNotNull(replacementValues);
    }

    @GwtSerializationConstructor
    private ReplaceAnnotationValuesAction() {
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
    public Optional<OWLAnnotationProperty> getProperty() {
        return Optional.ofNullable(property);
    }

    @Nonnull
    public String getMatchString() {
        return matchString;
    }

    public boolean isRegex() {
        return regex;
    }

    @Nonnull
    public ImmutableSet<PropertyAnnotationValue> getReplacementValues() {
        return replacementValues;
    }
}
