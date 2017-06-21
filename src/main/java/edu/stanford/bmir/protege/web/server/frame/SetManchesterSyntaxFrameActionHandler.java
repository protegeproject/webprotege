package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameActionHandler extends AbstractProjectChangeHandler<Void, SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    @Nonnull
    private final GetManchesterSyntaxFrameActionHandler handler;

    @Nonnull
    private final Provider<ManchesterSyntaxFrameParser> parserProvider;

    @Nonnull
    private final RenderingManager renderer;

    @Inject
    public SetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                 @Nonnull HasApplyChanges applyChanges,
                                                 @Nonnull GetManchesterSyntaxFrameActionHandler handler,
                                                 @Nonnull Provider<ManchesterSyntaxFrameParser> parserProvider,
                                                 @Nonnull RenderingManager renderer) {
        super(accessManager, eventManager, applyChanges);
        this.handler = handler;
        this.parserProvider = parserProvider;
        this.renderer = renderer;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Void> getChangeListGenerator(SetManchesterSyntaxFrameAction action,
                                                               ExecutionContext executionContext) {
        ManchesterSyntaxChangeGenerator changeGenerator = new ManchesterSyntaxChangeGenerator(parserProvider.get());
        try {
            List<OWLOntologyChange> changes = changeGenerator.generateChanges(action.getFromRendering(), action.getToRendering(), action);
            return new FixedChangeListGenerator<>(changes);
        } catch (ParserException e) {
            ManchesterSyntaxFrameParseError error = ManchesterSyntaxFrameParser.getParseError(e);
            throw new SetManchesterSyntaxFrameException(error);
        }
    }

    @Override
    protected ChangeDescriptionGenerator<Void> getChangeDescription(SetManchesterSyntaxFrameAction action,
                                                                    ExecutionContext executionContext) {
        String changeDescription = "Edited description of " + renderer.getShortForm(action.getSubject()) + ".";
        Optional<String> commitMessage = action.getCommitMessage();
        if(commitMessage.isPresent()) {
            changeDescription += "\n" + commitMessage.get();
        }
        return new FixedMessageChangeDescriptionGenerator<Void>(changeDescription);
    }

    @Override
    protected SetManchesterSyntaxFrameResult createActionResult(ChangeApplicationResult<Void> changeApplicationResult,
                                                                SetManchesterSyntaxFrameAction action,
                                                                ExecutionContext executionContext,
                                                                EventList<ProjectEvent<?>> eventList) {
        GetManchesterSyntaxFrameAction ac = new GetManchesterSyntaxFrameAction(action.getProjectId(),
                                                                                    action.getSubject());
        GetManchesterSyntaxFrameResult result = handler.execute(ac, executionContext);
        String reformattedFrame = result.getManchesterSyntax();
        return new SetManchesterSyntaxFrameResult(eventList, reformattedFrame);
    }

    @Override
    public Class<SetManchesterSyntaxFrameAction> getActionClass() {
        return SetManchesterSyntaxFrameAction.class;
    }


}
