package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public abstract class RenderableResult implements Result, HasEntityDataProvider, HasBrowserTextMap, HasEntityBrowserText {

    private BrowserTextMap browserTextMap;

    /**
     * For serialization purposes only
     */
    protected RenderableResult() {

    }

    public RenderableResult(BrowserTextMap browserTextMap) {
        this.browserTextMap = browserTextMap;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }

    @Override
    public Optional<String> getBrowserText(OWLEntity entity) {
        return browserTextMap.getBrowserText(entity);
    }

    @Override
    public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
        final Optional<String> browserText = browserTextMap.getBrowserText(entity);
        if(!browserText.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(DataFactory.getOWLEntityData(entity, browserText.get()));
    }
}
