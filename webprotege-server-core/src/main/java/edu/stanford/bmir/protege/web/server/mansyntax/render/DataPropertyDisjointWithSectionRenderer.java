package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.DisjointDataPropertiesAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyDisjointWithSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLDisjointDataPropertiesAxiom, OWLDataPropertyExpression> {


    @Nonnull
    private final DisjointDataPropertiesAxiomsIndex axiomsIndex;

    @Inject
    public DataPropertyDisjointWithSectionRenderer(@Nonnull DisjointDataPropertiesAxiomsIndex axiomsIndex) {
        this.axiomsIndex = axiomsIndex;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DISJOINT_WITH;
    }

    @Override
    protected Set<OWLDisjointDataPropertiesAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getDisjointDataPropertiesAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLDataPropertyExpression> getRenderablesForItem(OWLDataProperty subject,
                                                                 OWLDisjointDataPropertiesAxiom item,
                                                                 OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }
}
