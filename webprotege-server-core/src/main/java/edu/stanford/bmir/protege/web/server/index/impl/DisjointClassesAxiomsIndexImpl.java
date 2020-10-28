package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DisjointClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ProjectSingleton
public class DisjointClassesAxiomsIndexImpl implements DisjointClassesAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLClassExpression, OWLDisjointClassesAxiom> index;

    @Inject
    public DisjointClassesAxiomsIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLDisjointClassesAxiom.class,
                                                                   OWLDisjointClassesAxiom::getClassExpressions);

        index.setLazy(true);
    }

    @Nonnull
    @Override
    public Stream<OWLDisjointClassesAxiom> getDisjointClassesAxioms(@Nonnull OWLClass cls, OntologyDocumentId ontologyDocumentId) {
        checkNotNull(cls);
        checkNotNull(ontologyDocumentId);
        return index.getAxioms(cls, ontologyDocumentId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
