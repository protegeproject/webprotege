package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class DefaultOntologyIdManagerImpl implements DefaultOntologyIdManager, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    private OntologyDocumentId freshOntologyId;

    @Inject
    public DefaultOntologyIdManagerImpl(ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public synchronized OntologyDocumentId getDefaultOntologyDocumentId() {
        Stream<OntologyDocumentId> ontologyIds = projectOntologiesIndex.getOntologyDocumentIds();
        return ontologyIds.findFirst()
                .orElseGet(this::createFreshOntologyId);
    }

    private OntologyDocumentId createFreshOntologyId() {
        if(freshOntologyId == null) {
            freshOntologyId = OntologyDocumentId.generate();
        }
        return freshOntologyId;
    }
}
