package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertySubPropertyChainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLSubPropertyChainOfAxiom, OWLObjectPropertyExpression> {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public ObjectPropertySubPropertyChainSectionRenderer(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUB_PROPERTY_CHAIN;
    }

    @Override
    public Set<OWLSubPropertyChainOfAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                               OntologyDocumentId ontologyDocumentId) {
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.SUB_PROPERTY_CHAIN_OF, ontologyDocumentId)
                         .filter(ax -> ax.getSuperProperty().equals(subject))
                         .collect(toSet());
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLSubPropertyChainOfAxiom item,
                                                                   OntologyDocumentId ontologyDocumentId) {
        return item.getPropertyChain();
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObjectPropertyExpression> renderables) {
        return "<span class=\"ms-connective-kw\"> o </span>";
    }
}
