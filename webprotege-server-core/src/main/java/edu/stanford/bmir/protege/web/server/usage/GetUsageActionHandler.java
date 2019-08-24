package edu.stanford.bmir.protege.web.server.usage;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.index.AxiomsByReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.usage.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageActionHandler extends AbstractProjectActionHandler<GetUsageAction, GetUsageResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final ReferencingAxiomVisitorFactory referencingAxiomVisitorFactory;

    @Nonnull

    @Inject
    public GetUsageActionHandler(@Nonnull AccessManager accessManager,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                 @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex,
                                 @Nonnull EntityNodeRenderer entityNodeRenderer,
                                 @Nonnull ReferencingAxiomVisitorFactory referencingAxiomVisitorFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.axiomsByReferenceIndex = axiomsByReferenceIndex;
        this.entityNodeRenderer = entityNodeRenderer;
        this.referencingAxiomVisitorFactory = referencingAxiomVisitorFactory;
    }

    @Nonnull
    @Override
    public Class<GetUsageAction> getActionClass() {
        return GetUsageAction.class;
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(GetUsageAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetUsageResult execute(@Nonnull GetUsageAction action, @Nonnull ExecutionContext executionContext){
        var subject = action.getSubject();
        var referencingAxiomVisitor = referencingAxiomVisitorFactory.create(subject);
        var usageFilter = action.getUsageFilter();
        var referencingAxioms = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> axiomsByReferenceIndex.getReferencingAxioms(Collections.singleton(subject), ontId))
                         .collect(toImmutableList());

        var usageReferences = referencingAxioms
                .stream()
                .filter(ax -> usageFilter.isIncluded(ax.getAxiomType()))
                .flatMap(ax -> ax.accept(referencingAxiomVisitor).stream())
                .filter(usageReference -> isIncludedBySubject(usageFilter, subject, usageReference))
                .limit(action.getPageSize())
                .sorted(new UsageReferenceComparator(subject))
                .collect(toImmutableList());

        var entityNode = entityNodeRenderer.render(subject);
        return new GetUsageResult(projectId, entityNode, usageReferences, referencingAxioms.size());
    }

    private boolean isIncludedBySubject(UsageFilter usageFilter, OWLEntity subject, UsageReference ref) {
        if(ref.getAxiomSubject().isEmpty()) {
            return true;
        }
        var axiomSubject = ref.getAxiomSubject().get();
        if(!usageFilter.isIncluded(axiomSubject.getEntityType())) {
            return false;
        }
        if(!usageFilter.isShowDefiningAxioms()) {
            return !subject.equals(axiomSubject);
        }
        return true;
    }
}
