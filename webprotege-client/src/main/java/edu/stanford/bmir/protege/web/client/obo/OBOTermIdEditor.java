package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public interface OBOTermIdEditor extends ValueEditor<OBOTermId>, HasEnabled {

    void setAvailableNamespaces(Set<OBONamespace> namespaces);
}
