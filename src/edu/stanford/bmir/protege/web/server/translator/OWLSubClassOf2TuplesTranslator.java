package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualNamedClass;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualNamedIndividual;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualObjectProperty;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OWLSubClassOf2TuplesTranslator {

    private OWLAPIProject project;

    public OWLSubClassOf2TuplesTranslator(OWLAPIProject project) {
        this.project = project;
    }

//    public Collection<TripleTuple<?,?,?>> translate(final OWLSubClassOfAxiom ax) {
//        Collection<TripleTuple<?,?,?>> o = ax.getSuperClass().accept(new OWLClassExpressionVisitorExAdapter<Collection<TripleTuple<?,?,?>>>() {
//            @Override
//            public Collection<TripleTuple<?, ?, ?>> visit(OWLObjectSomeValuesFrom ce) {
//                if(ax.getSubClass().isAnonymous()) {
//                    return Collections.emptySet();
//                }
//                if(ce.getFiller().isAnonymous()) {
//                   return Collections.emptySet();
//                }
//                if(ce.getProperty().isAnonymous()) {
//                    return Collections.emptySet();
//                }
//                OWLClass subCls = ax.getSubClass().asOWLClass();
//                OWLObjectProperty prop = ce.getProperty().asOWLObjectProperty();
//                OWLClass filler = ce.getFiller().asOWLClass();
//                ObjectTranslator translator = new ObjectTranslator(project);
//                VisualNamedClass visSubject = translator.translateToVisual(subCls);
//                VisualObjectProperty visualProperty = translator.translateToVisual(prop);
//                VisualNamedClass visualFiller = translator.translateToVisual(filler);
//                return Collections.<TripleTuple<?,?,?>>singleton(new ClsObjectPropertyClsTripleTuple(visSubject, visualProperty, visualFiller));
//            }
//
//            @Override
//            public Collection<TripleTuple<?, ?, ?>> visit(OWLObjectHasValue ce) {
//                if(ax.getSubClass().isAnonymous()) {
//                    return Collections.emptySet();
//                }
//                if(ce.getValue().isAnonymous()) {
//                    return Collections.emptySet();
//                }
//                if(ce.getProperty().isAnonymous()) {
//                    return Collections.emptySet();
//                }
//                OWLClass subCls = ax.getSubClass().asOWLClass();
//                OWLObjectProperty prop = ce.getProperty().asOWLObjectProperty();
//                OWLNamedIndividual filler = ce.getValue().asOWLNamedIndividual();
//                ObjectTranslator translator = new ObjectTranslator(project);
//                VisualNamedClass visSubject = translator.translateToVisual(subCls);
//                VisualObjectProperty visualProperty = translator.translateToVisual(prop);
//                VisualNamedIndividual visualFiller = translator.translateToVisual(filler);
//                return Collections.<TripleTuple<?,?,?>>singleton(new ClsObjectPropertyIndividualTripleTuple(visSubject, visualProperty, visualFiller));
//            }
//        });
//        if(o == null) {
//            return Collections.emptySet();
//        }
//        else {
//            return o;
//        }
//    }
    
}
