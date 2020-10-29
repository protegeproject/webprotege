package edu.stanford.bmir.protege.web.server.mansyntax;

import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentIdDisplayNameProvider;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
public class ShellOntologyChecker implements OWLOntologyChecker {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologyDocumentIdDisplayNameProvider shortFormProvider;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public ShellOntologyChecker(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                @Nonnull OntologyDocumentIdDisplayNameProvider shortFormProvider,
                                DefaultOntologyIdManager defaultOntologyIdManager) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.shortFormProvider = checkNotNull(shortFormProvider);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    @Nullable
    @Override
    public OWLOntology getOntology(@Nullable String name) {
        if(name == null) {
            var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyDocumentId();
            return ShellOwlOntology.get(defaultOntologyId);
        }
        return projectOntologiesIndex.getOntologyDocumentIds()
                                     .filter(ontId -> shortFormProvider.getDisplayName(ontId)
                                                                       .equalsIgnoreCase(name))
                                     .findFirst()
                                     .map(ShellOwlOntology::new)
                                     .orElse(null);
    }
}
