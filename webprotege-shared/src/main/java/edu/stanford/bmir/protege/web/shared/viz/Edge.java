package edu.stanford.bmir.protege.web.shared.viz;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@GwtCompatible(serializable = true)
public interface Edge extends IsSerializable {

    @Nonnull
    OWLEntityData getHead();

    OWLEntityData getTail();

    String getRelationshipDescriptor();

    @Nonnull
    String getLabel();

    boolean isIsA();

    boolean isRelationship();

    Optional<OWLEntityData> getLabellingEntity();
}
