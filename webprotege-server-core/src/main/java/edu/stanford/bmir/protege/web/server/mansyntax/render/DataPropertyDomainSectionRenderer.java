package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.DataPropertyDomainAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLDataPropertyDomainAxiom, OWLClassExpression> {

    @Nonnull
    private final DataPropertyDomainAxiomsIndex axiomsIndex;

    @Inject
    public DataPropertyDomainSectionRenderer(@Nonnull DataPropertyDomainAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLDataPropertyDomainAxiom> getAxiomsInOntology(OWLDataProperty subject, OntologyDocumentId ontologyDocumentId) {
        return axiomsIndex.getDataPropertyDomainAxioms(subject, ontologyDocumentId).collect(toSet());
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLDataProperty subject,
                                                          OWLDataPropertyDomainAxiom item,
                                                          OntologyDocumentId ontologyDocumentId) {
        return Lists.newArrayList(item.getDomain());
    }
}
