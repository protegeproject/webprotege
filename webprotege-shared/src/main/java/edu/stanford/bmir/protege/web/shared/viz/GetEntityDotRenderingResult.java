package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityDotRenderingResult implements Result {

    public static GetEntityDotRenderingResult get(@Nonnull String rendering,
                                                  @Nonnull EntityGraph entityGraph) {
        return new AutoValue_GetEntityDotRenderingResult(rendering, entityGraph);
    }

    @Nonnull
    public abstract String getRendering();

    @Nonnull
    public abstract EntityGraph getEntityGraph();

}
