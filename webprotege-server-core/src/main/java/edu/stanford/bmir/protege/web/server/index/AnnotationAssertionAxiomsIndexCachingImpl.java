package edu.stanford.bmir.protege.web.server.index;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
@ProjectSingleton
public class AnnotationAssertionAxiomsIndexCachingImpl implements AnnotationAssertionAxiomsIndex {

    private final OWLOntology rootOntology;

    private final Cache<IRI, ImmutableCollection<OWLAnnotationAssertionAxiom>> axiomsBySubject;

    private boolean attached = false;

    @Inject
    public AnnotationAssertionAxiomsIndexCachingImpl(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
        axiomsBySubject = Caffeine.newBuilder()
                                  .build();
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms() {
        // Just delegate to OWLOntology implementation
        return rootOntology.getImportsClosure().stream()
                .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION).stream());
    }

    @Nonnull
    public synchronized Runnable attachOntologyListener() {
        if(attached) {
            return () -> {};
        }
        attached = true;
        OWLOntologyChangeListener listener = this::handleOntologyChanged;
        rootOntology.getOWLOntologyManager().addOntologyChangeListener(listener);
        return () -> rootOntology.getOWLOntologyManager().removeOntologyChangeListener(listener);
    }

    private void handleOntologyChanged(List<? extends OWLOntologyChange> changes) {
        changes.stream()
               .filter(OWLOntologyChange::isAxiomChange)
               .map(OWLOntologyChange::getAxiom)
               .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
               .map(ax -> (OWLAnnotationAssertionAxiom) ax)
               .map(OWLAnnotationAssertionAxiom::getSubject)
               .forEach(axiomsBySubject::invalidate);

    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject) {
        return getAxioms(subject).stream();
    }

    private ImmutableCollection<OWLAnnotationAssertionAxiom> getAxioms(@Nonnull IRI subject) {
        return axiomsBySubject.get(subject, (iri) -> rootOntology.getImportsClosure().stream()
                                                             .flatMap(ont -> ont.getAnnotationAssertionAxioms(iri).stream())
                                                             .collect(toImmutableList()));
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull IRI subject, @Nonnull OWLAnnotationProperty property) {
        return getAnnotationAssertionAxioms(subject).filter(ax -> ax.getProperty().equals(property));
    }

    @Override
    public long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject) {
        return getAxioms(subject).size();
    }

    @Override
    public long getAnnotationAssertionAxiomsCount(@Nonnull IRI subject, @Nonnull OWLAnnotationProperty property) {
        return getAnnotationAssertionAxioms(subject, property).count();
    }
}
