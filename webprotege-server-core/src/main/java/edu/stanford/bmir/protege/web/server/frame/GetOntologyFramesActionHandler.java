package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentIdDisplayNameProvider;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesActionHandler extends AbstractProjectActionHandler<GetOntologyFramesAction, GetOntologyFramesResult> {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final OntologyDocumentIdDisplayNameProvider displayNameProvider;

    @Nonnull
    private final ContextRenderer renderer;

    @Inject
    public GetOntologyFramesActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                          @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                                          @Nonnull OntologyDocumentIdDisplayNameProvider displayNameProvider,
                                          @Nonnull ContextRenderer renderer) {
        super(accessManager);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologyAnnotationsIndex = checkNotNull(ontologyAnnotationsIndex);
        this.displayNameProvider = displayNameProvider;
        this.renderer = checkNotNull(renderer);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOntologyFramesAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOntologyFramesResult execute(@Nonnull GetOntologyFramesAction action, @Nonnull ExecutionContext executionContext) {
        var frames = projectOntologiesIndex.getOntologyDocumentIds()
                .map(this::toOntologyFrame)
                .collect(toImmutableList());
        return new GetOntologyFramesResult(frames);
    }

    private OntologyFrame toOntologyFrame(OntologyDocumentId documentId) {
        var ontologyAnnotations = ontologyAnnotationsIndex.getOntologyAnnotations(documentId)
                .map(this::toPropertyValue)
                .collect(Collectors.toList());
        var propertyValueList = new PropertyValueList(ontologyAnnotations);
        var shortForm = displayNameProvider.getDisplayName(documentId);
        return new OntologyFrame(documentId, propertyValueList, shortForm);
    }

    private PropertyAnnotationValue toPropertyValue(OWLAnnotation annotation) {
        return PropertyAnnotationValue.get(
                renderer.getAnnotationPropertyData(annotation.getProperty()),
                renderer.getAnnotationValueData(annotation.getValue()),
                State.ASSERTED
        );
    }

    @Nonnull
    @Override
    public Class<GetOntologyFramesAction> getActionClass() {
        return GetOntologyFramesAction.class;
    }
}
