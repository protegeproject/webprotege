package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.server.index.Index;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-11
 */
public interface DependentIndex extends Index {

    @Nonnull
    Collection<Index> getDependencies();
}
