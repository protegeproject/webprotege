package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OWLEntity2VisualEntityTranslator implements OWLEntityVisitorEx<VisualEntity<?>> {

    private OWLAPIProject project;

    private OWLEntity2EntityTranslator entity2EntityTranslator = new OWLEntity2EntityTranslator();

    public OWLEntity2VisualEntityTranslator(OWLAPIProject project) {
        this.project = project;
    }

    public VisualEntity<?> visit(OWLClass cls) {
        return new VisualCls((Cls) cls.accept(entity2EntityTranslator), getShortForm(cls));
    }

    private String getShortForm(OWLEntity cls) {
        return project.getRenderingManager().getShortForm(cls);
    }

    public VisualEntity<?> visit(OWLObjectProperty property) {
        return new VisualObjectProperty((ObjectProperty) property.accept(entity2EntityTranslator), getShortForm(property));
    }

    public VisualEntity<?> visit(OWLDataProperty property) {
        return new VisualDataProperty((DataProperty) property.accept(entity2EntityTranslator), getShortForm(property));
    }

    public VisualEntity<?> visit(OWLNamedIndividual individual) {
        return new VisualNamedIndividual((NamedIndividual) individual.accept(entity2EntityTranslator), getShortForm(individual));
    }

    public VisualEntity<?> visit(OWLDatatype datatype) {
        return new VisualDatatype((Datatype) datatype.accept(entity2EntityTranslator), getShortForm(datatype));
    }

    public VisualEntity<?> visit(OWLAnnotationProperty property) {
        return new VisualAnnotationProperty((AnnotationProperty) property.accept(entity2EntityTranslator), getShortForm(property));
    }
}
