package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-01
 *
 * Contains and manages a list of {@link FormControl}s, the setting of control values and retrieval
 * of control values, nested page request changes etc.  If a form field is repeatable then the stack
 * will contain multiple form controls.  If a form field is non-repeatable then the stack will only
 * contain a single control.
 */
public interface FormControlStack extends ValueEditor<List<FormControlData>>, HasRequestFocus, HasPagination, HasFormRegionPagedChangedHandler {

    @Nonnull
    ImmutableList<FormRegionPageRequest> getPageRequests(FormSubject formSubject,
                                                         FormRegionId formRegionId);

    void setEnabled(boolean enabled);

    void forEachFormControl(@Nonnull Consumer<FormControl> controlConsumer);
}
