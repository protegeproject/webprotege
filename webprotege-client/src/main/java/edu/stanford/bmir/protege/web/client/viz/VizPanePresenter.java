package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.EditorPanePresenter;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2018
 */
public class VizPanePresenter implements EditorPanePresenter {

    @Nonnull
    private final VizPresenter presenter;

    @Inject
    public VizPanePresenter(@Nonnull VizPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Nonnull
    @Override
    public String getCaption() {
        return "Entity Graph";
    }

    @Nonnull
    @Override
    public String getAdditionalStyles() {
        return "wp-btn-g--viz";
    }

    @Nonnull
    @Override
    public BuiltInAction getRequiredAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        presenter.start(container);
    }

    @Override
    public void setEntity(@Nonnull OWLEntity entity) {
        presenter.displayEntity(entity);
    }

    @Override
    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        presenter.setHasBusy(hasBusy);
    }

    @Override
    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        presenter.setEntityDisplay(entityDisplay);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
