package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class FixedChangeListGenerator<R> implements ChangeListGenerator<R> {


    @Nonnull
    private final R result;

    @Nonnull
    private final List<OWLOntologyChange> fixedChangeList;

    public FixedChangeListGenerator(@Nonnull List<OWLOntologyChange> fixedChangeList, @Nonnull R result) {
        this.result = result;
        this.fixedChangeList = ImmutableList.copyOf(fixedChangeList);
    }

    public static <S> FixedChangeListGenerator<S> get(List<OWLOntologyChange> changes, @Nonnull S result) {
        return new FixedChangeListGenerator<>(changes, result);
    }

    @Override
    public OntologyChangeList<R> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<R> builder = new OntologyChangeList.Builder<>();
        builder.addAll(fixedChangeList);
        return builder.build(result);
    }

    @Override
    public R getRenamedResult(R result, RenameMap renameMap) {
        return result;
    }
}
