package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyCharacteristicsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyCharacteristicAxiom, String> {

    private static Map<AxiomType<?>, ManchesterOWLSyntax> keywordMap = Maps.newHashMap();

    static {
        keywordMap.put(AxiomType.FUNCTIONAL_OBJECT_PROPERTY, ManchesterOWLSyntax.FUNCTIONAL);
        keywordMap.put(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, ManchesterOWLSyntax.INVERSE_FUNCTIONAL);
        keywordMap.put(AxiomType.SYMMETRIC_OBJECT_PROPERTY, ManchesterOWLSyntax.SYMMETRIC);
        keywordMap.put(AxiomType.ASYMMETRIC_OBJECT_PROPERTY, ManchesterOWLSyntax.ASYMMETRIC);
        keywordMap.put(AxiomType.REFLEXIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.REFLEXIVE);
        keywordMap.put(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.IRREFLEXIVE);
        keywordMap.put(AxiomType.TRANSITIVE_OBJECT_PROPERTY, ManchesterOWLSyntax.TRANSITIVE);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.CHARACTERISTICS;
    }

    @Override
    protected Set<OWLObjectPropertyCharacteristicAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntology ontology) {
        Set<OWLObjectPropertyCharacteristicAxiom> result = Sets.newHashSet();
        result.addAll(ontology.getFunctionalObjectPropertyAxioms(subject));
        result.addAll(ontology.getInverseFunctionalObjectPropertyAxioms(subject));
        result.addAll(ontology.getSymmetricObjectPropertyAxioms(subject));
        result.addAll(ontology.getAsymmetricObjectPropertyAxioms(subject));
        result.addAll(ontology.getReflexiveObjectPropertyAxioms(subject));
        result.addAll(ontology.getIrreflexiveObjectPropertyAxioms(subject));
        result.addAll(ontology.getTransitiveObjectPropertyAxioms(subject));
       return result;
    }

    @Override
    public List<String> getRenderablesForItem(OWLObjectProperty subject,
                                              OWLObjectPropertyCharacteristicAxiom item,
                                              OWLOntology ontology) {
        ManchesterOWLSyntax kw = keywordMap.get(item.getAxiomType());
        if(kw != null) {
            return Lists.newArrayList(kw.keyword());
        }
        else {
            throw new RuntimeException("Missing axiom type rendering " + item.getAxiomType());
        }
    }
}
