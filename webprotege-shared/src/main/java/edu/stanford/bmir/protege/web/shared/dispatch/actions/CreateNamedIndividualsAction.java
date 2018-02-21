package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsAction extends AbstractHasProjectAction<CreateNamedIndividualsResult> {

    private OWLClass type;

    private String sourceText;

    private CreateNamedIndividualsAction() {
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param sourceText The input text that the individuals will be created from.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(@Nonnull ProjectId projectId,
                                        @Nonnull String sourceText) {
        super(projectId);
        this.type = null;
        this.sourceText = checkNotNull(sourceText);
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param type A type for the individuals.  Not {@code null}.
     * @param sourceText The input text that the individuals will be created from.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(@Nonnull ProjectId projectId,
                                        @Nonnull OWLClass type,
                                        @Nonnull String sourceText) {
        super(projectId);
        this.type = checkNotNull(type);
        this.sourceText = checkNotNull(sourceText);
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param type A type for the individuals.  Not {@code null}.
     * @param sourceText The input text that the individuals will be created from.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(@Nonnull ProjectId projectId,
                                        @Nonnull Optional<OWLClass> type,
                                        @Nonnull String sourceText) {
        super(projectId);
        this.type = type.orElse(null);
        this.sourceText = checkNotNull(sourceText);
    }


    /**
     * Gets the type for the individuals.
     * @return The type. Not {@code null}.
     */
    public Optional<OWLClass> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Gets the text that the individuals will be created from
     * @return The text
     */
    @Nonnull
    public String getSourceText() {
        return sourceText;
    }
}
