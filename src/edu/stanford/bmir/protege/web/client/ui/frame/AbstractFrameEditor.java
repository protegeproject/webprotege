package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.rpc.GetRendering;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingResponse;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2013
 */
public abstract class AbstractFrameEditor<O> extends SimplePanel implements ValueEditor<O> {

    private ProjectId projectId;

    /**
     * Creates an empty panel that uses a DIV for its contents.
     */
    protected AbstractFrameEditor(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    final public void setValue(final O object) {
        if (object instanceof HasSignature) {
            RenderingServiceManager renderingServiceManager = RenderingServiceManager.getManager();
            final HasSignature hasSignature = (HasSignature) object;
            renderingServiceManager.execute(new GetRendering(projectId, hasSignature), new AsyncCallback<GetRenderingResponse>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(GetRenderingResponse result) {
                    setValue(object, result);
                }
            });
        }
        else {
            setValue(object, new HasEntityDataProvider() {
                @Override
                public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
                    return Optional.absent();
                }
            });
        }
    }

    public abstract void setValue(O object, HasEntityDataProvider entityDataProvider);
}
