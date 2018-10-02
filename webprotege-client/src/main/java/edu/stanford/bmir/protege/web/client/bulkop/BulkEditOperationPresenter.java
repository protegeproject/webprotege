package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
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

    @Nonnull
    String getDefaultCommitMessage(@Nonnull ImmutableSet<? extends OWLEntityData> entities);

    String getHelpMessage();

    void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus);

    boolean isDataWellFormed();

    /**
     * Create the action that will perform the bulk operation when executed.
     * @param entities The set of entities that the bulk operation will be executed on.
     * @param commitMessage A commit message that should be contained in the change log that describes
     *                      the change.
     * @return The action, or empty if the action could not be created.
     */
    @Nonnull
    Optional<? extends Action<?>> createAction(@Nonnull ImmutableSet<OWLEntity> entities,
                                               @Nonnull String commitMessage);

    void displayErrorMessage();
}
