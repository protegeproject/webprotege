package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionPathDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class AnnotationAssertionPathDictionaryLanguagePresenter implements DictionaryLanguagePresenter {

    @Nonnull
    private final AnnotationAssertionPathDictionaryLanguageView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public AnnotationAssertionPathDictionaryLanguagePresenter(@Nonnull AnnotationAssertionPathDictionaryLanguageView view,
                                                              @Nonnull DispatchServiceManager dispatch,
                                                              @Nonnull ProjectId projectId) {
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setDictionaryLanguage(@Nonnull DictionaryLanguage language) {
        if(!(language instanceof AnnotationAssertionPathDictionaryLanguage)) {
            return;
        }
        AnnotationAssertionPathDictionaryLanguage lang = (AnnotationAssertionPathDictionaryLanguage) language;
        ImmutableList<IRI> propertyPathIris = lang.getAnnotationPropertyPath();
        renderAndSetPropertyPath(propertyPathIris);
        view.setLangTag(language.getLang());
    }

    private void renderAndSetPropertyPath(ImmutableList<IRI> propertyPathIris) {
        List<OWLAnnotationPropertyData> propertyDataPath = new ArrayList<>();
        for(int i = 0; i < propertyPathIris.size(); i++) {
            final int index = i;
            propertyDataPath.add(null);
            OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty(propertyPathIris.get(i));
            dispatch.execute(new GetEntityRenderingAction(projectId, property),
                             result -> {
                                 OWLAnnotationPropertyData annotationPropertyData = (OWLAnnotationPropertyData) result.getEntityData();
                                 propertyDataPath.set(index, annotationPropertyData);
                                 long renderingComplete = propertyDataPath.stream().filter(Objects::nonNull).count();
                                 if(renderingComplete == propertyPathIris.size()) {
                                     view.setPath(ImmutableList.copyOf(propertyDataPath));
                                 }
                             });
        }
    }

    @Override
    public Optional<DictionaryLanguage> getDictionaryLanguage() {
        ImmutableList<IRI> path = view.getPath()
                                      .stream()
                                      .map(OWLAnnotationPropertyData::getEntity)
                                      .map(OWLAnnotationProperty::getIRI)
                                      .collect(toImmutableList());
        String langTag = view.getLangTag();
        return Optional.of(AnnotationAssertionPathDictionaryLanguage.get(path, langTag));
    }

    @Override
    public void requestFocus() {

    }
}
