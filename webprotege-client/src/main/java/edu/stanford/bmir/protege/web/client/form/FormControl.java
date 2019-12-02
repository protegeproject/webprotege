package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormControl extends ValueEditor<FormDataValue>, HasRequestFocus {

}
