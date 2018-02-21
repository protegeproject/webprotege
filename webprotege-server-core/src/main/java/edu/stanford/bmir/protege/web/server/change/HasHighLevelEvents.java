package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface HasHighLevelEvents {

    List<ProjectEvent<?>> getHighLevelEvents();
}
