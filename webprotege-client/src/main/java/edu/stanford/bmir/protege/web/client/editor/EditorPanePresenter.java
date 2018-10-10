package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2018
 */
public interface EditorPanePresenter extends HasDispose {


    @Nonnull
    String getCaption();

    @Nonnull
    String getAdditionalStyles();

    @Nonnull
    BuiltInAction getRequiredAction();

    void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus);

    void setEntity(@Nonnull OWLEntity entity);

    void setHasBusy(@Nonnull HasBusy hasBusy);

    void setEntityDisplay(@Nonnull EntityDisplay entityDisplay);

    @Override
    void dispose();

    boolean isActive();
}
