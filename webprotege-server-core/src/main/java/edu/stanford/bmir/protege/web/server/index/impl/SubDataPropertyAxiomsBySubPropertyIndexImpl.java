package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.SubDataPropertyAxiomsBySubPropertyIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class SubDataPropertyAxiomsBySubPropertyIndexImpl implements SubDataPropertyAxiomsBySubPropertyIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public SubDataPropertyAxiomsBySubPropertyIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLSubDataPropertyOfAxiom> getSubPropertyOfAxioms(@Nonnull OWLDataProperty dataProperty,
                                                                    @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(ontologyID);
        checkNotNull(dataProperty);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.SUB_DATA_PROPERTY, ontologyID)
                                .filter(ax -> ax.getSubProperty()
                                                .equals(dataProperty));
    }
}
