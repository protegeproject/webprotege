package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyRangeAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyRangeAxiom, OWLClassExpression> {

    @Nonnull
    private final ObjectPropertyRangeAxiomsIndex axiomsIndex;

    @Inject
    public ObjectPropertyRangeSectionRenderer(@Nonnull ObjectPropertyRangeAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLObjectPropertyRangeAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getObjectPropertyRangeAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                          OWLObjectPropertyRangeAxiom item,
                                                          OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getRange());
    }
}
