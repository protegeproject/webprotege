package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorView<O> extends ValueEditor<O> {

    void setEntityDisplay(@Nonnull EntityDisplay entityDisplay);

    default void setTags(@Nonnull Collection<Tag> tags) {

    }
}
