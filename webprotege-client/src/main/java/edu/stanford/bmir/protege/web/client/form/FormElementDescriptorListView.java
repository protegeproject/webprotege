package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface FormElementDescriptorListView extends IsWidget {

    void clear();

    void addFormElementDescriptorEditorView(@Nonnull FormElementDescriptorViewHolder view);

    // TODO ADD, REMOVE
}
