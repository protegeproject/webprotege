package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult.CHANGED;
import static edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult.UNCHANGED;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameActionHandler extends AbstractHasProjectActionHandler<CheckManchesterSyntaxFrameAction, CheckManchesterSyntaxFrameResult> {

    @Nonnull
    private final Provider<ManchesterSyntaxFrameParser> parserProvider;

    @Inject
    public CheckManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull Provider<ManchesterSyntaxFrameParser> parserProvider) {
        super(accessManager);
        this.parserProvider = parserProvider;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public CheckManchesterSyntaxFrameResult execute(CheckManchesterSyntaxFrameAction action,
                                                       ExecutionContext executionContext) {

        ManchesterSyntaxFrameParser parser = parserProvider.get();
        ManchesterSyntaxChangeGenerator changeGenerator = new ManchesterSyntaxChangeGenerator(
                parser);
            try {
                List<OWLOntologyChange> changeList = changeGenerator.generateChanges(action.getFrom(), action.getTo(), action);
                if(changeList.isEmpty()) {
                    return new CheckManchesterSyntaxFrameResult(UNCHANGED);
                }
                else {
                    return new CheckManchesterSyntaxFrameResult(CHANGED);
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

