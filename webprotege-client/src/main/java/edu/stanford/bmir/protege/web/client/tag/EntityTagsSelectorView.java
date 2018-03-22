package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2018
 */
public interface EntityTagsSelectorView extends IsWidget, HasBusy {

    void clear();

    void addCheckBox(EntityTagCheckBox tagCheckBox);

    List<EntityTagCheckBox> getCheckBoxes();
}
