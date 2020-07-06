package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-17
 */
public interface EntityNameControlFilterView extends IsWidget {

    void setEntityMatchCriteria(@Nonnull CompositeRootCriteria criteria);

    Optional<OWLEntity> getEntity();

    void setEntity(OWLEntityData entity);

    void clear();
}
