package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsAction extends AbstractHasProjectAction<CreateNamedIndividualsResult> {

    private OWLClass type;

    private Set<String> shortNames = new HashSet<String>();

    private CreateNamedIndividualsAction() {
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param shortNames A set of short names for the individuals.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(ProjectId projectId, Set<String> shortNames) {
        super(projectId);
        this.type = null;
        this.shortNames = checkNotNull(shortNames);
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param type A type for the individuals.  Not {@code null}.
     * @param shortNames A set of short names for the individuals.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(ProjectId projectId, OWLClass type, Set<String> shortNames) {
        super(projectId);
        this.type = checkNotNull(type);
        this.shortNames = checkNotNull(shortNames);
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param type A type for the individuals.  Not {@code null}.
     * @param shortNames A set of short names for the individuals.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(ProjectId projectId, Optional<OWLClass> type, Set<String> shortNames) {
        super(projectId);
        this.type = checkNotNull(type).orElse(null);
        this.shortNames = checkNotNull(shortNames);
    }

    /**
     * Gets the type for the individuals.
     * @return The type. Not {@code null}.
     */
    public Optional<OWLClass> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Gets the set of short names for the new individuals.
     * @return The set of names.  Not {@code null}.
     */
    public Set<String> getShortNames() {
        return shortNames;
    }
}
