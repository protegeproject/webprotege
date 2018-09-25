package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.bulkop.ReplaceAnnotationValuesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
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
        return "Replace annotation values";
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "Replaces annotation values that match a regular expression";
    }

    @Nonnull
    @Override
    public IsWidget getView() {
        return view;
    }

    @Override
    public boolean isDataWellFormed() {
        return view.getAnnotationProperty().isPresent();
    }

    @Nonnull
    @Override
    public Optional<? extends Action> createAction(@Nonnull ImmutableSet<OWLEntity> entities) {
        return view.getAnnotationProperty().map(prop -> new ReplaceAnnotationValuesAction(projectId,
                                                                                          entities,
                                                                                          prop.getEntity(),
                                                                                          view.getMatch(),
                                                                                          view.getReplacement()));
    }

    @Override
    public void displayErrorMessage() {

    }
}
