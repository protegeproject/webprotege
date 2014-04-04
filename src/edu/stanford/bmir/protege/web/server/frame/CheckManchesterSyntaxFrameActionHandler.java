package edu.stanford.bmir.protege.web.server.frame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.inject.ManchesterSyntaxParsingContextModule;
import edu.stanford.bmir.protege.web.server.inject.ProjectModule;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseResult;
import org.coode.owlapi.manchesterowlsyntax.OntologyAxiomPair;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameActionHandler extends AbstractHasProjectActionHandler<CheckManchesterSyntaxFrameAction, CheckManchesterSyntaxFrameResult> {
    @Override
    protected RequestValidator<CheckManchesterSyntaxFrameAction> getAdditionalRequestValidator(CheckManchesterSyntaxFrameAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected CheckManchesterSyntaxFrameResult execute(CheckManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
//        try {
            Injector injector = Guice.createInjector(new ProjectModule(project), new ManchesterSyntaxParsingContextModule(action));
            ManchesterSyntaxChangeGenerator changeGenerator = injector.getInstance(ManchesterSyntaxChangeGenerator.class);
            try {
                List<OWLOntologyChange> changeList = changeGenerator.generateChanges(action.getFrom(), action.getTo());
                if(changeList.isEmpty()) {
                    return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseResult.UNCHANGED);
                }
                else {
                    return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParseResult.CHANGED);
                }
            } catch (ParserException e) {
                return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParser.getParseError(e));
            }
//            ManchesterSyntaxFrameParser fromParser = injector.getInstance(ManchesterSyntaxFrameParser.class);
//            Set<OntologyAxiomPair> fromPairs = fromParser.parse(action.getFrom());
//            try {
//                ManchesterSyntaxFrameParser toParser = injector.getInstance(ManchesterSyntaxFrameParser.class);
//                Set<OntologyAxiomPair> toPairs = toParser.parse(action.getTo());
//                ManchesterSyntaxFrameParseResult result;
//                if(fromPairs.equals(toPairs)) {
//                    result = ManchesterSyntaxFrameParseResult.UNCHANGED;
//                }
//                else {
//                    result = ManchesterSyntaxFrameParseResult.CHANGED;
//                }
//                return new CheckManchesterSyntaxFrameResult(result);
//            } catch (ParserException e) {
//                return new CheckManchesterSyntaxFrameResult(ManchesterSyntaxFrameParser.getParseError(e));
//            }
//        } catch (ParserException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public Class<CheckManchesterSyntaxFrameAction> getActionClass() {
        return CheckManchesterSyntaxFrameAction.class;
    }
}

