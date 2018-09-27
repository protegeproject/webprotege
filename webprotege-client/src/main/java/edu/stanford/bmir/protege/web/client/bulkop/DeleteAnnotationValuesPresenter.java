package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class DeleteAnnotationValuesPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final DeleteAnnotationValuesView view;

    public DeleteAnnotationValuesPresenter(@Nonnull DeleteAnnotationValuesView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public String getTitle() {
        return "Delete annotations";
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "Deletes annotations that match the specified property, value and language.";
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        container.setWidget(view);
    }

    @Override
    public boolean isDataWellFormed() {
        return false;
    }

    @Nonnull
    @Override
    public Optional<? extends Action<?>> createAction(@Nonnull ImmutableSet<OWLEntity> entities) {
        return Optional.empty();
    }

    @Override
    public void displayErrorMessage() {

    }
}
