package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
public class EntityDeprecationSettingsPresenter {

    @Nonnull
    private final EntityDeprecationSettingsView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public EntityDeprecationSettingsPresenter(@Nonnull EntityDeprecationSettingsView view,
                                              @Nonnull DispatchServiceManager dispatch, @Nonnull ProjectId projectId) {
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setValue(@Nonnull EntityDeprecationSettings settings) {
        try {
            view.clear();
            dispatch.beginBatch();
            settings.getReplacedByPropertyIri()
                    .ifPresent(iri -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, DataFactory.getOWLAnnotationProperty(iri)),
                                                                      result -> view.setReplacedByProperty((OWLAnnotationPropertyData) result.getEntityData()));
                    });
            settings.getDeprecatedClassesParent()
                    .ifPresent(classesParent -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, classesParent),
                                         result -> view.setDeprecatedClassesParent((OWLClassData) result.getEntityData()));
                    });
            settings.getDeprecatedObjectPropertiesParent()
                    .ifPresent(objectPropertiesParent -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, objectPropertiesParent),
                                         result -> view.setDeprecatedObjectPropertiesParent((OWLObjectPropertyData) result.getEntityData()));
                    });
            settings.getDeprecatedDataPropertiesParent()
                    .ifPresent(dataPropertiesParent -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, dataPropertiesParent),
                                         result -> view.setDeprecatedDataPropertiesParent((OWLDataPropertyData) result.getEntityData()));
                    });
            settings.getDeprecatedAnnotationPropertiesParent()
                    .ifPresent(annotationPropertiesParent -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, annotationPropertiesParent),
                                         result -> view.setDeprecatedAnnotationPropertiesParent((OWLAnnotationPropertyData) result.getEntityData()));
                    });
            settings.getDeprecatedIndividualsParent()
                    .ifPresent(individualsParent -> {
                        dispatch.execute(new GetEntityRenderingAction(projectId, individualsParent),
                                         result -> view.setDeprecatedIndividualsParent((OWLClassData) result.getEntityData()));
                    });

        } finally {
            dispatch.executeCurrentBatch();
        }

    }

    public EntityDeprecationSettings getValue() {
        return EntityDeprecationSettings.get(
                view.getReplacedByPropertyIri().orElse(null),
                view.getDeprecatedClassesParent().orElse(null),
                view.getDeprecatedObjectPropertiesParent().orElse(null),
                view.getDeprecatedDataPropertiesParent().orElse(null),
                view.getDeprecatedAnnotationPropertiesParent().orElse(null),
                view.getDeprecatedIndividualsParent().orElse(null)
        );
    }
}
