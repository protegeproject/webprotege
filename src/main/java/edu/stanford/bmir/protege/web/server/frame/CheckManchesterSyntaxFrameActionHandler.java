package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameActionHandler extends AbstractHasProjectActionHandler<CheckManchesterSyntaxFrameAction, CheckManchesterSyntaxFrameResult> {

    @Inject
    public CheckManchesterSyntaxFrameActionHandler(@Nonnull OWLAPIProjectManager projectManager,
                                                   @Nonnull AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    protected CheckManchesterSyntaxFrameResult execute(CheckManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {

            ManchesterSyntaxChangeGenerator changeGenerator = new ManchesterSyntaxChangeGenerator(project.getManchesterSyntaxFrameParser());
            try {
                List<OWLOntologyChange> changeList = changeGenerator.generateChanges(action.getFrom(), action.getTo(), action);
                if(changeList.isEmpty()) {
                    return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseResult.UNCHANGED);
                }
                else {
                    return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseResult.CHANGED);
                }
            } catch (ParserException e) {
                return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParser.getParseError(e));
            }
    }

    @Override
    public Class<CheckManchesterSyntaxFrameAction> getActionClass() {
        return CheckManchesterSyntaxFrameAction.class;
    }
}

