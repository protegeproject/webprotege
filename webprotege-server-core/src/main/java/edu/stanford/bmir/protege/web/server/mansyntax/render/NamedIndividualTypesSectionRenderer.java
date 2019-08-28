package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualTypesSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLClassAssertionAxiom, OWLClassExpression> {

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex axiomsIndex;

    @Inject
    public NamedIndividualTypesSectionRenderer(@Nonnull ClassAssertionAxiomsByIndividualIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.TYPES;
    }

    @Override
    protected Set<OWLClassAssertionAxiom> getAxiomsInOntology(OWLNamedIndividual subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getClassAssertionAxioms(subject, ontologyId).collect(Collectors.toSet());
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLNamedIndividual subject,
                                                          OWLClassAssertionAxiom item,
                                                          OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getClassExpression());
    }
}
