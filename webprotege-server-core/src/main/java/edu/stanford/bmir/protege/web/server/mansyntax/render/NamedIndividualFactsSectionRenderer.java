package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.DataPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualFactsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLPropertyAssertionAxiom<?, ?>, OWLObject> {

    @Nonnull
    private final ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAxiomsIndex;

    @Nonnull
    private final DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAxiomsIndex;

    @Inject
    public NamedIndividualFactsSectionRenderer(@Nonnull ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAxiomsIndex,
                                               @Nonnull DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAxiomsIndex) {
        this.objectPropertyAxiomsIndex = checkNotNull(objectPropertyAxiomsIndex);
        this.dataPropertyAxiomsIndex = checkNotNull(dataPropertyAxiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.FACTS;
    }

    @Override
    public Set<OWLPropertyAssertionAxiom<?, ?>> getAxiomsInOntology(OWLNamedIndividual subject,
                                                                    OWLOntologyID ontologyId) {
        return Stream.concat(
                objectPropertyAxiomsIndex.getObjectPropertyAssertions(subject, ontologyId),
                dataPropertyAxiomsIndex.getDataPropertyAssertions(subject, ontologyId)
        ).collect(toSet());
    }

    @Override
    public List<OWLObject> getRenderablesForItem(OWLNamedIndividual subject,
                                                 OWLPropertyAssertionAxiom<?, ?> item,
                                                 OWLOntologyID ontologyId) {
        return Lists.newArrayList(item.getProperty(), item.getObject());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObject> renderables) {
        return renderableIndex == 0 ? "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" : super.getSeparatorAfter(renderableIndex, renderables);
    }
}
