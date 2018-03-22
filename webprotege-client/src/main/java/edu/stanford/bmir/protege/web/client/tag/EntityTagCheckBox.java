package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public interface EntityTagCheckBox extends IsWidget {

    void setTag(@Nonnull Tag tag);

    @Nonnull
    Optional<Tag> getTag();

    void setSelected(boolean selected);

    boolean isSelected();
}
