package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.mansyntax.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameActionHandler extends AbstractProjectChangeHandler<Void, SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    @Inject
    public SetManchesterSyntaxFrameActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<SetManchesterSyntaxFrameAction> getAdditionalRequestValidator(SetManchesterSyntaxFrameAction action, RequestContext requestContext) {
        return UserHasProjectWritePermissionValidator.get();
    }

    @Override
    protected ChangeListGenerator<Void> getChangeListGenerator(SetManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ManchesterSyntaxChangeGenerator changeGenerator = new ManchesterSyntaxChangeGenerator(project.getManchesterSyntaxFrameParser());
        try {
            List<OWLOntologyChange> changes = changeGenerator.generateChanges(action.getFromRendering(), action.getToRendering(), action);
            return new FixedChangeListGenerator<>(changes);
        } catch (ParserException e) {
            ManchesterSyntaxFrameParseError error = ManchesterSyntaxFrameParser.getParseError(e);
            throw new SetManchesterSyntaxFrameException(error);
        }
    }

    @Override
    protected ChangeDescriptionGenerator<Void> getChangeDescription(SetManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        String changeDescription = "Edited description of " + project.getRenderingManager().getShortForm(action.getSubject()) + ".";
        Optional<String> commitMessage = action.getCommitMessage();
        if(commitMessage.isPresent()) {
            changeDescription += "\n" + commitMessage.get();
        }
        return new FixedMessageChangeDescriptionGenerator<Void>(changeDescription);
    }

    @Override
    protected SetManchesterSyntaxFrameResult createActionResult(ChangeApplicationResult<Void> changeApplicationResult, SetManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        GetManchesterSyntaxFrameActionHandler handler = WebProtegeInjector.get().getInstance(GetManchesterSyntaxFrameActionHandler.class);
        GetManchesterSyntaxFrameResult result = handler.execute(new GetManchesterSyntaxFrameAction(action.getProjectId(),
                                                                                          action.getSubject()),
                                                       project, executionContext);
        String reformattedFrame = result.getManchesterSyntax();
        return new SetManchesterSyntaxFrameResult(eventList, reformattedFrame);
    }

    @Override
    public Class<SetManchesterSyntaxFrameAction> getActionClass() {
        return SetManchesterSyntaxFrameAction.class;
    }


}
