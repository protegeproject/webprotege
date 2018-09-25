package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
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

    @Inject
    public ReplaceAnnotationValuesPresenter(@Nonnull ReplaceAnnotationValuesView view) {
        this.view = checkNotNull(view);
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
        return Optional.empty();
    }

    @Override
    public void displayErrorMessage() {

    }
}
