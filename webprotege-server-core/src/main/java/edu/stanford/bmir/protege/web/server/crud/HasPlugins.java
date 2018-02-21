package edu.stanford.bmir.protege.web.server.crud;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public interface HasPlugins<T> {

    List<T> getPlugins();
}
