package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-09
 */
public interface EntityIsCriteriaView extends IsWidget {

    void setEntity(@Nonnull OWLEntityData entity);

    @Nonnull
    Optional<OWLEntityData> getEntityData();
}
