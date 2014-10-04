package edu.stanford.bmir.protege.web.server.render;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.Map;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 03/10/2014
 */
public class AnnotationPropertyIndexProvider {

    private static final ImmutableMap<IRI, Integer> indexMap;

    static {

        ImmutableList<IRI> defaultOrdering = ImmutableList.<IRI>builder().add(
                // Labels
                OWLRDFVocabulary.RDFS_LABEL.getIRI(),
                SKOSVocabulary.PREFLABEL.getIRI(),
                DublinCoreVocabulary.TITLE.getIRI(),

                // OBO stuff that's important
                // Id.  I can't find a constant for this.
                IRI.create("http://www.geneontology.org/formats/oboInOwl#id"),

                // Replaced by
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_IAO_0100001.getIRI(),

                Obo2OWLConstants.Obo2OWLVocabulary.hasAlternativeId.getIRI(),

                // Definitions
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_IAO_0000115.getIRI(),

                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasOboNamespace.getIRI(),

                // Other definitions
                SKOSVocabulary.DEFINITION.getIRI(),
                DublinCoreVocabulary.DESCRIPTION.getIRI(),
                SKOSVocabulary.NOTE.getIRI(),

                OWLRDFVocabulary.RDFS_COMMENT.getIRI(),

                // Other labels
                SKOSVocabulary.ALTLABEL.getIRI(),

                OWLRDFVocabulary.RDFS_SEE_ALSO.getIRI(),
                OWLRDFVocabulary.RDFS_IS_DEFINED_BY.getIRI(),

                // Important OBO annotations.  Ordering derived from documents shared by Chris M.
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasExactSynonym.getIRI(),
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasRelatedSynonym.getIRI(),
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasBroadSynonym.getIRI(),
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasNarrowSynonym.getIRI(),

                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasDbXref.getIRI(),
                Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_Subset.getIRI()
        ).build();

        Map<IRI, Integer> map = Maps.newLinkedHashMap();
        for(IRI iri : defaultOrdering) {
            addProperty(iri, map);
        }
        indexMap = ImmutableMap.copyOf(map);
    }


    private static void addProperty(IRI property, Map<IRI, Integer> map) {
        int index = map.size();
        map.put(property, index);
    }



    public AnnotationPropertyIndexProvider() {

    }


    public int getIndex(OWLAnnotationProperty property) {
        Integer index = indexMap.get(property.getIRI());
        if(index == null) {
            return indexMap.size();
        }
        else {
            return index;
        }
    }
}
