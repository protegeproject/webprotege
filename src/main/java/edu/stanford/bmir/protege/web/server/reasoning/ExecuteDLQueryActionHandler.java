package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
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
import edu.stanford.bmir.protege.web.shared.reasoning.ExecuteDLQueryAction;
import edu.stanford.bmir.protege.web.shared.reasoning.ExecuteDLQueryResult;
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
            ListenableFuture<GetSubClassesResponse> subClassesResponse = reasoningService.execute(new GetSubClassesAction(kbId, ce));
            ListenableFuture<GetSuperClassesResponse> superClassesResponse = reasoningService.execute(new GetSuperClassesAction(kbId, ce,
                                                                                                                                HierarchyQueryType.DIRECT));

            GetSubClassesResponse response = subClassesResponse.get();
            Optional<KbQueryResult<NodeSet<OWLClass>>> subClasses = response.getResult();
            Optional<KbQueryResult<NodeSet<OWLClass>>> superClasses = superClassesResponse.get().getResult();

            final Pattern pattern;
            if(action.getFilter().isPresent()) {
                pattern = Pattern.compile(Pattern.quote(action.getFilter().get()));
            }
            else {
                pattern = Pattern.compile(".");
            }
            ImmutableList.Builder<OWLClassData> subClassesBuilder = ImmutableList.builder();
            Optional<RevisionNumber> revisionNumberOfDigest = Optional.absent();
            if(subClasses.isPresent()) {
                revisionNumberOfDigest = project.getChangeManager().getRevisionNumberOfDigest(response.getKbDigest());
                Set<OWLClass> subClassesFlattened = subClasses.get().getValue().getFlattened();
                for(OWLClass subClass : subClassesFlattened) {
                    OWLClassData rendering = project.getRenderingManager().getRendering(subClass);
                    if (pattern.matcher(rendering.getBrowserText()).find()) {
                        subClassesBuilder.add(rendering);
                    }
                }
            }
            ImmutableList.Builder<OWLClassData> superClassesBuilder = ImmutableList.builder();
            if(superClasses.isPresent()) {
                for(OWLClass superClass : superClasses.get().getValue().getFlattened()) {
                    OWLClassData rendering = project.getRenderingManager().getRendering(superClass);
                    if (pattern.matcher(rendering.getBrowserText()).find()) {
                        superClassesBuilder.add(rendering);
                    }
                }
            }
            return new ExecuteDLQueryResult(revisionNumberOfDigest,
                                            subClassesBuilder.build(), superClassesBuilder.build());

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
