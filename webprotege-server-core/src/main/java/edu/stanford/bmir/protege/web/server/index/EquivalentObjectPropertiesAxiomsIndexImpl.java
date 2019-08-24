package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
public class EquivalentObjectPropertiesAxiomsIndexImpl implements EquivalentObjectPropertiesAxiomsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public EquivalentObjectPropertiesAxiomsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(@Nonnull OWLObjectProperty property,
                                                                                          @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(property);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getEquivalentObjectPropertiesAxioms(property).stream());
    }
}
