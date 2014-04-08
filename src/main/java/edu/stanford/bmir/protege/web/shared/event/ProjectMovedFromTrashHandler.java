package edu.stanford.bmir.protege.web.shared.event;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ProjectMovedFromTrashHandler {

    void handleProjectMovedFromTrash(ProjectMovedFromTrashEvent event);
}
