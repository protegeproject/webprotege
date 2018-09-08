package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.Scanner;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.server.shortform.ShortFormMatch;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.apache.commons.lang.StringUtils;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.server.pagination.PageCollector.toPage;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetIndividualsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;


    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public GetIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull @RootOntology OWLOntology rootOntology,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull DictionaryManager dictionaryManager,
                                       @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.entityNodeRenderer = entityNodeRenderer;
        this.dictionaryManager = dictionaryManager;
        this.languageManager = languageManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetIndividualsResult execute(@Nonnull GetIndividualsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(action.getFilterString());
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
        PageRequest pageRequest = action.getPageRequest();

        Optional<Page<OWLNamedIndividual>> page = stream.peek(i -> counter.increment())
                                                        .filter(i -> {
                                                            if(searchStrings.isEmpty()) {
                                                                return true;
                                                            }
                                                            String shortForm = dictionaryManager.getShortForm(i);
                                                            Scanner scanner = new Scanner(shortForm, shortForm);
                                                            for(SearchString searchString : searchStrings) {
                                                                int index = scanner.indexOf(searchString, 0);
                                                                if(index == -1) {
                                                                    return false;
                                                                }
                                                            }
                                                            return true;
                                                        })
                                                        .distinct()
                                                        .map(i -> new IndividualRendering(i, renderingManager.getShortForm(i).toLowerCase()))
                                                        .sorted()
                                                        .map(IndividualRendering::getIndividual)
                                                        .collect(toPage(pageRequest.getPageNumber(),
                                                                        pageRequest.getPageSize()));
        OWLClassData type = renderingManager.getRendering(action.getType());
        logger.info(BROWSING,
                    "{} {} retrieved instances of {} ({})",
                    projectId,
                    executionContext.getUserId(),
                    action.getType(),
                    renderingManager.getRendering(action.getType()).getBrowserText());
        Page<EntityNode> entityNodes = page.map(pg -> pg.transform(entityNodeRenderer::render)).orElse(Page.emptyPage());
        return new GetIndividualsResult(type, entityNodes, counter.getCount(), (int) entityNodes.getTotalElements());
    }

    @Nonnull
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

    private static class IndividualRendering implements Comparable<IndividualRendering> {
        private final OWLNamedIndividual individual;
        private final String rendering;

        public IndividualRendering(OWLNamedIndividual individual, String rendering) {
            this.individual = individual;
            this.rendering = rendering;
        }

        public OWLNamedIndividual getIndividual() {
            return individual;
        }

        public String getRendering() {
            return rendering;
        }

        @Override
        public int compareTo(IndividualRendering o) {
            return this.rendering.compareTo(o.rendering);
        }
    }
}
