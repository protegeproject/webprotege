package edu.stanford.bmir.protege.web.client.editor;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormElementDescriptorEditorPresenter {

    @Nonnull
    private final FormElementDescriptorEditorView view;

    @Nonnull
    private final ImmutableList<FormFieldDescriptorEditorPresenterFactory> fieldPresenterFactories;

    @Nonnull
    private final Map<String, FormFieldDescriptorEditorPresenter> fieldType2FieldPresenter = new HashMap<>();

    @Nonnull
    private FormFieldDescriptorEditorPresenter currentFieldPresenter;


    @Inject
    public FormElementDescriptorEditorPresenter(@Nonnull FormElementDescriptorEditorView view,
                                                @Nonnull ImmutableList<FormFieldDescriptorEditorPresenterFactory> fieldPresenterFactories) {
        this.view = checkNotNull(view);
        this.fieldPresenterFactories = checkNotNull(fieldPresenterFactories);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setFormElementDescriptor(@Nonnull FormElementDescriptor descriptor) {
        String elementId = descriptor.getId().getId();
        view.setFormElementId(elementId);

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());

        Optional<OWLProperty> owlProperty = descriptor.getOwlProperty();
        view.clearOwlProperty();
        owlProperty.ifPresent(view::setOwlProperty);

        FormFieldDescriptor formFieldDescriptor = descriptor.getFieldDescriptor();
        String type = formFieldDescriptor.getAssociatedType();
        FormFieldDescriptorEditorPresenter fieldPresenter = getOrCreateFieldPresenter(type);
        fieldPresenter.start(view.getFieldEditorContainer());
        fieldPresenter.setFormFieldDescriptor(formFieldDescriptor);
        this.currentFieldPresenter = fieldPresenter;
    }

    public FormElementDescriptor getFormElementDescriptor() {
        return null;
    }

    @Nonnull
    public FormFieldDescriptorEditorPresenter getOrCreateFieldPresenter(String type) {
        FormFieldDescriptorEditorPresenter presenter = fieldType2FieldPresenter.get(type);
        if(presenter == null) {
            presenter = fieldPresenterFactories.stream()
                                                .filter(factory -> factory.getDescriptorEditorType().equalsIgnoreCase(type))
                                               .map(FormFieldDescriptorEditorPresenterFactory::create)
                                                .findFirst()
                    .get();
            fieldType2FieldPresenter.put(type, presenter);
        }
        return presenter;
    }
}
