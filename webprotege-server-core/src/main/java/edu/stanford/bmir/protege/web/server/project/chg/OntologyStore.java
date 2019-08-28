package edu.stanford.bmir.protege.web.server.project.chg;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@ProjectSingleton
abstract class OntologyStore {

    abstract List<OntologyChange> applyChanges(@Nonnull List<OntologyChange> changes);
}
