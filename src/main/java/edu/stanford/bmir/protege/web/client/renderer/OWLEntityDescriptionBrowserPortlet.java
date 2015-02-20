package edu.stanford.bmir.protege.web.client.renderer;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class OWLEntityDescriptionBrowserPortlet extends AbstractOWLEntityPortlet {

    private HTML html;

    public OWLEntityDescriptionBrowserPortlet(Project project) {
        super(project);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if(entityData.isPresent()) {
            DispatchServiceManager.get().execute(new GetEntityRenderingAction(getProjectId(), entityData.get().getEntity()),
                                                 new AbstractDispatchServiceCallback<GetEntityRenderingResult>() {
                                                     @Override
                                                     public void handleSuccess(GetEntityRenderingResult result) {
                                                         html.setHTML(result.getRendering());
                                                     }
                                                 });

            setTitle(entityData.get().getBrowserText());
        }
    }

    @Override
    public void initialize() {
        addStyleName("web-protege-laf");
        html = new HTML();
        add(new ScrollPanel(html));
        addProjectEventHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(ObjectPropertyFrameChangedEvent.TYPE, new ObjectPropertyFrameChangedEventHandler() {
            @Override
            public void objectPropertyFrameChanged(ObjectPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(DataPropertyFrameChangedEvent.TYPE, new DataPropertyFrameChangedEventHandler() {
            @Override
            public void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {

            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {

            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
    }

    private void handleEntityChange(OWLEntity entity) {
        if(Optional.of(entity).equals(getSelectedEntity())) {
            handleAfterSetEntity(getSelectedEntityData());
        }
    }

}
