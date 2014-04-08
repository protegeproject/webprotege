package edu.stanford.bmir.protege.web.client.ui.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/08/2012
 */
public interface HasFormData<T> {

    T getData();
    
    void setData(T data);
}
