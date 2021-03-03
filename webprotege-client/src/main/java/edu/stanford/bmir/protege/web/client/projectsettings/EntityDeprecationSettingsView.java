package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
public interface EntityDeprecationSettingsView extends IsWidget {

    void clear();

    void setDeprecatedClassesParent(@Nonnull OWLClassData data);

    Optional<OWLClass> getDeprecatedClassesParent();

    void setDeprecatedObjectPropertiesParent(@Nonnull OWLObjectPropertyData data);

    Optional<OWLObjectProperty> getDeprecatedObjectPropertiesParent();

    void setDeprecatedDataPropertiesParent(@Nonnull OWLDataPropertyData data);

    Optional<OWLDataProperty> getDeprecatedDataPropertiesParent();

    void setDeprecatedAnnotationPropertiesParent(@Nonnull OWLAnnotationPropertyData data);

    Optional<OWLAnnotationProperty> getDeprecatedAnnotationPropertiesParent();

    void setDeprecatedIndividualsParent(@Nonnull OWLClassData data);

    Optional<OWLClass> getDeprecatedIndividualsParent();

    Optional<IRI> getReplacedByPropertyIri();

    void setReplacedByProperty(@Nonnull OWLAnnotationPropertyData property);

    @Nonnull
    AcceptsOneWidget getReplacedByFilterContainer();
}
