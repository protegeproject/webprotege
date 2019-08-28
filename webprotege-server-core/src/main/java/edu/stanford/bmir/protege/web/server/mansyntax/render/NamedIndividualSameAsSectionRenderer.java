package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.SameIndividualAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualSameAsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLSameIndividualAxiom, OWLIndividual> {

    @Nonnull
    private final SameIndividualAxiomsIndex axiomsIndex;

    @Inject
    public NamedIndividualSameAsSectionRenderer(@Nonnull SameIndividualAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SAME_AS;
    }

    @Override
    protected Set<OWLSameIndividualAxiom> getAxiomsInOntology(OWLNamedIndividual subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getSameIndividualAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLIndividual> getRenderablesForItem(OWLNamedIndividual subject,
                                                     OWLSameIndividualAxiom item,
                                                     OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getIndividuals());
    }
}
