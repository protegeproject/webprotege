package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyDomainAxiomsIndex;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class ObjectPropertyDomainAxiomsIndexImpl implements ObjectPropertyDomainAxiomsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public ObjectPropertyDomainAxiomsIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = axiomsByTypeIndex;
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(@Nonnull OWLObjectProperty property,
                                                                              @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(property);
        checkNotNull(ontologyId);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.OBJECT_PROPERTY_DOMAIN, ontologyId)
                                .filter(ax -> ax.getProperty().equals(property));
    }
}
