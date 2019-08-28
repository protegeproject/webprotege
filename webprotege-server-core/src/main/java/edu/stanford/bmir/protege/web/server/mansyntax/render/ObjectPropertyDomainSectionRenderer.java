package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyDomainAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyDomainAxiom, OWLClassExpression> {

    @Nonnull
    private final ObjectPropertyDomainAxiomsIndex axiomsIndex;

    @Inject
    public ObjectPropertyDomainSectionRenderer(@Nonnull ObjectPropertyDomainAxiomsIndex axiomsIndex) {
        this.axiomsIndex = axiomsIndex;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLObjectPropertyDomainAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntologyID ontologyId) {
        return axiomsIndex.getObjectPropertyDomainAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                          OWLObjectPropertyDomainAxiom item,
                                                          OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getDomain());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLClassExpression> renderables) {
        return ", ";
    }
}
