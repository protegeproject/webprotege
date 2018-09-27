package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class DeleteAnnotationValuesViewImpl extends Composite implements DeleteAnnotationValuesView {

    interface DeleteAnnotationValuesViewImplUiBinder extends UiBinder<HTMLPanel, DeleteAnnotationValuesViewImpl> {

    }

    private static DeleteAnnotationValuesViewImplUiBinder ourUiBinder = GWT.create(DeleteAnnotationValuesViewImplUiBinder.class);

    @UiField(provided = true)
    AnnotationSimpleMatchingCriteriaViewImpl matchingCriteriaView;

    @Inject
    public DeleteAnnotationValuesViewImpl(@Nonnull AnnotationSimpleMatchingCriteriaViewImpl criteriaView) {
        this.matchingCriteriaView = criteriaView;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getProperty() {
        return matchingCriteriaView.getProperty();
    }

    @Override
    public boolean isMatchValue() {
        return matchingCriteriaView.isMatchValue();
    }

    @Nonnull
    @Override
    public String getValue() {
        return matchingCriteriaView.getValue();
    }

    @Override
    public boolean isValueRegularExpression() {
        return matchingCriteriaView.isValueRegularExpression();
    }

    @Override
    public boolean isMatchLang() {
        return matchingCriteriaView.isMatchLang();
    }

    @Nonnull
    @Override
    public String getLang() {
        return matchingCriteriaView.getLang();
    }
}