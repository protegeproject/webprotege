package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.DataPropertyRangeAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLDataPropertyRangeAxiom, OWLDataRange> {

    @Nonnull
    private final DataPropertyRangeAxiomsIndex axiomsIndex;

    @Inject
    public DataPropertyRangeSectionRenderer(@Nonnull DataPropertyRangeAxiomsIndex axiomsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLDataPropertyRangeAxiom> getAxiomsInOntology(OWLDataProperty subject, OntologyDocumentId ontologyDocumentId) {
        return axiomsIndex.getDataPropertyRangeAxioms(subject, ontologyDocumentId).collect(toSet());
    }

    @Override
    public List<OWLDataRange> getRenderablesForItem(OWLDataProperty subject,
                                                    OWLDataPropertyRangeAxiom item,
                                                    OntologyDocumentId ontologyDocumentId) {
        return Lists.newArrayList(item.getRange());
    }
}
