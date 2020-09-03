package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.State;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private DefaultOntologyIdManager defaultOntologyManager;

    @Inject
    public GetOntologyAnnotationsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                                               @Nonnull RenderingManager renderingManager,
                                               @Nonnull PropertyValueComparator propertyValueComparator,
                                               @Nonnull DefaultOntologyIdManager defaultOntologyManager) {
        super(accessManager);
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
        this.renderingManager = renderingManager;
        this.propertyValueComparator = propertyValueComparator;
        this.defaultOntologyManager = defaultOntologyManager;
    }

    @Nonnull
    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOntologyAnnotationsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOntologyAnnotationsResult execute(@Nonnull GetOntologyAnnotationsAction action, @Nonnull ExecutionContext executionContext) {
        var ontologyId = action.getOntologyId().orElse(defaultOntologyManager.getDefaultOntologyId());
        var annotations = ontologyAnnotationsIndex.getOntologyAnnotations(ontologyId)
                                .map(annotation -> PropertyAnnotationValue.get(
                                        renderingManager.getAnnotationPropertyData(annotation.getProperty()),
                                        renderingManager.getRendering(annotation.getValue()),
                                        State.ASSERTED
                                ))
                                .sorted(propertyValueComparator)
                                .collect(toImmutableList());
        return new GetOntologyAnnotationsResult(ontologyId, annotations);
    }

}
