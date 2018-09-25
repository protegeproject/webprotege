package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public interface BulkEditOperationPresenter {

    String getTitle();

    String getExecuteButtonText();

    String getHelpMessage();

    @Nonnull
    IsWidget getView();

    boolean isDataWellFormed();

    @Nonnull
    Optional<? extends Action> createAction(@Nonnull ImmutableSet<OWLEntity> entities);

    void displayErrorMessage();
}
