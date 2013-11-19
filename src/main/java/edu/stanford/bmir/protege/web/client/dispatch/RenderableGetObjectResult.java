package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class RenderableGetObjectResult<T> extends GetObjectResult<T> implements HasEntityDataProvider, HasBrowserTextMap, HasEntityBrowserText {

    private BrowserTextMap browserTextMap;

    /**
     * For serialization only
     */
    protected RenderableGetObjectResult() {
        super();
    }

    public RenderableGetObjectResult(T object, BrowserTextMap browserTextMap) {
        super(object);
        this.browserTextMap = browserTextMap;
    }

    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }

    @Override
    public Optional<String> getBrowserText(OWLEntity entity) {
        return browserTextMap.getBrowserText(checkNotNull(entity));
    }



    @Override
    public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
        final Optional<String> browserText = browserTextMap.getBrowserText(checkNotNull(entity));
        if(!browserText.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(DataFactory.getOWLEntityData(entity, browserText.get()));
    }
}
