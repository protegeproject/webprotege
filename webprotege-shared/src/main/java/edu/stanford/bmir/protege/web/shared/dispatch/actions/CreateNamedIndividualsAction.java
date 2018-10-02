package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsAction extends AbstractCreateEntitiesAction<CreateNamedIndividualsResult, OWLNamedIndividual> {

    private ImmutableSet<OWLClass> types;

    private CreateNamedIndividualsAction() {
    }

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param type A type for the individuals.  Not {@code null}.
     * @param sourceText The input text that the individuals will be created from.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public CreateNamedIndividualsAction(@Nonnull ProjectId projectId,
                                        @Nonnull ImmutableSet<OWLClass> type,
                                        @Nonnull String sourceText,
                                        @Nonnull String langTag) {
        super(projectId, sourceText, langTag);
        this.types = checkNotNull(type);
    }

    /**
     * Gets the type for the individuals.
     * @return The type. Not {@code null}.
     */
    public ImmutableSet<OWLClass> getTypes() {
        return types;
    }


    @Override
    public String toString() {
        return toStringHelper("CreateNamedIndividualsAction")
                .add("types", types)
                .add("sourceText", getSourceText())
                .add("langTag", getLangTag())
                .toString();
    }
}
