package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.server.diff.DiffElementRenderer;
import edu.stanford.bmir.protege.web.server.diff.Revision2DiffElementsTranslator;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomIRISubjectProvider;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
public class ProjectChangesManager {

    public static final int DEFAULT_CHANGE_LIMIT = 50;

    private final RevisionManager revisionManager;

    private final EntitiesByRevisionCache entitiesByRevisionCache;

    private final RenderingManager browserTextProvider;

    private final Comparator<OWLOntologyChangeRecord> changeRecordComparator;

    private final WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Inject
    public ProjectChangesManager(@Nonnull RevisionManager revisionManager,
                                 @Nonnull EntitiesByRevisionCache entitiesByRevisionCache,
                                 @Nonnull RenderingManager browserTextProvider,
                                 @Nonnull Comparator<OWLOntologyChangeRecord> changeRecordComparator,
                                 @Nonnull WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider) {
        this.revisionManager = revisionManager;
        this.entitiesByRevisionCache = entitiesByRevisionCache;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.browserTextProvider = browserTextProvider;
        this.changeRecordComparator = changeRecordComparator;
    }

    public ImmutableList<ProjectChange> getProjectChanges(Optional<OWLEntity> subject) {
        ImmutableList.Builder<ProjectChange> changes = ImmutableList.builder();
        for (Revision revision : revisionManager.getRevisions()) {
            getProjectChangesForRevision(revision, subject, changes);
        }
        return changes.build();
    }

    public ImmutableList<ProjectChange> getProjectChangesForSubjectInRevision(OWLEntity subject, Revision revision) {
        ImmutableList.Builder<ProjectChange> resultBuilder = ImmutableList.builder();
        getProjectChangesForRevision(revision, Optional.of(subject), resultBuilder);
        return resultBuilder.build();
    }

    private void getProjectChangesForRevision(Revision revision,
                                              Optional<OWLEntity> subject,
                                              ImmutableList.Builder<ProjectChange> changesBuilder) {
        if (subject.isPresent() && !entitiesByRevisionCache.containsEntity(revision, subject.get())) {
            return;
        }

        Map<Optional<IRI>, List<OWLOntologyChangeRecord>> recordsBySubject = getChangeRecordsBySubject(revision);

        List<OWLOntologyChangeRecord> limitedRecords = new ArrayList<>();
        final int totalChanges;
        if (subject.isPresent()) {
            List<OWLOntologyChangeRecord> records = recordsBySubject.get(subject.map(OWLEntity::getIRI));
            totalChanges = records.size();
            limitedRecords.addAll(records);
        }
        else {
            totalChanges = revision.getSize();
            for (Map.Entry<Optional<IRI>, List<OWLOntologyChangeRecord>> entry : recordsBySubject.entrySet()) {
                limitedRecords.addAll(entry.getValue());
                if (limitedRecords.size() >= DEFAULT_CHANGE_LIMIT) {
                    break;
                }
            }
        }

        Revision2DiffElementsTranslator translator = new Revision2DiffElementsTranslator(ontologyIRIShortFormProvider);

        List<DiffElement<String, OWLOntologyChangeRecord>> axiomDiffElements = translator.getDiffElementsFromRevision(
                limitedRecords);
        sortDiff(axiomDiffElements);
        List<DiffElement<String, SafeHtml>> renderedDiffElements = renderDiffElements(axiomDiffElements);
        int pageElements = renderedDiffElements.size();
        int pageCount = totalChanges / pageElements + (totalChanges % pageElements);
        Page<DiffElement<String, SafeHtml>> page = new Page<>(
                1,
                pageCount,
                renderedDiffElements,
                totalChanges
        );
        ProjectChange projectChange = new ProjectChange(
                revision.getRevisionNumber(),
                revision.getUserId(),
                revision.getTimestamp(),
                revision.getHighLevelDescription(),
                totalChanges,
                page);
        changesBuilder.add(projectChange);
    }

    private static Map<Optional<IRI>, List<OWLOntologyChangeRecord>> getChangeRecordsBySubject(Revision revision) {
        return revision.getChanges().stream()
                .collect(Collectors.groupingBy(ProjectChangesManager::getAxiom));
    }

    private static Optional<IRI> getAxiom(OWLOntologyChangeRecord rec) {
        OWLOntologyChangeData data = rec.getData();
        if (data instanceof AxiomChangeData) {
            OWLAxiom axiom = ((AxiomChangeData) data).getAxiom();
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

    private List<DiffElement<String, SafeHtml>> renderDiffElements(List<DiffElement<String, OWLOntologyChangeRecord>> axiomDiffElements) {

        List<DiffElement<String, SafeHtml>> diffElements = new ArrayList<>();
        DiffElementRenderer<String> renderer = new DiffElementRenderer<>(browserTextProvider);
        for (DiffElement<String, OWLOntologyChangeRecord> axiomDiffElement : axiomDiffElements) {
            diffElements.add(renderer.render(axiomDiffElement));
        }
        return diffElements;
    }


    private void sortDiff(List<DiffElement<String, OWLOntologyChangeRecord>> diffElements) {
        Comparator<DiffElement<String, OWLOntologyChangeRecord>> c =
                Comparator
                        .comparing((Function<DiffElement<String, OWLOntologyChangeRecord>, OWLOntologyChangeRecord>)
                                           DiffElement::getLineElement, changeRecordComparator)
                        .thenComparing(DiffElement::getDiffOperation)
                        .thenComparing(DiffElement::getSourceDocument);
        diffElements.sort(c);
    }
}
