package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.entity.SubjectClosureResolver;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class OntologyChangeSubjectProvider implements HasGetChangeSubjects {

    private ChangeSubjectProvider changeSubjectProvider;

    private final SubjectClosureResolver closureResolver;

    @Inject
    public OntologyChangeSubjectProvider(EntitiesInProjectSignatureByIriIndex entitiesByIRI,
                                         SubjectClosureResolver closureResolver) {
        this.changeSubjectProvider = new ChangeSubjectProvider(new AxiomEntitySubjectProvider(entitiesByIRI));
        this.closureResolver = checkNotNull(closureResolver);
    }

    @Override
    public Set<OWLEntity> getChangeSubjects(OntologyChange change) {
        return change.accept(changeSubjectProvider)
                .stream()
                .flatMap(closureResolver::resolve)
                .collect(Collectors.toSet());
    }



    private static class ChangeSubjectProvider implements OntologyChangeVisitorEx<Set<OWLEntity>> {

        private AxiomEntitySubjectProvider subjectProvider;

        private ChangeSubjectProvider(AxiomEntitySubjectProvider subjectProvider) {
            this.subjectProvider = subjectProvider;
        }

        @Override
        public Set<OWLEntity> getDefaultReturnValue() {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(@Nonnull AddAxiomChange addAxiomChange) {
            return subjectProvider.getSubject(addAxiomChange.getAxiom());
        }

        @Override
        public Set<OWLEntity> visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
            return subjectProvider.getSubject(removeAxiomChange.getAxiom());
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
                return entitiesByIri.getEntitiesInSignature((IRI) subject).collect(Collectors.toSet());
            }
            else {
                return Collections.emptySet();
            }
        }
    }

}
