package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.SubDataPropertyAxiomsBySubPropertyIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertySubPropertyOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLSubDataPropertyOfAxiom, OWLDataPropertyExpression> {

    @Nonnull
    private final SubDataPropertyAxiomsBySubPropertyIndex axiomsIndex;

    @Inject
    public DataPropertySubPropertyOfSectionRenderer(@Nonnull SubDataPropertyAxiomsBySubPropertyIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUB_PROPERTY_OF;
    }

    @Override
    protected Set<OWLSubDataPropertyOfAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getSubPropertyOfAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLDataPropertyExpression> getRenderablesForItem(OWLDataProperty subject,
                                                                 OWLSubDataPropertyOfAxiom item, OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getSuperProperty());
    }
}
