package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractHasProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetIndividualsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;


    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull @RootOntology OWLOntology rootOntology,
                                       @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetIndividualsResult execute(GetIndividualsAction action,
                                           ExecutionContext executionContext) {
        Stream<OWLNamedIndividual> stream;
        if (action.getType().isOWLThing()) {
            stream = rootOntology.getIndividualsInSignature(Imports.INCLUDED).stream();
        }
        else {
            stream = rootOntology.getImportsClosure().stream()
                            .flatMap(o -> o.getClassAssertionAxioms(action.getType()).stream())
                            .map(OWLClassAssertionAxiom::getIndividual)
                            .filter(OWLIndividual::isNamed)
                            .map(OWLIndividual::asOWLNamedIndividual);
        }
        Counter counter = new Counter();
        List<OWLNamedIndividualData> individualsData = stream.peek(i -> counter.increment())
                                                             .map(renderingManager::getRendering)
                                                             .filter(i -> {
                                                                 String searchString = action.getFilterString();
                                                                 return searchString.isEmpty()
                                                                         || StringUtils.containsIgnoreCase(
                                                                                i.getBrowserText(),
                                                                                searchString);
                                                             })
                                                             .distinct()
                                                             .sorted()
                                                             .collect(toList());
        PageRequest pageRequest = action.getPageRequest();
        Pager<OWLNamedIndividualData> pager = Pager.getPagerForPageSize(individualsData, pageRequest.getPageSize());
        Page<OWLNamedIndividualData> page = pager.getPage(pageRequest.getPageNumber());
        OWLClassData type = renderingManager.getRendering(action.getType());
        logger.info(BROWSING,
                    "{} {} retrieved instances of {} ({})",
                    projectId,
                    executionContext.getUserId(),
                    action.getType(),
                    renderingManager.getRendering(action.getType()).getBrowserText());
        return new GetIndividualsResult(type, page, counter.getCount(), individualsData.size());
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
