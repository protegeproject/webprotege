package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
@ProjectSingleton
public interface ProjectAction<R extends Result> extends Action<R>, HasProjectId {

}
