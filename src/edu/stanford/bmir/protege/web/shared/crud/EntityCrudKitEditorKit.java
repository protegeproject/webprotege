package edu.stanford.bmir.protege.web.shared.crud;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public interface EntityCrudKitEditorKit extends Serializable {

    EntityCrudKit getDescriptor();

    EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor();
}
