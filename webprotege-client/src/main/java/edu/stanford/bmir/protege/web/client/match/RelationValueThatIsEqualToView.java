package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public interface RelationValueThatIsEqualToView extends IsWidget {

    void setValue(@Nonnull OWLPrimitiveData value);

    @Nonnull
    Optional<OWLPrimitiveData> getValue();
}
