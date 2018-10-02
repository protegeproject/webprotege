package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.bulkop.SetAnnotationValueAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
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
public class SetAnnotationValuePresenter implements BulkEditOperationPresenter {

    private final ProjectId projectId;

    private final SetAnnotationValueView view;

    @Inject
    public SetAnnotationValuePresenter(@Nonnull ProjectId projectId,
                                       @Nonnull SetAnnotationValueView view) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        container.setWidget(view);
    }

    @Override
    public boolean isDataWellFormed() {
        return view.getProperty().isPresent() && view.getValue().isPresent();
    }

    @Override
    public String getTitle() {
        return "Set annotation value";
    }

    @Override
    public String getExecuteButtonText() {
        return "Set annotation value";
    }

    @Nonnull
    @Override
    public String getDefaultCommitMessage(@Nonnull ImmutableSet<? extends OWLEntityData> entities) {
        return "Set "
                + view.getProperty().map(HasBrowserText::getBrowserText).orElse("property")
                + " value to "
                + view.getValue().map(HasBrowserText::getBrowserText).orElse("")
                + " on "
                + BulkOpMessageFormatter.sortAndFormat(entities);
    }

    @Override
    public String getHelpMessage() {
        return "Sets the value for a specific annotation property to a specific value for the selected entities";
    }

    @Override
    public void displayErrorMessage() {
        if(!getProperty().isPresent()) {

        }
        else if(!getValue().isPresent()) {

        }
    }

    @Nonnull
    private Optional<OWLAnnotationProperty> getProperty() {
        return view.getProperty().map(OWLAnnotationPropertyData::getEntity);
    }

    @Nonnull
    private Optional<OWLAnnotationValue> getValue() {
        return view.getValue()
                .map(OWLPrimitiveData::getObject)
                .filter(val -> val instanceof OWLAnnotationValue)
                .map(val -> (OWLAnnotationValue) val);
    }

    @Nonnull
    @Override
    public Optional<SetAnnotationValueAction> createAction(@Nonnull ImmutableSet<OWLEntity> entities, @Nonnull String commitMessage) {
        return getProperty().flatMap(prop -> getValue().map(val -> createAction(entities, prop, val, commitMessage)));
    }

    private SetAnnotationValueAction createAction(@Nonnull ImmutableSet<OWLEntity> entities,
                                                  @Nonnull OWLAnnotationProperty prop,
                                                  @Nonnull OWLAnnotationValue val,
                                                  @Nonnull String commitMessage) {
        return new SetAnnotationValueAction(projectId,
                                            entities,
                                            prop,
                                            val,
                                            commitMessage);
    }
}
