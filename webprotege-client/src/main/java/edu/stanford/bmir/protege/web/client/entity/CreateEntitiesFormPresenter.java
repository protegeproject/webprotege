package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubjectDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-23
 */
public class CreateEntitiesFormPresenter {

    @Nonnull
    private final CreateEntitiesFormView view;

    @Nonnull
    private final FormPresenter formPresenter;

    private final UuidV4Provider uuidV4Provider;


    @Inject
    public CreateEntitiesFormPresenter(@Nonnull CreateEntitiesFormView view,
                                       @Nonnull FormPresenter formPresenter,
                                       @Nonnull UuidV4Provider uuidV4Provider) {
        this.view = checkNotNull(view);
        this.formPresenter = checkNotNull(formPresenter);
        this.uuidV4Provider = uuidV4Provider;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
//        container.setWidget(view);
//        String disc = uuidV4Provider.get();
//        FreshEntityIri freshEntityIri = FreshEntityIri.get("", "", disc, ImmutableSet.of());
//        FormId formId = FormId.get(uuidV4Provider.get());
//        FormDataDto dataDto = FormDataDto.get(FormSubjectDto.get(IRIData.get(freshEntityIri.getIri(),
//                                                       ImmutableMap.of())),
//                        FormDescriptorDto.get(formId, LanguageMap.empty(),
//                                              ImmutableList.of(),
//                                              FormSubjectFactoryDescriptor.get())
//                        );
//        formPresenter.displayForm(dataDto);
//        formPresenter.start(view.getFormContainer());
    }
}
