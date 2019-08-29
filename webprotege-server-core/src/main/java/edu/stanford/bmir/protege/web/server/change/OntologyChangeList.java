package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.HasResult;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
    private final List<OntologyChange> changes;

    /**
     * Constructs a {@link OntologyChangeList} object which bundles together a list of ontology changes with
     * a main result.
     * @param changes The list of ontology changes.
     * @param result The main result.
     */
    private OntologyChangeList(@Nonnull List<OntologyChange> changes, @Nonnull R result) {
        this.result = checkNotNull(result);
        this.changes = ImmutableList.copyOf(changes);
    }

    /**
     * Gets the list of changes.
     * @return An immutable list of changes.
     */
    @Nonnull
    public List<OntologyChange> getChanges() {
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

        private final ImmutableList.Builder<OntologyChange> listBuilder = ImmutableList.builder();

        public Builder() {
        }

        public void add(@Nonnull OntologyChange change) {
            listBuilder.add(checkNotNull(change));
        }

        public boolean isEmpty() {
            return listBuilder.build().isEmpty();
        }

        public void addAxiom(@Nonnull OWLOntologyID ontologyId, @Nonnull OWLAxiom axiom) {
            add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(axiom)));
        }

        public void removeAxiom(@Nonnull OWLOntologyID ontologyId, @Nonnull OWLAxiom axiom) {
            add(RemoveAxiomChange.of(checkNotNull(ontologyId), checkNotNull(axiom)));
        }

        public Builder<R> addAll(@Nonnull List<? extends OntologyChange> changes) {
            listBuilder.addAll(checkNotNull(changes));
            return this;
        }

        public OntologyChangeList<R> build(@Nonnull R subject) {
            return new OntologyChangeList<>(listBuilder.build(), checkNotNull(subject));
        }


    }
}
