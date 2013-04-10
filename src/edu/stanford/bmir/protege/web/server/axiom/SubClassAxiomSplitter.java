package edu.stanford.bmir.protege.web.server.axiom;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public class SubClassAxiomSplitter implements AxiomRewriter  {

    @Override
    public Set<OWLAxiom> rewriteAxiom(OWLAxiom axiom) {
        if(!(axiom instanceof OWLSubClassOfAxiom)) {
            return Collections.singleton(axiom);
        }
        OWLSubClassOfAxiom ax = (OWLSubClassOfAxiom) axiom;
        OWLClassExpression subClass = ax.getSubClass();
        OWLClassExpression superClass = ax.getSuperClass();
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(OWLClassExpression conj : superClass.asConjunctSet()) {
            OWLDataFactory df = DataFactory.get();
            result.add(df.getOWLSubClassOfAxiom(subClass, conj, axiom.getAnnotations()));
        }
        return result;
    }
}
