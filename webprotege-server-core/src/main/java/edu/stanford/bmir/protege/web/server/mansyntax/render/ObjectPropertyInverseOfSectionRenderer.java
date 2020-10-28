package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.InverseObjectPropertyAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyInverseOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLInverseObjectPropertiesAxiom, OWLObjectPropertyExpression> {

    @Nonnull
    private final InverseObjectPropertyAxiomsIndex axiomsIndex;

    @Inject
    public ObjectPropertyInverseOfSectionRenderer(@Nonnull InverseObjectPropertyAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.INVERSE_OF;
    }

    @Override
    protected Set<OWLInverseObjectPropertiesAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                                       OntologyDocumentId ontologyDocumentId) {
        return axiomsIndex.getInverseObjectPropertyAxioms(subject, ontologyDocumentId).collect(toSet());
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLInverseObjectPropertiesAxiom item,
                                                                   OntologyDocumentId ontologyDocumentId) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }
}
