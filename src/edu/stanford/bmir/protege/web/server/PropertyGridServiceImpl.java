package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.PropertyGridService;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.IRI;
import edu.stanford.bmir.protege.web.client.rpc.data.tuple.TripleTuple;
import edu.stanford.bmir.protege.web.client.ui.propertygrid.PropertyGrid;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.translator.OWLSubClassOf2TuplesTranslator;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class PropertyGridServiceImpl extends WebProtegeRemoteServiceServlet implements PropertyGridService {

    private OWLAPIProject getProject(ProjectId projectId) {
        return OWLAPIProjectManager.getProjectManager().getProject(projectId);
    }


    public PropertyGrid getPropertyGrid(ProjectId projectId, final Entity entity) {
        final OWLAPIProject project = getProject(projectId);
        return entity.accept(new EntityVisitor<PropertyGrid, RuntimeException>() {
            public PropertyGrid visit(NamedClass c) throws RuntimeException {
                IRI iri = c.getIRI();
                OWLClass cls = project.getDataFactory().getOWLClass(toIRI(iri));
                Collection<TripleTuple<?,?,?>> result = new HashSet<TripleTuple<?, ?, ?>>();
                Set<OWLSubClassOfAxiom> axioms = project.getRootOntology().getSubClassAxiomsForSubClass(cls);
                for(OWLSubClassOfAxiom axiom : axioms) {
                    OWLSubClassOf2TuplesTranslator translator = new OWLSubClassOf2TuplesTranslator(project);
                    result.addAll(translator.translate(axiom));
                }
                return new PropertyGrid(entity, result);
            }

            public PropertyGrid visit(ObjectProperty property) throws RuntimeException {
                return new PropertyGrid(entity);
            }

            public PropertyGrid visit(DataProperty property) throws RuntimeException {
                return new PropertyGrid(entity);
            }

            public PropertyGrid visit(AnnotationProperty property) throws RuntimeException {
                return new PropertyGrid(entity);
            }

            public PropertyGrid visit(Datatype datatype) throws RuntimeException {
                return new PropertyGrid(entity);
            }

            public PropertyGrid visit(NamedIndividual individual) throws RuntimeException {
                return new PropertyGrid(entity);
            }
        });
    }

    private org.semanticweb.owlapi.model.IRI toIRI(IRI iri) {
        return org.semanticweb.owlapi.model.IRI.create(iri.getIRI());
    }
}
