package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class ChangeApplicationResult<S> implements HasSubject<S> {

    @Nonnull
    private final RenameMap renameMap;

    @Nonnull
    private final List<OntologyChange> changeList;

    @Nonnull
    private final S subject;

    public ChangeApplicationResult(@Nonnull S subject,
                                   @Nonnull List<OntologyChange> changeList,
                                   @Nonnull RenameMap renameMap) {
        this.subject = checkNotNull(subject);
        this.changeList = ImmutableList.copyOf(checkNotNull(changeList));
        this.renameMap = checkNotNull(renameMap);
    }

    @Nonnull
    public RenameMap getRenameMap() {
        return renameMap;
    }

    @Nonnull
    public List<OntologyChange> getChangeList() {
        return changeList;
    }

    @Nonnull
    @Override
    public S getSubject() {
        return subject;
    }
}
