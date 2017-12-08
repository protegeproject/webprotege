package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter.formatMessage;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesAction, CreateClassesResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull RenderingManager renderingManager,
                                      @Nonnull OWLOntology rootOntology,
                                      @Nonnull OWLDataFactory dataFactory) {
        super(accessManager, eventManager, applyChanges);
        this.renderingManager = renderingManager;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    public Class<CreateClassesAction> getActionClass() {
        return CreateClassesAction.class;
    }

    @Nonnull
    @Override
    protected List<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }


    @Override
    protected ChangeListGenerator<Set<OWLClass>> getChangeListGenerator(CreateClassesAction action, ExecutionContext executionContext) {
        return new CreateClassesChangeGenerator(action.getSourceText(),
                                                action.getParent(),
                                                rootOntology,
                                                dataFactory);
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLClass>> getChangeDescription(CreateClassesAction action, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Created classes");
    }

    @Override
    protected CreateClassesResult createActionResult(ChangeApplicationResult<Set<OWLClass>> changeApplicationResult, CreateClassesAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Optional<Set<OWLClass>> subject = changeApplicationResult.getSubject();
        return subject.map(classes -> new CreateClassesResult(action.getProjectId(), ImmutableSet.copyOf(classes), eventList))
                      .orElse(new CreateClassesResult(action.getProjectId(), ImmutableSet.of(), eventList));
    }

    private ChangeDescriptionGenerator<Set<OWLClass>> createChangeText(CreateClassesAction action, Collection<String> shortForms) {
        String msg;
        if (shortForms.size() > 1) {
            msg = "Added {0} as subclasses of {1}";
        }
        else {
            msg = "Added {0} as a subclass of {1}";
        }
        return new FixedMessageChangeDescriptionGenerator<>(formatMessage(msg, renderingManager,
                                                                          shortForms,
                                                                          action.getParent()
                                                                                .orElse(dataFactory.getOWLThing())));
    }
}
