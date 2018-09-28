package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public interface ModalCallback {

    void start(@Nonnull AcceptsOneWidget container);
}
