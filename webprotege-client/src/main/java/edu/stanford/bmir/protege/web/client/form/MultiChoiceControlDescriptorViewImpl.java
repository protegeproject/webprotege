package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public class MultiChoiceControlDescriptorViewImpl extends Composite implements MultiChoiceControlDescriptorView {

    interface MultiChoiceControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, MultiChoiceControlDescriptorViewImpl> {

    }

    @UiField(provided = true)
    ValueListEditor<ChoiceDescriptor> choiceListEditor;

    @UiField(provided = true)
    static Counter counter = new Counter();

    private static MultiChoiceControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            MultiChoiceControlDescriptorViewImplUiBinder.class);

    @Inject
    public MultiChoiceControlDescriptorViewImpl(Provider<ChoiceDescriptorPresenter> choiceDescriptorPresenterProvider) {
        choiceListEditor = new ValueListFlexEditorImpl<>(choiceDescriptorPresenterProvider::get);
        choiceListEditor.setEnabled(true);
        choiceListEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setChoiceDescriptors(@Nonnull List<ChoiceDescriptor> choiceDescriptors) {
        choiceListEditor.setValue(choiceDescriptors);
    }

    @Nonnull
    @Override
    public ImmutableList<ChoiceDescriptor> getChoiceDescriptors() {
        return choiceListEditor.getValue().map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }
}
