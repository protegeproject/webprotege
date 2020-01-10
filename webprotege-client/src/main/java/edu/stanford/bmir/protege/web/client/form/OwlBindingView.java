package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public interface OwlBindingView extends IsWidget {

    void clear();

    void setOwlClassBinding(boolean classBinding);

    boolean isOwlClassBinding();

    void setProperty(@Nonnull OWLEntityData entity);

    Optional<OWLEntityData> getEntity();
}
