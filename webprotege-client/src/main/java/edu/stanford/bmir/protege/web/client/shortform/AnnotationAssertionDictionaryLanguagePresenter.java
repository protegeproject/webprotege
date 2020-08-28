package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class AnnotationAssertionDictionaryLanguagePresenter implements DictionaryLanguagePresenter {

    @Nonnull
    private final AnnotationAssertionDictionaryLanguageView view;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Inject
    public AnnotationAssertionDictionaryLanguagePresenter(@Nonnull AnnotationAssertionDictionaryLanguageView view,
                                                          @Nonnull ProjectId projectId,
                                                          @Nonnull DispatchServiceManager dispatch) {
        this.view = checkNotNull(view);
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setDictionaryLanguage(@Nonnull DictionaryLanguage language) {
        if(!(language instanceof AnnotationAssertionDictionaryLanguage)) {
            return;
        }
        AnnotationAssertionDictionaryLanguage dictionaryLanguage = (AnnotationAssertionDictionaryLanguage) language;
        IRI propertyIri = dictionaryLanguage.getAnnotationPropertyIri();
        OWLAnnotationProperty annotationProperty = DataFactory.getOWLAnnotationProperty(propertyIri);
        view.clearAnnotationProperty();
        view.setLanguageTag(dictionaryLanguage.getLang());
        dispatch.execute(new GetEntityRenderingAction(projectId, annotationProperty),
                         result -> view.setAnnotationProperty((OWLAnnotationPropertyData) result.getEntityData()));
    }

    @Override
    public Optional<DictionaryLanguage> getDictionaryLanguage() {
        return view.getAnnotationProperty()
            .map(OWLAnnotationPropertyData::getEntity)
            .map(OWLAnnotationProperty::getIRI)
            .map(iri -> AnnotationAssertionDictionaryLanguage.get(iri, view.getLanguageTag()));
    }

    @Override
    public void requestFocus() {
        view.requestFocus();
    }
}
