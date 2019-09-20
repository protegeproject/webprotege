package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
                                                                        @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(cls);
        checkNotNull(ontologyID);
        return index.getAxioms(cls, ontologyID);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
