package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Oct 2018
 */
public interface LargeGraphMessageView extends IsWidget {

    void setDisplayMessage(@Nonnull OWLEntityData entity,
                           int nodeCount,
                           int edgeCount);

    void setDisplayGraphHandler(@Nonnull Runnable handler);

    void setDisplaySettingsHandler(@Nonnull Runnable handler);
}
