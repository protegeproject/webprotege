package edu.stanford.bmir.protege.web.client.ui.obo;

import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.obo.*;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public interface OBOTermIdEditor extends ValueEditor<OBOTermId> {

    void setAvailableNamespaces(Set<OBONamespace> namespaces);
}
