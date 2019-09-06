package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.change.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-04
 */
public class AxiomChangeHandler {

    private final OntologyChangeVisitor visitor = new OntologyChangeVisitor() {
        @Override
        public void visit(@Nonnull AddAxiomChange addAxiomChange) {
            addAxiomChangeConsumer.accept(addAxiomChange);
            axiomChangeConsumer.accept(addAxiomChange);
        }

        @Override
        public void visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
            removeAxiomChangeConsumer.accept(removeAxiomChange);
            axiomChangeConsumer.accept(removeAxiomChange);
        }
    };

    @Nonnull
    private Consumer<AddAxiomChange> addAxiomChangeConsumer = change -> {};

    @Nonnull
    private Consumer<RemoveAxiomChange> removeAxiomChangeConsumer = change -> {};

    @Nonnull
    private Consumer<AxiomChange> axiomChangeConsumer = axiomChange -> {};

    public AxiomChangeHandler() {
    }


    public void setAddAxiomChangeConsumer(@Nonnull Consumer<AddAxiomChange> addAxiomChangeConsumer) {
        this.addAxiomChangeConsumer = checkNotNull(addAxiomChangeConsumer);
    }

    public void setRemoveAxiomChangeConsumer(@Nonnull Consumer<RemoveAxiomChange> removeAxiomChangeConsumer) {
        this.removeAxiomChangeConsumer = checkNotNull(removeAxiomChangeConsumer);
    }

    public void setAxiomChangeConsumer(@Nonnull Consumer<AxiomChange> axiomChangeConsumer) {
        this.axiomChangeConsumer = checkNotNull(axiomChangeConsumer);
    }

    public void handleOntologyChanges(List<OntologyChange> changes) {
        changes.forEach(chg -> chg.accept(visitor));
    }
}
