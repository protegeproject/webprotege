package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.HasResult;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     A list of ontology changes plus a result of applying the changes.
 * </p>
 */
public class OntologyChangeList<R> implements HasResult<R> {

    @Nonnull
    private final R result;

    @Nonnull
    private final List<OWLOntologyChange> changes;

    /**
     * Constructs a {@link OntologyChangeList} object which bundles together a list of ontology changes with
     * a main result.
     * @param changes The list of ontology changes.
     * @param result The main result.
     */
    private OntologyChangeList(@Nonnull List<OWLOntologyChange> changes, @Nonnull R result) {
        this.result = checkNotNull(result);
        this.changes = ImmutableList.copyOf(changes);
    }

    /**
     * Gets the list of changes.
     * @return An immutable list of changes.
     */
    @Nonnull
    public List<OWLOntologyChange> getChanges() {
        return changes;
    }

    /**
     * Gets the main result of generating the changes.
     * @return The result.  Not {@code null}.
     */
    @Nonnull
    @Override
    public R getResult() {
        return result;
    }

    public static <R> Builder<R> builder() {
        return new Builder<>();
    }

    /**
     * A builder for building a {@link OntologyChangeList} object.
     * @param <R> The type of result.
     */
    public static class Builder<R> {

        private final ImmutableList.Builder<OWLOntologyChange> listBuilder = ImmutableList.builder();

        public Builder() {
        }

        private void add(@Nonnull OWLOntologyChange change) {
            listBuilder.add(checkNotNull(change));
        }

        public boolean isEmpty() {
            return listBuilder.build().isEmpty();
        }

        public void addAxiom(@Nonnull OWLOntology ontology, @Nonnull OWLAxiom axiom) {
            add(new AddAxiom(checkNotNull(ontology), checkNotNull(axiom)));
        }

        public void removeAxiom(@Nonnull OWLOntology ontology, @Nonnull OWLAxiom axiom) {
            add(new RemoveAxiom(checkNotNull(ontology), checkNotNull(axiom)));
        }

        public Builder<R> addAll(@Nonnull List<? extends OWLOntologyChange> changes) {
            listBuilder.addAll(checkNotNull(changes));
            return this;
        }

        public OntologyChangeList<R> build(@Nonnull R subject) {
            return new OntologyChangeList<>(listBuilder.build(), checkNotNull(subject));
        }


    }
}
