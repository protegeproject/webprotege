package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class AnnotationSimpleMatchingCriteriaViewImpl extends Composite {

    interface AnnotationSimpleMatchingCriteriaViewImplUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, edu.stanford.bmir.protege.web.client.bulkop.AnnotationSimpleMatchingCriteriaViewImpl> {

    }

    private static AnnotationSimpleMatchingCriteriaViewImplUiBinder ourUiBinder = GWT.create(AnnotationSimpleMatchingCriteriaViewImplUiBinder.class);

    public AnnotationSimpleMatchingCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}