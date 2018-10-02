package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
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
public class EditAnnotationsAction implements ProjectAction<EditAnnotationsResult>, HasCommitMessage {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    private Operation operation;

    @Nullable
    private OWLAnnotationProperty property;

    @Nullable
    private String lexicalValueExpression;

    @Nullable
    private String langTagExpression;

    private boolean lexicalValueExpressionIsRegEx;

    private NewAnnotationData newAnnotationData;

    private String commitMessage;

    public EditAnnotationsAction(@Nonnull ProjectId projectId,
                                 @Nonnull ImmutableSet<OWLEntity> entities,
                                 Operation operation, @Nonnull Optional<OWLAnnotationProperty> property,
                                 @Nonnull Optional<String> lexicalValueExpression,
                                 boolean lexicalValueExpressionIsRegEx,
                                 @Nonnull Optional<String> langTagExpression,
                                 @Nonnull NewAnnotationData newAnnotationData,
                                 @Nonnull String commitMessage) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.operation = checkNotNull(operation);
        this.property = checkNotNull(property).orElse(null);
        this.lexicalValueExpression = checkNotNull(lexicalValueExpression).orElse(null);
        this.langTagExpression = checkNotNull(langTagExpression).orElse(null);
        this.lexicalValueExpressionIsRegEx = lexicalValueExpressionIsRegEx;
        this.newAnnotationData = checkNotNull(newAnnotationData);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @GwtSerializationConstructor
    private EditAnnotationsAction() {
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
    public Operation getOperation() {
        return operation;
    }

    @Nonnull
    public Optional<OWLAnnotationProperty> getProperty() {
        return Optional.ofNullable(property);
    }

    @Nonnull
    public Optional<String> getLexicalValueExpression() {
        return Optional.ofNullable(lexicalValueExpression);
    }

    public boolean isLexicalValueExpressionIsRegEx() {
        return lexicalValueExpressionIsRegEx;
    }

    @Nonnull
    public Optional<String> getLangTagExpression() {
        return Optional.ofNullable(langTagExpression);
    }

    @Nonnull
    public NewAnnotationData getNewAnnotationData() {
        return newAnnotationData;
    }

    @Nonnull
    public String getCommitMessage() {
        return commitMessage;
    }
}
