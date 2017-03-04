package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.base.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public interface ItemStyleProvider {

    Optional<String> getItemStyle(Object item);
}
