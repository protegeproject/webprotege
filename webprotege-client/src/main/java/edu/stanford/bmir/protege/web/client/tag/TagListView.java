package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public interface TagListView extends IsWidget {

    void clear();

    void setTagViews(@Nonnull List<TagView> tagViews);

}
