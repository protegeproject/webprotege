package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public class WebProtegeShortFormProvider implements ShortFormProvider {

    private OWLAPIProject project;
    
    private List<IRI> annotationPropertyIRIs;

    public WebProtegeShortFormProvider(OWLAPIProject project) {
        this.project = project;
        List<IRI> annotationPropertyIRIs = new ArrayList<IRI>();
        annotationPropertyIRIs.add(SKOSVocabulary.PREFLABEL.getIRI());
        annotationPropertyIRIs.add(SKOSVocabulary.ALTLABEL.getIRI());
        annotationPropertyIRIs.add(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        this.annotationPropertyIRIs = Collections.unmodifiableList(annotationPropertyIRIs);
    }

    public synchronized String getShortForm(OWLEntity owlEntity) {
        int matchedIndex = Integer.MAX_VALUE;
        OWLAnnotationValue renderingValue = null;
        for(OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(owlEntity.getIRI())) {
                // Think this is thread safe.  The list is immutable and each indexOf call creates a fresh iterator
                // object to find the index.
                int index = annotationPropertyIRIs.indexOf(ax.getProperty().getIRI());
                if(index < matchedIndex && index != -1) {
                   matchedIndex = index;
                   renderingValue = ax.getValue();
                }
            }
        }
        String result;
        if(renderingValue instanceof OWLLiteral) {
            result = ((OWLLiteral) renderingValue).getLiteral();
        }
        else {
            // Had this as an instance variable, but creating a new instance each time is definitely thread safe.
            SimpleShortFormProvider simpleShortFormProvider = new SimpleShortFormProvider();
            result = simpleShortFormProvider.getShortForm(owlEntity);
        }
        if(result.contains(" ")) {
            StringBuilder sb = getQuoted(result);
            result = sb.toString();
        }
        return result;
    }

    private static StringBuilder getQuoted(String result) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(result);
        sb.append("'");
        return sb;
    }

    public void dispose() {
    }


}
