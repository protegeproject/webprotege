package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Inject
    public GetOntologyAnnotationsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull @RootOntology OWLOntology rootOntology,
                                               @Nonnull RenderingManager renderingManager,
                                               @Nonnull PropertyValueComparator propertyValueComparator) {
        super(accessManager);
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nonnull
    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOntologyAnnotationsResult execute(@Nonnull GetOntologyAnnotationsAction action, @Nonnull ExecutionContext executionContext) {
        List<OWLAnnotation> result = new ArrayList<>(rootOntology.getAnnotations());
        ImmutableList.Builder<PropertyAnnotationValue> annotationValues = ImmutableList.builder();
        result.stream()
                .map(annotation -> PropertyAnnotationValue.get(
                        renderingManager.getAnnotationPropertyData(annotation.getProperty()),
                        renderingManager.getRendering(annotation.getValue()),
                        State.ASSERTED
                ))
                .sorted(propertyValueComparator)
                .forEach(annotationValues::add);
        return new GetOntologyAnnotationsResult(annotationValues.build());
    }

}
