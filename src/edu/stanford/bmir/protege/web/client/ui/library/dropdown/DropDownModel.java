package edu.stanford.bmir.protege.web.client.ui.library.dropdown;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/02/2012
 */
public interface DropDownModel<T> {

    int getSize();
    
    T getItemAt(int index);
    
    String getRendering(int index);

}
