package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
public interface RequiresOntologyChangeNotification extends Index {

    void applyChanges(@Nonnull ImmutableList<OntologyChange> changes);
}
