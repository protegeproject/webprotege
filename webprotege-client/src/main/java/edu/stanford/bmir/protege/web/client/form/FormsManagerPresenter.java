package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
@ProjectSingleton
public class FormsManagerPresenter implements Presenter {

    @Nonnull
    private final FormsManagerView view;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Inject
    public FormsManagerPresenter(@Nonnull FormsManagerView view,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormDescriptorPresenter formDescriptorPresenter) {
        this.view = checkNotNull(view);
        this.settingsPresenter = settingsPresenter;
        this.formDescriptorPresenter = formDescriptorPresenter;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
//        settingsPresenter.setBusy(container, true);
        settingsPresenter.setSettingsTitle("Forms");
        AcceptsOneWidget formsManagerContainer = settingsPresenter.addSection("Project forms");
        formsManagerContainer.setWidget(view);
        AcceptsOneWidget descriptorContainer = view.getFormDescriptorContainer();
        formDescriptorPresenter.start(descriptorContainer, eventBus);
        createBlank();
    }


    private void createBlank() {
        FormDescriptor descriptor = new FormDescriptor(FormId.get("MyForm"),
                                                       LanguageMap.of("en", "My form"),
                                                       Arrays.asList(
                                                               FormElementDescriptor.get(
                                                                       FormElementId.get("FirstName"),
                                                                       null,
                                                                       LanguageMap.of("en", "First name"),
                                                                       ElementRun.START,
                                                                       new TextFieldDescriptor(
                                                                               LanguageMap.empty(),
                                                                               StringType.SIMPLE_STRING,
                                                                               LineMode.SINGLE_LINE,
                                                                               "",
                                                                               LanguageMap.empty()
                                                                       ),
                                                                       Repeatability.REPEATABLE_VERTICALLY,
                                                                       Optionality.REQUIRED,
                                                                       LanguageMap.empty(),
                                                                       Collections.emptyMap()
                                                               ),
                                                               FormElementDescriptor.get(
                                                                       FormElementId.get("TheThingy"),
                                                                       null,
                                                                       LanguageMap.of("en", "The number"),
                                                                       ElementRun.START,
                                                                       new NumberFieldDescriptor(
                                                                               "###.##",
                                                                               NumberFieldRange.all(),
                                                                               NumberFieldType.PLAIN,
                                                                               5,
                                                                               LanguageMap.empty()
                                                                       ),
                                                                       Repeatability.NON_REPEATABLE,
                                                                       Optionality.OPTIONAL,
                                                                       LanguageMap.empty(),
                                                                       Collections.emptyMap()
                                                               )
                                                       ),
                                                       Optional.empty());
        formDescriptorPresenter.setFormDescriptor(descriptor);
    }
}
