package edu.stanford.bmir.protege.web.client.projectmanager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 * <p>
 *     An interface to an object that can handle requests to download projects.
 * </p>
 */
public interface HasDownloadProjectRequestHandler {

    void setDownloadProjectRequestHandler(DownloadProjectRequestHandler handler);
}
