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

    public static <F extends Frame<S>, S extends OWLEntityData> FrameUpdate get(F from,
                                                                                F to) {
        if(!from.getClass().equals(to.getClass())) {
            throw new RuntimeException("Frames must be of the same type");
        }
        if(from instanceof ClassFrame) {
            return get((ClassFrame) from, (ClassFrame) to);
        }
        if(from instanceof NamedIndividualFrame) {
            return get((NamedIndividualFrame) from, (NamedIndividualFrame) to);
        }
        if(from instanceof ObjectPropertyFrame) {
            return get((ObjectPropertyFrame) from, (ObjectPropertyFrame) to);
        }
        if(from instanceof DataPropertyFrame) {
            return get((DataPropertyFrame) from, (DataPropertyFrame) to);
        }
        if(from instanceof AnnotationPropertyFrame) {
            return get((AnnotationPropertyFrame) from, (AnnotationPropertyFrame) to);
        }
        throw new RuntimeException("Unknown frame type: " + from);
    }

    public static FrameUpdate get(@Nonnull ClassFrame fromFrame,
                                  @Nonnull ClassFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }
    
    public static FrameUpdate get(@Nonnull NamedIndividualFrame fromFrame,
                                  @Nonnull NamedIndividualFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }
    
    public static FrameUpdate get(@Nonnull ObjectPropertyFrame fromFrame,
                                  @Nonnull ObjectPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public static FrameUpdate get(@Nonnull DataPropertyFrame fromFrame,
                                  @Nonnull DataPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public static FrameUpdate get(@Nonnull AnnotationPropertyFrame fromFrame,
                                  @Nonnull AnnotationPropertyFrame toFrame) {
        return new AutoValue_FrameUpdate(fromFrame, toFrame);
    }

    public abstract Frame<? extends OWLEntityData> getFromFrame();

    public abstract Frame<? extends OWLEntityData> getToFrame();

}
