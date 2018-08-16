package edu.stanford.bmir.protege.web.client.individualslist;

import edu.stanford.bmir.protege.web.client.lang.PreferredLanguageManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.DisplayLanguageChangedEvent;
import edu.stanford.bmir.protege.web.client.lang.PreferredLanguageBrowserTextRenderer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

@Portlet(id = "portlets.IndividualsList", title = "Individuals by Class")
public class IndividualsListPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final IndividualsListPresenter presenter;

    private final PreferredLanguageManager preferredLanguageManager;

    /*
     * Retrieved from the project configuration. If it is set,
     * then the individuals list will always display the instances
     * of the preconfigured class.
     */
    // TODO: This needs fixing
    private Optional<OWLClass> preconfiguredClass = Optional.empty();

    @Inject
    public IndividualsListPortletPresenter(IndividualsListPresenter presenter,
                                           SelectionModel selectionModel,
                                           ProjectId projectId,
                                           PreferredLanguageBrowserTextRenderer preferredLanguageBrowserTextRenderer, PreferredLanguageManager preferredLanguageManager) {
        super(selectionModel, projectId, preferredLanguageBrowserTextRenderer);
        this.presenter = presenter;
        this.preferredLanguageManager = preferredLanguageManager;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        presenter.installActions(portletUi);
        presenter.start(portletUi, eventBus);
        presenter.setEntityDisplay(this);
        presenter.setDisplayLanguage(preferredLanguageManager.getDisplayLanguage());
        eventBus.addProjectEventHandler(getProjectId(),
                                        DisplayLanguageChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED,
                                        event -> presenter.setDisplayLanguage(event.getDisplayLanguage()));
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        Optional<OWLClass> selectedClass;
        if(preconfiguredClass.isPresent()) {
            selectedClass = preconfiguredClass;
        }
        else if(getSelectionModel().getLastSelectedClass().isPresent()) {
            selectedClass = Optional.of(getSelectionModel().getLastSelectedClass().get());
        }
        else {
            selectedClass = Optional.empty();
        }


        if(selectedClass.isPresent()) {
            presenter.setType(selectedClass.get());
        }
        else {
            presenter.clearType();
        }
    }
}
