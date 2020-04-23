package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;

import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
public interface FormControlStackBackingEditor extends ValueEditor<List<FormControlData>>, HasRequestFocus {

    void forEachFormControl(Consumer<FormControl> consumer);
}
