package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

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
public class ClassEquivalentToSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLEquivalentClassesAxiom, OWLClassExpression> {

    @Nonnull
    private final EquivalentClassesAxiomsIndex axiomsIndex;

    @Inject
    public ClassEquivalentToSectionRenderer(@Nonnull EquivalentClassesAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.EQUIVALENT_TO;
    }

    @Override
    protected Set<OWLEquivalentClassesAxiom> getAxiomsInOntology(OWLClass subject, OntologyDocumentId ontologyDocumentId) {
        return axiomsIndex.getEquivalentClassesAxioms(subject, ontologyDocumentId).collect(toSet());
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLClass subject,
                                                          OWLEquivalentClassesAxiom item,
                                                          OntologyDocumentId ontologyDocumentId) {
        return new ArrayList<>(item.getClassExpressionsMinus(subject));
    }
}
