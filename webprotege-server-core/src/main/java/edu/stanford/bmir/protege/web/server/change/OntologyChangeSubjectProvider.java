package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class OntologyChangeSubjectProvider implements HasGetChangeSubjects {

    private ChangeSubjectProvider changeSubjectProvider;

    @Inject
    public OntologyChangeSubjectProvider(EntitiesInProjectSignatureByIriIndex entitiesByIRI) {
        this.changeSubjectProvider = new ChangeSubjectProvider(new AxiomEntitySubjectProvider(entitiesByIRI));
    }

    @Override
    public Set<OWLEntity> getChangeSubjects(OWLOntologyChange change) {
        return change.accept(changeSubjectProvider);
    }



    private static class ChangeSubjectProvider implements OWLOntologyChangeVisitorEx<Set<OWLEntity>> {

        private AxiomEntitySubjectProvider subjectProvider;

        private ChangeSubjectProvider(AxiomEntitySubjectProvider subjectProvider) {
            this.subjectProvider = subjectProvider;
        }

        @Override
        public Set<OWLEntity> visit(AddAxiom addAxiom) {
            return subjectProvider.getSubject(addAxiom.getAxiom());
        }

        @Override
        public Set<OWLEntity> visit(RemoveAxiom removeAxiom) {
            return subjectProvider.getSubject(removeAxiom.getAxiom());
        }

        @Override
        public Set<OWLEntity> visit(SetOntologyID setOntologyID) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(AddImport addImport) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(RemoveImport removeImport) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(AddOntologyAnnotation addOntologyAnnotation) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(RemoveOntologyAnnotation removeOntologyAnnotation) {
            return Collections.emptySet();
        }
    }


    private static class AxiomEntitySubjectProvider {

        private EntitiesInProjectSignatureByIriIndex entitiesByIri;

        private AxiomEntitySubjectProvider(EntitiesInProjectSignatureByIriIndex entitiesByIri) {
            this.entitiesByIri = entitiesByIri;
        }

        public Set<OWLEntity> getSubject(OWLAxiom axiom) {
            OWLObject subject = new AxiomSubjectProvider().getSubject(axiom);
            if(subject instanceof OWLEntity) {
                return Collections.singleton((OWLEntity) subject);
            }
            else if(subject instanceof IRI) {
                return entitiesByIri.getEntityInSignature((IRI) subject).collect(Collectors.toSet());
            }
            else {
                return Collections.emptySet();
            }
        }
    }

}
