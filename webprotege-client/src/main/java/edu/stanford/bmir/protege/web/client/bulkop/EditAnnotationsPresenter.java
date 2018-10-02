package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
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
public class EditAnnotationsPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final EditAnnotationsView view;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public EditAnnotationsPresenter(@Nonnull EditAnnotationsView view, @Nonnull ProjectId projectId) {
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

    @Nonnull
    @Override
    public String getDefaultCommitMessage(@Nonnull ImmutableSet<? extends OWLEntityData> entities) {
        return view.getOperation().getPrintName()
                + " annotations on "
                + BulkOpMessageFormatter.sortAndFormat(entities);
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
    public Optional<EditAnnotationsAction> createAction(@Nonnull ImmutableSet<OWLEntity> entities, String commitMessage) {
        return Optional.of(new EditAnnotationsAction(projectId,
                                                     entities,
                                                     view.getOperation(),
                                                     view.getAnnotationProperty(),
                                                     view.getLexcialValueExpression(),
                                                     view.isLexicalValueExpressionRegEx(),
                                                     view.getLangTagExpression(),
                                                     view.getNewAnnotationData(),
                                                     commitMessage));
    }

    @Override
    public void displayErrorMessage() {

    }
}
