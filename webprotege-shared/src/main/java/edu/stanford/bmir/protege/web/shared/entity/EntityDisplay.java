package edu.stanford.bmir.protege.web.shared.entity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2017
 */
public interface EntityDisplay {

    void setDisplayedEntity(@Nonnull Optional<OWLEntityData> entityData);
}
