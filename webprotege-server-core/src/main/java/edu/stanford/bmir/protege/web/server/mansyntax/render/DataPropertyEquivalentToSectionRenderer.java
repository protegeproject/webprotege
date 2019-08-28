package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.EquivalentDataPropertiesAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyEquivalentToSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLEquivalentDataPropertiesAxiom, OWLDataPropertyExpression> {

    private final EquivalentDataPropertiesAxiomsIndex axiomsIndex;

    @Inject
    public DataPropertyEquivalentToSectionRenderer(EquivalentDataPropertiesAxiomsIndex axiomsIndex) {
        this.axiomsIndex = axiomsIndex;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.EQUIVALENT_TO;
    }

    @Override
    protected Set<OWLEquivalentDataPropertiesAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getEquivalentDataPropertiesAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLDataPropertyExpression> getRenderablesForItem(OWLDataProperty subject,
                                                                 OWLEquivalentDataPropertiesAxiom item,
                                                                 OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }
}
