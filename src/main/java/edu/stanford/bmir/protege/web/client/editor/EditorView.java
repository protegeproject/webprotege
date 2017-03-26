package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorView<O> extends ValueEditor<O> {

    void setEntityDisplay(@Nonnull EntityDisplay entityDisplay);
}
