package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public FormsManagerPresenter(@Nonnull FormsManagerView view,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = checkNotNull(view);
        this.settingsPresenter = settingsPresenter;
        this.formDescriptorPresenter = formDescriptorPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
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
                                                                       new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI()),
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
                                                               ),
                                                               FormElementDescriptor.get(
                                                                       FormElementId.get("TheChoices"),
                                                                       null,
                                                                       LanguageMap.empty(),
                                                                       ElementRun.START,
                                                                       new ChoiceFieldDescriptor(ChoiceFieldType.RADIO_BUTTON,
                                                                                                 Collections.emptyList(),
                                                                                                 Collections.emptyList()),
                                                                       Repeatability.REPEATABLE_HORIZONTALLY,
                                                                       Optionality.REQUIRED,
                                                                       LanguageMap.empty(),
                                                                       Collections.emptyMap()
                                                               )
                                                       ),
                                                       Optional.empty());
        try {
            dispatchServiceManager.beginBatch();
            formDescriptorPresenter.setFormDescriptor(descriptor);
        } finally {
            dispatchServiceManager.executeCurrentBatch();
        }

    }
}
