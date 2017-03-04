package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractHasProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    @Inject
    public GetIndividualsActionHandler(ProjectManager projectManager,
                                       AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetIndividualsResult execute(GetIndividualsAction action,
                                           OWLAPIProject project,
                                           ExecutionContext executionContext) {
        Stream<OWLNamedIndividual> stream;
        if (action.getType().isOWLThing()) {
            stream = project.getRootOntology().getIndividualsInSignature(Imports.INCLUDED).stream();
        }
        else {
            stream = project.getRootOntology().getImportsClosure().stream()
                            .flatMap(o -> o.getClassAssertionAxioms(action.getType()).stream())
                            .map(ax -> ax.getIndividual())
                            .filter(i -> i.isNamed())
                            .map(i -> i.asOWLNamedIndividual());
        }
        Counter counter = new Counter();
        List<OWLNamedIndividualData> individualsData = stream.peek(i -> counter.increment())
                                                             .map(i -> project.getRenderingManager().getRendering(i))
                                                             .filter(i -> {
                                                                 String searchString = action.getFilterString();
                                                                 return searchString.isEmpty()
                                                                         || i.getBrowserText().contains(searchString);
                                                             })
                                                             .distinct()
                                                             .sorted()
                                                             .collect(toList());
        PageRequest pageRequest = action.getPageRequest();
        Pager<OWLNamedIndividualData> pager = Pager.getPagerForPageSize(individualsData, pageRequest.getPageSize());
        Page<OWLNamedIndividualData> page = pager.getPage(pageRequest.getPageNumber());
        return new GetIndividualsResult(page, counter.getCount(), individualsData.size());
    }

    @Override
    public Class<GetIndividualsAction> getActionClass() {
        return GetIndividualsAction.class;
    }

    private static class Counter {

        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
