package edu.stanford.bmir.protege.web.server.usage;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.usage.*;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageActionHandler extends AbstractHasProjectActionHandler<GetUsageAction, GetUsageResult> {

    @Override
    public Class<GetUsageAction> getActionClass() {
        return GetUsageAction.class;
    }

    @Override
    protected RequestValidator<GetUsageAction> getAdditionalRequestValidator(GetUsageAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected GetUsageResult execute(GetUsageAction action, OWLAPIProject project, ExecutionContext executionContext) {
        List<UsageReference> usage = new ArrayList<UsageReference>();
        final OWLEntity subject = action.getSubject();
        ReferencingAxiomVisitor visitor = new ReferencingAxiomVisitor(project, subject);
        final UsageFilter usageFilter = action.getUsageFilter();
        int totalReferenceCount = 0;
        int counter = 0;

        final IRI subjectIRI = subject.getIRI();
        for (OWLOntology ont : project.getRootOntology().getImportsClosure()) {
            Set<OWLAxiom> references = ont.getReferencingAxioms(subject);
            for (OWLAxiom reference : references) {
                counter = processAxiom(reference, usageFilter, action, usage, visitor, counter);
                totalReferenceCount++;
            }


            final Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ont.getAxioms(AxiomType.ANNOTATION_ASSERTION);
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                if (ax.getSubject().equals(subjectIRI)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
                if (ax.getProperty().equals(subject)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
                if (ax.getValue().equals(subjectIRI)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
            }
            final Set<OWLAnnotationPropertyRangeAxiom> annotationPropertyRangeAxioms = ont.getAxioms(AxiomType.ANNOTATION_PROPERTY_RANGE);
            for (OWLAnnotationPropertyRangeAxiom ax : annotationPropertyRangeAxioms) {
                if (ax.getProperty().equals(subject)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
                if (ax.getRange().equals(subjectIRI)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
            }
            final Set<OWLAnnotationPropertyDomainAxiom> annotationPropertyDomainAxioms = ont.getAxioms(AxiomType.ANNOTATION_PROPERTY_DOMAIN);
            for (OWLAnnotationPropertyDomainAxiom ax : annotationPropertyDomainAxioms) {
                if (ax.getProperty().equals(subject)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
                if (ax.getDomain().equals(subjectIRI)) {
                    counter = processAxiom(ax, usageFilter, action, usage, visitor, counter);
                    totalReferenceCount++;
                }
            }
        }




        Collections.sort(usage, new UsageReferenceComparator(subject));
        return new GetUsageResult(project.getProjectId(), usage, totalReferenceCount);
    }

    private int processAxiom(OWLAxiom reference, UsageFilter usageFilter, GetUsageAction action, List<UsageReference> result, ReferencingAxiomVisitor visitor, int counter) {
        if (usageFilter.isIncluded(reference.getAxiomType())) {
            counter++;
            if (counter <= action.getPageSize()) {
                final Set<UsageReference> refs = reference.accept(visitor);
                for (UsageReference ref : refs) {
                    if (isIncludedBySubject(usageFilter, action, ref)) {
                        if (counter <= action.getPageSize()) {
                            result.addAll(refs);
                        }
                    }
                }
            }

        }
        return counter;
    }

    private boolean isIncludedBySubject(UsageFilter usageFilter, GetUsageAction action, UsageReference ref) {
        if(!ref.getAxiomSubject().isPresent()) {
            return true;
        }
        final OWLEntity axiomSubject = ref.getAxiomSubject().get();
        if(!usageFilter.isIncluded(axiomSubject.getEntityType())) {
            return false;
        }
        if(!usageFilter.isShowDefiningAxioms()) {
            return !action.getSubject().equals(axiomSubject);
        }
        return true;
    }


}
