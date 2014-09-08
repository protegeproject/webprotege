package edu.stanford.bmir.protege.web.server.reasoning;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLEntityChecker;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.reasoning.*;
import edu.stanford.bmir.protege.web.shared.reasoning.Consistency;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.KbQueryResult;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.action.*;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.expression.OWLExpressionParser;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class ExecuteDLQueryActionHandler extends AbstractHasProjectActionHandler<ExecuteDLQueryAction, ExecuteDLQueryResult> {

    private ReasoningService reasoningService;

    @Inject
    public ExecuteDLQueryActionHandler(ReasoningService reasoningService) {
        this.reasoningService = reasoningService;
    }

    @Override
    protected RequestValidator<ExecuteDLQueryAction> getAdditionalRequestValidator(ExecuteDLQueryAction
                                                                                                      action,
                                                                                          RequestContext
                                                                                                  requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected ExecuteDLQueryResult execute(ExecuteDLQueryAction action,
                                                  OWLAPIProject project,
                                                  ExecutionContext executionContext) {

        try {
            OWLExpressionParser<OWLClassExpression> classExpressionParser = new ManchesterOWLSyntaxClassExpressionParser(
                    project.getDataFactory(),
                    new WebProtegeOWLEntityChecker(project.getRenderingManager().getShortFormProvider(), new HasFreshEntities() {

                        @Override
                        public Set<OWLEntityData> getFreshEntities() {
                            return Collections.emptySet();
                        }
                    })
            );
            project.synchronizeReasoner();

            OWLClassExpression ce = classExpressionParser.parse(action.getEnteredClassExpression());
            KbId kbId = new KbId(project.getProjectId().getId());

            List<DLQueryResultsSectionHandler<?,?,?,?>> handlers = Lists.newArrayList();
            handlers.add(new EquivalentClassesSectionHandler());
            handlers.add(new DirectSuperClassesSectionHandler());
            handlers.add(new DirectSubClassesSectionHandler());
            handlers.add(new DirectInstancesSectionHandler());

            List<ListenableFuture<DLQueryEntitySetResult>> resultFutures = Lists.newArrayList();
            for(DLQueryResultsSectionHandler<?,?,?,?> handler : handlers) {
                ListenableFuture<DLQueryEntitySetResult> future = handler.executeQuery(kbId, ce, reasoningService, project);
                resultFutures.add(future);
            }
            ListenableFuture<List<DLQueryEntitySetResult>> futures = Futures.allAsList(resultFutures);
            List<DLQueryEntitySetResult> results = futures.get();

            ImmutableList.Builder<DLQueryEntitySetResult> resultList = ImmutableList.builder();
            for(DLQueryEntitySetResult result : results) {
                   resultList.add(result);
            }
            Optional<RevisionNumber> revisionNumber = results.get(0).getRevisionNumber();
            Optional<Consistency> consistency = results.get(0).getConsistency();
            return new ExecuteDLQueryResult(Optional.of(new DLQueryResult(revisionNumber, consistency, resultList.build())));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public Class<ExecuteDLQueryAction> getActionClass() {
        return null;
    }
}
