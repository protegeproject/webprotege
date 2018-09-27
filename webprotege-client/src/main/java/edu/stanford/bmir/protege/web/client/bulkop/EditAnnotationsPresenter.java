package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.bulkop.ReplaceAnnotationValuesAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class ReplaceAnnotationValuesPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final ReplaceAnnotationValuesView view;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ReplaceAnnotationValuesPresenter(@Nonnull ReplaceAnnotationValuesView view, @Nonnull ProjectId projectId) {
        this.view = checkNotNull(view);
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public String getTitle() {
        return "Edit annotations";
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "Replaces, adds or deletes annotation values that match a given property, value and language tag";
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        container.setWidget(view);
    }

    @Override
    public boolean isDataWellFormed() {
        return true;
    }

    @Nonnull
    @Override
    public Optional<ReplaceAnnotationValuesAction> createAction(@Nonnull ImmutableSet<OWLEntity> entities) {
        return Optional.of(new ReplaceAnnotationValuesAction(projectId,
                                                             entities,
                                                             view.getAnnotationProperty().map(OWLAnnotationPropertyData::getEntity),
                                                             view.getMatch(),
                                                             view.isRegEx(),
                                                             view.getReplacement()));
    }

    @Override
    public void displayErrorMessage() {

    }
}
