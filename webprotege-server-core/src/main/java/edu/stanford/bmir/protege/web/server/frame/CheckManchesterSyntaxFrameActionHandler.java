package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult.CHANGED;
import static edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult.UNCHANGED;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameActionHandler extends AbstractProjectActionHandler<CheckManchesterSyntaxFrameAction, CheckManchesterSyntaxFrameResult> {

    @Nonnull
    private final ManchesterSyntaxChangeGeneratorFactory factory;

    @Nonnull
    private final RenderingManager renderer;

    @Inject
    public CheckManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull ManchesterSyntaxChangeGeneratorFactory factory,
                                                   @Nonnull RenderingManager renderer) {
        super(accessManager);
        this.factory = factory;
        this.renderer = renderer;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CheckManchesterSyntaxFrameAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public CheckManchesterSyntaxFrameResult execute(@Nonnull CheckManchesterSyntaxFrameAction action,
                                                    @Nonnull ExecutionContext executionContext) {

        ManchesterSyntaxChangeGenerator changeGenerator = factory.create(
                renderer.getRendering(action.getSubject()),
                action.getFrom(),
                action.getTo(),
                "",
                action);
        try {
            OntologyChangeList<?> changeList = changeGenerator.generateChanges(new ChangeGenerationContext(executionContext.getUserId()));
            if (changeList.getChanges().isEmpty()) {
                return new CheckManchesterSyntaxFrameResult(UNCHANGED);
            }
            else {
                return new CheckManchesterSyntaxFrameResult(CHANGED);
            }
        } catch (ParserException e) {
            return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParser.getParseError(e));
        }
    }

    @Nonnull
    @Override
    public Class<CheckManchesterSyntaxFrameAction> getActionClass() {
        return CheckManchesterSyntaxFrameAction.class;
    }
}

