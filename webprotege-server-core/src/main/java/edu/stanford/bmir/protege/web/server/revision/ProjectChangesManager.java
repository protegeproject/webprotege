package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.base.Stopwatch;
import com.google.common.collect.*;
import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.server.axiom.AxiomIRISubjectProvider;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.diff.DiffElementRenderer;
import edu.stanford.bmir.protege.web.server.diff.Revision2DiffElementsTranslator;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
@ProjectSingleton
public class ProjectChangesManager {

    private static final Logger logger = LoggerFactory.getLogger(ProjectChangesManager.class);

    public static final int DEFAULT_CHANGE_LIMIT = 50;

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final RenderingManager browserTextProvider;

    private final Comparator<OntologyChange> changeRecordComparator;

    private final Provider<Revision2DiffElementsTranslator> revision2DiffElementsTranslatorProvider;

    private final Table<RevisionNumber, Optional<IRI>, ImmutableList<OntologyChange>> cache = HashBasedTable.create();

    @Inject
    public ProjectChangesManager(ProjectId projectId,
                                 @Nonnull RevisionManager revisionManager,
                                 @Nonnull RenderingManager browserTextProvider,
                                 @Nonnull Comparator<OntologyChange> changeRecordComparator,
                                 @Nonnull Provider<Revision2DiffElementsTranslator> revision2DiffElementsTranslatorProvider) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.browserTextProvider = browserTextProvider;
        this.changeRecordComparator = changeRecordComparator;
        this.revision2DiffElementsTranslatorProvider = revision2DiffElementsTranslatorProvider;
    }

    private static Multimap<Optional<IRI>, OntologyChange> getChangesBySubject(Revision revision) {
        Multimap<Optional<IRI>, OntologyChange> results = HashMultimap.create();
        revision.getChanges().forEach(record -> results.put(getSubject(record), record));
        return results;
    }

    private static Optional<IRI> getSubject(OntologyChange change) {
        if (change.isAxiomChange()) {
            var axiom = change.getAxiomOrThrow();
            return getSubject(axiom);
        }
        else {
            return Optional.empty();
        }
    }

    private static Optional<IRI> getSubject(OWLAxiom axiom) {
        AxiomIRISubjectProvider subjectProvider = new AxiomIRISubjectProvider(IRI::compareTo);
        return subjectProvider.getSubject(axiom);
    }

    public Page<ProjectChange> getProjectChanges(Optional<OWLEntity> subject,
                                                 PageRequest pageRequest) {
        ImmutableList<Revision> revisions = revisionManager.getRevisions();
        if (subject.isPresent()) {
            // We need to scan revisions to find the ones containing a particular subject
            // We ignore the page request here.
            // This needs reworking really, but the number of changes per entity is usually small
            // so this works for now.
            ImmutableList.Builder<ProjectChange> changes = ImmutableList.builder();
            for (Revision revision : revisions) {
                getProjectChangesForRevision(revision, subject, changes);
            }
            ImmutableList<ProjectChange> theChanges = changes.build();
            return new Page<>(1, 1, theChanges, theChanges.size());
        }
        else {
            // Pages are in reverse order
            ImmutableList.Builder<ProjectChange> changes = ImmutableList.builder();
            revisions.reverse().stream()
                    .skip(pageRequest.getSkip())
                    .limit(pageRequest.getPageSize())
                    .forEach(revision -> getProjectChangesForRevision(revision, subject, changes));
            ImmutableList<ProjectChange> changeList = changes.build();
            int pageCount = (revisions.size() / pageRequest.getPageSize()) + 1;
            return new Page<>(pageRequest.getPageNumber(),
                              pageCount,
                              changeList, changeList.size());
        }
    }

    public ImmutableList<ProjectChange> getProjectChangesForSubjectInRevision(OWLEntity subject, Revision revision) {
        ImmutableList.Builder<ProjectChange> resultBuilder = ImmutableList.builder();
        getProjectChangesForRevision(revision, Optional.of(subject), resultBuilder);
        return resultBuilder.build();
    }

    private void getProjectChangesForRevision(Revision revision,
                                              Optional<OWLEntity> subject,
                                              ImmutableList.Builder<ProjectChange> changesBuilder) {
        if(!cache.containsRow(revision.getRevisionNumber())) {
            logger.debug("{} Building cache for revision {}", projectId, revision.getRevisionNumber().getValue());
            var stopwatch = Stopwatch.createStarted();
            var changeRecordsBySubject = getChangesBySubject(revision);
            changeRecordsBySubject.asMap().forEach((subj, records) -> {
                cache.put(revision.getRevisionNumber(), subj, ImmutableList.copyOf(records));
            });
            logger.debug("{} Cached revision {} in {} ms", projectId, revision.getRevisionNumber().getValue(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
        List<OntologyChange> limitedRecords = new ArrayList<>();
        final int totalChanges;
        if (subject.isPresent()) {
            List<OntologyChange> records = cache.get(revision.getRevisionNumber(), subject.map(OWLEntity::getIRI));
            if (records == null) {
                // Nothing in this revision that changes the subject
                return;
            }
            totalChanges = records.size();
            limitedRecords.addAll(records);
        }
        else {
            totalChanges = revision.getSize();
            revision.getChanges().stream()
                    .limit(DEFAULT_CHANGE_LIMIT)
                    .forEach(limitedRecords::add);
        }


        Revision2DiffElementsTranslator translator = revision2DiffElementsTranslatorProvider.get();
        List<DiffElement<String, OntologyChange>> axiomDiffElements = translator.getDiffElementsFromRevision(limitedRecords);
        sortDiff(axiomDiffElements);
        List<DiffElement<String, SafeHtml>> renderedDiffElements = renderDiffElements(axiomDiffElements);
        int pageElements = renderedDiffElements.size();
        int pageCount;
        if (pageElements == 0) {
            pageCount = 1;
        }
        else {
            pageCount = totalChanges / pageElements + (totalChanges % pageElements);
        }
        Page<DiffElement<String, SafeHtml>> page = new Page<>(
                1,
                pageCount,
                renderedDiffElements,
                totalChanges
        );
        ProjectChange projectChange = ProjectChange.get(
                revision.getRevisionNumber(),
                revision.getUserId(),
                revision.getTimestamp(),
                revision.getHighLevelDescription(),
                totalChanges,
                page);
        changesBuilder.add(projectChange);
    }

    private List<DiffElement<String, SafeHtml>> renderDiffElements(List<DiffElement<String, OntologyChange>> axiomDiffElements) {

        List<DiffElement<String, SafeHtml>> diffElements = new ArrayList<>();
        DiffElementRenderer<String> renderer = new DiffElementRenderer<>(browserTextProvider);
        for (DiffElement<String, OntologyChange> axiomDiffElement : axiomDiffElements) {
            diffElements.add(renderer.render(axiomDiffElement));
        }
        return diffElements;
    }


    private void sortDiff(List<DiffElement<String, OntologyChange>> diffElements) {
        Comparator<DiffElement<String, OntologyChange>> c =
                Comparator
                        .comparing((Function<DiffElement<String, OntologyChange>, OntologyChange>)
                                           DiffElement::getLineElement, changeRecordComparator)
                        .thenComparing(DiffElement::getDiffOperation)
                        .thenComparing(DiffElement::getSourceDocument);
        diffElements.sort(c);
    }
}
