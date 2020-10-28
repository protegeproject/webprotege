package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyCharacteristicsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLFunctionalDataPropertyAxiom, ManchesterOWLSyntax> {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public DataPropertyCharacteristicsSectionRenderer(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.CHARACTERISTICS;
    }

    @Override
    protected Set<OWLFunctionalDataPropertyAxiom> getAxiomsInOntology(OWLDataProperty subject,
                                                                      OntologyDocumentId ontologyDocumentId) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.FUNCTIONAL_DATA_PROPERTY, ontologyDocumentId)
                                .filter(ax -> ax.getProperty()
                                                .equals(subject))
                                .collect(toSet());
    }

    @Override
    public List<ManchesterOWLSyntax> getRenderablesForItem(OWLDataProperty subject,
                                                           OWLFunctionalDataPropertyAxiom item,
                                                           OntologyDocumentId ontologyDocumentId) {
        return Lists.newArrayList(ManchesterOWLSyntax.FUNCTIONAL);
    }
}
