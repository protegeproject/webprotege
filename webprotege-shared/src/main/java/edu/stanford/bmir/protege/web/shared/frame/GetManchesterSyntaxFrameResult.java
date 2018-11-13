package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetManchesterSyntaxFrameResult implements Result {

    @Nonnull
    public static GetManchesterSyntaxFrameResult get(@Nonnull OWLEntityData frameSubject,
                                                     @Nonnull String frameRendering) {
        return new AutoValue_GetManchesterSyntaxFrameResult(frameSubject,
                                                            frameRendering);
    }

    @Nonnull
    public abstract OWLEntityData getFrameSubject();

    @Nonnull
    public abstract String getFrameManchesterSyntax();
}
