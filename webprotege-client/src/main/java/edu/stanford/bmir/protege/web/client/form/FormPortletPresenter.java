package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent.CLASS_FRAME_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@Portlet(id = "portlets.form", title = "Form", tooltip = "Displays a form")
public class FormPortletPresenter extends AbstractWebProtegePortletPresenter {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormStackPresenter entityFormStackPresenter;

    @Inject
    public FormPortletPresenter(SelectionModel selectionModel,
                                @Nonnull ProjectId projectId,
                                DisplayNameRenderer displayNameRenderer,
                                @Nonnull EntityFormStackPresenter entityFormStackPresenter) {
        super(selectionModel, projectId, displayNameRenderer);
        this.projectId = projectId;
        this.entityFormStackPresenter = entityFormStackPresenter;
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        if(entityData.isPresent()) {
            setNothingSelectedVisible(false);
            entityFormStackPresenter.setEntity(entityData.get());
        }
        else {
            setNothingSelectedVisible(true);
        }
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addAction(new PortletAction("Expand all", "wp-btn-g--expand-all", entityFormStackPresenter::expandAllFields));
        portletUi.addAction(new PortletAction("Collapse all", "wp-btn-g--collapse-all", entityFormStackPresenter::collapseAllFields));
        setDisplaySelectedEntityNameAsSubtitle(true);
        entityFormStackPresenter.start(portletUi);
        entityFormStackPresenter.setHasBusy(portletUi);
        entityFormStackPresenter.setSelectedFormIdStash(new SelectedFormIdStash(portletUi));
        entityFormStackPresenter.setLanguageFilterStash(new FormLanguageFilterStash(portletUi));
        handleAfterSetEntity(getSelectedEntity());
    }
}
