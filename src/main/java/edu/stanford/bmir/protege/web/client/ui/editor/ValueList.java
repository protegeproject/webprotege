package edu.stanford.bmir.protege.web.client.ui.editor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/06/2013
 */
public class ValueList<O> {

    private List<O> values = new ArrayList<O>();

    public ValueList(List<O> values) {
        this.values.addAll(values);
    }

    public List<O> getValues() {
        return new ArrayList<O>(values);
    }

}
