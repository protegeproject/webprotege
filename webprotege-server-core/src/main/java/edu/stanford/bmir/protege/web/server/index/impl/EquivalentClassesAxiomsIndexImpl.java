package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public class EquivalentClassesAxiomsIndexImpl implements EquivalentClassesAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLClass, OWLEquivalentClassesAxiom> index;

    @Inject
    public EquivalentClassesAxiomsIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLEquivalentClassesAxiom.class,
                                                                   OWLEquivalentClassesAxiom::getNamedClasses);
    }

    @Nonnull
    @Override
    public Stream<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(@Nonnull OWLClass cls,
                                                                        @Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(cls);
        checkNotNull(ontologyDocumentId);
        return index.getAxioms(cls, ontologyDocumentId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
