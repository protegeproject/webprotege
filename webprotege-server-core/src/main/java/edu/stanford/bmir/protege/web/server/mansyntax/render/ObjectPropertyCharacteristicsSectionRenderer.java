package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyCharacteristicAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyCharacteristicsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyCharacteristicAxiom, String> {

    private static Map<AxiomType<? extends OWLObjectPropertyCharacteristicAxiom>, ManchesterOWLSyntax> keywordMap = Maps.newHashMap();

    static {
        keywordMap.put(AxiomType.FUNCTIONAL_OBJECT_PROPERTY, ManchesterOWLSyntax.FUNCTIONAL);
        keywordMap.put(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, ManchesterOWLSyntax.INVERSE_FUNCTIONAL);
        keywordMap.put(AxiomType.SYMMETRIC_OBJECT_PROPERTY, ManchesterOWLSyntax.SYMMETRIC);
        keywordMap.put(AxiomType.ASYMMETRIC_OBJECT_PROPERTY, ManchesterOWLSyntax.ASYMMETRIC);
        keywordMap.put(AxiomType.REFLEXIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.REFLEXIVE);
        keywordMap.put(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.IRREFLEXIVE);
        keywordMap.put(AxiomType.TRANSITIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.TRANSITIVE);
    }

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public ObjectPropertyCharacteristicsSectionRenderer(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.CHARACTERISTICS;
    }

    @Override
    protected Set<OWLObjectPropertyCharacteristicAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntologyID ontologyId) {
        // Don't use specific indexes as the number of properties, and thus the number
        // of property characteristic axioms in an ontology is small
        return keywordMap.keySet()
                  .stream()
                  .flatMap(type -> getAxiom(type, ontologyId, subject))
                  .collect(toSet());
    }

    private Stream<? extends OWLObjectPropertyCharacteristicAxiom> getAxiom(AxiomType<? extends OWLObjectPropertyCharacteristicAxiom> type,
                                                                            OWLOntologyID ontologyId,
                                                                            OWLObjectProperty subject) {
        return axiomsByTypeIndex.getAxiomsByType(type, ontologyId)
                         .filter(ax ->  ax.getProperty().equals(subject));
    }

    @Override
    public List<String> getRenderablesForItem(OWLObjectProperty subject,
                                              OWLObjectPropertyCharacteristicAxiom item,
                                              OWLOntologyID ontologyId) {
        var axiomType = item.getAxiomType();
        ManchesterOWLSyntax kw = keywordMap.get(axiomType);
        if(kw != null) {
            return Lists.newArrayList(kw.keyword());
        }
        else {
            throw new RuntimeException("Missing axiom type rendering " + axiomType);
        }
    }
}
