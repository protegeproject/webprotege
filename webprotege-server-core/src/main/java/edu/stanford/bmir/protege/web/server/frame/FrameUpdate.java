package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@AutoValue
public abstract class FrameUpdate {

    public static FrameUpdate get(PlainEntityFrame from, PlainEntityFrame to) {
        if(!from.getClass().equals(to.getClass())) {
            throw new RuntimeException("Frames must be of the same type");
        }
        if(from instanceof PlainClassFrame) {
            return get((PlainClassFrame) from, (PlainClassFrame) to);
        }
        if(from instanceof PlainNamedIndividualFrame) {
            return get((PlainNamedIndividualFrame) from, (PlainNamedIndividualFrame) to);
        }
        if(from instanceof PlainObjectPropertyFrame) {
            return get((PlainObjectPropertyFrame) from, (PlainObjectPropertyFrame) to);
        }
        if(from instanceof PlainDataPropertyFrame) {
            return get((PlainDataPropertyFrame) from, (PlainDataPropertyFrame) to);
        }
        if(from instanceof PlainAnnotationPropertyFrame) {
            return get((PlainAnnotationPropertyFrame) from, (PlainAnnotationPropertyFrame) to);
        }
        throw new RuntimeException("Unknown frame type: " + from);
    }

    public static FrameUpdate get(@Nonnull PlainClassFrame fromFrame,
                                  @Nonnull PlainClassFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }
    
    public static FrameUpdate get(@Nonnull PlainNamedIndividualFrame fromFrame,
                                  @Nonnull PlainNamedIndividualFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }
    
    public static FrameUpdate get(@Nonnull PlainObjectPropertyFrame fromFrame,
                                  @Nonnull PlainObjectPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public static FrameUpdate get(@Nonnull PlainDataPropertyFrame fromFrame,
                                  @Nonnull PlainDataPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public static FrameUpdate get(@Nonnull PlainAnnotationPropertyFrame fromFrame,
                                  @Nonnull PlainAnnotationPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public abstract PlainEntityFrame getFromFrame();

    public abstract PlainEntityFrame getToFrame();

}
