package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.DisjointClassesAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ClassDisjointWithSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLDisjointClassesAxiom, OWLClassExpression> {

    @Nonnull
    private final DisjointClassesAxiomsIndex axiomsIndex;

    @Inject
    public ClassDisjointWithSectionRenderer(@Nonnull DisjointClassesAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DISJOINT_WITH;
    }

    @Override
    protected Set<OWLDisjointClassesAxiom> getAxiomsInOntology(OWLClass subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getDisjointClassesAxioms(subject, ontologyId).collect(toSet());
    }
    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLClass subject,
                                                          OWLDisjointClassesAxiom item,
                                                          OWLOntologyID ontologyId) {
        return new ArrayList<>(item.getClassExpressionsMinus(subject));
    }

}
