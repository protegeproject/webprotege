package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryEntitySetResult;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.protege.reasoning.*;
import edu.stanford.protege.reasoning.action.KbQueryResultResponse;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public abstract class DLQueryResultsSectionHandler<A extends Action<R, ?>,
        R extends KbQueryResultResponse<T>, E extends OWLEntity, T> {

    private DLQueryResultSection section;

    protected DLQueryResultsSectionHandler(DLQueryResultSection section) {
        this.section = section;
    }

    public DLQueryResultSection getSection() {
        return section;
    }

    public ListenableFuture<DLQueryEntitySetResult> executeQuery(
            KbId kbId,
            OWLClassExpression classExpression,
            ReasoningService reasoningService,
            final OWLAPIProject project) {
        Action action = createAction(kbId, classExpression);
        ListenableFuture<R> response = reasoningService.execute(action);

        return Futures.transform(response, new Function<R, DLQueryEntitySetResult>() {
            @Override
            public DLQueryEntitySetResult apply(R input) {
                if (!input.getResult().isPresent()) {
                    RevisionNumber revisionNumber;
                    return new DLQueryEntitySetResult(section);
                }
                KbQueryResult<T> result = input.getResult().get();
                Optional<RevisionNumber> rev = project.getChangeManager().getRevisionNumberOfDigest(
                        input.getKbDigest());
                if (!result.isConsistent()) {
                    return new DLQueryEntitySetResult(rev, Optional.of(edu.stanford.bmir.protege.web.shared.reasoning
                                                                               .Consistency.INCONSISTENT), section,
                                                      Optional.<ImmutableCollection<OWLEntityData>>absent());
                }
                ImmutableList<E> resultList = transform(result.getValue());
                EntityListToEntitySetTransformer transformer = new EntityListToEntitySetTransformer(project);
                return transformer.getEntitySetResult(rev.or(RevisionNumber.getRevisionNumber(0)), section, resultList);
            }
        });
    }

    protected abstract A createAction(KbId kbId, OWLClassExpression ce);

    protected abstract ImmutableList<E> transform(T result);
}
