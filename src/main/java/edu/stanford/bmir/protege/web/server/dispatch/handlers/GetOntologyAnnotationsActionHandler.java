package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
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
public class GetOntologyAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Inject
    public GetOntologyAnnotationsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager);
        this.rootOntology = rootOntology;
    }

    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetOntologyAnnotationsResult execute(GetOntologyAnnotationsAction action, ExecutionContext executionContext) {
        List<OWLAnnotation> result = new ArrayList<>(rootOntology.getAnnotations());
        return new GetOntologyAnnotationsResult(ImmutableList.of());
    }

}
