package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.AnnotationPropertyDomainAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLAnnotationProperty, OWLAnnotationPropertyDomainAxiom, OWLObject> {


    @Nonnull
    private final AnnotationPropertyDomainAxiomsIndex axiomsIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Inject
    public AnnotationPropertyDomainSectionRenderer(@Nonnull AnnotationPropertyDomainAxiomsIndex axiomsIndex,
                                                   @Nonnull EntitiesInProjectSignatureByIriIndex entitiesByIri) {
        this.axiomsIndex = axiomsIndex;
        this.entitiesByIri = entitiesByIri;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLAnnotationPropertyDomainAxiom> getAxiomsInOntology(OWLAnnotationProperty subject,
                                                                        OWLOntologyID ontologyId) {
        return axiomsIndex.getAnnotationPropertyDomainAxioms(subject, ontologyId).collect(Collectors.toSet());
    }


    @Override
    public List<OWLObject> getRenderablesForItem(OWLAnnotationProperty subject,
                                                 OWLAnnotationPropertyDomainAxiom item,
                                                 OWLOntologyID ontologyId) {
        return entitiesByIri.getEntitiesInSignature(item.getDomain()).collect(toList());
    }
}
