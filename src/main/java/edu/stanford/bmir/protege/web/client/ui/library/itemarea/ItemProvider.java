package edu.stanford.bmir.protege.web.client.ui.library.itemarea;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public interface ItemProvider<T> {

    List<T> getItems();
    
    List<T> getItemsMatching(String itemString);
    
    List<T> getItemsMatchingExactly(String itemString);
    
    String getRendering(T item);

}
