package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
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

    void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus);

    boolean isDataWellFormed();

    @Nonnull
    Optional<? extends Action<?>> createAction(@Nonnull ImmutableSet<OWLEntity> entities);

    void displayErrorMessage();
}
