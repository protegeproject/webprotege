package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectiveDetailsDefaultValueProvider implements Provider<PerspectiveDetails> {

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public PerspectiveDetailsDefaultValueProvider(@Nonnull UuidV4Provider uuidV4Provider) {
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    @Override
    public PerspectiveDetails get() {
        PerspectiveId perspectiveId = PerspectiveId.get(uuidV4Provider.get());
        return PerspectiveDetails.get(perspectiveId, LanguageMap.empty(), true, null);
    }
}
