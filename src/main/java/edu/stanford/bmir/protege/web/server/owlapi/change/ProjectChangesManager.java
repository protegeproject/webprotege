package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.server.change.ChangeRecordComparator;
import edu.stanford.bmir.protege.web.server.diff.DiffElementRenderer;
import edu.stanford.bmir.protege.web.server.diff.Revision2DiffElementsTranslator;
import edu.stanford.bmir.protege.web.server.diff.SameSubjectFilter;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.Filter;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomIRISubjectProvider;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
public class ProjectChangesManager {

    private final RevisionManager changeManager;

    private final EntitiesByRevisionCache entitiesByRevisionCache;

    private final OWLOntology rootOntology;

    private final BrowserTextProvider browserTextProvider;

    private final HasHtmlBrowserText hmlBrowserTextProvider;

    private final Comparator<OWLAxiom> axiomComparator;

    private final Comparator<? super OWLAnnotation> annotationComparator;

    @Inject
    public ProjectChangesManager(RevisionManager changeManager,
                                 EntitiesByRevisionCache entitiesByRevisionCache,
                                 @RootOntology OWLOntology rootOntology,
                                 BrowserTextProvider browserTextProvider,
                                 HasHtmlBrowserText hmlBrowserTextProvider,
                                 Comparator<OWLAxiom> axiomComparator,
                                 Comparator<? super OWLAnnotation> annotationComparator) {
        this.changeManager = changeManager;
        this.entitiesByRevisionCache = entitiesByRevisionCache;
        this.rootOntology = rootOntology;
        this.browserTextProvider = browserTextProvider;
        this.hmlBrowserTextProvider = hmlBrowserTextProvider;
        this.axiomComparator = axiomComparator;
        this.annotationComparator = annotationComparator;
    }

    public ImmutableList<ProjectChange> getProjectChanges(Optional<OWLEntity> subject) {
        ImmutableList.Builder<ProjectChange> changes = ImmutableList.builder();
        for (Revision revision : changeManager.getRevisions()) {
            getProjectChangesForRevision(revision, subject, changes);
        }
        return changes.build();
    }

    public ImmutableList<ProjectChange> getProjectChangesForSubjectInRevision(OWLEntity subject, Revision revision) {
        ImmutableList.Builder<ProjectChange> resultBuilder = ImmutableList.builder();
        getProjectChangesForRevision(revision, Optional.of(subject), resultBuilder);
        return resultBuilder.build();
    }

    private void getProjectChangesForRevision(Revision revision, Optional<OWLEntity> subject, ImmutableList.Builder<ProjectChange> changesBuilder) {
        if (!subject.isPresent() || entitiesByRevisionCache.containsEntity(revision, subject.get())) {

            final Filter<OWLOntologyChangeRecord> filter;
            if(subject.isPresent()) {
                filter = new SameSubjectFilter(
                        new AxiomIRISubjectProvider(IRI::compareTo), subject.transform(new Function<OWLEntity, IRI>() {
                    @Nullable
                    @Override
                    public IRI apply(OWLEntity entity) {
                        return entity.getIRI();
                    }
                }));
            }
            else {
                filter = object -> true;
            }

            Revision2DiffElementsTranslator translator = new Revision2DiffElementsTranslator(
                    filter, new WebProtegeOntologyIRIShortFormProvider(rootOntology)
            );

            List<DiffElement<String, OWLOntologyChangeRecord>> axiomDiffElements = translator.getDiffElementsFromRevision(revision, Integer.MAX_VALUE);
            ImmutableSet.Builder<OWLEntityData> subjectsBuilder = ImmutableSet.builder();

            if (axiomDiffElements.size() < 200) {
                sortDiff(axiomDiffElements);
                for(OWLEntity entity : entitiesByRevisionCache.getEntities(revision)) {
                    Optional<String> rendering = browserTextProvider.getOWLEntityBrowserText(entity);
                    OWLEntityData entityData = DataFactory.getOWLEntityData(entity, rendering.or(entity.getIRI().toString()));
                    subjectsBuilder.add(entityData);
                }
            }
            List<DiffElement<String, SafeHtml>> diffElements = getDiffElements(axiomDiffElements);
            Pager<DiffElement<String, SafeHtml>> pager = Pager.getPagerForPageSize(diffElements, 150);
            ProjectChange projectChange = new ProjectChange(
                    subjectsBuilder.build(),
                    revision.getRevisionNumber(),
                    revision.getUserId(),
                    revision.getTimestamp(),
                    revision.getHighLevelDescription(),
                    axiomDiffElements.size(),
                    pager.<DiffElement<String, SafeHtml>>getPage(1));
            changesBuilder.add(projectChange);
        }
    }

    private List<DiffElement<String, SafeHtml>> getDiffElements(List<DiffElement<String, OWLOntologyChangeRecord>> axiomDiffElements) {

        List<DiffElement<String, SafeHtml>> diffElements = new ArrayList<>();
        DiffElementRenderer<String> renderer = new DiffElementRenderer<>(hmlBrowserTextProvider);
        for (DiffElement<String, OWLOntologyChangeRecord> axiomDiffElement : axiomDiffElements) {
            diffElements.add(renderer.render(axiomDiffElement));
        }
        return diffElements;
    }


    private void sortDiff(List<DiffElement<String, OWLOntologyChangeRecord>> diffElements) {
        final Comparator<OWLOntologyChangeRecord> changeRecordComparator = new ChangeRecordComparator(axiomComparator, annotationComparator);
        Collections.sort(diffElements, new Comparator<DiffElement<String, OWLOntologyChangeRecord>>() {
            @Override
            public int compare(DiffElement<String, OWLOntologyChangeRecord> o1, DiffElement<String, OWLOntologyChangeRecord> o2) {
                int diff = changeRecordComparator.compare(o1.getLineElement(), o2.getLineElement());
                if (diff != 0) {
                    return diff;
                }
                int opDiff = o1.getDiffOperation().compareTo(o2.getDiffOperation());
                if (opDiff != 0) {
                    return opDiff;
                }
                return o1.getSourceDocument().compareTo(o2.getSourceDocument());
            }
        });
    }
}
