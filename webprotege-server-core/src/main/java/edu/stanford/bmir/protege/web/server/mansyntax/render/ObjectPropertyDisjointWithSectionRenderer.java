package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.DisjointObjectPropertiesAxiomsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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
public class ObjectPropertyDisjointWithSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLDisjointObjectPropertiesAxiom, OWLObjectPropertyExpression> {

    @Nonnull
    private final DisjointObjectPropertiesAxiomsIndex axiomsIndex;

    @Inject
    public ObjectPropertyDisjointWithSectionRenderer(@Nonnull DisjointObjectPropertiesAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DISJOINT_WITH;
    }

    @Override
    protected Set<OWLDisjointObjectPropertiesAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                                        OWLOntologyID ontologyId) {
        return axiomsIndex.getDisjointObjectPropertiesAxioms(subject, ontologyId).collect(toSet());
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLDisjointObjectPropertiesAxiom item,
                                                                   OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObjectPropertyExpression> renderables) {
        return ", ";
    }
}
